package com.releevante.core.application.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.releevante.core.domain.BookLoan;
import com.releevante.core.domain.BookLoanId;
import com.releevante.core.domain.LazyLoaderInit;
import com.releevante.types.ImmutableExt;
import com.releevante.types.Slid;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.immutables.value.Value;

@Value.Immutable()
@JsonDeserialize(as = BookLoanDto.class)
@JsonSerialize(as = BookLoanDto.class)
@ImmutableExt
public abstract class AbstractBookLoanDto {
  abstract String id();

  abstract List<LoanDetailDto> items();

  abstract ZonedDateTime returnsAt();

  abstract ZonedDateTime createdAt();

  abstract ZonedDateTime updatedAt();

  abstract ZonedDateTime endTime();

  public BookLoan toDomain(Slid slid) {
    return BookLoan.builder()
        .slid(slid)
        .id(BookLoanId.of(id()))
        .createdAt(createdAt())
        .updatedAt(updatedAt())
        .returnsAt(returnsAt())
        .loanDetails(
            new LazyLoaderInit<>(
                () -> items().stream().map(LoanDetailDto::toDomain).collect(Collectors.toList())))
        .build();
  }
}
