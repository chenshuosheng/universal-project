package personal.css.UniversalSpringbootProject.common.aspect;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import personal.css.UniversalSpringbootProject.common.dto.ResultDto;
import personal.css.UniversalSpringbootProject.common.vo.ResultVo;
import personal.css.UniversalSpringbootProject.module.account.pojo.ApiLog;
import personal.css.UniversalSpringbootProject.module.log.service.ApiLogService;
import personal.css.UniversalSpringbootProject.module.loginManage.dto.IdentityDto;

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
 * @Description: TODO
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
        httpServletRequest.setAttribute("methodName",methodName);

        //获取参数名称
        LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        Method[] methods = point.getTarget().getClass().getDeclaredMethods();
        Method method = null;
        for (int i = 0; i < methods.length; i++) {
            String name = methods[i].getName();
            if(signature.getName().equals(name)){
                method = methods[i];
                break;
            }
        }
        Parameter[] parameters = method.getParameters();

        //请求参数值处理
        StringJoiner sj = new StringJoiner(",","{","}");
        for (int i = 0; i < args.length; i++) {
            sj.add(new StringBuilder()
                    .append("\"")
                    .append(parameters[i])
                    .append("\":")
                    .append("\"")
                    .append((args[i]==null && i==0)?identityDto.getUserId():args[i].toString())
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
            data = new JSONObject(result);
        } catch (Exception e) {
            data = result;
        }

        ResultDto resultDto = new ResultDto()
                .setData(data != null ? data.toString() : null)
                .setError(errorMsg)
                .setStatusCode(httpServletResponse.getStatus());

        try {
            //异步入库
            CompletableFuture<Void> async = CompletableFuture.supplyAsync(() -> {
                //构造日志对象
                ApiLog apiLog = setProjectLog(took, identityDto, requestUrl, methodType, methodName, requestParameters, resultDto);
                //日志数据入库
                apiLogService.insert(apiLog);
                return null;
            });
            async.get();  //会阻塞当前线程，直到异步操作执行完成才继续往下执行
        } catch (Exception ex) {
            log.error("日志入库异常：{}", ex.getMessage());
        }
        return proceed;
    }


    @AfterThrowing(pointcut = "all()", throwing = "e")
    public Object logThrowing(Exception e) throws IOException {
        /*long start = System.currentTimeMillis();
        log.info("\n进入切面LogAspect...");*/

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

        ResultDto resultDto = new ResultDto()
                .setData(null)
                .setError(e.getMessage())
                .setStatusCode(httpServletResponse.getStatus());

        try {
            //异步入库
            CompletableFuture<Void> async = CompletableFuture.supplyAsync(() -> {
                //构造日志对象
                ApiLog apiLog = setProjectLog(took, identityDto, requestUrl, methodType, methodName, requestParameters, resultDto);
                //日志数据入库
                apiLogService.insert(apiLog);
                return null;
            });
            async.get();
        } catch (Exception ex) {
            log.error("日志入库异常：{}", ex.getMessage());
        }
        return null;
    }

    private ApiLog setProjectLog(double took, IdentityDto identityDto, String requestUrl, String methodType, String methodName, String args, ResultDto resultDto) {
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
