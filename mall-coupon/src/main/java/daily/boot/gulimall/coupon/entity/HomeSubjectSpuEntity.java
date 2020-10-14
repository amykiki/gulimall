package daily.boot.gulimall.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 专题商品
 * 
 * @author amy
 * @date 2020-10-14 16:05:21
 */
@Data
@TableName("sms_home_subject_spu")
@ApiModel(value = "专题商品类")
public class HomeSubjectSpuEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * 专题名字
     */
    @ApiModelProperty(value = "专题名字")
    private String name;
    /**
     * 专题id
     */
    @ApiModelProperty(value = "专题id")
    private Long subjectId;
    /**
     * spu_id
     */
    @ApiModelProperty(value = "spu_id")
    private Long spuId;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

}
