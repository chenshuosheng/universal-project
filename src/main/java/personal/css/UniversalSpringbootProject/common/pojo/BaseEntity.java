package personal.css.UniversalSpringbootProject.common.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: TODO
 * @Author: CSS
 * @Date: 2024/1/19 16:54
 */


@Data
public class BaseEntity implements Serializable {

    public static final Map<String, String> COLUMN_MAP = new HashMap<String, String>(18){{
        put("id", "id");
        put("creationTime", "creation_time");
        put("creatorUserId", "creator_user_id");
        put("deletionTime", "deletion_time");
        put("deleterUserId", "deleter_user_id");
        put("lastModificationTime", "last_modification_time");
        put("lastModifierUserId", "last_modifier_user_id");
        put("isDeleted", "is_deleted");
        put("lockVersion", "lock_version");
    }};

    @ApiModelProperty(value = "主键id")
    @TableId("id")
    private String id;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "creation_time", updateStrategy = FieldStrategy.NEVER, whereStrategy = FieldStrategy.NOT_EMPTY, jdbcType = JdbcType.TIMESTAMP)
    private Date creationTime;

    @ApiModelProperty(value = "创建人id")
    @TableField(value = "creator_user_id", updateStrategy = FieldStrategy.NEVER, whereStrategy = FieldStrategy.NOT_EMPTY)
    private Long creatorUserId;

    @ApiModelProperty(value = "删除人id")
    @TableField(value = "deleter_user_id", updateStrategy = FieldStrategy.NOT_EMPTY, insertStrategy = FieldStrategy.NEVER, whereStrategy = FieldStrategy.NOT_EMPTY)
    private Long deleterUserId;

    @ApiModelProperty(value = "删除时间")
    @TableField(value = "deletion_time", updateStrategy = FieldStrategy.NOT_EMPTY, insertStrategy = FieldStrategy.NEVER, whereStrategy = FieldStrategy.NOT_EMPTY, jdbcType = JdbcType.TIMESTAMP)
    private Date deletionTime;

    @ApiModelProperty(value = "最后修改时间")
    @TableField(value = "last_modification_time", updateStrategy = FieldStrategy.IGNORED, insertStrategy = FieldStrategy.NEVER, whereStrategy = FieldStrategy.NOT_EMPTY, jdbcType = JdbcType.TIMESTAMP)
    private Date lastModificationTime;

    @ApiModelProperty(value = "最后修改人id")
    @TableField(value = "last_modifier_user_id", updateStrategy = FieldStrategy.IGNORED, insertStrategy = FieldStrategy.NEVER, whereStrategy = FieldStrategy.NOT_EMPTY)
    private Long lastModifierUserId;

    @ApiModelProperty(value = "是否已删除")
    @TableField(value = "is_deleted", updateStrategy = FieldStrategy.NOT_EMPTY, whereStrategy = FieldStrategy.NOT_EMPTY)
    @TableLogic
    private Boolean isDeleted;

    @ApiModelProperty(value = "乐观锁版本号")
    @TableField(value = "lock_version", updateStrategy = FieldStrategy.IGNORED, insertStrategy = FieldStrategy.NEVER, whereStrategy = FieldStrategy.NOT_EMPTY)
    @Version
    private Integer lockVersion;
}
