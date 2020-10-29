package daily.boot.gulimall.coupon.controller;

import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.coupon.entity.SpuBoundsEntity;
import daily.boot.gulimall.coupon.service.SpuBoundsService;
import daily.boot.gulimall.service.api.to.SpuBoundsTo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;



/**
 * 商品spu积分设置
 *
 * @author amy
 * @date 2020-10-14 16:05:20
 */
@RestController
@RequestMapping("coupon/spubounds")
@Api(tags = "SpuBounds-商品spu积分设置接口")
public class SpuBoundsController {
    @Autowired
    private SpuBoundsService spuBoundsService;
    
    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("coupon:spubounds:list")
    @ApiOperation(value = "所有列表")
    public R list(){
        List<SpuBoundsEntity> list = spuBoundsService.list();

        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "根据主键ID查询")
    //@RequiresPermissions("coupon:spubounds:info")
    public R info(@PathVariable("id") Long id){
        SpuBoundsEntity spuBounds = spuBoundsService.getById(id);

        return R.ok().put("spuBounds", spuBounds);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增数据")
    //@RequiresPermissions("coupon:spubounds:save")
    public R save(@RequestBody SpuBoundsEntity spuBounds){
        spuBoundsService.save(spuBounds);
        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改数据")
    //@RequiresPermissions("coupon:spubounds:update")
    public R update(@RequestBody SpuBoundsEntity spuBounds){
        spuBoundsService.updateById(spuBounds);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除数据")
    //@RequiresPermissions("coupon:spubounds:delete")
    public R delete(@RequestBody Long[] ids){
        spuBoundsService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 无条件分页查询
     */
    @PostMapping("/page-list")
    @ApiOperation(value = "无条件分页查询", notes = "无条件分页查询")
    //@RequiresPermissions("coupon:spubounds:pagelist")
    public R pageList(PageQueryVo pageQueryVo){
        PageInfo<SpuBoundsEntity> pageInfo = spuBoundsService.queryPage(pageQueryVo);
        return R.ok().put("page", pageInfo);
    }

}
