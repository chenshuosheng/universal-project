package personal.css.UniversalSpringbootProject.module.user.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: 用户信息
 * @Author: CSS
 * @Date: 2024/3/3 16:27
 */

@Accessors(chain = true)
@Data
public class UserInfo {
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "用户名")
    private String name;

    @ApiModelProperty(value = "年龄")
    @TableField(value = "age")
    private Integer age;

    @ApiModelProperty(value = "邮箱")
    @TableField(value = "email")
    private String email;
}
