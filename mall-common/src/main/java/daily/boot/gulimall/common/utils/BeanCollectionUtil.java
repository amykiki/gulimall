package daily.boot.gulimall.common.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BeanCollectionUtil {
    
    public static <S, T> List<T> copyProperties(List<S> source, Supplier<T> supplier) {
        return copyProperties(source, supplier, null);
    }
    public static <S, T> List<T> copyProperties(List<S> source, Supplier<T> supplier, BiConsumer<S, T> consumer) {
        return source.stream().map(s -> {
            T t = supplier.get();
            BeanUtils.copyProperties(s, t);
            if (consumer != null) {
                consumer.accept(s, t);
            }
            return t;
        }).collect(Collectors.toList());
    }
}
