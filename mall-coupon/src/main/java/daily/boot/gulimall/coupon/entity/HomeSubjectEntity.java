package daily.boot.gulimall.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 首页专题表【jd首页下面很多专题，每个专题链接新的页面，展示专题商品信息】
 * 
 * @author amy
 * @date 2020-10-14 16:05:21
 */
@Data
@TableName("sms_home_subject")
@ApiModel(value = "首页专题表【jd首页下面很多专题，每个专题链接新的页面，展示专题商品信息】类")
public class HomeSubjectEntity implements Serializable {
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
     * 专题标题
     */
    @ApiModelProperty(value = "专题标题")
    private String title;
    /**
     * 专题副标题
     */
    @ApiModelProperty(value = "专题副标题")
    private String subTitle;
    /**
     * 显示状态
     */
    @ApiModelProperty(value = "显示状态")
    private Integer status;
    /**
     * 详情连接
     */
    @ApiModelProperty(value = "详情连接")
    private String url;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;
    /**
     * 专题图片地址
     */
    @ApiModelProperty(value = "专题图片地址")
    private String img;

}
