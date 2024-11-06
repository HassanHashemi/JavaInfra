package org.infra.cqrs.command;

import org.infra.cqrs.context.ContextFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.Supplier;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Service
@Scope(SCOPE_PROTOTYPE)
public class CommandProcessorImpl implements CommandProcessor {
    private final ApplicationContext context;
    private final ContextFactory contextFactory;
    private boolean initialized = false;
    private static final HashMap<Class<?>, Class<? extends CommandHandler<?, ?>>> handlers = new HashMap<>();
    private static final Object lock = new Object();

    public CommandProcessorImpl(ApplicationContext context, ContextFactory contextFactory) {
        this.context = context;
        this.contextFactory = contextFactory;
    }

    @Override
    public <TCommand extends Command<TResult>, TResult> TResult execute(TCommand command) {
        this.init();

        var handlerClass = handlers.get(command.getClass());
        var handler = (CommandHandler<TCommand, TResult>) this.context.getBean(handlerClass);

        return executePipeline(command, handler);
    }

    private <TResult, TCommand extends Command<TResult>> TResult executePipeline(TCommand command, CommandHandler<TCommand, TResult> handler) {
        var decorators = context
                .getBeansOfType(CommandHandlerDecorator.class)
                .values()
                .stream()
                .sorted(new CommandHandlerDecorator.Comparator())
                .toList();

        var handlerContext = this.contextFactory.get();
        if (decorators.isEmpty())
            return handler.handle(command, handlerContext);

        var functions = new LinkedList<Supplier<TResult>>();

        decorators.forEach(c -> functions.add(() -> (TResult) c.handle(handler, command, handlerContext)));

        TResult result = null;

        while (!functions.isEmpty() && handlerContext.moveNext())
            result = functions.poll().get();

        return result;
    }

    private void init() {
        if (initialized)
            return;

        synchronized (lock) {
            if (initialized)
                return;

            this.context.getBeansOfType(CommandHandler.class)
                    .values()
                    .forEach((handler) -> {
                        var args = GenericTypeResolver.resolveTypeArguments(handler.getClass(), CommandHandler.class);

                        if (args == null)
                            throw new RuntimeException(MessageFormat.format("Invalid CommandHandler {0}", handler.getClass().getName()));

                        handlers.put(args[0], (Class<? extends CommandHandler<?, ?>>) handler.getClass());
                    });

            this.initialized = true;
        }
    }
}