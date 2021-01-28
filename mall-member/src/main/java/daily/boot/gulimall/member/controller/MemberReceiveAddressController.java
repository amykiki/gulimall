package daily.boot.gulimall.member.controller;

import daily.boot.common.Result;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.member.entity.MemberReceiveAddressEntity;
import daily.boot.gulimall.member.service.MemberReceiveAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;



/**
 * 会员收货地址
 *
 * @author amy
 * @date 2020-10-14 16:46:51
 */
@RestController
@RequestMapping("/api/member/memberreceiveaddress")
@Api(tags = "MemberReceiveAddress-会员收货地址接口")
public class MemberReceiveAddressController {
    @Autowired
    private MemberReceiveAddressService memberReceiveAddressService;
    
    @GetMapping(value = "/{memberId}/address")
    public Result<List<MemberReceiveAddressEntity>> getAddress(@PathVariable("memberId") Long memberId) {
        List<MemberReceiveAddressEntity> addressList = memberReceiveAddressService.getAddress(memberId);
        return Result.ok(addressList);
    }

    @GetMapping("/addrInfo/{addrId}")
    public Result<MemberReceiveAddressEntity> getByAddrId(@PathVariable("addrId") Long addrId) {
        return Result.ok(memberReceiveAddressService.getById(addrId));
    }
    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("member:memberreceiveaddress:list")
    @ApiOperation(value = "所有列表")
    public R list(){
        List<MemberReceiveAddressEntity> list = memberReceiveAddressService.list();

        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "根据主键ID查询")
    //@RequiresPermissions("member:memberreceiveaddress:info")
    public R info(@PathVariable("id") Long id){
        MemberReceiveAddressEntity memberReceiveAddress = memberReceiveAddressService.getById(id);

        return R.ok().put("memberReceiveAddress", memberReceiveAddress);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增数据")
    //@RequiresPermissions("member:memberreceiveaddress:save")
    public R save(@RequestBody MemberReceiveAddressEntity memberReceiveAddress){
        memberReceiveAddressService.save(memberReceiveAddress);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改数据")
    //@RequiresPermissions("member:memberreceiveaddress:update")
    public R update(@RequestBody MemberReceiveAddressEntity memberReceiveAddress){
        memberReceiveAddressService.updateById(memberReceiveAddress);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除数据")
    //@RequiresPermissions("member:memberreceiveaddress:delete")
    public R delete(@RequestBody Long[] ids){
        memberReceiveAddressService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 无条件分页查询
     */
    @PostMapping("/page-list")
    @ApiOperation(value = "无条件分页查询", notes = "无条件分页查询")
    //@RequiresPermissions("member:memberreceiveaddress:pagelist")
    public R pageList(PageQueryVo pageQueryVo){
        PageInfo<MemberReceiveAddressEntity> pageInfo = memberReceiveAddressService.queryPage(pageQueryVo);
        return R.ok().put("page", pageInfo);
    }

}
