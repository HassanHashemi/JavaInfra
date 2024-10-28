package org.infra.cqrs.domain;

import java.time.LocalDateTime;

public class Event {
    private String eventName;
    private LocalDateTime timeStamp;
    private boolean mustPropagate;
    private int version;
    private Object eventMetaData;

    public String getEventName() {
        if (this.eventName == null || this.eventName.isEmpty()) {
            this.eventName = this.getClass().getName();
        }

        return this.eventName;
    }

    public void setEventName(String value) {
        if (value == null || value.isEmpty())
            throw new IllegalArgumentException("value can not be null");

        this.eventName = value;
    }

    public void setTimestamp(LocalDateTime timeStamp) {
        if (timeStamp == null)
            throw new IllegalArgumentException("timestamp can not be null");

        this.timeStamp = timeStamp;
    }

    public LocalDateTime getTimeStamp() {
        return this.timeStamp;
    }

    public boolean getMustPropagate() {
        return this.mustPropagate;
    }

    public void setMustPropagate(boolean value) {
        this.mustPropagate = value;
    }

    public int getVersion() {
        return this.version;
    }

    public void serVersion(int value) {
        if (value < 1)
            throw new IllegalArgumentException("version can not be less than 1");
    }

    public Object getEventMetaData() {
        return this.eventMetaData;
    }

    public void setEventMetaData(Object value) {
        this.eventMetaData = value;
    }
}
