package daily.boot.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.product.entity.AttrAttrgroupRelationEntity;


/**
 * 属性&属性分组关联
 *
 * @author amy
 * @date 2020-10-14 15:18:58
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    /**
     * 无条件翻页查询
     * @param queryVo 分页查询参数
     * @return
     */
    PageInfo<AttrAttrgroupRelationEntity> queryPage(PageQueryVo queryVo);
    
    /**
     * 根据属性ID查询
     * @param attrId
     * @return
     */
    AttrAttrgroupRelationEntity getByAttrId(Long attrId);
    
    /**
     * 根据属性ID更新关联表
     * @param attrAttrgroupRelationEntity
     * @param attrId
     */
    void updateByAttrId(AttrAttrgroupRelationEntity attrAttrgroupRelationEntity, Long attrId);
}

