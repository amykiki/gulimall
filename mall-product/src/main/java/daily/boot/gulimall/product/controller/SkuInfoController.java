package daily.boot.gulimall.product.controller;

import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.product.entity.SkuInfoEntity;
import daily.boot.gulimall.product.service.SkuInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;



/**
 * sku信息
 *
 * @author amy
 * @date 2020-10-14 15:18:58
 */
@RestController
@RequestMapping("product/skuinfo")
@Api(tags = "SkuInfo-sku信息接口")
public class SkuInfoController {
    @Autowired
    private SkuInfoService skuInfoService;

    /**
     * 列表
     */
    @GetMapping("/lists")
    //@RequiresPermissions("product:skuinfo:list")
    @ApiOperation(value = "所有列表")
    public R list(){
        List<SkuInfoEntity> list = skuInfoService.list();

        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{skuId}")
    @ApiOperation(value = "根据主键ID查询")
    //@RequiresPermissions("product:skuinfo:info")
    public R info(@PathVariable("skuId") Long skuId){
        SkuInfoEntity skuInfo = skuInfoService.getById(skuId);

        return R.ok().put("skuInfo", skuInfo);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增数据")
    //@RequiresPermissions("product:skuinfo:save")
    public R save(@RequestBody SkuInfoEntity skuInfo){
        skuInfoService.save(skuInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改数据")
    //@RequiresPermissions("product:skuinfo:update")
    public R update(@RequestBody SkuInfoEntity skuInfo){
        skuInfoService.updateById(skuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除数据")
    //@RequiresPermissions("product:skuinfo:delete")
    public R delete(@RequestBody Long[] skuIds){
        skuInfoService.removeByIds(Arrays.asList(skuIds));

        return R.ok();
    }

    /**
     * 无条件分页查询
     */
    @GetMapping("/list")
    @ApiOperation(value = "无条件分页查询", notes = "无条件分页查询")
    //@RequiresPermissions("product:skuinfo:pagelist")
    public R pageList(PageQueryVo pageQueryVo, SkuInfoEntity skuInfoEntity){
        PageInfo<SkuInfoEntity> pageInfo = skuInfoService.queryPage(pageQueryVo, skuInfoEntity);
        return R.ok().put("page", pageInfo);
    }

}
