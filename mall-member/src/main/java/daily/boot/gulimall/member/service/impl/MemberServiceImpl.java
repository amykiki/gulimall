package daily.boot.gulimall.member.service.impl;

import daily.boot.common.exception.BusinessException;
import daily.boot.gulimall.common.constant.MemberConstant;
import daily.boot.gulimall.member.entity.MemberLevelEntity;
import daily.boot.gulimall.member.exception.MemberErrorCode;
import daily.boot.gulimall.member.service.MemberLevelService;
import daily.boot.gulimall.member.vo.MemberUserRegisterVo;
import daily.boot.gulimall.service.api.to.MemberFullInfoTo;
import daily.boot.gulimall.service.api.to.MemberUserTo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.member.dao.MemberDao;
import daily.boot.gulimall.member.entity.MemberEntity;
import daily.boot.gulimall.member.service.MemberService;

import javax.validation.Valid;
import java.util.Date;
import java.util.Objects;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {
    @Autowired
    private MemberLevelService memberLevelService;

    @Override
    public PageInfo<MemberEntity> queryPage(PageQueryVo queryVo) {
        IPage<MemberEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }
    
    @Override
    public void register(MemberUserRegisterVo registerVo) {
        //确认手机号和用户名的唯一性
        // TODO: 2020/11/25 分库分表情况下，怎么确保唯一，使用分布式锁?
        int memberCount = getMemberCountByUsernameOrMobile(registerVo.getUserName(), registerVo.getPhone());
        if (memberCount > 0) {
            int memberCountByUsername = getMemberCountByUsername(registerVo.getUserName());
            if (memberCountByUsername > 0) {
                throw new BusinessException(MemberErrorCode.USERNAME_EXIST);
            }else {
                throw new BusinessException(MemberErrorCode.PHONE_EXIST);
            }
        }
        
        MemberEntity memberEntity = new MemberEntity();
        
        //设置默认等级
        MemberLevelEntity defaultMemberLevelEntity = memberLevelService.defaultLevel();
        memberEntity.setLevelId(defaultMemberLevelEntity.getId());
        
        //设置其他默认信息
        memberEntity.setNickname(registerVo.getUserName());
        memberEntity.setCreateTime(new Date());
        memberEntity.setGender(0);
        memberEntity.setStatus(MemberConstant.MemberStatus.INUSE.getCode());
        memberEntity.setUsername(registerVo.getUserName());
        memberEntity.setMobile(registerVo.getPhone());
        
        //密码进行MD5加盐加密
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodePwd = bCryptPasswordEncoder.encode(registerVo.getPassword());
        memberEntity.setPassword(encodePwd);
        
        //保存数据
        this.save(memberEntity);
    }
    
    @Override
    public int getMemberCountByUsernameOrMobile(String username, String mobile) {
        return this.lambdaQuery().eq(MemberEntity::getUsername, username).or().eq(MemberEntity::getMobile, mobile).count();
    }
    
    @Override
    public MemberEntity getMemberUserInfoByUsername(String username) {
        return this.baseMapper.getMemberUserInfo(username);
    }
    
    @Override
    public int getMemberCountByUsername(String username) {
        return this.lambdaQuery().eq(MemberEntity::getUsername, username).count();
    }
    
    @Override
    public int getMemberCountByMobile(String mobile) {
        return this.lambdaQuery().eq(MemberEntity::getMobile, mobile).count();
    }
    
    @Override
    public MemberEntity getMemberFullInfo(Long userId) {
        return this.baseMapper.getMemberFullInfo(userId);
    }
}