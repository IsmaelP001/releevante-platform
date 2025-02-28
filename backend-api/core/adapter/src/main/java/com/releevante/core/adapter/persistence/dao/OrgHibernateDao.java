package com.releevante.core.adapter.persistence.dao;

import com.releevante.core.adapter.persistence.records.CoreOrgRecord;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgHibernateDao extends ReactiveCrudRepository<CoreOrgRecord, String> {}
