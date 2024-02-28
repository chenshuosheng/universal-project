package personal.css.UniversalSpringbootProject.module.test_mybatis_plus.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

/**
 * @Description: TODO
 * @Author: CSS
 * @Date: 2024/2/28 15:56
 */

@Data
@TableName(value = "user")
public class User {

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private Long id;

    @TableField(value = "name")
    private String name;

    @TableField(value = "age")
    private Integer age;

    @TableField(value = "email")
    private String email;

    @TableField(exist = false)
    private List<User> friends;
}
