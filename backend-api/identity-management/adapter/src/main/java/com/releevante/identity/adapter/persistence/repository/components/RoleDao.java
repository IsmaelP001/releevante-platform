package com.releevante.identity.adapter.persistence.repository.components;

import com.releevante.identity.adapter.persistence.projections.PrivilegeProjection;
import com.releevante.identity.adapter.persistence.records.RoleRecord;
import java.util.List;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface RoleDao extends ReactiveCrudRepository<RoleRecord, String> {
  Flux<RoleRecord> findAllByRole(String role);

  Flux<RoleRecord> findByRoleIn(List<String> role);

  @Query("select privilege\n" + "from core.roles\n" + "where role in (:roles)")
  Flux<PrivilegeProjection> findAllPrivilegeBy(@Param("roles") List<String> roles);
}
