package daily.boot.gulimall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.member.dao.MemberAutoLoginDao;
import daily.boot.gulimall.member.entity.MemberAutoLoginEntity;
import daily.boot.gulimall.member.service.MemberAutoLoginService;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Service("memberAutoLoginService")
public class MemberAutoLoginServiceImpl extends ServiceImpl<MemberAutoLoginDao, MemberAutoLoginEntity> implements MemberAutoLoginService {
    @Override
    public void updateToken(@NotNull(message = "series不能为空") String series, @NotNull(message = "tokenValue不能为空") String tokenValue, Date lastUsed) {
        MemberAutoLoginEntity entity = new MemberAutoLoginEntity();
        entity.setDate(lastUsed);
        entity.setTokenValue(tokenValue);
        LambdaUpdateWrapper<MemberAutoLoginEntity> wrapper = Wrappers.lambdaUpdate(MemberAutoLoginEntity.class).eq(MemberAutoLoginEntity::getSeries, series);
        update(entity, wrapper);
    }
    
    @Override
    public void removeUserTokens(String username) {
        this.lambdaUpdate().eq(MemberAutoLoginEntity::getUsername, username).remove();
    }
}
