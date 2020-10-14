package daily.boot.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 商品评价回复关系
 * 
 * @author amy
 * @date 2020-10-14 15:18:58
 */
@Data
@TableName("pms_comment_replay")
@ApiModel(value = "商品评价回复关系类")
public class CommentReplayEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * 评论id
     */
    @ApiModelProperty(value = "评论id")
    private Long commentId;
    /**
     * 回复id
     */
    @ApiModelProperty(value = "回复id")
    private Long replyId;

}
