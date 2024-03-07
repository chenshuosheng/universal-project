package personal.css.UniversalSpringbootProject.module.admin.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import personal.css.UniversalSpringbootProject.common.pojo.BaseEntity;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 用户信息实体类
 * @Author: CSS
 * @Date: 2024/2/29 10:20
 */
@Accessors(chain = true)
@Data
@TableName(value = "user")
@ApiModel(value = "user", description = "用户信息实体类")
public class User extends BaseEntity {

    public static final Map<String, String> COLUMN_MAP = new HashMap<String, String>(){{
        put("name", "name");
        put("age", "age");
        put("email", "email");
        putAll(BaseEntity.COLUMN_MAP);
    }};

    @Pattern(regexp = "^[a-zA-Z]\\w{4,15}$",message = "账号规则：需要字母开头，允许5-16字符，允许字母数字下划线！")
    @ApiModelProperty(value = "用户名，定义规则：账号规则：需要字母开头，允许5-16字符，允许字母数字下划线！")
    @TableField(value = "name")
    private String name;

    @Min(value = 10, message = "年龄不能小于10！")
    @Max(value = 50, message = "年龄不能大于50！")
    @ApiModelProperty(value = "年龄，范围[10,50]")
    @TableField(value = "age")
    private Integer age;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "邮箱格式不符！")
    @ApiModelProperty(value = "邮箱")
    @TableField(value = "email")
    private String email;
}
