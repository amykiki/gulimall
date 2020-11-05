package daily.boot.gulimall.search.entity;

import daily.boot.gulimall.search.elasticsearch.annotation.ESField;
import daily.boot.gulimall.search.elasticsearch.annotation.ESId;
import daily.boot.gulimall.search.elasticsearch.enums.ESFieldType;
import lombok.Data;

@Data
public class SkuAttrES {
    @ESId
    private Long attrId;
    
    @ESField(type = ESFieldType.Keyword, index = false, docValues = false)
    private String attrName;
    
    @ESField(type = ESFieldType.Keyword)
    private String attrValue;
}