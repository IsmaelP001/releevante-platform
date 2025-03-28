package com.releevante.identity.application.service.user;

import com.releevante.identity.application.dto.AccountDto;
import com.releevante.identity.domain.model.*;
import com.releevante.identity.domain.repository.AccountRepository;
import com.releevante.identity.domain.service.PasswordEncoder;
import com.releevante.types.SequentialGenerator;
import com.releevante.types.exceptions.InvalidInputException;
import java.time.ZonedDateTime;
import reactor.core.publisher.Mono;

public class AccountService {
  final AccountRepository accountRepository;
  final PasswordEncoder passwordEncoder;
  final SequentialGenerator<String> uuidGenerator;
  final SequentialGenerator<ZonedDateTime> dateTimeGenerator;

  public AccountService(
      AccountRepository accountRepository,
      PasswordEncoder passwordEncoder,
      SequentialGenerator<String> uuidGenerator,
      SequentialGenerator<ZonedDateTime> dateTimeGenerator) {
    this.accountRepository = accountRepository;
    this.passwordEncoder = passwordEncoder;
    this.uuidGenerator = uuidGenerator;
    this.dateTimeGenerator = dateTimeGenerator;
  }

  protected Mono<LoginAccount> createAccount(AccountDto accountDto, OrgId orgId) {
    return Mono.just(UserName.of(accountDto.userName().orElse(accountDto.email())))
        .flatMap(
            userName ->
                accountRepository
                    .findBy(userName)
                    .filterWhen(this::throwEntityExists)
                    .switchIfEmpty(
                        Mono.fromCallable(
                            () -> {
                              var passwordHash =
                                  Password.from(accountDto.password(), passwordEncoder);
                              var accountId = AccountId.of(uuidGenerator.next());
                              var createdAt = dateTimeGenerator.next();
                              var email = Email.from(accountDto.email());
                              return LoginAccount.builder()
                                  .accountId(accountId)
                                  .orgId(orgId)
                                  .userName(userName)
                                  .password(passwordHash)
                                  .createdAt(createdAt)
                                  .updatedAt(createdAt)
                                  .email(email)
                                  .isActive(false)
                                  .build();
                            })));
  }

  Mono<Boolean> throwEntityExists(LoginAccount account) {
    return Mono.error(new InvalidInputException("account already exists"));
  }
}
