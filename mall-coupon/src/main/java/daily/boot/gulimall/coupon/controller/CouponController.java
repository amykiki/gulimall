package daily.boot.gulimall.coupon.controller;

import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.coupon.entity.CouponEntity;
import daily.boot.gulimall.coupon.service.CouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;



/**
 * 优惠券信息
 *
 * @author amy
 * @date 2020-10-14 16:05:20
 */
@RestController
@RequestMapping("coupon/coupon")
@Api(tags = "Coupon-优惠券信息接口")
public class CouponController {
    @Autowired
    private CouponService couponService;

    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("coupon:coupon:list")
    @ApiOperation(value = "所有列表")
    public R list(){
        List<CouponEntity> list = couponService.list();

        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "根据主键ID查询")
    //@RequiresPermissions("coupon:coupon:info")
    public R info(@PathVariable("id") Long id){
        CouponEntity coupon = couponService.getById(id);

        return R.ok().put("coupon", coupon);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增数据")
    //@RequiresPermissions("coupon:coupon:save")
    public R save(@RequestBody CouponEntity coupon){
        couponService.save(coupon);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改数据")
    //@RequiresPermissions("coupon:coupon:update")
    public R update(@RequestBody CouponEntity coupon){
        couponService.updateById(coupon);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除数据")
    //@RequiresPermissions("coupon:coupon:delete")
    public R delete(@RequestBody Long[] ids){
        couponService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 无条件分页查询
     */
    @PostMapping("/page-list")
    @ApiOperation(value = "无条件分页查询", notes = "无条件分页查询")
    //@RequiresPermissions("coupon:coupon:pagelist")
    public R pageList(PageQueryVo pageQueryVo){
        PageInfo<CouponEntity> pageInfo = couponService.queryPage(pageQueryVo);
        return R.ok().put("page", pageInfo);
    }

}
