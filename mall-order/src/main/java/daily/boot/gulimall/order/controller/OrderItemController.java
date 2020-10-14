package daily.boot.gulimall.order.controller;

import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.order.entity.OrderItemEntity;
import daily.boot.gulimall.order.service.OrderItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;



/**
 * 订单项信息
 *
 * @author amy
 * @date 2020-10-14 16:57:10
 */
@RestController
@RequestMapping("order/orderitem")
@Api(tags = "OrderItem-订单项信息接口")
public class OrderItemController {
    @Autowired
    private OrderItemService orderItemService;

    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("order:orderitem:list")
    @ApiOperation(value = "所有列表")
    public R list(){
        List<OrderItemEntity> list = orderItemService.list();

        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "根据主键ID查询")
    //@RequiresPermissions("order:orderitem:info")
    public R info(@PathVariable("id") Long id){
        OrderItemEntity orderItem = orderItemService.getById(id);

        return R.ok().put("orderItem", orderItem);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增数据")
    //@RequiresPermissions("order:orderitem:save")
    public R save(@RequestBody OrderItemEntity orderItem){
        orderItemService.save(orderItem);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改数据")
    //@RequiresPermissions("order:orderitem:update")
    public R update(@RequestBody OrderItemEntity orderItem){
        orderItemService.updateById(orderItem);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除数据")
    //@RequiresPermissions("order:orderitem:delete")
    public R delete(@RequestBody Long[] ids){
        orderItemService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 无条件分页查询
     */
    @PostMapping("/page-list")
    @ApiOperation(value = "无条件分页查询", notes = "无条件分页查询")
    //@RequiresPermissions("order:orderitem:pagelist")
    public R pageList(PageQueryVo pageQueryVo){
        PageInfo<OrderItemEntity> pageInfo = orderItemService.queryPage(pageQueryVo);
        return R.ok().put("page", pageInfo);
    }

}
