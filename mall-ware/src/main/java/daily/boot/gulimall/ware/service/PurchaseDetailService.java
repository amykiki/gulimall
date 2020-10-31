package daily.boot.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.ware.entity.PurchaseDetailEntity;

import java.util.List;


/**
 * 采购详情
 *
 * @author amy
 * @date 2020-10-14 17:07:54
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    /**
     * 无条件翻页查询
     * @param queryVo 分页查询参数
     * @param purchaseDetailEntity
     * @return
     */
    PageInfo<PurchaseDetailEntity> queryPage(PageQueryVo queryVo, PurchaseDetailEntity purchaseDetailEntity);
    
    /**
     * 根据采购ID查询采购需求
     * @param purchaseIds
     */
    List<PurchaseDetailEntity> listByPurchaseId(List<Long> purchaseIds);
    
    /**
     * 通过Id和purchaseId批量更新
     * @param finishedItems
     * @param oldStatus
     */
    void updateBatchByIdAndPurchaseId(List<PurchaseDetailEntity> finishedItems, int oldStatus);
}

