package personal.css.UniversalSpringbootProject.common.aspect;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import personal.css.UniversalSpringbootProject.common.dto.ResultDto;
import personal.css.UniversalSpringbootProject.common.pojo.ProjectLog;
import personal.css.UniversalSpringbootProject.common.service.LogService;
import personal.css.UniversalSpringbootProject.common.vo.ListResult;
import personal.css.UniversalSpringbootProject.common.vo.ResultVo;
import personal.css.UniversalSpringbootProject.module.loginManage.dto.IdentityDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private LogService logService;

    @Pointcut("execution(public * personal.css.UniversalSpringbootProject..controller..*.*(..))")
    public void all() {
    }

    @Around("all()")
    public Object log(ProceedingJoinPoint point) throws Throwable {
        long start = System.currentTimeMillis();
        log.info("\n进入切面LogAspect...");
        httpServletRequest.setAttribute("start", start);

        Object[] args = point.getArgs();
        Object proceed = point.proceed(args);

        long end = System.currentTimeMillis();
        double took = (end - start) / 1000.0;
        log.info("退出切面LogAspect耗时：{}秒", took);

        Map<String, Object> parameters = getParameters();
        Object body = ((ResponseEntity) proceed).getBody();
        ResultVo vo = (ResultVo) body;
        Object result = vo.getResult();
        Object data = null;
        String errorMsg = null;
        if (vo.isSuccess())
            errorMsg = null;
        else
            errorMsg = vo.getError();

        if (result instanceof ListResult || result instanceof List) {
            data = new JSONObject(result);
        } else {
            data = result;
        }

        ResultDto resultDto = new ResultDto()
                .setData(data != null ? data.toString() : null)
                .setError(errorMsg)
                .setStatusCode(httpServletResponse.getStatus());
        IdentityDto identityDto = getIdentityDto();
        String requestUrl = httpServletRequest.getRequestURL().toString();
        String method = httpServletRequest.getMethod();
        try {
            //异步入库
            CompletableFuture<Void> async = CompletableFuture.supplyAsync(() -> {
                //构造日志对象
                ProjectLog projectLog = setProjectLog(took, resultDto, identityDto, parameters, requestUrl, method);
                //日志数据入库
                logService.insert(projectLog);
                return null;
            });
            async.get();
        } catch (Exception ex) {
            log.error("日志入库异常：{}", ex.getMessage());
        }
        return proceed;
    }


    @AfterThrowing(pointcut = "all()", throwing = "e")
    public Object logThrowing(Exception e) {
        /*long start = System.currentTimeMillis();
        log.info("\n进入切面LogAspect...");*/

        long start = (long) httpServletRequest.getAttribute("start");
        long end = System.currentTimeMillis();
        double took = (end - start) / 1000.0;
        log.info("退出切面LogAspect耗时：{}秒", took);

        Map<String, Object> parameters = getParameters();
        ResultDto resultDto = new ResultDto()
                .setData(null)
                .setError(e.getMessage())
                .setStatusCode(httpServletResponse.getStatus());
        IdentityDto identityDto = getIdentityDto();
        String requestUrl = httpServletRequest.getRequestURL().toString();
        String method = httpServletRequest.getMethod();
        try {
            //异步入库
            CompletableFuture<Void> async = CompletableFuture.supplyAsync(() -> {
                //构造日志对象
                ProjectLog projectLog = setProjectLog(took, resultDto, identityDto, parameters, requestUrl, method);
                //日志数据入库
                logService.insert(projectLog);
                return null;
            });
            async.get();
        } catch (Exception ex) {
            log.error("日志入库异常：{}", ex.getMessage());
        }
        return null;
    }

    private ProjectLog setProjectLog(double took, ResultDto resultDto, IdentityDto identityDto, Map<String, Object> parameters, String requestUrl, String method) {
        ProjectLog projectLog = new ProjectLog();
        projectLog.setUserId(identityDto.getUserId())
                .setUsername(identityDto.getName())
                .setRequestUrl(requestUrl)
                .setRequestData(parameters.toString())
                .setMethod(method)
                .setResponseData(resultDto.getData())
                .setErrorMsg(resultDto.getError())
                .setStatusCode(resultDto.getStatusCode())
                .setCreateTime(new Date(System.currentTimeMillis()))
                .setTookTime(took);
        return projectLog;
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
        if (parametersMap != null) {
            parametersMap.forEach((key, value) -> {
                parameters.put(key, value[0]);
            });
        }
        return parameters;
    }
}
