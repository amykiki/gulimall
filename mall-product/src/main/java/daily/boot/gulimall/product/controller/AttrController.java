package daily.boot.gulimall.product.controller;

import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.common.valid.ValidateGroup;
import daily.boot.gulimall.product.entity.AttrEntity;
import daily.boot.gulimall.product.service.AttrService;
import daily.boot.gulimall.product.vo.AttrRespVo;
import daily.boot.gulimall.product.vo.AttrVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;



/**
 * 商品属性
 *
 * @author amy
 * @date 2020-10-14 15:18:58
 */
@RestController
@RequestMapping("product/attr")
@Api(tags = "Attr-商品属性接口")
public class AttrController {
    @Autowired
    private AttrService attrService;

    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("product:attr:list")
    @ApiOperation(value = "所有列表")
    public R list(){
        List<AttrEntity> list = attrService.list();

        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{attrId}")
    @ApiOperation(value = "根据主键ID查询")
    //@RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
        AttrRespVo attrRespVo = attrService.getAttrInfo(attrId);

        return R.ok().put("attr", attrRespVo);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增数据")
    //@RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVo attrVo){
        //级联更新
        attrService.saveCascaded(attrVo);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改数据")
    //@RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrVo attrVo){
        attrService.updateAttrInfo(attrVo);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除数据")
    //@RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
        attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

    /**
     * 无条件分页查询
     */
    @PostMapping("/page-list")
    @ApiOperation(value = "无条件分页查询", notes = "无条件分页查询")
    //@RequiresPermissions("product:attr:pagelist")
    public R pageList(PageQueryVo pageQueryVo){
        PageInfo<AttrEntity> pageInfo = attrService.queryPage(pageQueryVo);
        return R.ok().put("page", pageInfo);
    }
    
    @GetMapping("/{attrType}/list/{catelogId}")
    @ApiOperation(value = "根据商品品类分页查询商品属性")
    public R baseAttrList(PageQueryVo pageQueryVo,
                          @PathVariable("attrType") String attrType,
                          @PathVariable("catelogId") Long catelogId) {
        PageInfo<AttrRespVo> pageInfo = attrService.queryPage(pageQueryVo, catelogId, attrType);
        return R.ok().put("page", pageInfo);
        
    }

}
