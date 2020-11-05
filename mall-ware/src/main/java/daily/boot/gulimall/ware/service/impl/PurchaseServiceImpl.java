package daily.boot.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.common.exception.BusinessException;
import daily.boot.gulimall.common.constant.WareConstant;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.ware.dao.PurchaseDao;
import daily.boot.gulimall.ware.entity.PurchaseDetailEntity;
import daily.boot.gulimall.ware.entity.PurchaseEntity;
import daily.boot.gulimall.ware.exception.WareErrorCode;
import daily.boot.gulimall.ware.service.PurchaseDetailService;
import daily.boot.gulimall.ware.service.PurchaseService;
import daily.boot.gulimall.ware.service.WareSkuService;
import daily.boot.gulimall.ware.vo.MergeVo;
import daily.boot.gulimall.ware.vo.PurchaseDoneVo;
import daily.boot.gulimall.ware.vo.PurchaseItemDoneVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service("purchaseService")
@Slf4j
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {
    @Autowired
    private PurchaseDetailService purchaseDetailService;
    @Autowired
    private WareSkuService wareSkuService;
    
    @Override
    public PageInfo<PurchaseEntity> queryPage(PageQueryVo queryVo) {
        IPage<PurchaseEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }
    
    @Override
    public List<PurchaseEntity> unreceivePurchase() {
        LambdaQueryWrapper<PurchaseEntity> queryWrapper = Wrappers.lambdaQuery(PurchaseEntity.class);
        queryWrapper.eq(PurchaseEntity::getStatus, WareConstant.PurchaseStatusEnum.CREATED.getCode())
                    .or()
                    .eq(PurchaseEntity::getStatus, WareConstant.PurchaseStatusEnum.ASSIGNED.getCode());
        return this.list(queryWrapper);
    }
    
    @Override
    @Transactional
    public void mergePurchase(MergeVo mergeVo) {
        //1. 确定采购需求存在且状态是0或1才能合并
        if (CollectionUtils.isEmpty(mergeVo.getItems())) {
            throw new BusinessException(WareErrorCode.WARE_PURCHASE_DETAIL_NOT_EXIST);
        }
        List<PurchaseDetailEntity> detailEntities = purchaseDetailService.listByIds(mergeVo.getItems());
        if (CollectionUtils.isEmpty(detailEntities) || detailEntities.size() != mergeVo.getItems().size()) {
            throw new BusinessException(WareErrorCode.WARE_PURCHASE_DETAIL_NOT_EXIST);
        }
        boolean cannotMerge = detailEntities.stream()
                                            .anyMatch(e -> (WareConstant.PurchaseDetailStatusEnum.CREATED.getCode() != e.getStatus())
                                                           && (WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode() != e.getStatus()));
        if (cannotMerge) {
            throw new BusinessException(WareErrorCode.WARE_PURCHASE_DETAIL_CANNOT_MERGED);
        }
        
        Long purchaseId = mergeVo.getPurchaseId();
        if (Objects.nonNull(purchaseId)) {
            //已有订购单，校验订购单状态
            PurchaseEntity entity = this.getById(purchaseId);
            if (Objects.isNull(entity)) {
                throw new BusinessException(WareErrorCode.WARE_PURCHASE_NOT_EXIST_ERROR);
            }
            if (WareConstant.PurchaseStatusEnum.CREATED.getCode() != entity.getStatus()
                &&
                WareConstant.PurchaseStatusEnum.ASSIGNED.getCode() != entity.getStatus()) {
                throw new BusinessException(WareErrorCode.WARE_PURCHASE_STATUS_ERROR);
            }
        } else {
            //创建新订购单
            PurchaseEntity entity = new PurchaseEntity();
            entity.setPriority(1);
            entity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            this.save(entity);
            purchaseId = entity.getId();
        }
        
        //合并采购需求，更改采购需求状态，需确认采购状态是0或1才能合并
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> detailList = mergeVo.getItems().stream().map(i -> {
            PurchaseDetailEntity entity = new PurchaseDetailEntity();
            entity.setPurchaseId(finalPurchaseId);
            entity.setId(i);
            entity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
            return entity;
        }).collect(Collectors.toList());
        
        purchaseDetailService.updateBatchById(detailList);
        
        
        //更新采购单时间
        PurchaseEntity updateEntity = new PurchaseEntity();
        updateEntity.setId(purchaseId);
        updateEntity.setUpdateTime(LocalDateTime.now());
        this.updateById(updateEntity);
    }
    
    @Override
    @Transactional
    public void received(List<Long> ids) {
        // TODO: 2020/10/30 采购员身份验证
        //改变采购单状态，采购单状态在采购前必须已经为ASSIGNED，分配给了采购员才能变成正在采购中
        List<PurchaseEntity> purchaseList = ids.stream().map(i -> {
            PurchaseEntity entity = new PurchaseEntity();
            entity.setId(i);
            entity.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
            entity.setUpdateTime(LocalDateTime.now());
            return entity;
        }).collect(Collectors.toList());
        int updated = this.baseMapper.updateBatchAssgined(purchaseList);
        log.debug("更新采购单数目...[{}]", updated);
        if (updated != ids.size()) {
            throw new BusinessException(WareErrorCode.WARE_PURCHASE_NOT_VALID);
        }
        //2. 批量更新采购需求
        List<PurchaseDetailEntity> detailList =
                purchaseDetailService.listByPurchaseId(ids)
                                     .stream()
                                     .map(p -> {
                                         PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
                                         detailEntity.setId(p.getId());
                                         detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
                                         return detailEntity;
                                     }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(detailList);
        
    }
    
    @Override
    @Transactional
    public void done(PurchaseDoneVo purchaseDoneVo) {
        // 1. 验证采购单
        PurchaseEntity oldPurchase = this.getById(purchaseDoneVo.getId());
        if (Objects.isNull(oldPurchase) || oldPurchase.getStatus() != WareConstant.PurchaseStatusEnum.RECEIVE.getCode())
            throw new BusinessException(WareErrorCode.WARE_PURCHASE_NOT_VALID);
        
        
        // 2. 更新采购需求状态
        if (CollectionUtils.isEmpty(purchaseDoneVo.getItems())) {
            throw new BusinessException(WareErrorCode.WARE_PURCHASE_DONE_EMPTY);
        }
        
        //采购单完成状态
        boolean purchaseDone = true;
        List<PurchaseDetailEntity> detailList = new ArrayList<>();
        List<Long> finishedIds = new ArrayList<>();
        
        for (PurchaseItemDoneVo item : purchaseDoneVo.getItems()) {
            PurchaseDetailEntity detail = new PurchaseDetailEntity();
            detail.setPurchaseId(purchaseDoneVo.getId());
            detail.setId(item.getItemId());
            if (item.getStatus() == WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode()) {
                purchaseDone = false;
                detail.setStatus(item.getStatus());
            } else {
                detail.setStatus(WareConstant.PurchaseDetailStatusEnum.FINISH.getCode());
                //保存完成采购需求id
                finishedIds.add(item.getItemId());
            }
            detailList.add(detail);
        }
        //修改采购需求状态
        purchaseDetailService.updateBatchByIdAndPurchaseId(detailList, WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
        
        //修改仓库商品库存
        wareSkuService.addStockByPurchaseDetail(finishedIds);
        
        // 改变采购单状态
        int newStatus = purchaseDone
                        ? WareConstant.PurchaseStatusEnum.FINISH.getCode()
                        : WareConstant.PurchaseStatusEnum.HASERROR.getCode();
        this.updateStatus(purchaseDoneVo.getId(), newStatus, WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
    }
    
    @Override
    public void updateStatus(Long id, int newStatus, int oldStatus) {
        LambdaUpdateWrapper<PurchaseEntity> updateWrapper = Wrappers.lambdaUpdate(PurchaseEntity.class);
        updateWrapper.set(PurchaseEntity::getStatus, newStatus)
                     .set(PurchaseEntity::getUpdateTime, LocalDateTime.now())
                     .eq(PurchaseEntity::getId, id)
                     .eq(PurchaseEntity::getStatus, oldStatus);
        this.update(updateWrapper);
    }
}