package com.releevante.core.application.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.releevante.core.domain.BookRating;
import com.releevante.types.AccountPrincipal;
import com.releevante.types.ImmutableExt;
import com.releevante.types.SequentialGenerator;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.immutables.value.Value;

@ImmutableExt
@Value.Immutable()
@JsonDeserialize(as = BookRatingDto.class)
@JsonSerialize(as = BookRatingDto.class)
public abstract class AbstractBookRatingDto {
  abstract String isbn();

  abstract int rating();

  abstract Optional<ZonedDateTime> createdAt();

  public BookRating toDomain(
      AccountPrincipal principal,
      SequentialGenerator<String> uuidGenerator,
      SequentialGenerator<ZonedDateTime> dateTimeGenerator) {
    return BookRating.builder()
        .id(uuidGenerator.next())
        .rating(rating())
        .isbn(isbn())
        .createdAt(createdAt().orElse(dateTimeGenerator.next()))
        .origin(principal.audience())
        .audit(principal.subject())
        .build();
  }
}
