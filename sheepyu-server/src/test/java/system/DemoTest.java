package system;

import cn.hutool.core.lang.TypeReference;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author ygq
 * @date 2023-01-24 16:09
 **/
public class DemoTest {
    public static void main(String[] args) {
        Set<Demo> set = new HashSet<>();
        set.add(new Demo("aaa"));
        set.add(new Demo("bbb"));
        set.add(new Demo("aaa"));

    }

    private static <T> T testTypeReference(Object obj, TypeReference<T> type) {
//        Type typeType = type.getType();
//        Class<T> clazz = (Class<T>) typeType;
        ParameterizedType parameterizedType = (ParameterizedType) type.getType();
        System.out.println(parameterizedType.getRawType());
        System.out.println(parameterizedType.getOwnerType());
        for (Type t : parameterizedType.getActualTypeArguments()) {
            System.out.println(t);
        }
//        return clazz.cast(obj);
        return null;
    }

    @AllArgsConstructor
    @Data
    static class Demo {
        private String name;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Demo demo = (Demo) o;
            return Objects.equals(name, demo.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }
}
