package daily.boot.gulimall.member.controller;

import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.member.entity.MemberLoginLogEntity;
import daily.boot.gulimall.member.service.MemberLoginLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;



/**
 * 会员登录记录
 *
 * @author amy
 * @date 2020-10-14 16:46:51
 */
@RestController
@RequestMapping("/api/member/memberloginlog")
@Api(tags = "MemberLoginLog-会员登录记录接口")
public class MemberLoginLogController {
    @Autowired
    private MemberLoginLogService memberLoginLogService;

    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("member:memberloginlog:list")
    @ApiOperation(value = "所有列表")
    public R list(){
        List<MemberLoginLogEntity> list = memberLoginLogService.list();

        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "根据主键ID查询")
    //@RequiresPermissions("member:memberloginlog:info")
    public R info(@PathVariable("id") Long id){
        MemberLoginLogEntity memberLoginLog = memberLoginLogService.getById(id);

        return R.ok().put("memberLoginLog", memberLoginLog);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增数据")
    //@RequiresPermissions("member:memberloginlog:save")
    public R save(@RequestBody MemberLoginLogEntity memberLoginLog){
        memberLoginLogService.save(memberLoginLog);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改数据")
    //@RequiresPermissions("member:memberloginlog:update")
    public R update(@RequestBody MemberLoginLogEntity memberLoginLog){
        memberLoginLogService.updateById(memberLoginLog);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除数据")
    //@RequiresPermissions("member:memberloginlog:delete")
    public R delete(@RequestBody Long[] ids){
        memberLoginLogService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 无条件分页查询
     */
    @PostMapping("/page-list")
    @ApiOperation(value = "无条件分页查询", notes = "无条件分页查询")
    //@RequiresPermissions("member:memberloginlog:pagelist")
    public R pageList(PageQueryVo pageQueryVo){
        PageInfo<MemberLoginLogEntity> pageInfo = memberLoginLogService.queryPage(pageQueryVo);
        return R.ok().put("page", pageInfo);
    }

}
