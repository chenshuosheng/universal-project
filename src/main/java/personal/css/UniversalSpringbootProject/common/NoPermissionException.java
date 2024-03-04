package personal.css.UniversalSpringbootProject.common;

/**
 * @Description: 自定义无权限异常类
 * @Author: CSS
 * @Date: 2024/3/4 23:04
 */
public class NoPermissionException extends RuntimeException{
    public NoPermissionException() {
        super("无权限访问此资源");
    }

    public NoPermissionException(String message) {
        super(message);
    }
}
