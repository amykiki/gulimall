package daily.boot.gulimall.ware.dao;

import daily.boot.gulimall.ware.entity.PurchaseEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采购信息
 * 
 * @author amy
 * @email amy@gmail.com
 * @date 2020-10-14 17:07:54
 */
@Mapper
public interface PurchaseDao extends BaseMapper<PurchaseEntity> {
    
    int updateBatchAssgined(@Param("list") List<PurchaseEntity> purchaseList);
}
