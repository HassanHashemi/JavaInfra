package org.infra.cqrs.query;

public class DecoratorContext {
    public boolean moveNext = true;

    public boolean moveNext() {
        return this.moveNext;
    }

    public void stopPipeline() {
        this.moveNext = false;
    }
}
