package daily.boot.gulimall.search.service.impl;

import daily.boot.gulimall.search.dto.ESPageInfo;
import daily.boot.gulimall.search.elasticsearch.utils.StringExtUtils;
import daily.boot.gulimall.search.entity.SkuEs;
import daily.boot.gulimall.search.service.SkuEsService;
import daily.boot.gulimall.search.vo.SearchParam;
import daily.boot.gulimall.search.vo.SkuSearchResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SkuEsServiceImpl extends BaseESServiceImpl<SkuEs> implements SkuEsService {
    @Override
    public boolean statusUp(List<SkuEs> skuEsList) {
        return this.bulkSaveOrUpdate(skuEsList);
    }
    
    @Override
    public SkuSearchResult skuSearch(SearchParam param) {
        
        SearchSourceBuilder sourceBuilder = buildSearchSouceBuilder(param);
        ESPageInfo<SkuEs> search = this.search(sourceBuilder);
        return buildSearchResult(search, param);
    }
    
    private SkuSearchResult buildSearchResult(ESPageInfo<SkuEs> searchInfo, SearchParam param) {
        SkuSearchResult result = new SkuSearchResult();
        //1. 设置分页参数
        Integer pageSize = param.getPageSize();
        Long total = searchInfo.getTotal();
        int totalPages = (total.intValue() + pageSize - 1) / pageSize;
        result.setTotal(total);
        result.setTotalPages(totalPages);
        
        result.setPageNum(param.getPageNum());
        
        //2. 设置查询结果值
        List<SkuEs> skuEsList = searchInfo.getList().stream().map(l -> {
            SkuEs skuEs = l.getEntity();
            if (!CollectionUtils.isEmpty(l.getHighlightFields())) {
                Map<String, HighlightField> highlightFields = l.getHighlightFields();
                HighlightField highlightField = highlightFields.get("sku_title");
                if (Objects.nonNull(highlightField) && StringUtils.isNotBlank(highlightField.getName())) {
                    Text fragment = highlightField.getFragments()[0];
                    skuEs.setSkuTitle(fragment.string());
                }
            }
            return skuEs;
        }).collect(Collectors.toList());
        result.setProducts(skuEsList);
    
        //3. 设置聚合数据
        Map<String, Aggregation> aggregationMap = searchInfo.getAggregations().asMap();
        //3.1. 设置聚合品牌数据
        ParsedLongTerms brandAgg = (ParsedLongTerms) aggregationMap.get("brand_agg");
        List<SkuSearchResult.BrandVo> brandVos = brandAgg.getBuckets().stream().map(b -> {
            SkuSearchResult.BrandVo vo = new SkuSearchResult.BrandVo();
            Long brandId = (Long) b.getKey();
            vo.setBrandId(brandId);
            //一个品牌id只对应一个品牌图片
            ParsedStringTerms brandImgAgg = b.getAggregations().get("brand_img_agg");
            Terms.Bucket bucket = brandImgAgg.getBuckets().get(0);
            String brandImg = bucket.getKeyAsString();
            vo.setBrandImg(brandImg);
            
            //一个品牌Id只对应一个品牌名
            ParsedStringTerms brandNameAgg = b.getAggregations().get("brand_name_agg");
            Terms.Bucket nameBucket = brandNameAgg.getBuckets().get(0);
            String brandName = nameBucket.getKeyAsString();
            vo.setBrandName(brandName);
            return vo;
        }).collect(Collectors.toList());
        result.setBrands(brandVos);
        
        
        //3.2 设置聚合分类数据
        ParsedLongTerms catalogAgg = (ParsedLongTerms) aggregationMap.get("catalog_agg");
        List<SkuSearchResult.CatalogVo> catalogVos = catalogAgg.getBuckets().stream().map(c -> {
            SkuSearchResult.CatalogVo vo = new SkuSearchResult.CatalogVo();
            Long catalogId = (Long) c.getKey();
            ParsedStringTerms catalogNameAgg = c.getAggregations().get("catalog_name_agg");
            Terms.Bucket bucket = catalogNameAgg.getBuckets().get(0);
            String catalogName = bucket.getKeyAsString();
            vo.setCatalogId(catalogId);
            vo.setCatalogName(catalogName);
            return vo;
        }).collect(Collectors.toList());
        result.setCatalogs(catalogVos);
        
        //3.3 设置聚合属性数据
        ParsedNested attrAgg = (ParsedNested) aggregationMap.get("attr_agg");
        ParsedLongTerms attrIdAgg = attrAgg.getAggregations().get("attr_id_agg");
        List<SkuSearchResult.AttrVo> attrVos = attrIdAgg.getBuckets().stream().map(id -> {
            SkuSearchResult.AttrVo vo = new SkuSearchResult.AttrVo();
            Long attrId = (Long) id.getKey();
            //设置属性id
            vo.setAttrId(attrId);
    
            //设置属性名，属性名只有一个
            ParsedStringTerms attrNameAgg = id.getAggregations().get("attr_name_agg");
            Terms.Bucket attrNameBucket = attrNameAgg.getBuckets().get(0);
            String attrName = attrNameBucket.getKeyAsString();
            vo.setAttrName(attrName);
            
            //设置属性值，可能有多个
            ParsedStringTerms attrValueAgg = id.getAggregations().get("attr_value_agg");
            List<String> attrValues = attrValueAgg.getBuckets().stream()
                                                  .map(MultiBucketsAggregation.Bucket::getKeyAsString)
                                                  .collect(Collectors.toList());
            vo.setAttrValues(attrValues);
            return vo;
        }).collect(Collectors.toList());
        result.setAttr(attrVos);
        
        return result;
    }
    
    private SearchSourceBuilder buildSearchSouceBuilder(SearchParam param) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //关键字查询
        if (StringUtils.isNotBlank(param.getKeyword())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("sku_title", param.getKeyword()));
        }
        
        //filter--条件过滤
        // catalogId
        if (Objects.nonNull(param.getCatalog3Id())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("catalog_id", param.getCatalog3Id()));
        }
        //brandIds
        if (!CollectionUtils.isEmpty(param.getBrandId())) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("brand_id", param.getBrandId()));
        }
        //属性 attr
        if (!CollectionUtils.isEmpty(param.getAttrs())) {
            param.getAttrs().forEach(attr -> {
                String[] s = attr.split("_");
                if (s.length != 2) {
                    return;
                }
                String attrId = s[0];
                String[] attrValues = s[1].split(":");
                BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery()
                                                                .must(QueryBuilders.termQuery("attrs.attr_id", attrId))
                                                                .must(QueryBuilders.termsQuery("attrs.attr_value", attrValues));
                boolQueryBuilder.filter(QueryBuilders.nestedQuery("attrs", nestedBoolQuery, ScoreMode.None));
            });
        }
        //skuPrice
        if (StringUtils.isNotBlank(param.getSkuPrice())) {
            String skuPrice = param.getSkuPrice();
            String[] s = skuPrice.split("_");
            String gte = null;
            String lte = null;
            if (s.length == 2) {
                gte = s[0];
                lte = s[1];
            } else if (skuPrice.endsWith("_")) {
                gte = s[0];
            } else if (skuPrice.startsWith("_")) {
                lte = s[0];
            }
            if (Objects.nonNull(gte) || Objects.nonNull(lte)) {
                RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("sku_price");
                if (Objects.nonNull(gte)) {
                    rangeQuery.gte(gte);
                }
                if (Objects.nonNull(lte)) {
                    rangeQuery.lte(lte);
                }
                boolQueryBuilder.filter(rangeQuery);
            }
        }
        //是否有库存
        if (Objects.nonNull(param.getHasStock())) {
            boolean hasStock = param.getHasStock() > 0;
            boolQueryBuilder.filter(QueryBuilders.termQuery("has_stock", hasStock));
        }
        //设置查询条件
        sourceBuilder.query(boolQueryBuilder);
        
        //设置聚合条件
        TermsAggregationBuilder brandAgg = AggregationBuilders.terms("brand_agg").field("brand_id").size(50);
        TermsAggregationBuilder brandNameAgg = AggregationBuilders.terms("brand_name_agg").field("brand_name").size(1);
        TermsAggregationBuilder brandImgAgg = AggregationBuilders.terms("brand_img_agg").field("brand_img").size(1);
        brandAgg.subAggregation(brandNameAgg).subAggregation(brandImgAgg);
        sourceBuilder.aggregation(brandAgg);
        
        TermsAggregationBuilder catalogAgg = AggregationBuilders.terms("catalog_agg").field("catalog_id").size(50);
        TermsAggregationBuilder catalogNameAgg = AggregationBuilders.terms("catalog_name_agg").field("catalog_name").size(1);
        catalogAgg.subAggregation(catalogNameAgg);
        sourceBuilder.aggregation(catalogAgg);
        
        NestedAggregationBuilder nestedAttrAgg = AggregationBuilders.nested("attr_agg", "attrs");
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attr_id_agg").field("attrs.attr_id").size(20);
        TermsAggregationBuilder attrNameAgg = AggregationBuilders.terms("attr_name_agg").field("attrs.attr_name").size(1);
        TermsAggregationBuilder attrValueAgg = AggregationBuilders.terms("attr_value_agg").field("attrs.attr_value").size(20);
        attrIdAgg.subAggregation(attrNameAgg);
        attrIdAgg.subAggregation(attrValueAgg);
        nestedAttrAgg.subAggregation(attrIdAgg);
        sourceBuilder.aggregation(nestedAttrAgg);
        
        //排序
        if (StringUtils.isNotBlank(param.getSort())) {
            String sort = param.getSort();
            String[] sorts = sort.split("_");
            
            if (sorts.length == 2) {
                String sortName = StringExtUtils.camelToUnderline(sorts[0]);
                String order = sorts[1];
                sourceBuilder.sort(sortName, "asc".equalsIgnoreCase(order) ? SortOrder.ASC : SortOrder.DESC);
            }
        }
        
        //分页,pageSize是必须
        Integer pageSize = param.getPageSize();
        Integer pageNum = param.getPageNum();
        int from = (pageNum - 1) * pageSize;
        sourceBuilder.from(from);
        sourceBuilder.size(pageSize);
        
        //高亮
        if (StringUtils.isNotBlank(param.getKeyword())) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("sku_title");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            
            sourceBuilder.highlighter(highlightBuilder);
        }
        
        
        log.debug(sourceBuilder.toString());
        return sourceBuilder;
    }
}
