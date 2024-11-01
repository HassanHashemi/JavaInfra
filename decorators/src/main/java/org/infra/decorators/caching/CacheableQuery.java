package org.infra.decorators.caching;

import org.infra.cqrs.query.Query;

import java.time.Duration;
import java.time.LocalTime;

public interface CacheableQuery<T> extends Query<T> {
     default LocalTime absoluteExpireTime() {
         return LocalTime.now().plusHours(24);
     };

     default Duration slidingExpiration() {
         return Duration.ofHours(24);
     }

     default String getKey() {
         return this.getClass().getName();
     }
}
