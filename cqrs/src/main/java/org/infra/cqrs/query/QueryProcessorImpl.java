package org.infra.cqrs.query;

import org.infra.cqrs.context.ContextFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;
import java.util.function.Supplier;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Service
@Scope(SCOPE_PROTOTYPE)
public class QueryProcessorImpl implements QueryProcessor {
    private final ApplicationContext context;
    private final ContextFactory contextFactory;
    private static final Map<Class<?>, Class<? extends QueryHandler<?, ?>>> handlers = new HashMap<>();
    private static final Object lockObject = new Object();
    private boolean initialized = false;

    public QueryProcessorImpl(ApplicationContext context, ContextFactory contextFactory) {
        this.context = context;
        this.contextFactory = contextFactory;
    }

    @Override
    public <TQuery extends Query<TResult>, TResult> TResult execute(TQuery query) {
        this.init();
        var handlerClass = handlers.get(query.getClass());
        var handler = (QueryHandler<TQuery, TResult>) this.context.getBean(handlerClass);

        return executePipeline(query, handler);
    }

    private <TResult, TQuery extends Query<TResult>> TResult executePipeline(TQuery query, QueryHandler<TQuery, TResult> handler) {
        var decorators = this.context
                .getBeansOfType(QueryHandlerDecorator.class)
                .values();
        var handlerContext = this.contextFactory.get();

        if (decorators.isEmpty())
            return handler.handle(query, handlerContext);

        var functions = new LinkedList<Supplier<TResult>>();

        decorators.stream()
                .sorted(new QueryHandlerDecorator.Comparator())
                .forEach(c -> functions.add(() -> (TResult) c.handle(handler, query, handlerContext)));

        TResult result = null;

        while (!functions.isEmpty() && handlerContext.moveNext())
            result = functions.poll().get();

        return result;
    }

    private void init() {
        if (initialized)
            return;

        synchronized (lockObject) {
            this.context.getBeansOfType(QueryHandler.class)
                    .values()
                    .forEach((handler) -> {
                        var handlerClass = handler.getClass();

                        var args = GenericTypeResolver.resolveTypeArguments(handlerClass, QueryHandler.class);

                        if (args == null)
                            throw new RuntimeException(
                                    MessageFormat.format("Invalid handler encountered {0}", handler.getClass().getName()));

                        handlers.put(args[0], (Class<QueryHandler<?, ?>>) handler.getClass());
                    });
        }

        this.initialized = true;
    }
}
