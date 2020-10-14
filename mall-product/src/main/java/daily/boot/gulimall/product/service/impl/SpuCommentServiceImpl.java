package daily.boot.gulimall.product.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.product.dao.SpuCommentDao;
import daily.boot.gulimall.product.entity.SpuCommentEntity;
import daily.boot.gulimall.product.service.SpuCommentService;


@Service("spuCommentService")
public class SpuCommentServiceImpl extends ServiceImpl<SpuCommentDao, SpuCommentEntity> implements SpuCommentService {

    @Override
    public PageInfo<SpuCommentEntity> queryPage(PageQueryVo queryVo) {
        IPage<SpuCommentEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}