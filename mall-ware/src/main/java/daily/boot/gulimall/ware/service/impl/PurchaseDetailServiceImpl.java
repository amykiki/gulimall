package daily.boot.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import daily.boot.gulimall.common.constant.WareConstant;
import org.apache.ibatis.binding.MapperMethod;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.ware.dao.PurchaseDetailDao;
import daily.boot.gulimall.ware.entity.PurchaseDetailEntity;
import daily.boot.gulimall.ware.service.PurchaseDetailService;

import java.util.*;
import java.util.stream.Collectors;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageInfo<PurchaseDetailEntity> queryPage(PageQueryVo queryVo, PurchaseDetailEntity purchaseDetailEntity) {
        LambdaQueryWrapper<PurchaseDetailEntity> queryWrapper = Wrappers.lambdaQuery(PurchaseDetailEntity.class);
        String key = queryVo.getKey();
        if (StringUtils.isNotBlank(key)) {
            queryWrapper.and(q -> {
                q.eq(PurchaseDetailEntity::getPurchaseId, key)
                 .or()
                 .eq(PurchaseDetailEntity::getSkuId, key);
            });
        }
        queryWrapper.eq(Objects.nonNull(purchaseDetailEntity.getWareId()), PurchaseDetailEntity::getWareId, purchaseDetailEntity.getWareId())
                    .eq(Objects.nonNull(purchaseDetailEntity.getStatus()), PurchaseDetailEntity::getStatus, purchaseDetailEntity.getStatus());
        IPage<PurchaseDetailEntity> page = this.page(Query.getPage(queryVo), queryWrapper);
        return PageInfo.of(page);
    }
    
    @Override
    public List<PurchaseDetailEntity> listByPurchaseId(List<Long> purchaseIds) {
        LambdaQueryWrapper<PurchaseDetailEntity> query = Wrappers.lambdaQuery(PurchaseDetailEntity.class)
                                                                 .in(PurchaseDetailEntity::getPurchaseId, purchaseIds);
        return this.list(query);
    }
    
    @Override
    public void updateBatchByIdAndPurchaseId(List<PurchaseDetailEntity> finishedItems, int oldStatus) {
        List<Map<PurchaseDetailEntity, Wrapper<PurchaseDetailEntity>>> paramPairList = finishedItems.stream().map(item -> {
            PurchaseDetailEntity updateEntity = new PurchaseDetailEntity();
            updateEntity.setStatus(item.getStatus());
            Map<PurchaseDetailEntity, Wrapper<PurchaseDetailEntity>> map = new HashMap<>();
            map.put(updateEntity, Wrappers.lambdaQuery(PurchaseDetailEntity.class)
                                  .eq(PurchaseDetailEntity::getId, item.getId())
                                  .eq(PurchaseDetailEntity::getPurchaseId, item.getPurchaseId())
                                  .eq(PurchaseDetailEntity::getStatus, oldStatus));
            return map;
        }).collect(Collectors.toList());
    
        executeBatch(paramPairList, (sqlSession, element) -> {
            for (Map.Entry entrySet : element.entrySet()) {
                MapperMethod.ParamMap param = new MapperMethod.ParamMap();
                param.put(Constants.ENTITY, entrySet.getKey());
                param.put(Constants.WRAPPER, entrySet.getValue());
                sqlSession.update(sqlStatement(SqlMethod.UPDATE), param);
            }
        });
    }
}