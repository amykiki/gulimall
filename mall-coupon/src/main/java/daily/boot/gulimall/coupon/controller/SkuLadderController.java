package daily.boot.gulimall.coupon.controller;

import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.coupon.entity.SkuLadderEntity;
import daily.boot.gulimall.coupon.service.SkuLadderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;



/**
 * 商品阶梯价格
 *
 * @author amy
 * @date 2020-10-14 16:05:20
 */
@RestController
@RequestMapping("coupon/skuladder")
@Api(tags = "SkuLadder-商品阶梯价格接口")
public class SkuLadderController {
    @Autowired
    private SkuLadderService skuLadderService;

    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("coupon:skuladder:list")
    @ApiOperation(value = "所有列表")
    public R list(){
        List<SkuLadderEntity> list = skuLadderService.list();

        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "根据主键ID查询")
    //@RequiresPermissions("coupon:skuladder:info")
    public R info(@PathVariable("id") Long id){
        SkuLadderEntity skuLadder = skuLadderService.getById(id);

        return R.ok().put("skuLadder", skuLadder);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增数据")
    //@RequiresPermissions("coupon:skuladder:save")
    public R save(@RequestBody SkuLadderEntity skuLadder){
        skuLadderService.save(skuLadder);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改数据")
    //@RequiresPermissions("coupon:skuladder:update")
    public R update(@RequestBody SkuLadderEntity skuLadder){
        skuLadderService.updateById(skuLadder);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除数据")
    //@RequiresPermissions("coupon:skuladder:delete")
    public R delete(@RequestBody Long[] ids){
        skuLadderService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 无条件分页查询
     */
    @PostMapping("/page-list")
    @ApiOperation(value = "无条件分页查询", notes = "无条件分页查询")
    //@RequiresPermissions("coupon:skuladder:pagelist")
    public R pageList(PageQueryVo pageQueryVo){
        PageInfo<SkuLadderEntity> pageInfo = skuLadderService.queryPage(pageQueryVo);
        return R.ok().put("page", pageInfo);
    }

}
