package daily.boot.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.product.entity.SpuInfoDescEntity;

import java.util.List;


/**
 * spu信息介绍
 *
 * @author amy
 * @date 2020-10-14 15:18:58
 */
public interface SpuInfoDescService extends IService<SpuInfoDescEntity> {

    /**
     * 无条件翻页查询
     * @param queryVo 分页查询参数
     * @return
     */
    PageInfo<SpuInfoDescEntity> queryPage(PageQueryVo queryVo);
    
    void save(Long spuId, List<String> decript);
}

