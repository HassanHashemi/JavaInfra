package org.infra.cqrs.query;

public class DecoratorContext {
    public boolean moveNext = true;

    public boolean getMoveNext() {
        return this.moveNext;
    }

    public void setMoveNext(boolean value) {
        this.moveNext = value;
    }
}
