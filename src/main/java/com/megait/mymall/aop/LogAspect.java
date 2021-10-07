package com.megait.mymall.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

@Aspect
@Slf4j
@Component
public class LogAspect {

    @Around("within(com.megait.mymall.service..*)")
    public Object loggin(ProceedingJoinPoint pjp) throws Throwable {
        String params = getRequestParams();

        long start = System.currentTimeMillis();
        Signature requestSignature = pjp.getSignature();
        log.info("request : {} ({}) = {}", requestSignature.getDeclaringTypeName(), requestSignature.getName(), params);

        Object result = pjp.proceed();

        long end = System.currentTimeMillis();

        Signature responseSignature = pjp.getSignature();
        log.info("client IP : {}", getRequestParams());
        log.info("response : {} ({}) = {} ({}ms)",
                responseSignature.getDeclaringTypeName(), requestSignature.getName(), result, end - start);

        return result;

    }
    // request.getParameter("id")

    // 요청
    private String getRequestParams() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

        if(attributes == null){
            return null;
        }

        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

        Map<String, String[]> parameterMap = request.getParameterMap(); // getParameterMap : 모든 파라미터 받기

        return parameterMap.toString();
    }

    private String mapToString(Map<String, String[]> map){
        StringBuilder sb = new StringBuilder();
        for(String k : map.keySet()){
            sb.append(String.format("%s=%s ", k, Arrays.toString(map.get(k))));
        }

        sb.append("}");
        return sb.toString();
    }
}