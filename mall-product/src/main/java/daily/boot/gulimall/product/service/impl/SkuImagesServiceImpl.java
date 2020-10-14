package daily.boot.gulimall.product.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.product.dao.SkuImagesDao;
import daily.boot.gulimall.product.entity.SkuImagesEntity;
import daily.boot.gulimall.product.service.SkuImagesService;


@Service("skuImagesService")
public class SkuImagesServiceImpl extends ServiceImpl<SkuImagesDao, SkuImagesEntity> implements SkuImagesService {

    @Override
    public PageInfo<SkuImagesEntity> queryPage(PageQueryVo queryVo) {
        IPage<SkuImagesEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}