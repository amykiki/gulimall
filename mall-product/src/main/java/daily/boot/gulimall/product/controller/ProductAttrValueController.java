package daily.boot.gulimall.product.controller;

import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.product.entity.ProductAttrValueEntity;
import daily.boot.gulimall.product.service.ProductAttrValueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;



/**
 * spu属性值
 *
 * @author amy
 * @date 2020-10-14 15:18:58
 */
@RestController
@RequestMapping("product/productattrvalue")
@Api(tags = "ProductAttrValue-spu属性值接口")
public class ProductAttrValueController {
    @Autowired
    private ProductAttrValueService productAttrValueService;

    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("product:productattrvalue:list")
    @ApiOperation(value = "所有列表")
    public R list(){
        List<ProductAttrValueEntity> list = productAttrValueService.list();

        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "根据主键ID查询")
    //@RequiresPermissions("product:productattrvalue:info")
    public R info(@PathVariable("id") Long id){
        ProductAttrValueEntity productAttrValue = productAttrValueService.getById(id);

        return R.ok().put("productAttrValue", productAttrValue);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增数据")
    //@RequiresPermissions("product:productattrvalue:save")
    public R save(@RequestBody ProductAttrValueEntity productAttrValue){
        productAttrValueService.save(productAttrValue);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改数据")
    //@RequiresPermissions("product:productattrvalue:update")
    public R update(@RequestBody ProductAttrValueEntity productAttrValue){
        productAttrValueService.updateById(productAttrValue);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除数据")
    //@RequiresPermissions("product:productattrvalue:delete")
    public R delete(@RequestBody Long[] ids){
        productAttrValueService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 无条件分页查询
     */
    @PostMapping("/page-list")
    @ApiOperation(value = "无条件分页查询", notes = "无条件分页查询")
    //@RequiresPermissions("product:productattrvalue:pagelist")
    public R pageList(PageQueryVo pageQueryVo){
        PageInfo<ProductAttrValueEntity> pageInfo = productAttrValueService.queryPage(pageQueryVo);
        return R.ok().put("page", pageInfo);
    }
    
    @GetMapping("/listforspu/{spuId}")
    @ApiOperation(value = "根据spuId查询商品规格属性")
    public R baseAttrForSpu(@PathVariable("spuId") Long spuId) {
        List<ProductAttrValueEntity> list = productAttrValueService.listBySpuId(spuId);
        return R.ok().putData(list);
    }
    
    @PostMapping("/update/{spuId}")
    @ApiOperation(value = "修改spu的商品规格属性")
    public R updateBySpuId(@PathVariable("spuId") Long spuId, @RequestBody List<ProductAttrValueEntity> productAttrValueEntities) {
        productAttrValueService.updateBySpuId(spuId, productAttrValueEntities);
        return R.ok();
    }
    

}
