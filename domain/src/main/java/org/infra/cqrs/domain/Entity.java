package org.infra.cqrs.domain;

public class Entity<T> {
    private T id;

    public T getId() {
        return this.id;
    }
}
