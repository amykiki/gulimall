package daily.boot.gulimall.member.controller;

import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.member.entity.MemberCollectSpuEntity;
import daily.boot.gulimall.member.service.MemberCollectSpuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;



/**
 * 会员收藏的商品
 *
 * @author amy
 * @date 2020-10-14 16:46:51
 */
@RestController
@RequestMapping("/api/member/membercollectspu")
@Api(tags = "MemberCollectSpu-会员收藏的商品接口")
public class MemberCollectSpuController {
    @Autowired
    private MemberCollectSpuService memberCollectSpuService;

    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("member:membercollectspu:list")
    @ApiOperation(value = "所有列表")
    public R list(){
        List<MemberCollectSpuEntity> list = memberCollectSpuService.list();

        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "根据主键ID查询")
    //@RequiresPermissions("member:membercollectspu:info")
    public R info(@PathVariable("id") Long id){
        MemberCollectSpuEntity memberCollectSpu = memberCollectSpuService.getById(id);

        return R.ok().put("memberCollectSpu", memberCollectSpu);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增数据")
    //@RequiresPermissions("member:membercollectspu:save")
    public R save(@RequestBody MemberCollectSpuEntity memberCollectSpu){
        memberCollectSpuService.save(memberCollectSpu);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改数据")
    //@RequiresPermissions("member:membercollectspu:update")
    public R update(@RequestBody MemberCollectSpuEntity memberCollectSpu){
        memberCollectSpuService.updateById(memberCollectSpu);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除数据")
    //@RequiresPermissions("member:membercollectspu:delete")
    public R delete(@RequestBody Long[] ids){
        memberCollectSpuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 无条件分页查询
     */
    @PostMapping("/page-list")
    @ApiOperation(value = "无条件分页查询", notes = "无条件分页查询")
    //@RequiresPermissions("member:membercollectspu:pagelist")
    public R pageList(PageQueryVo pageQueryVo){
        PageInfo<MemberCollectSpuEntity> pageInfo = memberCollectSpuService.queryPage(pageQueryVo);
        return R.ok().put("page", pageInfo);
    }

}
