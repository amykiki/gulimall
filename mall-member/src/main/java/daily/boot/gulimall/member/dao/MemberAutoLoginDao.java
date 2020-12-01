package daily.boot.gulimall.member.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import daily.boot.gulimall.member.entity.MemberAutoLoginEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberAutoLoginDao extends BaseMapper<MemberAutoLoginEntity> {
}
