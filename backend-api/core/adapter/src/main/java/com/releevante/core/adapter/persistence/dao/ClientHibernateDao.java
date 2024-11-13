package com.releevante.core.adapter.persistence.dao;

import com.releevante.core.adapter.persistence.records.ClientRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface ClientHibernateDao extends JpaRepository<ClientRecord, String> {}
