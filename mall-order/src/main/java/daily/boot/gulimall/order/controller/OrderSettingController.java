package daily.boot.gulimall.order.controller;

import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.order.entity.OrderSettingEntity;
import daily.boot.gulimall.order.service.OrderSettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;



/**
 * 订单配置信息
 *
 * @author amy
 * @date 2020-10-14 16:57:10
 */
@RestController
@RequestMapping("/api/order/ordersetting")
@Api(tags = "OrderSetting-订单配置信息接口")
public class OrderSettingController {
    @Autowired
    private OrderSettingService orderSettingService;

    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("order:ordersetting:list")
    @ApiOperation(value = "所有列表")
    public R list(){
        List<OrderSettingEntity> list = orderSettingService.list();

        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "根据主键ID查询")
    //@RequiresPermissions("order:ordersetting:info")
    public R info(@PathVariable("id") Long id){
        OrderSettingEntity orderSetting = orderSettingService.getById(id);

        return R.ok().put("orderSetting", orderSetting);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增数据")
    //@RequiresPermissions("order:ordersetting:save")
    public R save(@RequestBody OrderSettingEntity orderSetting){
        orderSettingService.save(orderSetting);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改数据")
    //@RequiresPermissions("order:ordersetting:update")
    public R update(@RequestBody OrderSettingEntity orderSetting){
        orderSettingService.updateById(orderSetting);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除数据")
    //@RequiresPermissions("order:ordersetting:delete")
    public R delete(@RequestBody Long[] ids){
        orderSettingService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 无条件分页查询
     */
    @PostMapping("/page-list")
    @ApiOperation(value = "无条件分页查询", notes = "无条件分页查询")
    //@RequiresPermissions("order:ordersetting:pagelist")
    public R pageList(PageQueryVo pageQueryVo){
        PageInfo<OrderSettingEntity> pageInfo = orderSettingService.queryPage(pageQueryVo);
        return R.ok().put("page", pageInfo);
    }

}
