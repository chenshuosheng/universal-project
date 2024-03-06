package personal.css.UniversalSpringbootProject.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description: TODO
 * @Author: CSS
 * @Date: 2024/3/6 14:56
 */
@Data
@TableName(value = "slf4j_logging")
public class Slf4jLogging {
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("序号")
    private Integer id;

    @TableField(value = "log_level")
    @ApiModelProperty("日志级别")
    private String logLevel;

    @TableField(value = "log_class")
    @ApiModelProperty("日志发生类")
    private String logClass;

    @TableField(value = "log_method")
    @ApiModelProperty("日志发生方法")
    private String logMethod;

    @TableField(value = "content")
    @ApiModelProperty("日志内容")
    private String content;

    @TableField(value = "create_time")
    @ApiModelProperty("日志生成时间")
    private Date createTime;
}
