package personal.css.UniversalSpringbootProject.module.user.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import personal.css.UniversalSpringbootProject.common.pojo.BaseEntity;

/**
 * @Description: 用户信息实体类
 * @Author: CSS
 * @Date: 2024/2/29 10:20
 */
@Data
@TableName(value = "user")
@ApiModel(value = "user", description = "用户信息实体类")
public class User extends BaseEntity {

    @ApiModelProperty(value = "用户名")
    @TableField(value = "name")
    private String name;

    @ApiModelProperty(value = "年龄")
    @TableField(value = "age")
    private Integer age;

    @ApiModelProperty(value = "邮箱")
    @TableField(value = "email")
    private String email;
}
