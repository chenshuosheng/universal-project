package personal.css.UniversalSpringbootProject.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 账户
 * @Author: CSS
 * @Date: 2024/3/2 21:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "account")
public class Account {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "name")
    private String name;

    @TableField(value = "password")
    private String password;
}
