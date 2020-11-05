package daily.boot.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.common.Result;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.service.api.feign.ProductFeignService;
import daily.boot.gulimall.service.api.to.SkuHasStockTo;
import daily.boot.gulimall.service.api.to.SkuInfoVo;
import daily.boot.gulimall.ware.dao.WareSkuDao;
import daily.boot.gulimall.ware.entity.PurchaseDetailEntity;
import daily.boot.gulimall.ware.entity.WareSkuEntity;
import daily.boot.gulimall.ware.service.PurchaseDetailService;
import daily.boot.gulimall.ware.service.WareSkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service("wareSkuService")
@Slf4j
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {
    
    @Autowired
    private ProductFeignService productFeignService;
    @Autowired
    private PurchaseDetailService purchaseDetailService;

    @Override
    public PageInfo<WareSkuEntity> queryPage(PageQueryVo queryVo, WareSkuEntity wareSkuEntity) {
        LambdaQueryWrapper<WareSkuEntity> queryWrapper = Wrappers.lambdaQuery(WareSkuEntity.class);
        queryWrapper.eq(Objects.nonNull(wareSkuEntity.getWareId()), WareSkuEntity::getWareId, wareSkuEntity.getWareId())
                    .eq(Objects.nonNull(wareSkuEntity.getSkuId()), WareSkuEntity::getSkuId, wareSkuEntity.getSkuId());
        IPage<WareSkuEntity> page = this.page(Query.getPage(queryVo), queryWrapper);
        return PageInfo.of(page);
    }
    
    @Override
    public void addStockByPurchaseDetail(List<Long> finishedIds) {
        // 获取完整的采购需求
        List<PurchaseDetailEntity> purchaseDetailEntities = purchaseDetailService.listByIds(finishedIds);
        
        //先判断仓库对应sku是否存在
        purchaseDetailEntities.forEach(detail -> {
            LambdaQueryWrapper<WareSkuEntity> queryWrapper = Wrappers.lambdaQuery(WareSkuEntity.class)
                                                           .eq(WareSkuEntity::getWareId, detail.getWareId())
                                                           .eq(WareSkuEntity::getSkuId, detail.getSkuId());
            WareSkuEntity entity = this.getOne(queryWrapper);
            // 条目不存在，新创建条目
            if (Objects.isNull(entity)) {
                entity = new WareSkuEntity();
                entity.setWareId(detail.getWareId());
                entity.setSkuId(detail.getSkuId());
                entity.setStock(detail.getSkuNum());
                entity.setStockLocked(0);
                //远程查询skuName
                try {
                    Result<SkuInfoVo> info = productFeignService.info(detail.getSkuId());
                    log.info(info.toString());
                    if (Objects.nonNull(info) && info.isOk()) {
                        entity.setSkuName(info.getData().getSkuName());
                    } else {
                        log.warn("远程查询productFeignService.info({})信息为空！！", detail.getSkuId());
                    }
                } catch (Exception e) {
                    log.warn("远程查询productFeignService.info({})信息失败", detail.getSkuId(), e);
                }
                this.save(entity);
            } else {
                //条目存在，更新
                int newStock = entity.getStock() + detail.getSkuNum();
                this.lambdaUpdate()
                    .set(WareSkuEntity::getStock, newStock)
                    .eq(WareSkuEntity::getStock, entity.getStock())
                    .eq(WareSkuEntity::getSkuId, entity.getSkuId())
                    .eq(WareSkuEntity::getWareId, entity.getWareId())
                    .update();
                
            }
        
        });
    }
    
    @Override
    public List<SkuHasStockTo> getSkuHasStock(List<Long> skuIds) {
        String ids = skuIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("sku_id as skuId, ifnull(sum(stock - stock_locked), 0) as stockSum").inSql("sku_id", ids).groupBy("sku_id");
        List<Map<String, Object>> mapList = this.listMaps(queryWrapper);
        Map<Long, Integer> stockMap = mapList.stream()
                                             //不知道为什么这里mp自动把stockSum的类型转为BigDecimal了
                                             .collect(Collectors.toMap(item -> (Long) item.get("skuId"), item -> ((BigDecimal)item.get("stockSum")).intValue()));
    
        List<SkuHasStockTo> skuHasStockList = skuIds.stream().map(skuId -> {
            SkuHasStockTo to = new SkuHasStockTo();
            to.setSkuId(skuId);
            if (stockMap.containsKey(skuId)) {
                to.setHasStock(true);
            } else {
                to.setHasStock(false);
            }
            return to;
        }).collect(Collectors.toList());
    
        return skuHasStockList;
    }
}