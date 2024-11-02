package org.infra.decorators.caching;

import org.infra.cqrs.query.Query;

import java.time.Duration;

public interface CacheableQuery<T> extends Query<T> {
     default Duration timeOut() {
         return Duration.ofHours(24);
     }

     default String getKey() {
         return this.getClass().getName();
     }
}
