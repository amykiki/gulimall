package daily.boot.gulimall.ware.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.ware.dao.WareOrderTaskDetailDao;
import daily.boot.gulimall.ware.entity.WareOrderTaskDetailEntity;
import daily.boot.gulimall.ware.service.WareOrderTaskDetailService;


@Service("wareOrderTaskDetailService")
public class WareOrderTaskDetailServiceImpl extends ServiceImpl<WareOrderTaskDetailDao, WareOrderTaskDetailEntity> implements WareOrderTaskDetailService {

    @Override
    public PageInfo<WareOrderTaskDetailEntity> queryPage(PageQueryVo queryVo) {
        IPage<WareOrderTaskDetailEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}