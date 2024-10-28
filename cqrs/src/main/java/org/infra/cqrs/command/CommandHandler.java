package org.infra.cqrs.command;

public interface CommandHandler<TCommand extends Command, TResult> {
    TResult handle(TCommand command);
}
