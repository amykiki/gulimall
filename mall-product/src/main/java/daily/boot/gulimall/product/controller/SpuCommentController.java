package daily.boot.gulimall.product.controller;

import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.product.entity.SpuCommentEntity;
import daily.boot.gulimall.product.service.SpuCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;



/**
 * 商品评价
 *
 * @author amy
 * @date 2020-10-14 15:18:58
 */
@RestController
@RequestMapping("product/spucomment")
@Api(tags = "SpuComment-商品评价接口")
public class SpuCommentController {
    @Autowired
    private SpuCommentService spuCommentService;

    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("product:spucomment:list")
    @ApiOperation(value = "所有列表")
    public R list(){
        List<SpuCommentEntity> list = spuCommentService.list();

        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "根据主键ID查询")
    //@RequiresPermissions("product:spucomment:info")
    public R info(@PathVariable("id") Long id){
        SpuCommentEntity spuComment = spuCommentService.getById(id);

        return R.ok().put("spuComment", spuComment);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增数据")
    //@RequiresPermissions("product:spucomment:save")
    public R save(@RequestBody SpuCommentEntity spuComment){
        spuCommentService.save(spuComment);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改数据")
    //@RequiresPermissions("product:spucomment:update")
    public R update(@RequestBody SpuCommentEntity spuComment){
        spuCommentService.updateById(spuComment);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除数据")
    //@RequiresPermissions("product:spucomment:delete")
    public R delete(@RequestBody Long[] ids){
        spuCommentService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 无条件分页查询
     */
    @PostMapping("/page-list")
    @ApiOperation(value = "无条件分页查询", notes = "无条件分页查询")
    //@RequiresPermissions("product:spucomment:pagelist")
    public R pageList(PageQueryVo pageQueryVo){
        PageInfo<SpuCommentEntity> pageInfo = spuCommentService.queryPage(pageQueryVo);
        return R.ok().put("page", pageInfo);
    }

}
