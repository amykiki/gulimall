package daily.boot.gulimall.ware.controller;

import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.ware.entity.WareOrderTaskDetailEntity;
import daily.boot.gulimall.ware.service.WareOrderTaskDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;



/**
 * 库存工作单
 *
 * @author amy
 * @date 2020-10-14 17:07:54
 */
@RestController
@RequestMapping("ware/wareordertaskdetail")
@Api(tags = "WareOrderTaskDetail-库存工作单接口")
public class WareOrderTaskDetailController {
    @Autowired
    private WareOrderTaskDetailService wareOrderTaskDetailService;

    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("ware:wareordertaskdetail:list")
    @ApiOperation(value = "所有列表")
    public R list(){
        List<WareOrderTaskDetailEntity> list = wareOrderTaskDetailService.list();

        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "根据主键ID查询")
    //@RequiresPermissions("ware:wareordertaskdetail:info")
    public R info(@PathVariable("id") Long id){
        WareOrderTaskDetailEntity wareOrderTaskDetail = wareOrderTaskDetailService.getById(id);

        return R.ok().put("wareOrderTaskDetail", wareOrderTaskDetail);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增数据")
    //@RequiresPermissions("ware:wareordertaskdetail:save")
    public R save(@RequestBody WareOrderTaskDetailEntity wareOrderTaskDetail){
        wareOrderTaskDetailService.save(wareOrderTaskDetail);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改数据")
    //@RequiresPermissions("ware:wareordertaskdetail:update")
    public R update(@RequestBody WareOrderTaskDetailEntity wareOrderTaskDetail){
        wareOrderTaskDetailService.updateById(wareOrderTaskDetail);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除数据")
    //@RequiresPermissions("ware:wareordertaskdetail:delete")
    public R delete(@RequestBody Long[] ids){
        wareOrderTaskDetailService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 无条件分页查询
     */
    @PostMapping("/page-list")
    @ApiOperation(value = "无条件分页查询", notes = "无条件分页查询")
    //@RequiresPermissions("ware:wareordertaskdetail:pagelist")
    public R pageList(PageQueryVo pageQueryVo){
        PageInfo<WareOrderTaskDetailEntity> pageInfo = wareOrderTaskDetailService.queryPage(pageQueryVo);
        return R.ok().put("page", pageInfo);
    }

}
