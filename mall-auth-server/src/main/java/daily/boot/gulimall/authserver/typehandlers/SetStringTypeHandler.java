package daily.boot.gulimall.authserver.typehandlers;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.util.StringUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(Set.class)
//参考 https://my.oschina.net/u/4101481/blog/3089891
public class SetStringTypeHandler extends BaseTypeHandler<Set<String>> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Set<String> parameter, JdbcType jdbcType) throws SQLException {
        String params = String.join(",", parameter);
        ps.setString(i, params);
    }
    
    @Override
    public Set<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String rtn = rs.getString(columnName);
        return parseValue(rtn);
    }
    
    @Override
    public Set<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String rtn = rs.getString(columnIndex);
        return parseValue(rtn);
    }
    
    @Override
    public Set<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String rtn = cs.getString(columnIndex);
        return parseValue(rtn);
    }
    
    private Set<String> parseValue(String value) {
        return value == null ? null : new LinkedHashSet<>(Arrays.asList(StringUtils.commaDelimitedListToStringArray(value)));
    }
}
