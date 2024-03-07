package personal.css.UniversalSpringbootProject.module.admin.service;

import org.springframework.jdbc.BadSqlGrammarException;
import personal.css.UniversalSpringbootProject.common.vo.ListResult;
import personal.css.UniversalSpringbootProject.module.admin.pojo.Slf4jLogging;

/**
 * @Description: Slf4j日志业务处理接口
 * @Author: CSS
 * @Date: 2024/3/6 14:55
 */
public interface Slf4jLoggingService {

    void asyncSave(Slf4jLogging log);

    ListResult<Slf4jLogging> list(String filter, String order) throws BadSqlGrammarException;

    ListResult<Slf4jLogging> pageList(String filter, Integer pageNum, Integer pageSize, String order) throws BadSqlGrammarException;

}
