package daily.boot.gulimall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.common.exception.BusinessException;
import daily.boot.gulimall.member.dao.MemberAutoLoginDao;
import daily.boot.gulimall.member.entity.MemberAutoLoginEntity;
import daily.boot.gulimall.member.exception.MemberErrorCode;
import daily.boot.gulimall.member.service.MemberAutoLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("memberAutoLoginService")
@Slf4j
public class MemberAutoLoginServiceImpl extends ServiceImpl<MemberAutoLoginDao, MemberAutoLoginEntity> implements MemberAutoLoginService {
    @Override
    public void updateToken(MemberAutoLoginEntity entity) {
        MemberAutoLoginEntity updateEntity = new MemberAutoLoginEntity();
        updateEntity.setToken(entity.getToken());
        updateEntity.setVersion(entity.getVersion() + 1);
        updateEntity.setUpdateTime(entity.getUpdateTime());
        LambdaUpdateWrapper<MemberAutoLoginEntity> wrapper = Wrappers.lambdaUpdate(MemberAutoLoginEntity.class)
                                                                     .eq(MemberAutoLoginEntity::getSeries, entity.getSeries())
                                                                     .eq(MemberAutoLoginEntity::getVersion, entity.getVersion());
        boolean update = update(updateEntity, wrapper);
        if (!update) {
            log.error("根据乐观锁更新自动登录标识series:{}, {}失败", entity.getSeries(), updateEntity);
            throw new BusinessException(MemberErrorCode.AUTO_LOGIN_UPDATE_FAIL);
        }
    }
    
    @Override
    public void removeUserTokens(String username) {
        this.lambdaUpdate().eq(MemberAutoLoginEntity::getUsername, username).remove();
    }
}
