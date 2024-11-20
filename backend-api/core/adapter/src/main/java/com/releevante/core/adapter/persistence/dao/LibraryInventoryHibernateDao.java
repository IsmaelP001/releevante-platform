package com.releevante.core.adapter.persistence.dao;

import com.releevante.core.adapter.persistence.dao.projections.BookCopyData;
import com.releevante.core.adapter.persistence.records.LibraryInventoryRecord;
import java.util.Set;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface LibraryInventoryHibernateDao
    extends ReactiveCrudRepository<LibraryInventoryRecord, String> {

  @Modifying
  @Query("update core.library_inventories set status = :status where cpy in (:copies)")
  Mono<Void> updateInventoryStatusByCpy(
      @Param("status") String status, @Param("copies") Set<String> copies);

  @Modifying
  Flux<LibraryInventoryRecord> findAllBySlidAndIsSyncFalse(String slid);

  @Query(
      value =
          "select\n"
              + "\tli.cpy,\n"
              + "\tli.isbn,\n"
              + "\tli.slid,\n"
              + "\tb.price as price,\n"
              + "\tb.title\n"
              + "from\n"
              + "\tcore.library_inventories li\n"
              + "inner join core.books b on\n"
              + "\tb.isbn = li.isbn\n"
              + "where\n"
              + "\tli.slid=:slid\n"
              + "\tand li.is_sync = false\n"
              + "order by li.created_at asc \n"
              + "limit 200 offset :offset*200;")
  Flux<BookCopyData> findAllCopiesUnSynced(@Param("slid") String slid, @Param("offset") int offset);
}
