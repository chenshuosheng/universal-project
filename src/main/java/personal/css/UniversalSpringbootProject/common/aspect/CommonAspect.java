package personal.css.UniversalSpringbootProject.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import personal.css.UniversalSpringbootProject.common.vo.ResultVo;

import javax.servlet.http.HttpServletRequest;

import static personal.css.UniversalSpringbootProject.common.consts.MyConst.USER_ID;

/**
 * @Description: 切面类
 * @Author: CSS
 * @Date: 2024/2/29 16:23
 */
@Aspect
@Component
@Slf4j
@Order(3)
public class CommonAspect {

    //使用@Slf4j
    //private static final Logger log = LoggerFactory.getLogger(MyAspect.class);

    @Autowired
    private HttpServletRequest httpServletRequest;

    //定义切点包含控制层接口且不包含公共接口（含Public结尾的方法）
    @Pointcut("execution(public * personal.css.UniversalSpringbootProject..controller..*.*(..))" +
            "&& !execution(public * personal.css.UniversalSpringbootProject..controller..*.*Public(..))")
    private void notPublicController(){}


    //引用切点
    @Around(value = "notPublicController()")
    private Object Handle(ProceedingJoinPoint point) throws Throwable {

        long start = System.currentTimeMillis();
        log.info("\n进入切面CommonAspect...");

        //userId的属性值来源于HandleHeaderFilter
        Long userId = (Long)httpServletRequest.getAttribute(USER_ID);
        if(userId == null){
            return new ResponseEntity<>(new ResultVo<>(false, "非法访问！",null), HttpStatus.UNAUTHORIZED);
        }

        Object[] args = point.getArgs();
        args[0] = userId;
        Object proceed = point.proceed(args);

        long end = System.currentTimeMillis();
        log.info("退出切面Common耗时：{}秒", (end-start)/1000.0);
        return proceed;
    }


    //ProceedingJoinPoint类是AspectJ框架中的一个接口，可以在环绕通知（@Around）中使用它来获取关于当前连接点的信息和控制连接点执行流程。通过ProceedingJoinPoint，你可以得到以下信息：
    //
    //获取目标方法的参数：通过ProceedingJoinPoint的getArgs()方法可以获取目标方法的参数数组。
    //
    //控制目标方法的执行：在环绕通知中，可以使用ProceedingJoinPoint的proceed()方法来显式地调用目标方法的执行。
    //
    //获取目标方法的签名信息：可以通过ProceedingJoinPoint的getSignature()方法获取目标方法的签名信息，如方法名、声明类型等。
    //
    //获取目标对象：可以通过ProceedingJoinPoint的getTarget()方法获取目标对象实例。
    //
    //获取代理对象：可以通过ProceedingJoinPoint的getThis()方法获取代理对象实例。
}
