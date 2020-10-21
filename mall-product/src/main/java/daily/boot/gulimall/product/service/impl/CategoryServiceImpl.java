package daily.boot.gulimall.product.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.product.dao.CategoryDao;
import daily.boot.gulimall.product.entity.CategoryEntity;
import daily.boot.gulimall.product.service.CategoryService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageInfo<CategoryEntity> queryPage(PageQueryVo queryVo) {
        IPage<CategoryEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }
    
    @Override
    public List<CategoryEntity> listWithTree() {
        //1. 找出所有分类
        List<CategoryEntity> list = this.list();
        
        //2. 组装成父子树形结构
    
        return treeList(list, 0L);
    }
    
    @Override
    public void removeMenuByIds(List<Long> delIds) {
        // TODO: 2020/10/18 检查是否有业务引用ID
        this.removeByIds(delIds);
    }
    
    private List<CategoryEntity> treeList(List<CategoryEntity> dataList, Long parentId) {
        return dataList.stream()
                .filter(entity -> parentId.equals(entity.getParentCid()))
                .peek(entity -> entity.setChildren(treeList(dataList, entity.getCatId())))
                .sorted(Comparator.comparingInt(entity -> (entity.getSort() == null ? 0 : entity.getSort())))
                .collect(Collectors.toList());
        
    }
}