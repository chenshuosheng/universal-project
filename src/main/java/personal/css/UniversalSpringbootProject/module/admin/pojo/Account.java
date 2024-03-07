package personal.css.UniversalSpringbootProject.module.admin.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import personal.css.UniversalSpringbootProject.common.pojo.BaseEntity;

import javax.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 账户
 * @Author: CSS
 * @Date: 2024/3/2 21:17
 */
@Accessors(chain = true)
@Data
@TableName(value = "account")
public class Account extends BaseEntity {

    public static final Map<String, String> COLUMN_MAP = new HashMap<String, String>(){{
        put("name", "name");
        put("password", "password");
        putAll(BaseEntity.COLUMN_MAP);
    }};


    @Pattern(regexp = "^[a-zA-Z]\\w{4,15}$",message = "账号规则：需要字母开头，允许5-16字符，允许字母数字下划线！")
    @ApiModelProperty(value = "用户名，定义规则：账号规则：需要字母开头，允许5-16字符，允许字母数字下划线！")
    @TableField(value = "name")
    private String name;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$",message = "密码规则：必须包含大小写字母和数字，至少一个特殊字符，长度8-16！")
    @ApiModelProperty(value = "密码，定义规则：必须包含大小写字母和数字，至少一个特殊字符，长度8-16！")
    @TableField(value = "password")
    @JsonIgnore
    private String password;
}
