package daily.boot.gulimall.ware.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.ware.dao.WareSkuDao;
import daily.boot.gulimall.ware.entity.WareSkuEntity;
import daily.boot.gulimall.ware.service.WareSkuService;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Override
    public PageInfo<WareSkuEntity> queryPage(PageQueryVo queryVo) {
        IPage<WareSkuEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}