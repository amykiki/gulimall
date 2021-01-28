package daily.boot.gulimall.coupon.controller;

import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.coupon.entity.SkuFullReductionEntity;
import daily.boot.gulimall.coupon.service.SkuFullReductionService;
import daily.boot.gulimall.service.api.to.SkuReductionTo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;



/**
 * 商品满减信息
 *
 * @author amy
 * @date 2020-10-14 16:05:20
 */
@RestController
@RequestMapping("/api/coupon/skufullreduction")
@Api(tags = "SkuFullReduction-商品满减信息接口")
public class SkuFullReductionController {
    @Autowired
    private SkuFullReductionService skuFullReductionService;
    
    @PostMapping("/saveSkuReduction")
    public R saveSkuReduction(@RequestBody List<SkuReductionTo> skuReductionTos) {
        skuFullReductionService.saveSkuReduction(skuReductionTos);
        return R.ok();
    }
    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("coupon:skufullreduction:list")
    @ApiOperation(value = "所有列表")
    public R list(){
        List<SkuFullReductionEntity> list = skuFullReductionService.list();

        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "根据主键ID查询")
    //@RequiresPermissions("coupon:skufullreduction:info")
    public R info(@PathVariable("id") Long id){
        SkuFullReductionEntity skuFullReduction = skuFullReductionService.getById(id);

        return R.ok().put("skuFullReduction", skuFullReduction);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增数据")
    //@RequiresPermissions("coupon:skufullreduction:save")
    public R save(@RequestBody SkuFullReductionEntity skuFullReduction){
        skuFullReductionService.save(skuFullReduction);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改数据")
    //@RequiresPermissions("coupon:skufullreduction:update")
    public R update(@RequestBody SkuFullReductionEntity skuFullReduction){
        skuFullReductionService.updateById(skuFullReduction);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除数据")
    //@RequiresPermissions("coupon:skufullreduction:delete")
    public R delete(@RequestBody Long[] ids){
        skuFullReductionService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 无条件分页查询
     */
    @PostMapping("/page-list")
    @ApiOperation(value = "无条件分页查询", notes = "无条件分页查询")
    //@RequiresPermissions("coupon:skufullreduction:pagelist")
    public R pageList(PageQueryVo pageQueryVo){
        PageInfo<SkuFullReductionEntity> pageInfo = skuFullReductionService.queryPage(pageQueryVo);
        return R.ok().put("page", pageInfo);
    }

}
