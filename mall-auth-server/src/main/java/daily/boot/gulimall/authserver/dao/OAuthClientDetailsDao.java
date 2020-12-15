package daily.boot.gulimall.authserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import daily.boot.gulimall.authserver.security.OAuthClientDetails;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OAuthClientDetailsDao extends BaseMapper<OAuthClientDetails> {
}
