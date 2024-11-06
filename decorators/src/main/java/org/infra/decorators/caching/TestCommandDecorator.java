package org.infra.decorators.caching;

import org.infra.cqrs.command.Command;
import org.infra.cqrs.command.CommandHandler;
import org.infra.cqrs.command.CommandHandlerDecorator;
import org.infra.cqrs.context.HandlerContext;
import org.springframework.stereotype.Component;

@Component
public class TestCommandDecorator<TCommand extends Command<TResult>, TResult> implements CommandHandlerDecorator<TCommand, TResult> {
    @Override
    public TResult handle(CommandHandler<TCommand, TResult> innerHandler, TCommand command, HandlerContext context) {
        System.out.println("Command decorator");

        return innerHandler.handle(command, context);
    }
}
