package com.releevante.core.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.releevante.types.ImmutableObject;
import java.util.List;
import java.util.Optional;
import org.immutables.value.Value;

@Value.Immutable()
@JsonDeserialize(as = LoanItem.class)
@JsonSerialize(as = LoanItem.class)
@ImmutableObject
public abstract class AbstractLoanItem {
  abstract String id();

  abstract Optional<String> cpy();

  abstract List<LoanItemStatus> statuses();
}
