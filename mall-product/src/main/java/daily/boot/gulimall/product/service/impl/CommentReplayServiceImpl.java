package daily.boot.gulimall.product.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.product.dao.CommentReplayDao;
import daily.boot.gulimall.product.entity.CommentReplayEntity;
import daily.boot.gulimall.product.service.CommentReplayService;


@Service("commentReplayService")
public class CommentReplayServiceImpl extends ServiceImpl<CommentReplayDao, CommentReplayEntity> implements CommentReplayService {

    @Override
    public PageInfo<CommentReplayEntity> queryPage(PageQueryVo queryVo) {
        IPage<CommentReplayEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}