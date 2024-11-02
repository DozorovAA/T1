package org.starter.aspect;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.stereotype.Component;
import org.starter.config.LogConfig;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
@AllArgsConstructor
public class LoggingAspect {
    private final LogConfig logConfig;

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object logBeforeAfter(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!logConfig.getEnableLog())
            return joinPoint.proceed();

        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        logAtLevel("Начало выполнения метода: " + methodName + " с аргументами: " + Arrays.toString(args), logConfig.getLogLevel());

        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();

        if (logConfig.getLogDuration())
            logAtLevel("Метод " + methodName + " выполнен за " + (end - start) + " мс", logConfig.getLogLevel());
        logAtLevel("Метод " + methodName + " завершен, результат: " + result, logConfig.getLogLevel());

        return result;
    }
    private void logAtLevel(String message, String logLevel) {
        switch (logLevel.toUpperCase()) {
            case "TRACE" -> log.trace(message);
            case "DEBUG" -> log.debug(message);
            case "INFO" -> log.info(message);
            case "WARN" -> log.warn(message);
            case "ERROR" -> log.error(message);
            default -> log.info(message);
        }
    }
}
