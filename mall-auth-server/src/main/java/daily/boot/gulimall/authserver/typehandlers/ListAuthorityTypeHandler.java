package daily.boot.gulimall.authserver.typehandlers;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.util.StringUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(List.class)
public class ListAuthorityTypeHandler extends BaseTypeHandler<List<GrantedAuthority>> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<GrantedAuthority> parameter, JdbcType jdbcType) throws SQLException {
        String params = parameter.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        ps.setObject(i, params, jdbcType.TYPE_CODE);
    }
    
    @Override
    public List<GrantedAuthority> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String rtn = rs.getString(columnName);
        return parseValue(parseStr(rtn));
    }
    
    @Override
    public List<GrantedAuthority> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String rtn = rs.getString(columnIndex);
        return parseValue(parseStr(rtn));
    }
    
    @Override
    public List<GrantedAuthority> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String rtn = cs.getString(columnIndex);
        return parseValue(parseStr(rtn));
    }
    
    private List<String> parseStr(String value) {
        return Arrays.asList(StringUtils.commaDelimitedListToStringArray(value));
    }
    private List<GrantedAuthority> parseValue(List<String> values) {
        return AuthorityUtils.createAuthorityList(values.toArray(new String[0]));
    }
}
