package daily.boot.gulimall.member.controller;

import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.member.entity.IntegrationChangeHistoryEntity;
import daily.boot.gulimall.member.service.IntegrationChangeHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;



/**
 * 积分变化历史记录
 *
 * @author amy
 * @date 2020-10-14 16:46:51
 */
@RestController
@RequestMapping("/api/member/integrationchangehistory")
@Api(tags = "IntegrationChangeHistory-积分变化历史记录接口")
public class IntegrationChangeHistoryController {
    @Autowired
    private IntegrationChangeHistoryService integrationChangeHistoryService;

    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("member:integrationchangehistory:list")
    @ApiOperation(value = "所有列表")
    public R list(){
        List<IntegrationChangeHistoryEntity> list = integrationChangeHistoryService.list();

        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "根据主键ID查询")
    //@RequiresPermissions("member:integrationchangehistory:info")
    public R info(@PathVariable("id") Long id){
        IntegrationChangeHistoryEntity integrationChangeHistory = integrationChangeHistoryService.getById(id);

        return R.ok().put("integrationChangeHistory", integrationChangeHistory);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增数据")
    //@RequiresPermissions("member:integrationchangehistory:save")
    public R save(@RequestBody IntegrationChangeHistoryEntity integrationChangeHistory){
        integrationChangeHistoryService.save(integrationChangeHistory);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改数据")
    //@RequiresPermissions("member:integrationchangehistory:update")
    public R update(@RequestBody IntegrationChangeHistoryEntity integrationChangeHistory){
        integrationChangeHistoryService.updateById(integrationChangeHistory);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除数据")
    //@RequiresPermissions("member:integrationchangehistory:delete")
    public R delete(@RequestBody Long[] ids){
        integrationChangeHistoryService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 无条件分页查询
     */
    @PostMapping("/page-list")
    @ApiOperation(value = "无条件分页查询", notes = "无条件分页查询")
    //@RequiresPermissions("member:integrationchangehistory:pagelist")
    public R pageList(PageQueryVo pageQueryVo){
        PageInfo<IntegrationChangeHistoryEntity> pageInfo = integrationChangeHistoryService.queryPage(pageQueryVo);
        return R.ok().put("page", pageInfo);
    }

}
