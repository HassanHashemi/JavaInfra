package org.infra.base;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtils {
    public static <E> List<E> toList(final Iterable<? extends E> iterable) {
        var iterator = iterable.iterator();

        final List<E> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }
}
