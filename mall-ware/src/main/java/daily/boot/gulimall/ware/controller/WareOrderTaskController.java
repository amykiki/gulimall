package daily.boot.gulimall.ware.controller;

import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.ware.entity.WareOrderTaskEntity;
import daily.boot.gulimall.ware.service.WareOrderTaskService;
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
@RequestMapping("ware/wareordertask")
@Api(tags = "WareOrderTask-库存工作单接口")
public class WareOrderTaskController {
    @Autowired
    private WareOrderTaskService wareOrderTaskService;

    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("ware:wareordertask:list")
    @ApiOperation(value = "所有列表")
    public R list(){
        List<WareOrderTaskEntity> list = wareOrderTaskService.list();

        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "根据主键ID查询")
    //@RequiresPermissions("ware:wareordertask:info")
    public R info(@PathVariable("id") Long id){
        WareOrderTaskEntity wareOrderTask = wareOrderTaskService.getById(id);

        return R.ok().put("wareOrderTask", wareOrderTask);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增数据")
    //@RequiresPermissions("ware:wareordertask:save")
    public R save(@RequestBody WareOrderTaskEntity wareOrderTask){
        wareOrderTaskService.save(wareOrderTask);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改数据")
    //@RequiresPermissions("ware:wareordertask:update")
    public R update(@RequestBody WareOrderTaskEntity wareOrderTask){
        wareOrderTaskService.updateById(wareOrderTask);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除数据")
    //@RequiresPermissions("ware:wareordertask:delete")
    public R delete(@RequestBody Long[] ids){
        wareOrderTaskService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 无条件分页查询
     */
    @PostMapping("/page-list")
    @ApiOperation(value = "无条件分页查询", notes = "无条件分页查询")
    //@RequiresPermissions("ware:wareordertask:pagelist")
    public R pageList(PageQueryVo pageQueryVo){
        PageInfo<WareOrderTaskEntity> pageInfo = wareOrderTaskService.queryPage(pageQueryVo);
        return R.ok().put("page", pageInfo);
    }

}
