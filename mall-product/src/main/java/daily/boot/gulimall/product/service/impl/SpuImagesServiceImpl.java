package daily.boot.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.product.dao.SpuImagesDao;
import daily.boot.gulimall.product.entity.SpuImagesEntity;
import daily.boot.gulimall.product.service.SpuImagesService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service("spuImagesService")
public class SpuImagesServiceImpl extends ServiceImpl<SpuImagesDao, SpuImagesEntity> implements SpuImagesService {

    @Override
    public PageInfo<SpuImagesEntity> queryPage(PageQueryVo queryVo) {
        IPage<SpuImagesEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }
    
    @Override
    public void save(Long spuId, List<String> images) {
        if (CollectionUtils.isEmpty(images)) return;
    
        List<SpuImagesEntity> entities = images.stream().map(image -> {
            SpuImagesEntity entity = new SpuImagesEntity();
            entity.setSpuId(spuId);
            entity.setImgUrl(image);
            return entity;
        }).collect(Collectors.toList());
    
        this.saveBatch(entities);
    }
}