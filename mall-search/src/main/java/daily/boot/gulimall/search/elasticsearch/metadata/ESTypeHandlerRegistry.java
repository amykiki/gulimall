package daily.boot.gulimall.search.elasticsearch.metadata;

import daily.boot.gulimall.search.elasticsearch.enums.ESFieldType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public final class ESTypeHandlerRegistry {
    private static final Map<Class<?>, ESFieldType> esTypeMap = new HashMap<>();
    
    static {
        esTypeMap.put(boolean.class, ESFieldType.Boolean);
        esTypeMap.put(Boolean.class, ESFieldType.Boolean);
    
        esTypeMap.put(byte.class, ESFieldType.Byte);
        esTypeMap.put(Byte.class, ESFieldType.Byte);
    
        esTypeMap.put(short.class, ESFieldType.Short);
        esTypeMap.put(Short.class, ESFieldType.Short);
    
        esTypeMap.put(int.class, ESFieldType.Integer);
        esTypeMap.put(Integer.class, ESFieldType.Integer);
    
        esTypeMap.put(long.class, ESFieldType.Long);
        esTypeMap.put(Long.class, ESFieldType.Long);
    
        esTypeMap.put(double.class, ESFieldType.Double);
        esTypeMap.put(Double.class, ESFieldType.Double);
    
        esTypeMap.put(float.class, ESFieldType.Float);
        esTypeMap.put(Float.class, ESFieldType.Float);
    
        esTypeMap.put(LocalDateTime.class, ESFieldType.Date);
        esTypeMap.put(LocalDate.class, ESFieldType.Date);
    
        esTypeMap.put(String.class, ESFieldType.Text);
    }
    public ESTypeHandlerRegistry() {
    }
    
    public static ESFieldType getESFieldType(Class<?> clazz) {
        ESFieldType type = esTypeMap.get(clazz);
        if (type == null) {
            type =  ESFieldType.Object;
        }
        return type;
    }
}
