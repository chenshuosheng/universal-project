package personal.css.UniversalSpringbootProject.module.admin.mapper.aspect;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import personal.css.UniversalSpringbootProject.common.config.ThreadPoolExecutorConfig;
import personal.css.UniversalSpringbootProject.common.dto.ResultDto;
import personal.css.UniversalSpringbootProject.common.exceptions.NoPermissionException;
import personal.css.UniversalSpringbootProject.common.vo.ResultVo;
import personal.css.UniversalSpringbootProject.module.admin.pojo.ApiLog;
import personal.css.UniversalSpringbootProject.module.admin.service.ApiLogService;
import personal.css.UniversalSpringbootProject.module.loginManage.dto.IdentityDto;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;

import static personal.css.UniversalSpringbootProject.common.consts.MyConst.ACCOUNT;
import static personal.css.UniversalSpringbootProject.common.consts.MyConst.USER_ID;

/**
 * @Description: 请求接口日志切面
 * @Author: CSS
 * @Date: 2024/3/6 16:36
 */
@Aspect
@Component
@Slf4j
@Order(1)
public class LogAspect {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private ApiLogService apiLogService;

    @Autowired
    private ThreadPoolExecutorConfig threadPoolExecutorConfig;

    private ThreadPoolExecutor executor;

    @PostConstruct
    public void init(){
        executor = new ThreadPoolExecutor(
                threadPoolExecutorConfig.getCorePoolSize(),
                threadPoolExecutorConfig.getMaximumPoolSize(),
                threadPoolExecutorConfig.getKeepAliveTime(),
                threadPoolExecutorConfig.getUnit(),
                threadPoolExecutorConfig.getArrayBlockingQueue(),
                new ThreadPoolExecutor.CallerRunsPolicy() // 饱和策略
        );
    }


    @Pointcut("execution(public * personal.css.UniversalSpringbootProject..controller..*.*(..))")
    public void all() {
    }

    @Around("all()")
    public Object log(ProceedingJoinPoint point) throws Throwable {
        long start = System.currentTimeMillis();
        log.info("\n进入切面LogAspect...");
        httpServletRequest.setAttribute("start", start);

        //获取请求参数，处理请求
        Object[] args = point.getArgs();
        Object proceed = point.proceed(args);
        Signature signature = point.getSignature();

        long end = System.currentTimeMillis();

        //请求耗时
        double took = (end - start) / 1000.0;
        log.info("退出切面LogAspect耗时：{}秒", took);

        //身份信息
        IdentityDto identityDto = getIdentityDto();

        //请求url
        String requestUrl = httpServletRequest.getRequestURL().toString();

        //请求方法类型
        String methodType = httpServletRequest.getMethod();

        //处理请求的方法名((MethodInvocationProceedingJoinPoint.MethodSignatureImpl) signature).getParameterNames()
        String methodName = signature.toString();
        httpServletRequest.setAttribute("methodName", methodName);

        //获取参数名称
        Class<?> sonClazz = point.getTarget().getClass();
        Method[] methods = sonClazz.getDeclaredMethods();
        Method method = null;
        for (int i = 0; i < methods.length; i++) {
            String name = methods[i].getName();
            if (signature.getName().equals(name)) {
                method = methods[i];
                break;
            }
        }
        //方法可能继承自super类
        if (method == null) {
            Class<?> superclass = sonClazz.getSuperclass();
            Method[] declaredMethods = superclass.getDeclaredMethods();
            for (int i = 0; i < declaredMethods.length; i++) {
                String name = declaredMethods[i].getName();
                if (signature.getName().equals(name)) {
                    method = declaredMethods[i];
                    break;
                }
            }
        }
        Parameter[] parameters = method.getParameters();

        //请求参数值处理
        StringJoiner sj = new StringJoiner(",", "{", "}");
        for (int i = 0; i < args.length; i++) {
            sj.add(new StringBuilder()
                    .append("\"")
                    .append(parameters[i])
                    .append("\":")
                    .append("\"")
                    .append((args[i] == null && i == 0) ? identityDto.getUserId() : args[i])
                    .append("\"")
            );
        }
        String requestParameters = sj.toString();
        httpServletRequest.setAttribute("requestParameters", requestParameters);

        //响应数据
        Object body = ((ResponseEntity) proceed).getBody();
        ResultVo vo = (ResultVo) body;
        Object result = vo.getResult();
        Object data = null;
        String errorMsg = null;
        if (vo.isSuccess())
            errorMsg = null;
        else
            errorMsg = vo.getError();

        try {
            data = JSONObject.toJSONString(result);
        } catch (Exception e) {
            data = result;
        }

        ResultDto resultDto = new ResultDto()
                .setData(data != null ? data.toString() : null)
                .setError(errorMsg)
                .setStatusCode(httpServletResponse.getStatus());

        //异步入库
        asyncToDB(took, identityDto, requestUrl, methodType, methodName, requestParameters, resultDto);
        return proceed;
    }


    @AfterThrowing(pointcut = "all()", throwing = "e")
    public Object logThrowing(Exception e) throws IOException {

        long start = (long) httpServletRequest.getAttribute("start");
        long end = System.currentTimeMillis();
        double took = (end - start) / 1000.0;
        log.info("退出切面LogAspect耗时：{}秒", took);

        //身份信息
        IdentityDto identityDto = getIdentityDto();

        //请求url
        String requestUrl = httpServletRequest.getRequestURL().toString();

        //请求方法类型
        String methodType = httpServletRequest.getMethod();

        //处理请求的方法名
        String methodName = (String) httpServletRequest.getAttribute("methodName");

        //请求参数
        String requestParameters = (String) httpServletRequest.getAttribute("requestParameters");

        //响应状态码
        Integer statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        if (e instanceof NoPermissionException || e instanceof TokenExpiredException || e instanceof JWTDecodeException)
            statusCode = HttpStatus.UNAUTHORIZED.value();

        ResultDto resultDto = new ResultDto()
                .setData(null)
                .setError(e.getMessage())
                .setStatusCode(statusCode);

        //异步入库
        asyncToDB(took, identityDto, requestUrl, methodType, methodName, requestParameters, resultDto);
        return null;
    }


    /**
     * 异步入库
     *
     * @param took
     * @param identityDto
     * @param requestUrl
     * @param methodType
     * @param methodName
     * @param requestParameters
     * @param resultDto
     */
    private void asyncToDB(double took, IdentityDto identityDto, String requestUrl, String methodType, String methodName, String requestParameters, ResultDto resultDto) {
        CompletableFuture<Object> async = CompletableFuture.supplyAsync(() -> {
            try {
                //构造日志对象
                ApiLog apiLog = setApiLog(took, identityDto, requestUrl, methodType, methodName, requestParameters, resultDto);
                //日志数据入库
                apiLogService.insert(apiLog);
                return null;
            } catch (Exception e) {
                log.error("异步操作异常：{}", e.getMessage());
                return null;
            }
        }, executor).exceptionally(e -> { // 异常处理
            log.error("异步操作异常：{}", e.getMessage());
            return null;
        });
    }


    /**
     * 构造日志对象
     *
     * @param took
     * @param identityDto
     * @param requestUrl
     * @param methodType
     * @param methodName
     * @param args
     * @param resultDto
     * @return
     */
    private ApiLog setApiLog(double took, IdentityDto identityDto, String requestUrl, String methodType, String methodName, String args, ResultDto resultDto) {
        ApiLog apiLog = new ApiLog();
        apiLog.setUserId(identityDto.getUserId())
                .setUsername(identityDto.getName())
                .setRequestUrl(requestUrl)
                .setMethodType(methodType)
                .setMethod(methodName)
                .setRequestData(args)
                .setStatusCode(resultDto.getStatusCode())
                .setResponseData(resultDto.getData())
                .setErrorMsg(resultDto.getError())
                .setCreateTime(new Date(System.currentTimeMillis()))
                .setTookTime(took);
        return apiLog;
    }


    /**
     * 从请求对象中获取用户身份信息
     *
     * @return
     */
    private IdentityDto getIdentityDto() {
        Object attribute1 = httpServletRequest.getAttribute(USER_ID);
        Object attribute2 = httpServletRequest.getAttribute(ACCOUNT);
        Long userId = (attribute1 != null) ? (Long) attribute1 : null;
        String userName = (attribute2 != null) ? (String) attribute2 : null;
        IdentityDto identityDto = new IdentityDto().setUserId(userId)
                .setName(userName);
        return identityDto;
    }

    private Map<String, Object> getParameters() {
        Map<String, String[]> parametersMap = httpServletRequest.getParameterMap();
        Map<String, Object> parameters = new HashMap<>();

        if (parametersMap != null && !parametersMap.isEmpty()) {
            parametersMap.forEach((key, value) -> {
                parameters.put(key, value[0]);
            });
        }
        return parameters;
    }
}
