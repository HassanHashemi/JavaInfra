package org.infra.cqrs.context;

import java.util.function.Supplier;

public interface ContextFactory extends Supplier<HandlerContext> {
}