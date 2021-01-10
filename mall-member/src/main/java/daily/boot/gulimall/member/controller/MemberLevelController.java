package daily.boot.gulimall.member.controller;

import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.member.entity.MemberLevelEntity;
import daily.boot.gulimall.member.service.MemberLevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;



/**
 * 会员等级
 *
 * @author amy
 * @date 2020-10-14 16:46:51
 */
@RestController
@RequestMapping("/api/member/memberlevel")
@Api(tags = "MemberLevel-会员等级接口")
public class MemberLevelController {
    @Autowired
    private MemberLevelService memberLevelService;

    /**
     * 列表
     */
    @GetMapping("/lists")
    //@RequiresPermissions("member:memberlevel:list")
    @ApiOperation(value = "所有列表")
    public R list(){
        List<MemberLevelEntity> list = memberLevelService.list();

        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "根据主键ID查询")
    //@RequiresPermissions("member:memberlevel:info")
    public R info(@PathVariable("id") Long id){
        MemberLevelEntity memberLevel = memberLevelService.getById(id);

        return R.ok().put("memberLevel", memberLevel);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增数据")
    //@RequiresPermissions("member:memberlevel:save")
    public R save(@RequestBody MemberLevelEntity memberLevel){
        memberLevelService.save(memberLevel);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改数据")
    //@RequiresPermissions("member:memberlevel:update")
    public R update(@RequestBody MemberLevelEntity memberLevel){
        memberLevelService.updateById(memberLevel);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除数据")
    //@RequiresPermissions("member:memberlevel:delete")
    public R delete(@RequestBody Long[] ids){
        memberLevelService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 无条件分页查询
     */
    @GetMapping("/list")
    @ApiOperation(value = "无条件分页查询", notes = "无条件分页查询")
    //@RequiresPermissions("member:memberlevel:pagelist")
    public R pageList(PageQueryVo pageQueryVo){
        PageInfo<MemberLevelEntity> pageInfo = memberLevelService.queryPage(pageQueryVo);
        return R.ok().put("page", pageInfo);
    }

}
