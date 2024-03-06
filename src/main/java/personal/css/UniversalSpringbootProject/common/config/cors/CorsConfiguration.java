package personal.css.UniversalSpringbootProject.common.config.cors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description: 全局跨域配置
 * @Author: CSS
 * @Date: 2024/2/28 17:44
 */
@Configuration
public class CorsConfiguration {

    @Autowired
    private CorsPropertiesConfig corsPropertiesConfig;

    @Bean
    WebMvcConfigurer webMvcConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping(corsPropertiesConfig.getAddMapping())
                        .allowedOriginPatterns(corsPropertiesConfig.getAllowedOriginPattern())
                        //.allowedOrigins(corsPropertiesConfig.getAllowedOrigins())
                        .allowCredentials(corsPropertiesConfig.getAllowCredentials())
                        //.allowedHeaders(corsPropertiesConfig.getAllowedHeaders())
                        .allowedMethods(corsPropertiesConfig.getAllowedMethods())
                        .maxAge(corsPropertiesConfig.getMaxAge());
                System.out.println(corsPropertiesConfig);
            }
        };
    }
}
