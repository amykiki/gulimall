package daily.boot.gulimall.product.controller;

import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.product.entity.BrandEntity;
import daily.boot.gulimall.product.service.BrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;



/**
 * 品牌
 * @author amy
 * @date 2020-10-13 16:31:34
 */
@RestController
@RequestMapping("product/brand")
@Api(tags = "品牌接口")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("product:brand:list")
    @ApiOperation(value = "所有列表")
    public R list(){
        List<BrandEntity> list = brandService.list();
    
        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{brandId}")
    @ApiOperation(value = "根据主键ID查询")
    //@RequiresPermissions("product:brand:info")
    public R info(@PathVariable("brandId") Long brandId){
        BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增数据")
    //@RequiresPermissions("product:brand:save")
    public R save(@RequestBody BrandEntity brand){
        brandService.save(brand);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改数据")
    //@RequiresPermissions("product:brand:update")
    public R update(@RequestBody BrandEntity brand){
        brandService.updateById(brand);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除数据")
    //@RequiresPermissions("product:brand:delete")
    public R delete(@RequestBody Long[] brandIds){
        brandService.removeByIds(Arrays.asList(brandIds));
        return R.ok();
    }
    
    /**
     * 通用查询条件分页查询
     */
    @ApiOperation(value = "通用查询条件分页", notes = "通用条件分页查询")
    @GetMapping("/page-list")
    //@RequiresPermissions("product:brand:delete")
    public R pageList(PageQueryVo pageQueryVo){
        PageInfo<BrandEntity> pageInfo = brandService.queryPage(pageQueryVo);
        
        return R.ok().put("page", pageInfo);
    }
    /**
     * 带条件分页查询
     */
    @ApiOperation(value = "带条件分页查询", notes = "分页查询，根据当前页数+每页显示查询，带条件查询")
    @GetMapping("/page-query")
    //@RequiresPermissions("product:brand:delete")
    public R pageQuery(PageQueryVo pageQueryVo, BrandEntity brand){
        PageInfo<BrandEntity> pageInfo = brandService.queryPage(pageQueryVo, brand);
    
        return R.ok().put("page", pageInfo);
    }

}
