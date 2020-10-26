package daily.boot.gulimall.product.controller;

import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.product.entity.AttrEntity;
import daily.boot.gulimall.product.entity.AttrGroupEntity;
import daily.boot.gulimall.product.service.AttrAttrgroupRelationService;
import daily.boot.gulimall.product.service.AttrGroupService;
import daily.boot.gulimall.product.service.AttrService;
import daily.boot.gulimall.product.service.CategoryService;
import daily.boot.gulimall.product.vo.AttrGroupRelationVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;



/**
 * 属性分组
 *
 * @author amy
 * @date 2020-10-14 15:18:58
 */
@RestController
@RequestMapping("product/attrgroup")
@Api(tags = "AttrGroup-属性分组接口")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AttrService attrService;
    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("product:attrgroup:list")
    @ApiOperation(value = "所有列表")
    public R list(){
        List<AttrGroupEntity> list = attrGroupService.list();

        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{attrGroupId}")
    @ApiOperation(value = "根据主键ID查询")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        //获取属性对应的完整三级分类路径
        Long catelogId = attrGroup.getCatelogId();
        Long[] catelogPaths = categoryService.findCatelogPath(catelogId);
        attrGroup.setCatelogPath(catelogPaths);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增数据")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
        attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改数据")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
        attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除数据")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }
    
    /**
     * 通用查询条件分页查询
     */
    @ApiOperation(value = "通用查询条件分页", notes = "通用条件分页查询")
    @GetMapping("/page-list/{categoryId}")
    //@RequiresPermissions("product:attrgroup:pagelist")
    public R pageList(PageQueryVo pageQueryVo, @PathVariable("categoryId") Long categoryId){
        PageInfo<AttrGroupEntity> pageInfo = attrGroupService.queryPage(pageQueryVo, categoryId);
        return R.ok().put("page", pageInfo);
    }
    
    @ApiOperation(value = "查询分组属性关联的所有规格参数")
    @GetMapping("/{attrGroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrGroupId") Long attrGroupId) {
        List<AttrEntity> attrEntities = attrService.getRelationAttr(attrGroupId);
        return R.ok().putData(attrEntities);
    }
    
    @ApiOperation(value = "删除分组属性关联的规格参数")
    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrGroupRelationVo[] attrGroupRelationVos) {
        attrAttrgroupRelationService.deleteRelation(attrGroupRelationVos);
        return R.ok();
    }
    
    @GetMapping("/{attrGroupId}/noattr/relation")
    public R attrNoRelation(PageQueryVo pageQueryVo, @PathVariable("attrGroupId") Long attrGroupId) {
        PageInfo<AttrEntity> pageInfo = attrService.getNoAttrRelation(pageQueryVo, attrGroupId);
        return R.ok().put("page", pageInfo);
    }
}
