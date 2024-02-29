package personal.css.UniversalSpringbootProject.common.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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

        String tenantId = httpServletRequest.getHeader(ABP_TENANTID);
        String token = httpServletRequest.getHeader(AUTHORIZATION);
        String requestURI = httpServletRequest.getRequestURI();

        if (null != token && !"".equals(token.trim())){
            //解析token获得userId
            Long userId = 1001L;
            httpServletRequest.setAttribute(USER_ID, userId);
            log.info("请求路径如下：\n{}", requestURI);
            log.info(
                    "获取到部分请求头信息如下：" +
                            "\ntenantId: {}" +
                            "\ntoken: {}" +
                            "\nuserId：{}",
                    tenantId, token, userId
            );
        }

        //将请求传递给下一个过滤器或者目标Servlet
        //如果注释掉下面的代码，客户端将收不到响应
        chain.doFilter(request, response);
    }
}
