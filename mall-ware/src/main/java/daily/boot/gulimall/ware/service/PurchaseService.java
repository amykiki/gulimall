package daily.boot.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.ware.entity.PurchaseEntity;
import daily.boot.gulimall.ware.vo.MergeVo;
import daily.boot.gulimall.ware.vo.PurchaseDoneVo;

import java.util.List;


/**
 * 采购信息
 *
 * @author amy
 * @date 2020-10-14 17:07:54
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    /**
     * 无条件翻页查询
     * @param queryVo 分页查询参数
     * @return
     */
    PageInfo<PurchaseEntity> queryPage(PageQueryVo queryVo);
    
    List<PurchaseEntity> unreceivePurchase();
    
    void mergePurchase(MergeVo mergeVo);
    
    /**
     * 采购员领取采购单
     * @param ids
     */
    void received(List<Long> ids);
    
    void done(PurchaseDoneVo purchaseDoneVo);
    
    void updateStatus(Long id, int newStatus, int oldStatus);
}

