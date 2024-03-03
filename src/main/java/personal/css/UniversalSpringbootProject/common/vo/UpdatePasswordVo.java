package personal.css.UniversalSpringbootProject.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * @Description: 更新密码所需数据
 * @Author: CSS
 * @Date: 2024/3/3 21:28
 */

@Data
public class UpdatePasswordVo {
    @ApiModelProperty(value = "用户名")
    private String name;

    @ApiModelProperty(value = "旧密码")
    private String oldPassword;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$",message = "密码规则：必须包含大小写字母和数字，至少一个特殊字符，长度8-16！")
    @ApiModelProperty(value = "新密码，定义规则：必须包含大小写字母和数字，至少一个特殊字符，长度8-16！")
    private String newPassword;
}
