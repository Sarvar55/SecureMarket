package com.codems.securemarket.audit.internal.adapter.out.persistence;

import com.codems.securemarket.audit.internal.application.port.out.AppendAuditPort;
import com.codems.securemarket.audit.internal.application.port.out.LoadAuditPort;
import com.codems.securemarket.audit.internal.domain.model.AuditEntry;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
class AuditPersistenceAdapter implements AppendAuditPort, LoadAuditPort {

    private final JpaAuditRepository repository;

    AuditPersistenceAdapter(JpaAuditRepository repository) {
        this.repository = repository;
    }

    @Override
    public void append(AuditEntry entry) {
        repository.save(AuditEntity.from(entry));
    }

    @Override
    public List<AuditEntry> findRecent(int limit) {
        var page = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "occurredAt"));
        return repository.findAll(page).stream()
                .map(AuditEntity::toDomain)
                .toList();
    }
}
