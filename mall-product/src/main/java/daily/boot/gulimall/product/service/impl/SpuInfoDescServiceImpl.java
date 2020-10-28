package daily.boot.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.product.dao.SpuInfoDescDao;
import daily.boot.gulimall.product.entity.SpuInfoDescEntity;
import daily.boot.gulimall.product.service.SpuInfoDescService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("spuInfoDescService")
public class SpuInfoDescServiceImpl extends ServiceImpl<SpuInfoDescDao, SpuInfoDescEntity> implements SpuInfoDescService {

    @Override
    public PageInfo<SpuInfoDescEntity> queryPage(PageQueryVo queryVo) {
        IPage<SpuInfoDescEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }
    
    @Override
    public void save(Long spuId, List<String> decript) {
        if (CollectionUtils.isEmpty(decript)) {
            return;
        }
        SpuInfoDescEntity entity = new SpuInfoDescEntity();
        entity.setSpuId(spuId);
        entity.setDecript(String.join(",", decript));
    
        this.save(entity);
    }
}