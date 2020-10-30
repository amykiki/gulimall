package daily.boot.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.ware.dao.WareInfoDao;
import daily.boot.gulimall.ware.entity.WareInfoEntity;
import daily.boot.gulimall.ware.service.WareInfoService;


@Service("wareInfoService")
public class WareInfoServiceImpl extends ServiceImpl<WareInfoDao, WareInfoEntity> implements WareInfoService {

    @Override
    public PageInfo<WareInfoEntity> queryPage(PageQueryVo queryVo) {
        String key = queryVo.getKey();
        LambdaQueryWrapper<WareInfoEntity> queryWrapper = Wrappers.lambdaQuery(WareInfoEntity.class);
        if (StringUtils.isNotBlank(key)) {
            queryWrapper.eq(WareInfoEntity::getId, key)
                        .or().like(WareInfoEntity::getName, key)
                        .or().like(WareInfoEntity::getAddress, key)
                        .or().like(WareInfoEntity::getAreacode, key);
        }
        IPage<WareInfoEntity> page = this.page(Query.getPage(queryVo), queryWrapper);
        return PageInfo.of(page);
    }

}