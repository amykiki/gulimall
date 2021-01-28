package daily.boot.gulimall.coupon.controller;

import daily.boot.common.Result;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.coupon.entity.SeckillSessionEntity;
import daily.boot.gulimall.coupon.service.SeckillSessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;



/**
 * 秒杀活动场次
 *
 * @author amy
 * @date 2020-10-14 16:05:21
 */
@RestController
@RequestMapping("/api/coupon/seckillsession")
@Api(tags = "SeckillSession-秒杀活动场次接口")
public class SeckillSessionController {
    @Autowired
    private SeckillSessionService seckillSessionService;
    
    @GetMapping("/Latest3DaySession")
    public Result<List<SeckillSessionEntity>> getLatest3DaySession() {
        List<SeckillSessionEntity> sessionEntities = seckillSessionService.getLatest3DaySession();
        return Result.ok(sessionEntities);
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("coupon:seckillsession:list")
    @ApiOperation(value = "所有列表")
    public R list(){
        List<SeckillSessionEntity> list = seckillSessionService.list();

        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "根据主键ID查询")
    //@RequiresPermissions("coupon:seckillsession:info")
    public R info(@PathVariable("id") Long id){
        SeckillSessionEntity seckillSession = seckillSessionService.getById(id);

        return R.ok().put("seckillSession", seckillSession);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增数据")
    //@RequiresPermissions("coupon:seckillsession:save")
    public R save(@RequestBody SeckillSessionEntity seckillSession){
        seckillSessionService.save(seckillSession);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改数据")
    //@RequiresPermissions("coupon:seckillsession:update")
    public R update(@RequestBody SeckillSessionEntity seckillSession){
        seckillSessionService.updateById(seckillSession);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除数据")
    //@RequiresPermissions("coupon:seckillsession:delete")
    public R delete(@RequestBody Long[] ids){
        seckillSessionService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 无条件分页查询
     */
    @RequestMapping("/page-list")
    @ApiOperation(value = "无条件分页查询", notes = "无条件分页查询")
    //@RequiresPermissions("coupon:seckillsession:pagelist")
    public R pageList(PageQueryVo pageQueryVo){
        PageInfo<SeckillSessionEntity> pageInfo = seckillSessionService.queryPage(pageQueryVo);
        return R.ok().put("page", pageInfo);
    }

}
