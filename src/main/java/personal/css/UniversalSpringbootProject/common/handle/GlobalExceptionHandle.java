package personal.css.UniversalSpringbootProject.common.handle;

import cn.hutool.json.JSONArray;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

/*    //捕获到令牌异常 用不到
    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<ResultVo<?>> CaughtJWTVerificationException(HttpServletRequest request, JWTVerificationException exception){
        exception.printStackTrace();
        String message = "令牌有误！身份信息异常！";
        return new ResponseEntity<>(new ResultVo<>(false, message, null), HttpStatus.BAD_REQUEST);
    }*/


    //捕获到数据库重复键异常
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ResultVo<?>> CaughtDuplicateKeyException(HttpServletRequest request, DuplicateKeyException exception) {
        exception.printStackTrace();
        String message = exception.getMessage();
        return new ResponseEntity<>(new ResultVo<>(false, message, null), HttpStatus.BAD_REQUEST);
    }


    //捕获到查询条件异常
    @ExceptionHandler(BadSqlGrammarException.class)
    public ResponseEntity<ResultVo<?>> CaughtBadSqlGrammarException(HttpServletRequest request, BadSqlGrammarException exception) {
        exception.printStackTrace();
        SQLException sqlException = exception.getSQLException();
        String message = sqlException.getMessage();
        return new ResponseEntity<>(new ResultVo<>(false, message, null), HttpStatus.BAD_REQUEST);
    }

    //捕获到mybatis查询异常
    @ExceptionHandler(MyBatisSystemException.class)
    public ResponseEntity<ResultVo<?>> CaughtMyBatisSystemException(HttpServletRequest request, MyBatisSystemException exception) {
        exception.printStackTrace();
        String message = "数据库操作异常，请联系管理员处理";
        return new ResponseEntity<>(new ResultVo<>(false, message, null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //捕获到JSON数据解析异常
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResultVo<?>> CaughtHttpMessageNotReadableException(HttpServletRequest request, HttpMessageNotReadableException exception) {
        exception.printStackTrace();
        String message = "解析JSON数据时出现错误，请检查数据格式是否正确";
        return new ResponseEntity<>(new ResultVo<>(false, message, null), HttpStatus.BAD_REQUEST);
    }

    //捕获到参数异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResultVo<?>> CaughtMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException exception) {
        exception.printStackTrace();
        JSONArray array = new JSONArray();
        for (ObjectError error : exception.getAllErrors()) {
            array.add(error.getDefaultMessage());
        }
        String message = array.toString();
        return new ResponseEntity<>(new ResultVo<>(false, message, null), HttpStatus.BAD_REQUEST);
    }

    //捕获到运行时异常
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResultVo<?>> handleRuntimeException(HttpServletRequest request, RuntimeException e) {
        e.printStackTrace();
        String message;
        if (null != e.getCause())
            message = e.getCause().getMessage();
        else
            message = e.getMessage();
        return new ResponseEntity<>(new ResultVo<>(false, message, null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //捕获到Exception异常
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultVo<?>> handleException(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(new ResultVo<>(false, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
