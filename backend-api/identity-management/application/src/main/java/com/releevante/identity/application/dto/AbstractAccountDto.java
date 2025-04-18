/* (C)2024 */
package com.releevante.identity.application.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.releevante.types.ImmutableExt;
import java.util.Optional;
import org.immutables.value.Value;

@Value.Immutable()
@JsonDeserialize(as = AccountDto.class)
@JsonSerialize(as = AccountDto.class)
@ImmutableExt
public abstract class AbstractAccountDto {
  abstract Optional<String> userName();

  abstract String password();

  abstract String email();
}
