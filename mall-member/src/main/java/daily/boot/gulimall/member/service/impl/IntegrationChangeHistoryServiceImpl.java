package daily.boot.gulimall.member.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.member.dao.IntegrationChangeHistoryDao;
import daily.boot.gulimall.member.entity.IntegrationChangeHistoryEntity;
import daily.boot.gulimall.member.service.IntegrationChangeHistoryService;


@Service("integrationChangeHistoryService")
public class IntegrationChangeHistoryServiceImpl extends ServiceImpl<IntegrationChangeHistoryDao, IntegrationChangeHistoryEntity> implements IntegrationChangeHistoryService {

    @Override
    public PageInfo<IntegrationChangeHistoryEntity> queryPage(PageQueryVo queryVo) {
        IPage<IntegrationChangeHistoryEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}