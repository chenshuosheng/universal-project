package personal.css.UniversalSpringbootProject.common.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import personal.css.UniversalSpringbootProject.common.utils.JWTUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static personal.css.UniversalSpringbootProject.common.consts.MyConst.*;

/**
 * @Description: 对请求头进行处理的过滤器：
 * 在请求到达Servlet之前或响应返回给客户端之前对请求和响应进行预处理和后处理操作
 * @Author: CSS
 * @Date: 2024/2/29 14:24
 */

@WebFilter(
        urlPatterns = "*",                //匹配任意请求路径
        filterName = "HandleHeaderFilter" //默认为类名
)
public class HandleHeaderFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(HandleHeaderFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String tenantId = httpServletRequest.getHeader(ABP_TENANT_ID);
        String token = httpServletRequest.getHeader(AUTHORIZATION);
        String jwt = token;
        String requestURI = httpServletRequest.getRequestURI();

        if (null != token && !"".equals(token.trim())){
            if(token.startsWith("Bearer "))
                jwt = token.substring(7);

            //解析token获得userId
            try {
                //解析token获取有效载荷
                Map<String, Claim> payLoad = JWTUtil.getPayLoadByAnalysisJWT(jwt);
                //获取userId
                Long userId = Long.valueOf(payLoad.get("id").asString());
                //将userId存入内存，供本次请求使用
                httpServletRequest.setAttribute(USER_ID, userId);

                log.info("请求路径如下：\n{}", requestURI);
                log.info(
                        "获取到部分请求头信息如下：" +
                                "\ntenantId: {}" +
                                "\ntoken: {}" +
                                "\nuserId：{}",
                        tenantId, token, userId
                );
            } catch (JWTVerificationException e) {
                e.printStackTrace();
                httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
                httpServletResponse.setContentType("application/json;charset=UTF-8");
                httpServletResponse.getWriter().write("{\"success\":false,\"error\":\"令牌有误！身份信息异常！\",\"result\":null}");
                return; // 直接返回，不再向下传递请求
            }
        }

        //将请求传递给下一个过滤器或者目标Servlet
        //如果注释掉下面的代码，客户端将收不到响应
        chain.doFilter(request, response);
    }
}


/**
 * 异常抛出的位置: 过滤器（Filter）和Spring的异常处理机制（例如@RestControllerAdvice）工作在不同的层次上。
 *          Spring的全局异常处理主要捕获的是控制器（Controller）层抛出的异常。
 *          如果异常是在过滤器中抛出的，那么它不会被Spring的全局异常处理器捕获，因为此时请求可能还没有进入到Spring的DispatcherServlet中。
 *
 * 异常处理的顺序: 如果有多个异常处理器都能处理同一个异常，那么Spring会根据异常处理器的顺序来决定使用哪一个。
 *          但这里的关键点在于，过滤器中抛出的异常并不会进入到Spring的异常处理流程中。
 *
 * 解决方案:
 *
 *      1.自定义错误响应: 在过滤器中直接捕获JWTVerificationException异常，并设置HTTP响应的状态码和消息体，而不是抛出异常。
 *          这样可以直接向客户端返回错误信息，而不是依赖于Spring的异常处理机制。
 *      2.使用Spring的拦截器: 考虑将功能从过滤器迁移到Spring拦截器（Interceptor）。
 *          拦截器在Spring的上下文中执行，因此在拦截器中抛出的异常能够被Spring的全局异常处理器捕获。
 *          但请注意，拦截器也有它们自己的限制，例如它们不会处理静态资源的请求。
 */
