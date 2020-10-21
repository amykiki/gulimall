package daily.boot.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.product.entity.BrandEntity;

/**
 * 品牌
 *
 * @author amy
 * @email amy@gmail.com
 * @date 2020-10-13 14:20:31
 */
public interface BrandService extends IService<BrandEntity> {
    
    /**
     * 条件翻页查询
     * @param queryVo
     * @param brand 查询条件
     * @return
     */
    PageInfo<BrandEntity> queryPage(PageQueryVo queryVo, BrandEntity brand);
    
    /**
     * 通用条件翻页查询
     * @param queryVo
     * @return
     */
    PageInfo<BrandEntity> queryPage(PageQueryVo queryVo);
}

