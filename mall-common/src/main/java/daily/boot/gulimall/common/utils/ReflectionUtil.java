package daily.boot.gulimall.common.utils;

import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ReflectionUtil {
    /**
     * class field cache
     */
    private static final Map<Class<?>, List<Field>> CLASS_FIELD_CACHE = new ConcurrentHashMap<>();
    private ReflectionUtil() {
    
    }
    
    public static List<Field> getFieldList(Class<?> clazz) {
        if (Objects.isNull(clazz)) {
            return Collections.emptyList();
        }
        List<Field> fields = CLASS_FIELD_CACHE.get(clazz);
        if (CollectionUtils.isEmpty(fields)) {
            synchronized (CLASS_FIELD_CACHE) {
                fields = doGetFieldList(clazz);
                CLASS_FIELD_CACHE.put(clazz, fields);
            }
        }
        return fields;
    }
    
    private static List<Field> doGetFieldList(Class<?> clazz) {
        if (clazz.getSuperclass() != null) {//排除Object类
            /*
            排除重载属性
             */
            Map<String, Field> fieldMap = excludeOverrideSuperField(clazz.getDeclaredFields(), getFieldList(clazz.getSuperclass()));
            /*
             * 重写父类属性过滤后处理忽略部分，支持过滤父类属性功能
             */
            return fieldMap.values().stream()
                           /* 过滤静态属性 */
                           .filter(f -> !Modifier.isStatic(f.getModifiers()))
                           /* 过滤 transient关键字修饰的属性 */
                           .filter(f -> !Modifier.isTransient(f.getModifiers()))
                           .collect(Collectors.toList());
        }else {
            return Collections.emptyList();
        }
    }
    
    private static Map<String, Field> excludeOverrideSuperField(Field[] fields, List<Field> superFieldList) {
        //子类属性
        LinkedHashMap<String, Field> fieldMap = Stream.of(fields)
                                                      .collect(Collectors.toMap(Field::getName,
                                                                                Function.identity(),
                                                                                          (u, v) -> {
                                                                                              throw new IllegalStateException(String.format("Duplicate key %s", u));
                                                                                          },
                                                                                LinkedHashMap::new));
        superFieldList.stream().filter(field -> !fieldMap.containsKey(field.getName()))
                      .forEach(f -> fieldMap.put(f.getName(), f));
        return fieldMap;
    }
}
