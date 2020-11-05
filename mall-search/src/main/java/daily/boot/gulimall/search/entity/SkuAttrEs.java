package daily.boot.gulimall.search.entity;

import daily.boot.gulimall.search.elasticsearch.annotation.ESField;
import daily.boot.gulimall.search.elasticsearch.annotation.ESId;
import daily.boot.gulimall.search.elasticsearch.enums.ESFieldType;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class SkuAttrEs {
    @ESId
    private Long attrId;
    
    @ESField(type = ESFieldType.Keyword, index = false, docValues = false)
    private String attrName;
    
    @ESField(type = ESFieldType.Keyword)
    private String attrValue;
}
