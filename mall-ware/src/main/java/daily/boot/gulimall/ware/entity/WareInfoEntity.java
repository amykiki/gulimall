package daily.boot.gulimall.ware.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 仓库信息
 * 
 * @author amy
 * @date 2020-10-14 17:07:54
 */
@Data
@TableName("wms_ware_info")
@ApiModel(value = "仓库信息类")
public class WareInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * 仓库名
     */
    @ApiModelProperty(value = "仓库名")
    private String name;
    /**
     * 仓库地址
     */
    @ApiModelProperty(value = "仓库地址")
    private String address;
    /**
     * 区域编码
     */
    @ApiModelProperty(value = "区域编码")
    private String areacode;

}
