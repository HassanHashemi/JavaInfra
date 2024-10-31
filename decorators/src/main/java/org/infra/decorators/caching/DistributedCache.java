package org.infra.decorators.caching;

import java.time.Duration;

public interface DistributedCache {
    void set(String key, String value, Duration expirationTimeout);
    String get(String key);
}
