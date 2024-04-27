package personal.css.UniversalSpringbootProject.common.annotation;

import java.lang.annotation.*;

/**
 * @Description: TODO
 * @Author: CSS
 * @Date: 2024/4/26 22:45
 */
@Target(value = ElementType.FIELD)//仅作用于属性
@Retention(value = RetentionPolicy.RUNTIME)//运行时有效
@Documented//将显示在文档上
public @interface Dict {

    //数据表，默认从dict表中查询
    String table() default "dict";

    //需要解释的词
    String code();

    //解释
    String displayName();
}
