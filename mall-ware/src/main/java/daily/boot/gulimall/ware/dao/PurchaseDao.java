package daily.boot.gulimall.ware.dao;

import daily.boot.gulimall.ware.entity.PurchaseEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采购信息
 * 
 * @author amy
 * @email amy@gmail.com
 * @date 2020-10-14 17:07:54
 */
@Mapper
public interface PurchaseDao extends BaseMapper<PurchaseEntity> {
	
}
