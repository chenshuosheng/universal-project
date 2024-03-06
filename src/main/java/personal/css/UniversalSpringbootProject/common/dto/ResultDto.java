package personal.css.UniversalSpringbootProject.common.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: TODO
 * @Author: CSS
 * @Date: 2024/3/6 23:08
 */
@Accessors(chain = true)
@Data
public class ResultDto {

    private String data;

    private Integer statusCode;

    private String error;
}
