package daily.boot.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.product.entity.AttrEntity;
import daily.boot.gulimall.product.vo.AttrRespVo;
import daily.boot.gulimall.product.vo.AttrVo;

import java.util.List;


/**
 * 商品属性
 *
 * @author amy
 * @date 2020-10-14 15:18:58
 */
public interface AttrService extends IService<AttrEntity> {

    /**
     * 无条件翻页查询
     * @param queryVo 分页查询参数
     * @return
     */
    PageInfo<AttrEntity> queryPage(PageQueryVo queryVo);
    
    void saveCascaded(AttrVo attrVo);
    
    PageInfo<AttrRespVo> queryPage(PageQueryVo pageQueryVo, Long catelogId, String attrType);
    
    AttrRespVo getAttrInfo(Long attrId);
    
    /**
     * 更新AttrEntity & 关联的AttrGroup属性
     * @param attrVo
     */
    void updateAttrInfo(AttrVo attrVo);
    
    List<AttrEntity> listRelationAttr(Long attrGroupId);
    
    PageInfo<AttrEntity> getNoAttrRelation(PageQueryVo pageQueryVo, Long attrGroupId);
    
    List<Long> selectableIds(List<Long> attrIds);
}

