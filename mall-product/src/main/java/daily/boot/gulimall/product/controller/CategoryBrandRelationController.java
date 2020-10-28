package daily.boot.gulimall.product.controller;

import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.product.entity.BrandEntity;
import daily.boot.gulimall.product.entity.CategoryBrandRelationEntity;
import daily.boot.gulimall.product.service.CategoryBrandRelationService;
import daily.boot.gulimall.product.vo.BrandVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 品牌分类关联
 *
 * @author amy
 * @date 2020-10-14 15:18:58
 */
@RestController
@RequestMapping("product/categorybrandrelation")
@Api(tags = "CategoryBrandRelation-品牌分类关联接口")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;
    
    @GetMapping("/catelog/list")
    @ApiOperation(value = "获取当前品牌关联的所有分类列表")
    public R catelogList(@RequestParam("brandId") Long brandId) {
        List<CategoryBrandRelationEntity> data = categoryBrandRelationService.getBrandCategories(brandId);
        return R.ok().put("data", data);
    }
    
    @GetMapping("/brands/list")
    public R relationBrandsList(@RequestParam(value = "catId", required = true) Long catId) {
        List<BrandEntity> brands =categoryBrandRelationService.getBrandsByCatId(catId);
        //brands转成brandVo
        List<BrandVo> vos = brands.stream().map(brand -> {
            BrandVo vo = new BrandVo();
            vo.setBrandId(brand.getBrandId());
            vo.setBrandName(brand.getName());
            return vo;
        }).collect(Collectors.toList());
        return R.ok().putData(vos);
    }
    
    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("product:categorybrandrelation:list")
    @ApiOperation(value = "所有列表")
    public R list(){
        List<CategoryBrandRelationEntity> list = categoryBrandRelationService.list();

        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "根据主键ID查询")
    //@RequiresPermissions("product:categorybrandrelation:info")
    public R info(@PathVariable("id") Long id){
        CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增数据")
    //@RequiresPermissions("product:categorybrandrelation:save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
        //需要获取品牌名&分类名
        categoryBrandRelationService.details(categoryBrandRelation);
        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改数据")
    //@RequiresPermissions("product:categorybrandrelation:update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
        categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除数据")
    //@RequiresPermissions("product:categorybrandrelation:delete")
    public R delete(@RequestBody Long[] ids){
        categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 无条件分页查询
     */
    @PostMapping("/page-list")
    @ApiOperation(value = "无条件分页查询", notes = "无条件分页查询")
    //@RequiresPermissions("product:categorybrandrelation:pagelist")
    public R pageList(PageQueryVo pageQueryVo){
        PageInfo<CategoryBrandRelationEntity> pageInfo = categoryBrandRelationService.queryPage(pageQueryVo);
        return R.ok().put("page", pageInfo);
    }

}
