package daily.boot.gulimall.product.controller;

import daily.boot.common.Result;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.product.entity.SpuInfoEntity;
import daily.boot.gulimall.product.service.SpuInfoService;
import daily.boot.gulimall.product.vo.SpuSaveVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;



/**
 * spu信息
 *
 * @author amy
 * @date 2020-10-14 15:18:58
 */
@RestController
@RequestMapping("/api/product/spuinfo")
@Api(tags = "SpuInfo-spu信息接口")
public class SpuInfoController {
    @Autowired
    private SpuInfoService spuInfoService;

    @GetMapping("/skuId/{skuId}")
    public Result<SpuInfoEntity> getSpuInfoBySkuId(@PathVariable("skuId") Long skuId) {
        SpuInfoEntity spuInfoEntity = spuInfoService.getSpuInfoBySkuId(skuId);
        return Result.ok(spuInfoEntity);
    }
    /**
     * 列表
     */
    @GetMapping("/lists")
    //@RequiresPermissions("product:spuinfo:list")
    @ApiOperation(value = "所有列表")
    public R list(){
        List<SpuInfoEntity> list = spuInfoService.list();

        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "根据主键ID查询")
    //@RequiresPermissions("product:spuinfo:info")
    public R info(@PathVariable("id") Long id){
        SpuInfoEntity spuInfo = spuInfoService.getById(id);

        return R.ok().put("spuInfo", spuInfo);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增数据")
    //@RequiresPermissions("product:spuinfo:save")
    public R save(@RequestBody SpuSaveVo spuSaveVo){
        spuInfoService.saveSpuInfo(spuSaveVo);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改数据")
    //@RequiresPermissions("product:spuinfo:update")
    public R update(@RequestBody SpuInfoEntity spuInfo){
        spuInfoService.updateById(spuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除数据")
    //@RequiresPermissions("product:spuinfo:delete")
    public R delete(@RequestBody Long[] ids){
        spuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 无条件分页查询
     */
    @GetMapping("/list")
    @ApiOperation(value = "条件分页查询", notes = "条件分页查询")
    //@RequiresPermissions("product:spuinfo:pagelist")
    public R pageList(PageQueryVo pageQueryVo, SpuInfoEntity spuInfoEntity){
        PageInfo<SpuInfoEntity> pageInfo = spuInfoService.queryPage(pageQueryVo, spuInfoEntity);
        return R.ok().put("page", pageInfo);
    }
    
    /**
     * 修改
     */
    @PutMapping("/{id}/up")
    @ApiOperation(value = "上架")
    //@RequiresPermissions("product:spuinfo:update")
    public R upSpu(@PathVariable("id") Long id){
        spuInfoService.upStatus(id);
        
        return R.ok();
    }
    
    /**
     * 修改
     */
    @PutMapping("/{id}/down")
    @ApiOperation(value = "下架")
    //@RequiresPermissions("product:spuinfo:update")
    public R downSpu(@PathVariable("id") Long id){
        spuInfoService.updateStatusById(id, 2);
        return R.ok();
    }

}
