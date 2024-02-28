package personal.css.UniversalSpringbootProject.common.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

/*
    * 通过自定义拦截器实现RequestInterceptor接口，重写apply方法，在需要的服务处如：
    * @FeignClient(name = "MESSAGE-MANAGEMENT-CENTER", configuration = CustomFeignInterceptor.class)
    * 添加 configuration = CustomFeignInterceptor.class
    * 在每次使用 Feign 进行服务间通信时，自动地向请求中添加 "platform-center.user.id" 头部信息，并将其值设置为 "1"。
    * 这样做可以确保在服务间通信时，目标服务可以获取到发送请求的用户标识，从而进行相应的权限验证或其他操作。
    * 通过这种方式，我们可以实现统一的请求头信息设置，而不需要在每次 Feign 请求中手动添加相同的头部信息
*/
@Component
public class CustomFeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        template.header("platform-center.user.id", "1");
    }
}
