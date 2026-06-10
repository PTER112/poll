package org.yjx.pollservice.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Around("execution(* org.yjx.pollservice.controller..*(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString();  // 方法名
        Object[] args = joinPoint.getArgs();                        // 入参
        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();  // 执行目标方法

        long time = System.currentTimeMillis() - start;
        log.info("{} | 耗时: {}ms", method, time);
        return result;
    }
}
