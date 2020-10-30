package daily.boot.gulimall.ware.controller;

import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.ware.entity.PurchaseEntity;
import daily.boot.gulimall.ware.service.PurchaseService;
import daily.boot.gulimall.ware.vo.MergeVo;
import daily.boot.gulimall.ware.vo.PurchaseDoneVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;



/**
 * 采购信息
 *
 * @author amy
 * @date 2020-10-14 17:07:54
 */
@RestController
@RequestMapping("ware/purchase")
@Api(tags = "Purchase-采购信息接口")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;
    
    @PostMapping("/done")
    @ApiOperation(value = "采购员完成采购单")
    public R finish(@RequestBody PurchaseDoneVo purchaseDoneVo) {
        purchaseService.done(purchaseDoneVo);
        return R.ok();
    }
    
    @GetMapping("/unreceive/list")
    @ApiOperation(value = "查找还未被领取的采购单")
    public R unreceiveList() {
        List<PurchaseEntity> unreceivePurchase= purchaseService.unreceivePurchase();
        return R.ok().putData(unreceivePurchase);
    }
    
    @PostMapping("/received")
    @ApiOperation(value = "采购员领取采购单")
    public R received(@RequestBody List<Long> ids) {
        purchaseService.received(ids);
        return R.ok();
    }
    
    @PostMapping("/merge")
    @ApiOperation(value = "合并采购需求")
    public R meger(@RequestBody MergeVo mergeVo) {
        purchaseService.mergePurchase(mergeVo);
        return R.ok();
    }

    /**
     * 列表
     */
    @GetMapping("/lists")
    //@RequiresPermissions("ware:purchase:list")
    @ApiOperation(value = "所有列表")
    public R list(){
        List<PurchaseEntity> list = purchaseService.list();

        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "根据主键ID查询")
    //@RequiresPermissions("ware:purchase:info")
    public R info(@PathVariable("id") Long id){
        PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增数据")
    //@RequiresPermissions("ware:purchase:save")
    public R save(@RequestBody PurchaseEntity purchase){
        purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改数据")
    //@RequiresPermissions("ware:purchase:update")
    public R update(@RequestBody PurchaseEntity purchase){
        purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除数据")
    //@RequiresPermissions("ware:purchase:delete")
    public R delete(@RequestBody Long[] ids){
        purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 无条件分页查询
     */
    @GetMapping("/list")
    @ApiOperation(value = "无条件分页查询", notes = "无条件分页查询")
    //@RequiresPermissions("ware:purchase:pagelist")
    public R pageList(PageQueryVo pageQueryVo){
        PageInfo<PurchaseEntity> pageInfo = purchaseService.queryPage(pageQueryVo);
        return R.ok().put("page", pageInfo);
    }

}
