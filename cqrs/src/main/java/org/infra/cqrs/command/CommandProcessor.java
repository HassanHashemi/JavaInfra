package org.infra.cqrs.command;

public interface CommandProcessor {
    <TCommand extends Command<TResult>, TResult> TResult execute(TCommand command);
}
