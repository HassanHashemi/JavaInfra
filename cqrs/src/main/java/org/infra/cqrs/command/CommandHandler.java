package org.infra.cqrs.command;

import org.infra.cqrs.context.HandlerContext;

public interface CommandHandler<TCommand extends Command<TResult>, TResult> {
    TResult handle(TCommand command, HandlerContext context);
}
