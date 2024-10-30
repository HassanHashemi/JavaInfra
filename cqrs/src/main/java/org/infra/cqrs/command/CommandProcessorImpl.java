package org.infra.cqrs.command;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.HashMap;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Service
@Scope(SCOPE_PROTOTYPE)
public class CommandProcessorImpl implements CommandProcessor {
    private final ApplicationContext context;
    private boolean initialized = false;
    private static final HashMap<Class<?>, Class<? extends CommandHandler<?,?>>> _handlers = new HashMap<>();
    private static final Object lock = new Object();

    public CommandProcessorImpl(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public <TCommand extends Command, TResult> TResult execute(TCommand command) {
        this.init();

        var handlerClass = _handlers.get(command.getClass());
        var handler = (CommandHandler<TCommand, TResult>)this.context.getBean(handlerClass);

        return handler.handle(command);
    }

    private void init() {
        if (initialized)
            return;

        synchronized (lock) {
            this.context.getBeansOfType(CommandHandler.class)
                    .values()
                    .forEach((handler) -> {
                        var args = GenericTypeResolver.resolveTypeArguments(handler.getClass(), CommandHandler.class);

                        if (args == null)
                            throw new RuntimeException(MessageFormat.format("Invalid CommandHandler {0}", handler.getClass().getName()));

                        _handlers.put(args[0], (Class<? extends CommandHandler<?, ?>>) handler.getClass());
                    });

            this.initialized = true;
        }
    }
}

