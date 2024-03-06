package personal.css.UniversalSpringbootProject.module.loginManage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Description: 身份标识实体类
 * @Author: CSS
 * @Date: 2024/3/4 20:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class IdentityDto {
    private Long UserId;
    private String name;
}
