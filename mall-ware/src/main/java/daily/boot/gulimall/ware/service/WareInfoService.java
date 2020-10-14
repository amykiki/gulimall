package daily.boot.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.ware.entity.WareInfoEntity;


/**
 * 仓库信息
 *
 * @author amy
 * @date 2020-10-14 17:07:54
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    /**
     * 无条件翻页查询
     * @param queryVo 分页查询参数
     * @return
     */
    PageInfo<WareInfoEntity> queryPage(PageQueryVo queryVo);
}

