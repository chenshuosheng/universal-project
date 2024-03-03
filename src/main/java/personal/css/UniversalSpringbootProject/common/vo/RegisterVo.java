package personal.css.UniversalSpringbootProject.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

/**
 * @Description: 用户注册信息
 * @Author: CSS
 * @Date: 2024/3/3 14:48
 */
@Data
@ApiModel(value = "用户注册信息")
public class RegisterVo {

    @Pattern(regexp = "^[a-zA-Z]\\w{4,15}$",message = "账号规则：需要字母开头，允许5-16字符，允许字母数字下划线！")
    @ApiModelProperty(value = "用户名，定义规则：账号规则：需要字母开头，允许5-16字符，允许字母数字下划线！")
    private String name;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$",message = "密码规则：必须包含大小写字母和数字，至少一个特殊字符，长度8-16！")
    @ApiModelProperty(value = "密码，定义规则：必须包含大小写字母和数字，至少一个特殊字符，长度8-16！")
    private String password;

    @Min(value = 10, message = "年龄不能小于10！")
    @Max(value = 50, message = "年龄不能大于50！")
    @ApiModelProperty(value = "年龄，范围[10,50]")
    private Integer age;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "邮箱格式不符！")
    @ApiModelProperty(value = "邮箱")
    private String email;
}

/**
 * ^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$详细介绍
 *
 *
 * 这个正则表达式是用来验证密码的强度，确保密码同时包含小写字母、大写字母、数字和特殊字符，并且长度在8到20个字符之间。下面是对这个正则表达式各部分的详细解释：
 *
 * ^ 和 $：
 *
 * ^ 表示字符串的开始。
 * $ 表示字符串的结束。 这两个符号确保整个表达式匹配整个输入字符串，而不是字符串中的某个子串。
 * (?=.*[a-z])：
 *
 * (?=...) 是一个正向前瞻断言，用来指定一个子表达式的预期布局。这里它表示后面的字符串需要满足括号内的条件，但不会消耗字符（即不移动匹配位置）。
 * .* 表示任意数量的任意字符（除了换行符）。
 * [a-z] 表示任意一个小写字母。 综上，(?=.*[a-z]) 确保字符串中至少有一个小写字母。
 * (?=.*[A-Z])：
 *
 * 类似地，这部分确保字符串中至少有一个大写字母。
 * (?=.*\d)：
 *
 * \d 表示任意一个数字（0-9）。
 * 这部分确保字符串中至少有一个数字。
 * (?=.[@$!%?&])：
 *
 * [...]{8,20} 中的 [...] 部分列出了允许的特殊字符集合。
 * 这部分确保字符串中至少有一个特殊字符（在这个例子中是@$!%*?&中的任何一个）。
 * [A-Za-z\d@$!%*?&]{8,20}：
 *
 * 最后这部分定义了密码可以使用的所有字符类型：小写字母、大写字母、数字和特殊字符（@$!%*?&）。
 * {8,20} 指定了密码的长度限制，至少8个字符，最多20个字符。
 * 总结起来，这个正则表达式要求输入的字符串（在这个场景中是密码）必须满足以下条件：
 *
 * 长度在8到20个字符之间。
 * 至少包含一个小写字母。
 * 至少包含一个大写字母。
 * 至少包含一个数字。
 * 至少包含一个特殊字符（特殊字符集合为@$!%*?&）。
 */
