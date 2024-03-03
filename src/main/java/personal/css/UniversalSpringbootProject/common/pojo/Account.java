package personal.css.UniversalSpringbootProject.common.pojo;

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
public class Account {
    private Long id;
    private String name;
    private String password;
}
