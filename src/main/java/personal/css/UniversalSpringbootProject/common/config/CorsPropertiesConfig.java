package personal.css.UniversalSpringbootProject.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Description: TODO
 * @Author: CSS
 * @Date: 2024/2/28 18:01
 */

@Configuration
@PropertySource(value = {"classpath:/application.yml"})
@ConfigurationProperties(prefix = "cors")
@Data
public class CorsPropertiesConfig {
    //允许跨域请求的路径模式
    private String addMapping;
    //类似于 allowedOrigins，但是使用通配符模式进行匹配，可以更灵活地设置允许访问资源的源
    private String allowedOriginPattern;
    //允许访问资源的源
    //private String allowedOrigins;
    //是否允许发送认证信息（如 cookies、HTTP 认证信息）到服务器
    private Boolean allowCredentials;
    //允许的请求头
    //private String allowedHeaders;
    //允许的 HTTP 方法
    private String allowedMethods;
    //预检请求的有效期
    private Long maxAge;
}
