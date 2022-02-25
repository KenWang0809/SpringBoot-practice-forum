package com.wcw.forum.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

//必須兩個註解都有，才能被Spring掃描
@Aspect
@Component
public class LogAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

//    第一個*指定所有封裝的型別 (public/protected....)
//    第二個*指定所有套件(controller)下面的類別
//    第三個*(..)指定類別內部所有的方法，不管回傳任何參數
    @Pointcut("execution(* com.wcw.forum.web.*.*(..))")
    public void log(){}

//    @Before指定某個切面之前執行的方法
    @Before("log()")
    public void doBefore(JoinPoint joinPoint){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();
        String classMethod = joinPoint.getSignature().getDeclaringType() + "." + joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        RequestLog requestLog = new RequestLog(url, ip, classMethod, args);
        logger.info("requestLog : {} ", requestLog);
    }

    //    @After指定某個切面之後執行的方法
    @After("log()")
    public void doAfter(){
//        logger.info("---------After-----------");
    }

    //    @AfterReturning指定某個切面參數回傳之後執行的方法
    @AfterReturning(returning = "result", pointcut = "log()")
    public void afterReturning(Object result){
        logger.info("----Return {}----", result);
    }


/*
    紀錄日誌包含
    -請求url
    -請求ip
    -引用的類別方法
    -傳遞的參數
    -返回的內容

    將上述的內容包裝成一個內部類，放在切面的doBefore內
 */

    private class RequestLog{
        private String url;
        private String ip;
        private String classMethod;
        private Object[] args;

        public RequestLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }

        @Override
        public String toString() {
            return "RequestLog{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }
}
