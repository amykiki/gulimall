package daily.boot.gulimall.member.controller;

import daily.boot.common.Result;
import daily.boot.common.exception.BusinessException;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.member.entity.MemberEntity;
import daily.boot.gulimall.member.exception.MemberErrorCode;
import daily.boot.gulimall.member.service.MemberService;
import daily.boot.gulimall.member.vo.MemberUserRegisterVo;
import daily.boot.gulimall.service.api.feign.OrderFeignService;
import daily.boot.gulimall.service.api.to.MemberFullInfoTo;
import daily.boot.gulimall.service.api.to.MemberUserTo;
import daily.boot.gulimall.service.api.to.OrderTo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * 会员
 *
 * @author amy
 * @date 2020-10-14 16:46:51
 */
@RestController
@RequestMapping("/api/member/member")
@Api(tags = "Member-会员接口")
public class MemberController {
    @Autowired
    private MemberService memberService;
    
    @Autowired
    private OrderFeignService orderFeignService;
    
    @GetMapping("/getMemberFullInfo/{userId}")
    public Result<MemberFullInfoTo> getMemberFullInfo(@PathVariable Long userId) {
        MemberEntity memberEntity = memberService.getMemberFullInfo(userId);
        if (Objects.nonNull(memberEntity)) {
            MemberFullInfoTo to = new MemberFullInfoTo();
            BeanUtils.copyProperties(memberEntity, to);
            return Result.ok(to);
        } else {
            throw new BusinessException(MemberErrorCode.USERID_NOT_EXIST);
        }
    }
    
    /**
     * 注册
     */
    @PostMapping("/register")
    public Result register(@Valid @RequestBody MemberUserRegisterVo registerVo) {
        memberService.register(registerVo);
        return Result.ok();
    }
    
    @GetMapping("/userinfo-by-username")
    public Result<MemberUserTo> getUserInfoByUsername(@RequestParam("username") String username) {
        MemberEntity entity = memberService.getMemberUserInfoByUsername(username);
        if (Objects.nonNull(entity)) {
            MemberUserTo to = new MemberUserTo();
            BeanUtils.copyProperties(entity, to);
            return Result.ok(to);
        } else {
            throw new BusinessException(MemberErrorCode.USERNAME_NOT_EXIST);
        }
    }
    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("member:member:list")
    @ApiOperation(value = "所有列表")
    public R list(){
        List<MemberEntity> list = memberService.list();

        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "根据主键ID查询")
    //@RequiresPermissions("member:member:info")
    public R info(@PathVariable("id") Long id){
        MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增数据")
    //@RequiresPermissions("member:member:save")
    public R save(@RequestBody MemberEntity member){
        memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改数据")
    //@RequiresPermissions("member:member:update")
    public R update(@RequestBody MemberEntity member){
        memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除数据")
    //@RequiresPermissions("member:member:delete")
    public R delete(@RequestBody Long[] ids){
        memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 无条件分页查询
     */
    @PostMapping("/page-list")
    @ApiOperation(value = "无条件分页查询", notes = "无条件分页查询")
    //@RequiresPermissions("member:member:pagelist")
    public R pageList(PageQueryVo pageQueryVo){
        PageInfo<MemberEntity> pageInfo = memberService.queryPage(pageQueryVo);
        return R.ok().put("page", pageInfo);
    }
    
    @GetMapping("/order/order/{memberId}")
    public Result<List<OrderTo>> getOrdersByMemberId(@PathVariable("memberId") Long memberId) {
        Result<List<OrderTo>> orders = orderFeignService.getOrdersByMemberId(memberId);
        return orders;
    }

}
