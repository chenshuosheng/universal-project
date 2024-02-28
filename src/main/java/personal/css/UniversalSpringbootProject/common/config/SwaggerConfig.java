package personal.css.UniversalSpringbootProject.common.config;

import io.swagger.annotations.Api;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO
 * @Author: CSS
 * @Date: 2024/2/28 22:33
 */


@EnableSwagger2
@Configuration
@ConditionalOnProperty(prefix = "myConfig", name = "swagger-ui-open", havingValue = "true")
public class SwaggerConfig {

    @Bean
    public Docket creatRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()//必须有select扫描
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(security());
    }

    //配置页面显示信息
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .description("通用springboot项目接口文档")
                .title("通用springboot项目")
                .version("1.0")
                .build();
    }

    //配置接口的安全认证方式
    private List<ApiKey> security() {
        ArrayList<ApiKey> apiKeys = new ArrayList<>();
        apiKeys.add(new ApiKey("Authorization", "Authorization", "header"));
        apiKeys.add(new ApiKey("Abp.TenantId", "Abp.TenantId", "header"));
        return apiKeys;
    }
}
