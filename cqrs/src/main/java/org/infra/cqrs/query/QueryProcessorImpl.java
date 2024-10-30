package org.infra.cqrs.query;

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
    private static final Map<Class<?>, Class<? extends QueryHandler<?, ?>>> handlers = new HashMap<>();
    private static final Object lockObject = new Object();
    private boolean initialized = false;

    public QueryProcessorImpl(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public <TQuery extends Query, TResult> TResult execute(TQuery query) {
        this.init();

        var handlerClass = handlers.get(query.getClass());
        var handler = (QueryHandler<TQuery, TResult>) this.context.getBean(handlerClass);

        return executePipeline(query, handler);
    }

    private <TQuery extends Query, TResult> TResult executePipeline(TQuery query, QueryHandler<TQuery, TResult> handler) {
        var decorators = new ArrayList<QueryHandlerDecorator>();

        for (var name : this.context.getBeanNamesForType(QueryHandlerDecorator.class)) {
            decorators.add((QueryHandlerDecorator) context.getBean(name, handler));
        }

        var functions = new LinkedList<Supplier<TResult>>();
        var decoratorContext = new DecoratorContext();
        decorators.stream().sorted((o1, o2) -> {
                    if (o1.priority() < o2.priority())  return -1;
                    else if (o1.priority() == o2.priority()) return 0;
                    else return Integer.MAX_VALUE;
                })
                .forEach(c -> functions.add(() -> (TResult) c.handle(query, decoratorContext)));

        TResult result = null;

        while (!functions.isEmpty() && decoratorContext.getMoveNext())
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
