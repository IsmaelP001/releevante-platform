/* (C)2024 */
package com.releevante.identity.application.service.user;

import static com.releevante.types.utils.DateTimeUtils.daysDifference;

import com.releevante.identity.application.dto.*;
import com.releevante.identity.application.service.auth.AuthorizationService;
import com.releevante.identity.domain.model.*;
import com.releevante.identity.domain.repository.AccountRepository;
import com.releevante.identity.domain.repository.SmartLibraryAccessControlRepository;
import com.releevante.identity.domain.repository.UserRepository;
import com.releevante.identity.domain.service.PasswordEncoder;
import com.releevante.types.SequentialGenerator;
import com.releevante.types.ZonedDateTimeGenerator;
import com.releevante.types.utils.DateTimeUtils;
import java.time.ZonedDateTime;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class DefaultUserServiceImpl extends AccountService implements UserService {
  final UserRepository userRepository;
  final AuthorizationService authorizationService;
  final SmartLibraryAccessControlRepository accessControlRepository;

  public DefaultUserServiceImpl(
      UserRepository userRepository,
      AccountRepository accountRepository,
      PasswordEncoder passwordEncoder,
      SequentialGenerator<String> uuidGenerator,
      SequentialGenerator<ZonedDateTime> dateTimeGenerator,
      AuthorizationService authorizationService,
      SmartLibraryAccessControlRepository accessControlRepository) {
    super(accountRepository, passwordEncoder, uuidGenerator, dateTimeGenerator);
    this.userRepository = userRepository;
    this.authorizationService = authorizationService;
    this.accessControlRepository = accessControlRepository;
  }

  @Override
  public Mono<UserIdDto> createUser(UserDto userDto) {

    return null;
  }

  @Override
  public Mono<AccountIdDto> createAccount(AccountDto accountDto) {
    return authorizationService
        .checkAuthorities("account:create")
        .flatMap(principal -> createAccount(accountDto, OrgId.of(principal.orgId())))
        .flatMap(accountRepository::upsert)
        .map(LoginAccount::accountId)
        .map(AccountId::value)
        .map(AccountIdDto::of);
  }

  protected Mono<SmartLibraryGrantedAccess> create(
      List<String> sLids,
      ZonedDateTime expiresAt,
      Integer accessDueDays,
      AccessCredential credentials,
      String userId) {

    return authorizationService
        .checkAuthorities("smart-library-access:create")
        .flatMap(
            principal ->
                Flux.fromIterable(sLids)
                    .map(
                        slid ->
                            SmartLibraryAccess.builder()
                                .id(uuidGenerator.next())
                                .credential(credentials)
                                .orgId(OrgId.of(principal.orgId()))
                                .createdAt(dateTimeGenerator.next())
                                .updatedAt(dateTimeGenerator.next())
                                .userId(userId)
                                .slid(slid)
                                .isActive(true)
                                .expiresAt(expiresAt)
                                .accessDueDays(accessDueDays)
                                .build())
                    .collectList()
                    .flatMapMany(accessControlRepository::save)
                    .map(
                        libraryAccess ->
                            GrantedAccess.builder()
                                .accessId(libraryAccess.id())
                                .slid(libraryAccess.slid())
                                .accessDueDays(libraryAccess.accessDueDays())
                                .expiresAt(libraryAccess.expiresAt())
                                .build())
                    .collectList()
                    .map(
                        grantedAccesses ->
                            SmartLibraryGrantedAccess.builder()
                                .access(grantedAccesses)
                                .clientId(userId)
                                .build()));
  }

  @Override
  public Mono<SmartLibraryGrantedAccess> create(UserAccessDto access) {
    return Mono.fromCallable(() -> AccessCredential.from(access.key(), access.value()))
        .flatMap(this::validateCredentials)
        .flatMap(
            credential -> {
              var now = ZonedDateTimeGenerator.instance().next();
              var accessDueDays =
                  access
                      .expiresAt()
                      .map(expiresAt -> daysDifference(now, expiresAt))
                      .orElseGet(() -> access.accessDueDays().orElseThrow());
              var expiresAt =
                  access
                      .accessDueDays()
                      .map(DateTimeUtils::daysToAdd)
                      .orElseGet(() -> access.expiresAt().orElseThrow());

              var userId = uuidGenerator.next();

              return create(access.sLids(), expiresAt, accessDueDays, credential, userId);
            });
  }

  Mono<AccessCredential> validateCredentials(AccessCredential credential) {
    return accessControlRepository
        .findBy(credential.value())
        .map(
            access -> {
              if (access.isActive()) throw new RuntimeException("access is still active");
              return credential;
            })
        .last()
        .thenReturn(credential)
        .defaultIfEmpty(credential);
  }
}
