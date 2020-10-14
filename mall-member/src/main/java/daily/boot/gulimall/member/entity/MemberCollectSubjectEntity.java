package daily.boot.gulimall.member.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 会员收藏的专题活动
 * 
 * @author amy
 * @date 2020-10-14 16:46:51
 */
@Data
@TableName("ums_member_collect_subject")
@ApiModel(value = "会员收藏的专题活动类")
public class MemberCollectSubjectEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * subject_id
     */
    @ApiModelProperty(value = "subject_id")
    private Long subjectId;
    /**
     * subject_name
     */
    @ApiModelProperty(value = "subject_name")
    private String subjectName;
    /**
     * subject_img
     */
    @ApiModelProperty(value = "subject_img")
    private String subjectImg;
    /**
     * 活动url
     */
    @ApiModelProperty(value = "活动url")
    private String subjectUrll;

}
