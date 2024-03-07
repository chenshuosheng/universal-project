package personal.css.UniversalSpringbootProject.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 线程池配置
 * @Author: CSS
 * @Date: 2024/3/7 21:39
 */

@Configuration
@PropertySource(value = {"classpath:/application.yml"})
@ConfigurationProperties(prefix = "thread")
@Data
public class ThreadPoolExecutorConfig {

    private Integer corePoolSize = 8;           //核心线程数
    private Integer maximumPoolSize = 16;       //最大线程数
    private Long  keepAliveTime = 1L;           //非核心线程的闲置存活时间
    private Integer queueLength = 50;           //队列长度
    private TimeUnit unit = TimeUnit.MINUTES;   //存活时间单位

    ArrayBlockingQueue<Runnable> arrayBlockingQueue = new ArrayBlockingQueue<Runnable>(queueLength);
}
