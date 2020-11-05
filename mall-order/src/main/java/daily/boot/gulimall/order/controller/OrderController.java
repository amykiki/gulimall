package daily.boot.gulimall.order.controller;

import daily.boot.common.Result;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.order.entity.OrderEntity;
import daily.boot.gulimall.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;



/**
 * 订单
 *
 * @author amy
 * @date 2020-10-14 16:57:10
 */
@RestController
@RequestMapping("order/order")
@Api(tags = "Order-订单接口")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("order:order:list")
    @ApiOperation(value = "所有列表")
    public R list(){
        List<OrderEntity> list = orderService.list();

        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "根据主键ID查询")
    //@RequiresPermissions("order:order:info")
    public R info(@PathVariable("id") Long id){
        OrderEntity order = orderService.getById(id);

        return R.ok().put("order", order);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增数据")
    //@RequiresPermissions("order:order:save")
    public R save(@RequestBody OrderEntity order){
        orderService.save(order);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改数据")
    //@RequiresPermissions("order:order:update")
    public R update(@RequestBody OrderEntity order){
        orderService.updateById(order);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除数据")
    //@RequiresPermissions("order:order:delete")
    public R delete(@RequestBody Long[] ids){
        orderService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 无条件分页查询
     */
    @PostMapping("/page-list")
    @ApiOperation(value = "无条件分页查询", notes = "无条件分页查询")
    //@RequiresPermissions("order:order:pagelist")
    public R pageList(PageQueryVo pageQueryVo){
        PageInfo<OrderEntity> pageInfo = orderService.queryPage(pageQueryVo);
        return R.ok().put("page", pageInfo);
    }

    @GetMapping("/{memberId}")
    public Result<List<OrderEntity>> getOrdersByMemberId(@PathVariable Long memberId) {
        List<OrderEntity> orders = orderService.getOrdersByMemberId(memberId);
        return Result.ok(orders);
    }
}
