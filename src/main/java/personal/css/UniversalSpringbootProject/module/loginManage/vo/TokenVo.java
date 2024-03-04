package personal.css.UniversalSpringbootProject.module.loginManage.vo;

import lombok.Data;

/**
 * @Description: token组
 * @Author: CSS
 * @Date: 2024/3/4 16:55
 */
@Data
public class TokenVo {

    private String accessToken;
    private String refreshToken;
}
