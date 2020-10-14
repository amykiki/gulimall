package daily.boot.gulimall.product.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.product.dao.SpuInfoDao;
import daily.boot.gulimall.product.entity.SpuInfoEntity;
import daily.boot.gulimall.product.service.SpuInfoService;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Override
    public PageInfo<SpuInfoEntity> queryPage(PageQueryVo queryVo) {
        IPage<SpuInfoEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}