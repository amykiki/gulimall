package daily.boot.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.member.entity.MemberEntity;
import daily.boot.gulimall.member.vo.MemberUserRegisterVo;
import daily.boot.gulimall.service.api.to.MemberUserTo;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 会员
 *
 * @author amy
 * @date 2020-10-14 16:46:51
 */
@Validated
public interface MemberService extends IService<MemberEntity> {

    /**
     * 无条件翻页查询
     * @param queryVo 分页查询参数
     * @return
     */
    PageInfo<MemberEntity> queryPage(PageQueryVo queryVo);
    
    void register(@NotNull @Valid MemberUserRegisterVo registerVo);
    
    int getMemberCountByUsername(@NotBlank String username);
    int getMemberCountByMobile(@NotBlank String mobile);
    
    int getMemberCountByUsernameOrMobile(@NotBlank String username, @NotBlank String mobile);
    
    
    MemberEntity getMemberUserInfoByUsername(@NotNull(message = "用户名不能为空") String username);
}

