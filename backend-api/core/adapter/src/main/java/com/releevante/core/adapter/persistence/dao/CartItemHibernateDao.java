package com.releevante.core.adapter.persistence.dao;

import com.releevante.core.adapter.persistence.records.CartItemRecord;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CartItemHibernateDao extends ReactiveCrudRepository<CartItemRecord, String> {
  Flux<CartItemRecord> findAllByCartId(String cartId);
}
