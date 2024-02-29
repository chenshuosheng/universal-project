package personal.css.UniversalSpringbootProject.common.handle;

import org.mybatis.spring.MyBatisSystemException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import personal.css.UniversalSpringbootProject.common.vo.ResultVo;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

/**
 * @Description: 全局异常处理，当请求处理出现异常时，会根据 异常处理器 的 配置顺序 依次尝试 异常匹配 和 处理。
 * @Author: CSS
 * @Date: 2024/2/29 10:46
 */

@RestControllerAdvice
public class GlobalExceptionHandle {

    //捕获到查询条件异常
    @ExceptionHandler(BadSqlGrammarException.class)
    public ResponseEntity<ResultVo<?>> CaughtBadSqlGrammarException(HttpServletRequest request, BadSqlGrammarException exception){
        exception.printStackTrace();
        SQLException sqlException = exception.getSQLException();
        String message = sqlException.getMessage();
        return new ResponseEntity<>(new ResultVo<>(false, message, null), HttpStatus.BAD_REQUEST);
    }

    //捕获到mybatis查询异常
    @ExceptionHandler(MyBatisSystemException.class)
    public ResponseEntity<ResultVo<?>> CaughtMyBatisSystemException(HttpServletRequest request, MyBatisSystemException exception){
        exception.printStackTrace();
        String message = "数据库操作异常，请联系管理员处理";
        return new ResponseEntity<>(new ResultVo<>(false, message, null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //捕获到JSON数据解析异常
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResultVo<?>> CaughtHttpMessageNotReadableException(HttpServletRequest request, HttpMessageNotReadableException exception){
        exception.printStackTrace();
        String message = "解析JSON数据时出现错误，请检查数据格式是否正确";
        return new ResponseEntity<>(new ResultVo<>(false, message, null), HttpStatus.BAD_REQUEST);
    }

    //捕获到运行时异常
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResultVo<?>> handleException(Exception e){
        e.printStackTrace();
        return new ResponseEntity<>(new ResultVo<>(false, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
