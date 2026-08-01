CREATE TABLE audit_events (
    id BIGSERIAL PRIMARY KEY,
    source_module VARCHAR(40) NOT NULL,
    action VARCHAR(80) NOT NULL,
    actor_id BIGINT,
    resource_type VARCHAR(40) NOT NULL,
    resource_id BIGINT,
    outcome VARCHAR(20) NOT NULL,
    details TEXT,
    occurred_at TIMESTAMPTZ NOT NULL,
    recorded_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_audit_events_actor_id ON audit_events (actor_id);
CREATE INDEX idx_audit_events_resource ON audit_events (resource_type, resource_id);
CREATE INDEX idx_audit_events_occurred_at ON audit_events (occurred_at);

