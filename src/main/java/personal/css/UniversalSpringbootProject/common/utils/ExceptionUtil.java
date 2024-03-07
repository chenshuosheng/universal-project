package personal.css.UniversalSpringbootProject.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;

/**
 * @Description: 异常处理工具
 * @Author: CSS
 * @Date: 2024/3/7 9:31
 */
@Slf4j
public class ExceptionUtil {

    public  static <T extends RuntimeException> void recordLogAndThrowException(Class<T> exceptionClass, String logMsg, String returnMsg)  {
        log.error(logMsg);
        try {
            throw exceptionClass.getDeclaredConstructor(String.class).newInstance(returnMsg);
        } catch (InstantiationException e) {
            log.error(e.getMessage());
        } catch (IllegalAccessException e) {
            log.error(e.getMessage());
        } catch (InvocationTargetException e) {
            log.error(e.getMessage());
        } catch (NoSuchMethodException e) {
            log.error(e.getMessage());
        }
    }
}
