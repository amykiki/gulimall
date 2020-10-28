package daily.boot.gulimall.product.service.impl;

import daily.boot.gulimall.product.vo.SpuSaveVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.product.dao.SkuImagesDao;
import daily.boot.gulimall.product.entity.SkuImagesEntity;
import daily.boot.gulimall.product.service.SkuImagesService;

import java.util.List;
import java.util.stream.Collectors;


@Service("skuImagesService")
public class SkuImagesServiceImpl extends ServiceImpl<SkuImagesDao, SkuImagesEntity> implements SkuImagesService {

    @Override
    public PageInfo<SkuImagesEntity> queryPage(PageQueryVo queryVo) {
        IPage<SkuImagesEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }
    
    @Override
    public void saveSku(Long skuId, List<SpuSaveVo.Skus.Image> images) {
        List<SkuImagesEntity> imageEntites = images.stream().map(image -> {
            SkuImagesEntity entity = new SkuImagesEntity();
            BeanUtils.copyProperties(image, entity);
            entity.setSkuId(skuId);
            return entity;
        }).filter(imageEntity -> StringUtils.isNotBlank(imageEntity.getImgUrl())).collect(Collectors.toList());
        this.saveBatch(imageEntites);
    }
}