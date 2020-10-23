package daily.boot.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import daily.boot.gulimall.common.valid.ListValue;
import daily.boot.gulimall.common.valid.NullOrNotBlank;
import daily.boot.gulimall.common.valid.ValidateGroup;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * 品牌
 * 
 * @author amy
 * @email amy@gmail.com
 * @date 2020-10-13 14:20:31
 */
@Data
@TableName("pms_brand")
@ApiModel(value = "品牌类")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@TableId
    @Null(message = "新建品牌不能传ID",groups = ValidateGroup.Add.class)
    @NotNull(message = "品牌ID不能为空", groups = ValidateGroup.Update.class)
	private Long brandId;
	
	/**
	 * 品牌名
	 */
	@NotBlank(message = "品牌名不能为空", groups = ValidateGroup.Add.class)
    @NullOrNotBlank(message = "品牌名{daily.boot.gulimall.common.valid.NullOrNotBlank.message}",
                    groups = ValidateGroup.Update.class)
    @Size(min = 1, message = "最小长度为1", groups = ValidateGroup.Update.class)
	private String name;
	
	/**
	 * 品牌logo地址
	 */
	@NotBlank(message = "品牌地址不能为空", groups = ValidateGroup.Add.class)
    @NullOrNotBlank(message = "品牌地址{daily.boot.gulimall.common.valid.NullOrNotBlank.message}",
            groups = ValidateGroup.Update.class)
	@URL(message = "品牌地址必须为URL形式")
	private String logo;
	
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@NotNull(message = "显示状态不能为空", groups = ValidateGroup.Add.class)
    //@ListValue(values = {0, 1}, message = "显示状态可选值0, 1")
    @ListValue(values = {0, 1})
	private Integer showStatus;
	
	/**
	 * 检索首字母
	 */
	@NotBlank(message = "首字母不能为空", groups = ValidateGroup.Add.class)
    @NullOrNotBlank(message = "检索字母有且仅有一个字母",
            groups = ValidateGroup.Update.class)
	@Pattern(regexp = "^[a-zA-Z]$", message = "检索字母有且仅有一个字母")
	private String firstLetter;
	
	/**
	 * 排序
	 */
	@NotNull(message = "排序值不能为空", groups = ValidateGroup.Add.class)
	@Min(value = 0, message = "排序号大于等于0")
    @TableField
	private Integer sort;

}
