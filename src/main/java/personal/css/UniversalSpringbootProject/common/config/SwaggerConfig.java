package personal.css.UniversalSpringbootProject.common.config;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.List;

import static personal.css.UniversalSpringbootProject.common.consts.MyConst.ABP_TENANT_ID;
import static personal.css.UniversalSpringbootProject.common.consts.MyConst.ACCESS_TOKEN;

/**
 * @Description: 接口文档配置
 * @Author: CSS
 * @Date: 2024/2/28 22:33
 */


@Configuration
@EnableSwagger2WebMvc
//要记得在配置文件配置打开，否则打开文档会啥也没有
//@ConditionalOnProperty(prefix = "myConfig", name = "swagger-ui-open", havingValue = "true")
public class SwaggerConfig {

    @Value("${myconfig.swagger-ui-open}")
    private Boolean enable;

    @Bean
    public Docket creatRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(enable)
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
        apiKeys.add(new ApiKey(ACCESS_TOKEN, ACCESS_TOKEN, "header"));
        apiKeys.add(new ApiKey(ABP_TENANT_ID, ABP_TENANT_ID, "header"));
        return apiKeys;
    }
}
