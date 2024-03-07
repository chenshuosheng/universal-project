package personal.css.UniversalSpringbootProject.common.filter;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import personal.css.UniversalSpringbootProject.common.exceptions.NoPermissionException;
import personal.css.UniversalSpringbootProject.common.utils.CookieUtil;
import personal.css.UniversalSpringbootProject.common.utils.ExceptionUtil;
import personal.css.UniversalSpringbootProject.common.utils.TokenUtil;
import personal.css.UniversalSpringbootProject.common.utils.code.Base64Util;
import personal.css.UniversalSpringbootProject.module.loginManage.dto.IdentityDto;
import personal.css.UniversalSpringbootProject.module.loginManage.vo.TokenVo;

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
 * 详细功能：从请求头中获取token，解析并将userId、账户名存入内存，供本次请求使用
 * @Author: CSS
 * @Date: 2024/2/29 14:24
 */

@WebFilter(
        urlPatterns = {
                "/loginManage/*",
                "/admin/*"
        },                //匹配请求路径
        filterName = "HandleTokenFromHeaderToSetAttributeFilter" //默认为类名
)
@Slf4j
public class HandleTokenFromHeaderToSetAttributeFilter implements Filter {

    //private static final Logger log = LoggerFactory.getLogger(HandleTokenFromHeaderToSetAttributeFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String accessToken = httpServletRequest.getHeader(ACCESS_TOKEN);
        String jwt = null;

        //身份令牌判空
        if (StringUtils.isNotBlank(accessToken)) {
            //解码身份令牌
            try {
                accessToken = Base64Util.decodeWithBase64(accessToken);
            } catch (RuntimeException e) {
                log.error(e.getMessage());
                writeResponse(httpServletResponse, HttpStatus.INTERNAL_SERVER_ERROR.value(), "application/json;charset=UTF-8", "{\"success\":false,\"error\":\"" + e.getMessage() + "\",\"result\":null}");
                return;
            }

            //去掉"Bearer "
            jwt = TokenUtil.getJwtWithoutBearer(accessToken);

            //根据jwt提取用户信息存入内存供本次请求使用
            try {
                setAttributes(jwt, httpServletRequest);
            } catch (NoPermissionException | JWTDecodeException e) {
                log.error(e.getMessage());
                writeResponse(httpServletResponse, HttpStatus.UNAUTHORIZED.value(), "application/json;charset=UTF-8", "{\"success\":false,\"error\":\"" + e.getMessage() + "\",\"result\":null}");
                return;
            } catch (TokenExpiredException e) {
                log.error(e.getMessage());
                //身份令牌过期，从cookie中获取刷新令牌
                String refreshToken = null;
                try {
                    refreshToken = CookieUtil.getValueFromCookies(httpServletRequest, Refresh_TOKEN);
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                    writeResponse(httpServletResponse, HttpStatus.INTERNAL_SERVER_ERROR.value(), "application/json;charset=UTF-8", "{\"success\":false,\"error\":\"" + ex.getMessage() + "\",\"result\":null}");
                    return;
                }

                //成功获取到刷新令牌，判空
                if (StringUtils.isNotBlank(refreshToken)) {
                    //解码刷新令牌
                    try {
                        refreshToken = Base64Util.decodeWithBase64(refreshToken);
                    } catch (RuntimeException ex) {
                        log.error(ex.getMessage());
                        writeResponse(httpServletResponse, HttpStatus.INTERNAL_SERVER_ERROR.value(), "application/json;charset=UTF-8", "{\"success\":false,\"error\":\"" + ex.getMessage() + "\",\"result\":null}");
                        return;
                    }

                    //去掉"Bearer "
                    jwt = TokenUtil.getJwtWithoutBearer(refreshToken);

                    //新token组
                    TokenVo newTokens = null;

                    //根据jwt提取用户信息存入内存供本次请求使用
                    try {
                        IdentityDto identityDto = setAttributes(jwt, httpServletRequest);
                        //重新构造有效载荷，受困于类型不同，无法直接使用payLoad
                        newTokens = TokenUtil.getTokenVo(identityDto.getUserId(), identityDto.getName());
                    } catch (NoPermissionException | TokenExpiredException | JWTDecodeException ex) {
                        log.error(ex.getMessage());
                        writeResponse(httpServletResponse, HttpStatus.UNAUTHORIZED.value(), "application/json;charset=UTF-8", "{\"success\":false,\"error\":\"" + e.getMessage() + "\",\"result\":null}");
                        return;
                    } catch (IllegalArgumentException ex) {
                        log.error(ex.getMessage());
                        writeResponse(httpServletResponse, HttpStatus.INTERNAL_SERVER_ERROR.value(), "application/json;charset=UTF-8", "{\"success\":false,\"error\":\"" + ex.getMessage() + "\",\"result\":null}");
                        return;
                    }

                    //将token存储到客户端Cookie
                    CookieUtil.setCookie(httpServletResponse, ACCESS_TOKEN, newTokens.getAccessToken());
                    CookieUtil.setCookie(httpServletResponse, Refresh_TOKEN, newTokens.getRefreshToken());

                } else {
                    log.error("未获取到刷新令牌，无法重新生成jwt！");
                    writeResponse(httpServletResponse, HttpStatus.UNAUTHORIZED.value(), "application/json;charset=UTF-8", "{\"success\":false,\"error\":\"令牌已过期！请重新登录！\",\"result\":null}");
                    return;
                }
            } catch (RuntimeException e) {
                log.error(e.getMessage());
                writeResponse(httpServletResponse, HttpStatus.INTERNAL_SERVER_ERROR.value(), "application/json;charset=UTF-8", "{\"success\":false,\"error\":\"" + e.getMessage() + "\",\"result\":null}");
                return;
            }
        }

        //将请求传递给下一个过滤器或者目标Servlet
        //如果注释掉下面的代码，客户端将收不到响应
        chain.doFilter(request, response);
    }


    private void writeResponse(HttpServletResponse httpServletResponse, int status, String contentType, String message) throws IOException {
        httpServletResponse.setStatus(status);
        httpServletResponse.setContentType(contentType);
        httpServletResponse.getWriter().write(message);
    }


    /**
     * 根据jwt提取用户信息存入内存供本次请求使用
     *
     * @param jwt
     * @param httpServletRequest
     * @return
     * @throws NoPermissionException
     * @throws IllegalArgumentException
     * @throws TokenExpiredException
     * @throws JWTDecodeException
     */
    private IdentityDto setAttributes(String jwt, HttpServletRequest httpServletRequest) throws NoPermissionException, IllegalArgumentException, TokenExpiredException, JWTDecodeException {
        try {
            String tenantId = httpServletRequest.getHeader(ABP_TENANT_ID);
            String requestURI = httpServletRequest.getRequestURI();

            //解析token获取有效载荷
            Map<String, Claim> payLoad = TokenUtil.getPayLoadByAnalysisJWT(jwt);
            //获取userId
            Long userId = payLoad.get("id").asLong();

            //超级管理员才有权访问这两个路径下的接口
            if (requestURI.startsWith("/admin") && !requestURI.startsWith("/admin/user/getUserInfo") && SUPER_ADMIN_ID != userId) {
                throw new NoPermissionException();
            }

            //获取name
            String name = payLoad.get("name").asString();
            //将userId、账户名存入内存，供本次请求使用
            httpServletRequest.setAttribute(USER_ID, userId);
            httpServletRequest.setAttribute(ACCOUNT, name);

            log.info("请求路径如下：\n{}", requestURI);
            log.info(
                    "获取到部分请求头信息如下：" +
                            "\ntenantId: {}" +
                            "\naccessToken: {}" +
                            "\nuserId：{}" +
                            "\nname：{}",
                    tenantId, "Bearer " + jwt, userId, name
            );

            return new IdentityDto(userId, name);
        } catch (IllegalArgumentException e) {
            ExceptionUtil.recordLogAndThrowException(IllegalArgumentException.class, e.getMessage(), "参数异常！");
        } catch (TokenExpiredException e) {
            ExceptionUtil.recordLogAndThrowException(TokenExpiredException.class, e.getMessage(), "令牌过期！");
        } catch (JWTDecodeException e) {
            ExceptionUtil.recordLogAndThrowException(JWTDecodeException.class, e.getMessage(), "身份异常！");
        } catch (NoPermissionException e) {
            ExceptionUtil.recordLogAndThrowException(NoPermissionException.class, e.getMessage(), "很抱歉，您无权限访问此资源！");
        } catch (RuntimeException e) {
            ExceptionUtil.recordLogAndThrowException(RuntimeException.class, e.getMessage(), "服务器异常！");
        }
        return null;
    }
}


/**
 * 异常抛出的位置: 过滤器（Filter）和Spring的异常处理机制（例如@RestControllerAdvice）工作在不同的层次上。
 * Spring的全局异常处理主要捕获的是控制器（Controller）层抛出的异常。
 * 如果异常是在过滤器中抛出的，那么它不会被Spring的全局异常处理器捕获，因为此时请求可能还没有进入到Spring的DispatcherServlet中。
 * <p>
 * 异常处理的顺序: 如果有多个异常处理器都能处理同一个异常，那么Spring会根据异常处理器的顺序来决定使用哪一个。
 * 但这里的关键点在于，过滤器中抛出的异常并不会进入到Spring的异常处理流程中。
 * <p>
 * 解决方案:
 * <p>
 * 1.自定义错误响应: 在过滤器中直接捕获JWTVerificationException异常，并设置HTTP响应的状态码和消息体，而不是抛出异常。
 * 这样可以直接向客户端返回错误信息，而不是依赖于Spring的异常处理机制。
 * 2.使用Spring的拦截器: 考虑将功能从过滤器迁移到Spring拦截器（Interceptor）。
 * 拦截器在Spring的上下文中执行，因此在拦截器中抛出的异常能够被Spring的全局异常处理器捕获。
 * 但请注意，拦截器也有它们自己的限制，例如它们不会处理静态资源的请求。
 */
