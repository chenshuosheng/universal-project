package personal.css.UniversalSpringbootProject.common.exceptions;

/**
 * @Description: 自定义无权限异常类
 * @Author: CSS
 * @Date: 2024/3/4 23:04
 */
public class NoPermissionException extends RuntimeException{
    public NoPermissionException() {
        super("该用户无访问该资源的权限！");
    }

    public NoPermissionException(String message) {
        super(message);
    }
}
