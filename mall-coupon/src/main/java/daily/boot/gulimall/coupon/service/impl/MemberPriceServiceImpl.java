package daily.boot.gulimall.coupon.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.coupon.dao.MemberPriceDao;
import daily.boot.gulimall.coupon.entity.MemberPriceEntity;
import daily.boot.gulimall.coupon.service.MemberPriceService;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Service("memberPriceService")
public class MemberPriceServiceImpl extends ServiceImpl<MemberPriceDao, MemberPriceEntity> implements MemberPriceService {

    @Override
    public PageInfo<MemberPriceEntity> queryPage(PageQueryVo queryVo) {
        IPage<MemberPriceEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }
    
    @Override
    public void saveBatch(List<MemberPriceEntity> memberPriceEntities, Predicate<? super MemberPriceEntity> predicate) {
        List<MemberPriceEntity> filteredList = memberPriceEntities.stream()
                                                                  .filter(predicate)
                                                                  .collect(Collectors.toList());
        this.saveBatch(filteredList);
    }
}