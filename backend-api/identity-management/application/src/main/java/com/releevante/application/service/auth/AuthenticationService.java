/* (C)2024 */
package com.releevante.application.service.auth;

import com.releevante.application.dto.*;
import com.releevante.identity.domain.model.*;
import com.releevante.types.AccountPrincipal;
import reactor.core.publisher.Mono;

public interface AuthenticationService {
  Mono<UserAuthenticationDto> authenticate(LoginDto loginDto);

  Mono<SmartLibraryAccessDto> authenticate(PinLoginDto loginDto);

  Mono<AccountPrincipal> authenticate(LoginTokenDto token);
}
