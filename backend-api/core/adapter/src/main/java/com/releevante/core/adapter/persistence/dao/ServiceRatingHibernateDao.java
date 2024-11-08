package com.releevante.core.adapter.persistence.dao;

import com.releevante.core.adapter.persistence.records.ServiceRatingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRatingHibernateDao extends JpaRepository<ServiceRatingRecord, String> {}
