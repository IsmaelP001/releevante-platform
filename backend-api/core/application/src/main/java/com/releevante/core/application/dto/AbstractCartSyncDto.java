package com.releevante.core.application.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.releevante.core.domain.*;
import com.releevante.types.ImmutableExt;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.immutables.value.Value;

@Value.Immutable()
@JsonDeserialize(as = CartSyncDto.class)
@JsonSerialize(as = CartSyncDto.class)
@ImmutableExt
public abstract class AbstractCartSyncDto {
  abstract String id();

  abstract CartStateDto cartState();

  abstract ZonedDateTime createdAt();

  abstract ZonedDateTime updatedAt();

  abstract List<CartItemSyncDto> cartItems();

  public Cart toDomain() {
    return Cart.builder()
        .id(CartId.of(id()))
        .createAt(createdAt())
        .updatedAt(createdAt())
        .state(CartState.of(cartState().name()))
        .items(new LazyLoaderInit<>(this::domainCartItem))
        .build();
  }

  private List<CartItem> domainCartItem() {
    return cartItems().stream()
        .map(
            itemDto ->
                CartItem.builder()
                    .id(itemDto.id())
                    .qty(itemDto.qty())
                    .createdAt(createdAt())
                    .updated(updatedAt())
                    .isbn(itemDto.isbn())
                    .itemPrice(itemDto.itemPrice())
                    .build())
        .collect(Collectors.toList());
  }
}
