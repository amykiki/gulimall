package daily.boot.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.ware.dao.WareSkuDao;
import daily.boot.gulimall.ware.entity.WareSkuEntity;
import daily.boot.gulimall.ware.service.WareSkuService;

import java.util.Objects;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Override
    public PageInfo<WareSkuEntity> queryPage(PageQueryVo queryVo, WareSkuEntity wareSkuEntity) {
        LambdaQueryWrapper<WareSkuEntity> queryWrapper = Wrappers.lambdaQuery(WareSkuEntity.class);
        queryWrapper.eq(Objects.nonNull(wareSkuEntity.getWareId()), WareSkuEntity::getWareId, wareSkuEntity.getWareId())
                    .eq(Objects.nonNull(wareSkuEntity.getSkuId()), WareSkuEntity::getSkuId, wareSkuEntity.getSkuId());
        IPage<WareSkuEntity> page = this.page(Query.getPage(queryVo), queryWrapper);
        return PageInfo.of(page);
    }

}