package org.infra.cqrs.command;

import org.infra.cqrs.context.HandlerContext;
import org.infra.cqrs.query.QueryHandlerDecorator;

public interface CommandHandlerDecorator<TCommand extends Command<TResult>, TResult> {
    TResult handle(TCommand command, HandlerContext context);
    default int priority() { return 0; }

    class Comparator implements java.util.Comparator<CommandHandlerDecorator> {

        @Override
        public int compare(CommandHandlerDecorator o1, CommandHandlerDecorator o2) {
            if (o1.priority() < o2.priority()) return -1;
            else if (o1.priority() == o2.priority()) return 0;
            else return Integer.MAX_VALUE;
        }
    }
}