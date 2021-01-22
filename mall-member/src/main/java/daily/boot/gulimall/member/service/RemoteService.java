package daily.boot.gulimall.member.service;

import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.service.api.to.OrderTo;

public interface RemoteService {
    PageInfo<OrderTo> listWithItem(PageQueryVo pageQueryVo);
}
