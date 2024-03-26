package org.infra.domain;

import java.util.ArrayList;
import java.util.List;

public class AggregateRoot<T> extends Entity<T> {
    private final List<Event> uncommittedChanges = new ArrayList<>();
    private int version;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        if (version < 1)
            throw new IllegalArgumentException("invalid value for version, must be >= 1");

        this.version = version;
    }

    public List<Event> getUncommitted() {
        return this.uncommittedChanges;
    }

    public boolean isNew() {
        return this.version == 0;
    }

    public void markChangeAsCommitted(Event event)
    {
        if (event == null)
            throw new IllegalArgumentException("event can not be null");

        this.uncommittedChanges.remove(event);
    }

    public void markChangesAsCommitted() {
        this.uncommittedChanges.clear();
    }

    public String getStreamName() {
        return String.format("{%s}{%s}", this.getClass().getName(), this.getId().toString());
    }

    private void applyChange(DomainEvent event, boolean isNew)
    {
        this.AsDynamic().Apply(@event);

        if (isNew)
        {
            this.uncommittedChanges.add(event);
        }

        this.version++;
    }
}