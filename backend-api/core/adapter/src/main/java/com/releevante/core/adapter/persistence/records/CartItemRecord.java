package com.releevante.core.adapter.persistence.records;

import com.releevante.core.domain.CartItem;
import com.releevante.core.domain.Isbn;
import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "cart_items", schema = "core")
@Getter
@Setter
@NoArgsConstructor
@Entity
public class CartItemRecord {
  @Id private String id;

  private String isbn;
  private Integer qty;
  private ZonedDateTime createdAt;
  private ZonedDateTime updatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cart_id")
  private CartRecord cart;

  public CartItem toDomain() {
    return CartItem.builder().id(id).qty(qty).createdAt(createdAt).isbn(Isbn.of(isbn)).build();
  }

  protected static CartItemRecord fromDomain(CartRecord cart, CartItem cartItem) {
    var record = new CartItemRecord();
    record.setId(cartItem.id());
    record.setCreatedAt(cartItem.createdAt());
    record.setIsbn(cartItem.isbn().value());
    record.setQty(cartItem.qty());
    record.setCart(cart);
    return record;
  }

  protected static Set<CartItemRecord> fromDomain(CartRecord cart, List<CartItem> cartItems) {
    return cartItems.stream().map(item -> fromDomain(cart, item)).collect(Collectors.toSet());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CartItemRecord that = (CartItemRecord) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
