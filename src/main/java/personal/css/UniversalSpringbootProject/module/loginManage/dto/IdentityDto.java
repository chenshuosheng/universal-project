package personal.css.UniversalSpringbootProject.module.loginManage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 身份标识实体类
 * @Author: CSS
 * @Date: 2024/3/4 20:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdentityDto {
    private Long UserId;
    private String name;
}
