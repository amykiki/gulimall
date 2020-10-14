package daily.boot.gulimall.order.controller;

import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.order.entity.RefundInfoEntity;
import daily.boot.gulimall.order.service.RefundInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;



/**
 * 退款信息
 *
 * @author amy
 * @date 2020-10-14 16:57:10
 */
@RestController
@RequestMapping("order/refundinfo")
@Api(tags = "RefundInfo-退款信息接口")
public class RefundInfoController {
    @Autowired
    private RefundInfoService refundInfoService;

    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("order:refundinfo:list")
    @ApiOperation(value = "所有列表")
    public R list(){
        List<RefundInfoEntity> list = refundInfoService.list();

        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "根据主键ID查询")
    //@RequiresPermissions("order:refundinfo:info")
    public R info(@PathVariable("id") Long id){
        RefundInfoEntity refundInfo = refundInfoService.getById(id);

        return R.ok().put("refundInfo", refundInfo);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增数据")
    //@RequiresPermissions("order:refundinfo:save")
    public R save(@RequestBody RefundInfoEntity refundInfo){
        refundInfoService.save(refundInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改数据")
    //@RequiresPermissions("order:refundinfo:update")
    public R update(@RequestBody RefundInfoEntity refundInfo){
        refundInfoService.updateById(refundInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除数据")
    //@RequiresPermissions("order:refundinfo:delete")
    public R delete(@RequestBody Long[] ids){
        refundInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 无条件分页查询
     */
    @PostMapping("/page-list")
    @ApiOperation(value = "无条件分页查询", notes = "无条件分页查询")
    //@RequiresPermissions("order:refundinfo:pagelist")
    public R pageList(PageQueryVo pageQueryVo){
        PageInfo<RefundInfoEntity> pageInfo = refundInfoService.queryPage(pageQueryVo);
        return R.ok().put("page", pageInfo);
    }

}
