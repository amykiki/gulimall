package daily.boot.gulimall.ware.controller;

import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.ware.entity.PurchaseDetailEntity;
import daily.boot.gulimall.ware.service.PurchaseDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;



/**
 * 采购详情
 *
 * @author amy
 * @date 2020-10-14 17:07:54
 */
@RestController
@RequestMapping("ware/purchasedetail")
@Api(tags = "PurchaseDetail-采购详情接口")
public class PurchaseDetailController {
    @Autowired
    private PurchaseDetailService purchaseDetailService;

    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("ware:purchasedetail:list")
    @ApiOperation(value = "所有列表")
    public R list(){
        List<PurchaseDetailEntity> list = purchaseDetailService.list();

        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "根据主键ID查询")
    //@RequiresPermissions("ware:purchasedetail:info")
    public R info(@PathVariable("id") Long id){
        PurchaseDetailEntity purchaseDetail = purchaseDetailService.getById(id);

        return R.ok().put("purchaseDetail", purchaseDetail);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增数据")
    //@RequiresPermissions("ware:purchasedetail:save")
    public R save(@RequestBody PurchaseDetailEntity purchaseDetail){
        purchaseDetailService.save(purchaseDetail);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改数据")
    //@RequiresPermissions("ware:purchasedetail:update")
    public R update(@RequestBody PurchaseDetailEntity purchaseDetail){
        purchaseDetailService.updateById(purchaseDetail);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除数据")
    //@RequiresPermissions("ware:purchasedetail:delete")
    public R delete(@RequestBody Long[] ids){
        purchaseDetailService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 无条件分页查询
     */
    @PostMapping("/page-list")
    @ApiOperation(value = "无条件分页查询", notes = "无条件分页查询")
    //@RequiresPermissions("ware:purchasedetail:pagelist")
    public R pageList(PageQueryVo pageQueryVo){
        PageInfo<PurchaseDetailEntity> pageInfo = purchaseDetailService.queryPage(pageQueryVo);
        return R.ok().put("page", pageInfo);
    }

}
