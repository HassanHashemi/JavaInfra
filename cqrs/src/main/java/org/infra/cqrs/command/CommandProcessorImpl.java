package org.infra.cqrs.command;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Service
@Scope(SCOPE_PROTOTYPE)
public class CommandProcessorImpl implements CommandProcessor {
    private final ApplicationContext context;

    public CommandProcessorImpl(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public <TCommand extends Command, TResult> TResult execute(TCommand command) {
        var handlerClass = command.getClass().getAnnotation(CommandHandlerType.class);
        if (handlerClass == null) {
            throw new RuntimeException("Handler annotation was not found");
        }

        var handler = (CommandHandler<TCommand, TResult>) this.context.getBean(handlerClass.handlerClass());

        return handler.handle(command);
    }
}

