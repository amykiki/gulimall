package daily.boot.gulimall.product.controller;

import daily.boot.common.Result;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.product.entity.SkuSaleAttrValueEntity;
import daily.boot.gulimall.product.service.SkuSaleAttrValueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;



/**
 * sku销售属性&值
 *
 * @author amy
 * @date 2020-10-14 15:18:58
 */
@RestController
@RequestMapping("product/skusaleattrvalue")
@Api(tags = "SkuSaleAttrValue-sku销售属性&值接口")
public class SkuSaleAttrValueController {
    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;
    
    @GetMapping("/stringList/{skuId}")
    public Result<List<String>> getSKuSaleAttrValues(@PathVariable("skuId") Long skuId) {
        List<String> stringList = skuSaleAttrValueService.getSkuSaleAttrValuesAsString(skuId);
        return Result.ok(stringList);
    }
    
    @GetMapping("/listbySkuIds")
    public Result<List<SkuSaleAttrValueEntity>> listBySkuIds(@RequestParam("skuIds") List<Long> skuIds) {
        List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = skuSaleAttrValueService.listBySkuIds(skuIds);
        return Result.ok(skuSaleAttrValueEntities);
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("product:skusaleattrvalue:list")
    @ApiOperation(value = "所有列表")
    public R list(){
        List<SkuSaleAttrValueEntity> list = skuSaleAttrValueService.list();

        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "根据主键ID查询")
    //@RequiresPermissions("product:skusaleattrvalue:info")
    public R info(@PathVariable("id") Long id){
        SkuSaleAttrValueEntity skuSaleAttrValue = skuSaleAttrValueService.getById(id);

        return R.ok().put("skuSaleAttrValue", skuSaleAttrValue);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增数据")
    //@RequiresPermissions("product:skusaleattrvalue:save")
    public R save(@RequestBody SkuSaleAttrValueEntity skuSaleAttrValue){
        skuSaleAttrValueService.save(skuSaleAttrValue);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改数据")
    //@RequiresPermissions("product:skusaleattrvalue:update")
    public R update(@RequestBody SkuSaleAttrValueEntity skuSaleAttrValue){
        skuSaleAttrValueService.updateById(skuSaleAttrValue);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除数据")
    //@RequiresPermissions("product:skusaleattrvalue:delete")
    public R delete(@RequestBody Long[] ids){
        skuSaleAttrValueService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 无条件分页查询
     */
    @PostMapping("/page-list")
    @ApiOperation(value = "无条件分页查询", notes = "无条件分页查询")
    //@RequiresPermissions("product:skusaleattrvalue:pagelist")
    public R pageList(PageQueryVo pageQueryVo){
        PageInfo<SkuSaleAttrValueEntity> pageInfo = skuSaleAttrValueService.queryPage(pageQueryVo);
        return R.ok().put("page", pageInfo);
    }

}
