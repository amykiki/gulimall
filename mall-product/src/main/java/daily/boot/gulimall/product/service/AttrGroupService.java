package daily.boot.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.product.entity.AttrGroupEntity;
import daily.boot.gulimall.product.vo.AttrGroupWithAttrsVo;
import daily.boot.gulimall.product.vo.SkuItemVo;

import java.util.List;


/**
 * 属性分组
 *
 * @author amy
 * @date 2020-10-14 15:18:58
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    /**
     * 无条件翻页查询
     * @param queryVo 分页查询参数
     * @return
     */
    PageInfo<AttrGroupEntity> queryPage(PageQueryVo queryVo);
    
    /**
     * 根据三级分类ID无条件翻页查询
     * @param queryVo 分页查询参数
     * @return
     */
    PageInfo<AttrGroupEntity> queryPage(PageQueryVo queryVo, Long categoryId);
    
    List<AttrGroupEntity> listByCatelogId(Long catelogId);
    
    List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId);
    
    List<SkuItemVo.SpuItemAttrGroup> listAttrGroupWithAttrsBySpuId(Long spuId);
}

