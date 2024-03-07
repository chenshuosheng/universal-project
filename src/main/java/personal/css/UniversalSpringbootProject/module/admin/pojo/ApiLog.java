package personal.css.UniversalSpringbootProject.module.admin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @Description: 请求日志记录实体类
 * @Author: CSS
 * @Date: 2024/3/6 17:11
 */
@Accessors(chain = true)
@Data
@TableName(value = "api_log")
public class ApiLog {

    @TableId(value = "id",type = IdType.AUTO)
    @ApiModelProperty(value = "序号")
    private Long id;

    @TableField(value = "user_id")
    @ApiModelProperty(value = "用户id")
    private Long userId;//通过token获取

    @TableField(value = "username")
    @ApiModelProperty(value = "账号（用户名）")
    private String username;//通过token获取

    @TableField(value = "request_url")
    @ApiModelProperty(value = "请求URL")
    private String requestUrl;

    @TableField(value = "method_type")
    @ApiModelProperty(value = "请求方法类型")
    private String methodType;

    @TableField(value = "method")
    @ApiModelProperty(value = "处理请求的方法")
    private String method;

    @TableField(value = "request_data")
    @ApiModelProperty(value = "请求数据")
    private String requestData;

    @TableField(value = "status_code")
    @ApiModelProperty(value = "响应状态码")
    private Integer statusCode;

    @TableField(value = "response_data")
    @ApiModelProperty(value = "响应数据")
    private String responseData;

    @TableField(value = "error_msg")
    @ApiModelProperty(value = "错误信息")
    private String errorMsg;

    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(value = "took_time")
    @ApiModelProperty(value = "耗时，单位：秒")
    private Double tookTime;
}
