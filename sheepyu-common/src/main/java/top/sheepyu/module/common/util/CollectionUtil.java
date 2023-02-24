package top.sheepyu.module.common.util;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author ygq
 * @date 2023-01-31 15:45
 **/
public class CollectionUtil {
    public static <T, U> Set<U> convertSet(Collection<T> collection, Function<T, U> fun) {
        return collection.stream().map(fun).collect(Collectors.toSet());
    }

    public static <T, U> Set<U> convertSetFilter(Collection<T> collection, Function<T, U> fun, Predicate<T> predicate) {
        return collection.stream().filter(predicate).map(fun).collect(Collectors.toSet());
    }

    public static <T, U> List<U> convertList(Collection<T> collection, Function<T, U> fun) {
        return collection.stream().map(fun).collect(Collectors.toList());
    }

    public static <T> List<T> convertListFilter(Collection<T> collection, Predicate<T> predicate) {
        return collection.stream().filter(predicate).collect(Collectors.toList());
    }

    public static <T, U> List<U> convertListFilter(Collection<T> collection, Function<T, U> fun, Predicate<T> predicate) {
        return collection.stream().filter(predicate).map(fun).collect(Collectors.toList());
    }
}
