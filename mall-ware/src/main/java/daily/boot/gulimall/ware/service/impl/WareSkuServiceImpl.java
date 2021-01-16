package daily.boot.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.common.Result;
import daily.boot.common.exception.BusinessException;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.service.api.feign.ProductFeignService;
import daily.boot.gulimall.service.api.to.OrderItemTo;
import daily.boot.gulimall.service.api.to.SkuHasStockTo;
import daily.boot.gulimall.service.api.to.SkuInfoVo;
import daily.boot.gulimall.service.api.to.WareSkuLockTo;
import daily.boot.gulimall.service.api.to.mq.StockLockedTo;
import daily.boot.gulimall.ware.dao.WareSkuDao;
import daily.boot.gulimall.ware.entity.PurchaseDetailEntity;
import daily.boot.gulimall.ware.entity.WareOrderTaskDetailEntity;
import daily.boot.gulimall.ware.entity.WareOrderTaskEntity;
import daily.boot.gulimall.ware.entity.WareSkuEntity;
import daily.boot.gulimall.ware.exception.WareErrorCode;
import daily.boot.gulimall.ware.service.PurchaseDetailService;
import daily.boot.gulimall.ware.service.WareOrderTaskDetailService;
import daily.boot.gulimall.ware.service.WareOrderTaskService;
import daily.boot.gulimall.ware.service.WareSkuService;
import io.seata.core.context.RootContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
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
    @Autowired
    private WareOrderTaskService wareOrderTaskService;
    @Autowired
    private WareOrderTaskDetailService wareOrderTaskDetailService;

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
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean orderLockStock(WareSkuLockTo lockTo) {
        String xid = RootContext.getXID();
        System.out.println("xid = " + xid);
        /**
         * 保存库存工作单详情信息，追溯
         */
        WareOrderTaskEntity wareOrderTaskEntity = new WareOrderTaskEntity();
        wareOrderTaskEntity.setOrderSn(lockTo.getOrderSn());
        wareOrderTaskEntity.setCreateTime(new Date());
        wareOrderTaskService.save(wareOrderTaskEntity);
        
        //1. 按照下单的收货地址，找到一个就近仓库，锁定库存
        //2. 找到每个商品在哪个仓库都有库存
        List<OrderItemTo> locks = lockTo.getLocks();
    
        List<SkuWareHasStock> skuWareHasStockList = locks.stream().map(item -> {
            SkuWareHasStock stock = new SkuWareHasStock();
            Long skuId = item.getSkuId();
            stock.setSkuId(skuId);
            stock.setNum(item.getCount());
            //查询这个商品在哪个仓库有库存
            List<Long> wareIdList = this.listWareIdHasSkuStock(skuId);
            stock.setWareId(wareIdList);
            return stock;
        }).collect(Collectors.toList());
        
        //3. 锁定库存
        boolean skuStocked = false;
        for (SkuWareHasStock hasStock : skuWareHasStockList) {
            Long skuId = hasStock.getSkuId();
            List<Long> wareIds = hasStock.getWareId();
    
            if (CollectionUtils.isEmpty(wareIds)) {
                throw new BusinessException(WareErrorCode.WARE_SKU_NO_STOCK, skuId + "无货");
            }
            
            //如果每一个商品都锁定成功，将当前商品锁定了几件的工作单记录发给MQ
            //锁定失败，前面保存的工作单信息都回滚，
            //发送出去的消息，即使要解锁库存，由于在数据库查不到指定的ID，所以不用解锁
            for (Long wareId : wareIds) {
                //锁定成功返回1， 失败就返回0
                Long updated = this.baseMapper.lockSkuStock(skuId, wareId, hasStock.getNum());
                if (updated == 1) {
                    skuStocked = true;
                    WareOrderTaskDetailEntity taskDetailEntity =
                            WareOrderTaskDetailEntity.builder()
                                                     .skuId(skuId).skuName("")
                                                     .skuNum(hasStock.getNum()).taskId(wareOrderTaskEntity.getId())
                                                     .wareId(wareId).lockStatus(1)
                                                     .build();
                    wareOrderTaskDetailService.save(taskDetailEntity);
    
                    // TODO: 2021/1/13 告诉MQ库存锁定成功
                    StockLockedTo lockedTo = new StockLockedTo();
                    lockedTo.setWareOrderTaskId(wareOrderTaskEntity.getId());
                    lockedTo.setWareOrderTaskDetailId(taskDetailEntity.getId());
                    break;
                } else {
                    //当前仓库锁失败，重试下一个仓库
                }
            }
        }
        if (!skuStocked) {
            throw new BusinessException(WareErrorCode.WARE_SKU_NO_STOCK,  "OrderSn[" + lockTo.getOrderSn() + "]锁定失败，没有足够的库存!" );
        }
        return true;
    }
    
    @Override
    public List<Long> listWareIdHasSkuStock(Long skuId) {
        LambdaQueryWrapper<WareSkuEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(WareSkuEntity::getWareId)
                    .eq(WareSkuEntity::getSkuId, skuId)
                    .apply("stock - stock_locked > 0");
        return this.listObjs(queryWrapper, wareId -> (Long) wareId);
    }
    
    @Data
    class SkuWareHasStock {
        private Long skuId;
        private Integer num;
        private List<Long> wareId;
    }
}