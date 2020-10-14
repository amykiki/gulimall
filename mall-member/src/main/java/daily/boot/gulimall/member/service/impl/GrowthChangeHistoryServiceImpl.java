package daily.boot.gulimall.member.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.member.dao.GrowthChangeHistoryDao;
import daily.boot.gulimall.member.entity.GrowthChangeHistoryEntity;
import daily.boot.gulimall.member.service.GrowthChangeHistoryService;


@Service("growthChangeHistoryService")
public class GrowthChangeHistoryServiceImpl extends ServiceImpl<GrowthChangeHistoryDao, GrowthChangeHistoryEntity> implements GrowthChangeHistoryService {

    @Override
    public PageInfo<GrowthChangeHistoryEntity> queryPage(PageQueryVo queryVo) {
        IPage<GrowthChangeHistoryEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}