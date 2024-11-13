package com.releevante.core.adapter.persistence.records;

import com.releevante.core.domain.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "book_sales", schema = "core")
@Getter
@Setter
@NoArgsConstructor
@Entity
public class BookSaleRecord {
  @Id private String id;
  private BigDecimal total;

  private String clientId;

  private ZonedDateTime createdAt;
  private ZonedDateTime updatedAt;

  @Transient private Set<SaleItemsRecord> saleItems = new LinkedHashSet<>();

  public BookSale toDomain() {
    return BookSale.builder()
        .id(SaleId.of(id))
        .total(total)
        .createdAt(createdAt)
        .updatedAt(updatedAt)
        .build();
  }

  public static List<BookSale> toDomain(Set<BookSaleRecord> records) {
    return records.stream().map(BookSaleRecord::toDomain).collect(Collectors.toList());
  }

  private static BookSaleRecord fromDomain(ClientRecord client, BookSale sale) {
    var record = new BookSaleRecord();
    record.setId(sale.id().value());
    record.setTotal(sale.total());
    record.setCreatedAt(sale.createdAt());
    record.setUpdatedAt(sale.updatedAt());
    record.setClientId(client.getId());
    return record;
  }

  protected static Set<BookSaleRecord> fromDomain(ClientRecord client, List<BookSale> sales) {
    return sales.stream().map(sale -> fromDomain(client, sale)).collect(Collectors.toSet());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BookSaleRecord that = (BookSaleRecord) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
