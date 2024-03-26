package org.infra.domain;

import java.util.UUID;

public class DomainEvent extends Event {
    protected DomainEvent() {
    }

    protected DomainEvent(UUID aggregateRootId) {
        var a= UUID.ZERO
    }
}
