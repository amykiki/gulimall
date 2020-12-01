package daily.boot.gulimall.member.dao;

import daily.boot.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 会员
 * 
 * @author amy
 * @email amy@gmail.com
 * @date 2020-10-14 16:46:51
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
    
    MemberEntity getMemberUserInfo(@Param("username") String username);
}
