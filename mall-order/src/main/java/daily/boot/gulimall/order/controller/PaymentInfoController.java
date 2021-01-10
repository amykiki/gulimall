package daily.boot.gulimall.order.controller;

import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.order.entity.PaymentInfoEntity;
import daily.boot.gulimall.order.service.PaymentInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;



/**
 * 支付信息表
 *
 * @author amy
 * @date 2020-10-14 16:57:10
 */
@RestController
@RequestMapping("/api/order/paymentinfo")
@Api(tags = "PaymentInfo-支付信息表接口")
public class PaymentInfoController {
    @Autowired
    private PaymentInfoService paymentInfoService;

    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("order:paymentinfo:list")
    @ApiOperation(value = "所有列表")
    public R list(){
        List<PaymentInfoEntity> list = paymentInfoService.list();

        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "根据主键ID查询")
    //@RequiresPermissions("order:paymentinfo:info")
    public R info(@PathVariable("id") Long id){
        PaymentInfoEntity paymentInfo = paymentInfoService.getById(id);

        return R.ok().put("paymentInfo", paymentInfo);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增数据")
    //@RequiresPermissions("order:paymentinfo:save")
    public R save(@RequestBody PaymentInfoEntity paymentInfo){
        paymentInfoService.save(paymentInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改数据")
    //@RequiresPermissions("order:paymentinfo:update")
    public R update(@RequestBody PaymentInfoEntity paymentInfo){
        paymentInfoService.updateById(paymentInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除数据")
    //@RequiresPermissions("order:paymentinfo:delete")
    public R delete(@RequestBody Long[] ids){
        paymentInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 无条件分页查询
     */
    @PostMapping("/page-list")
    @ApiOperation(value = "无条件分页查询", notes = "无条件分页查询")
    //@RequiresPermissions("order:paymentinfo:pagelist")
    public R pageList(PageQueryVo pageQueryVo){
        PageInfo<PaymentInfoEntity> pageInfo = paymentInfoService.queryPage(pageQueryVo);
        return R.ok().put("page", pageInfo);
    }

}
