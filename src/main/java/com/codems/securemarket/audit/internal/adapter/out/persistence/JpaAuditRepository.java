package com.codems.securemarket.audit.internal.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface JpaAuditRepository extends JpaRepository<AuditEntity, Long> {
}
