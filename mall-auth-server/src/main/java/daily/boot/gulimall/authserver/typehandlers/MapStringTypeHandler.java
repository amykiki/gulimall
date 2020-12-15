package daily.boot.gulimall.authserver.typehandlers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(Map.class)
public class MapStringTypeHandler extends BaseTypeHandler<Map<String, Object>> {
    
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Map<String, Object> parameter, JdbcType jdbcType) throws SQLException {
        String jsonString = JSON.toJSONString(parameter);
        ps.setObject(i, jsonString, jdbcType.TYPE_CODE);
    }
    
    @Override
    public Map<String, Object> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String rtn = rs.getString(columnName);
        return parseStr(rtn);
    }
    
    @Override
    public Map<String, Object> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String rtn = rs.getString(columnIndex);
        return parseStr(rtn);
    }
    
    @Override
    public Map<String, Object> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String rtn = cs.getString(columnIndex);
        return parseStr(rtn);
    }
    
    private Map<String, Object> parseStr(String value) {
        return JSON.parseObject(value, new TypeReference<Map<String, Object>>(){});
    }
   
}
