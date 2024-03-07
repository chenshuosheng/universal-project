package personal.css.UniversalSpringbootProject.common.filter;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import cn.hutool.extra.spring.SpringUtil;
import org.springframework.context.annotation.Configuration;
import personal.css.UniversalSpringbootProject.common.pojo.Slf4jLogging;
import personal.css.UniversalSpringbootProject.common.service.Slf4jLoggingService;

import java.util.Date;

/**
 * @Description: Slf4j日志入库
 * @Author: CSS
 * @Date: 2024/3/6 14:39
 */
@Configuration
public class Slf4jLogFilter extends Filter<LoggingEvent> {

    private Slf4jLoggingService loggingService = null;

    @Override
    public FilterReply decide(LoggingEvent event) {
        String loggerName = event.getLoggerName();
        if(loggerName.startsWith("personal.css.UniversalSpringbootProject")){
            //项目本身的日志才会入库
            Slf4jLogging log = new Slf4jLogging();
            log.setLogLevel(event.getLevel().levelStr);//级别
            log.setLogClass(loggerName);//类
            log.setLogMethod(null);//方法
            log.setContent(event.getFormattedMessage());//内容
            log.setCreateTime(new Date(System.currentTimeMillis()));//时间

            loggingService = SpringUtil.getBean(Slf4jLoggingService.class);
            //日志入库
            loggingService.asyncSave(log);
            return FilterReply.ACCEPT;
        }else{
            //非项目本身的日志不会入库
            return FilterReply.DENY;
        }
    }
}
