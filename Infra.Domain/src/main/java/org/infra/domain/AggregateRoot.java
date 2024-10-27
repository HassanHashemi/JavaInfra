package org.infra.domain;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AggregateRoot<T> extends Entity<T> {
    private final List<Event> uncommittedChanges = new ArrayList<>();
    private int version;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        if (version < 1)
            throw new IllegalArgumentException("invalid value for version, must be >= 1");

        this.version = version;
    }

    public List<Event> getUncommitted() {
        return this.uncommittedChanges;
    }

    public boolean isNew() {
        return this.version == 0;
    }

    public void markChangeAsCommitted(Event event) {
        if (event == null)
            throw new IllegalArgumentException("event can not be null");

        this.uncommittedChanges.remove(event);
    }

    public void markChangesAsCommitted() {
        this.uncommittedChanges.clear();
    }

    public String getStreamName() {
        return String.format("{%s}{%s}", this.getClass().getName(), this.getId().toString());
    }

    protected final void apply(DomainEvent event) {
        this.apply(event, true);
    }

    private void apply(DomainEvent event, boolean isNew) {
        if (callAnnotatedEventHandlerMethod(this, event)) {
            if (isNew) {
                this.uncommittedChanges.add(event);
            }
        } else {
            throw new IllegalStateException("Couldn't find an event handler for: " + event.getClass().getName());
        }

        this.version++;
    }

    public final void loadFromHistory(DomainEvent... history) {
        if (history == null) {
            return;
        }

        for (final DomainEvent event : history) {
            apply(event, false);
        }
    }

    private static <T> T invoke(final Method method, final Object target, final Object... args) {
        try {
            if (!method.canAccess(target)) {
                method.setAccessible(true);
            }

            return (T) method.invoke(target, args);
        } catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    static boolean callAnnotatedEventHandlerMethod(final Entity<?> entity, final DomainEvent event) {
        final Method method = findDeclaredAnnotatedMethod(entity, ApplyAnnotation.class, event.getClass());
        if (method == null) {
            return false;
        }

        invoke(method, entity, event);
        return true;
    }

    public static boolean same(final Class<?>[] expected, final Class<?>[] actual) {
        if (expected == null) {
            return actual == null;
        }

        if (actual == null) {
            return false;
        }

        if (expected.length != actual.length) {
            return false;
        }

        for (int i = 0; i < expected.length; i++) {
            if (expected[0] != actual[i]) {
                return false;
            }
        }
        return true;
    }

    public static Method findDeclaredAnnotatedMethod(final Object obj, final Class<? extends Annotation> annotationType,
                                                     final Class<?>... expectedArgumentTypes) {
        final List<Method> methods = getDeclaredMethodsIncludingSuperClasses(obj.getClass(), AggregateRoot.class);
        for (final Method method : methods) {
            if (method.getAnnotation(annotationType) != null) {
                final Class<?>[] types = method.getParameterTypes();
                if (same(expectedArgumentTypes, types)) {
                    return method;
                }
            }
        }

        return null;
    }

    private static List<Method> getDeclaredMethodsIncludingSuperClasses(final Class<?> clasz, final Class<?>... stopParents) {
        final List<Class<?>> stopList = new ArrayList<>(Arrays.asList(stopParents));
        if (!stopList.contains(Object.class)) {
            stopList.add(Object.class);
        }

        final List<Method> list = new ArrayList<>();
        Class<?> toInspect = clasz;

        while (!stopList.contains(toInspect)) {
            final Method[] methods = toInspect.getDeclaredMethods();
            Collections.addAll(list, methods);
            toInspect = toInspect.getSuperclass();
        }

        return list;
    }
}