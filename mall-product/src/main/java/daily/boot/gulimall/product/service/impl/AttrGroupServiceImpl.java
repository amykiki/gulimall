package daily.boot.gulimall.product.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.product.dao.AttrGroupDao;
import daily.boot.gulimall.product.entity.AttrGroupEntity;
import daily.boot.gulimall.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Override
    public PageInfo<AttrGroupEntity> queryPage(PageQueryVo queryVo) {
        IPage<AttrGroupEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}