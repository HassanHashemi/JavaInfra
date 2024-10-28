package org.infra.cqrs.domain;

import java.util.UUID;

public class DomainEvent extends Event {
    protected DomainEvent() {
    }

    public UUID rootId;
    protected DomainEvent(UUID aggregateRootId) {
        this.rootId = aggregateRootId;
    }
}
