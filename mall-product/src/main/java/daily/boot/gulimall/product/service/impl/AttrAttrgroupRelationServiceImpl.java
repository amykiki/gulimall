package daily.boot.gulimall.product.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.product.dao.AttrAttrgroupRelationDao;
import daily.boot.gulimall.product.entity.AttrAttrgroupRelationEntity;
import daily.boot.gulimall.product.service.AttrAttrgroupRelationService;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Override
    public PageInfo<AttrAttrgroupRelationEntity> queryPage(PageQueryVo queryVo) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}