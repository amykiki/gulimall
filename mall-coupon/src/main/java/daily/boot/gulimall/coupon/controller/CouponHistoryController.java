package daily.boot.gulimall.coupon.controller;

import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.coupon.entity.CouponHistoryEntity;
import daily.boot.gulimall.coupon.service.CouponHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;



/**
 * 优惠券领取历史记录
 *
 * @author amy
 * @date 2020-10-14 16:05:20
 */
@RestController
@RequestMapping("coupon/couponhistory")
@Api(tags = "CouponHistory-优惠券领取历史记录接口")
public class CouponHistoryController {
    @Autowired
    private CouponHistoryService couponHistoryService;

    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("coupon:couponhistory:list")
    @ApiOperation(value = "所有列表")
    public R list(){
        List<CouponHistoryEntity> list = couponHistoryService.list();

        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "根据主键ID查询")
    //@RequiresPermissions("coupon:couponhistory:info")
    public R info(@PathVariable("id") Long id){
        CouponHistoryEntity couponHistory = couponHistoryService.getById(id);

        return R.ok().put("couponHistory", couponHistory);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增数据")
    //@RequiresPermissions("coupon:couponhistory:save")
    public R save(@RequestBody CouponHistoryEntity couponHistory){
        couponHistoryService.save(couponHistory);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改数据")
    //@RequiresPermissions("coupon:couponhistory:update")
    public R update(@RequestBody CouponHistoryEntity couponHistory){
        couponHistoryService.updateById(couponHistory);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除数据")
    //@RequiresPermissions("coupon:couponhistory:delete")
    public R delete(@RequestBody Long[] ids){
        couponHistoryService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 无条件分页查询
     */
    @PostMapping("/page-list")
    @ApiOperation(value = "无条件分页查询", notes = "无条件分页查询")
    //@RequiresPermissions("coupon:couponhistory:pagelist")
    public R pageList(PageQueryVo pageQueryVo){
        PageInfo<CouponHistoryEntity> pageInfo = couponHistoryService.queryPage(pageQueryVo);
        return R.ok().put("page", pageInfo);
    }

}
