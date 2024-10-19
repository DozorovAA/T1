package org.t1.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Aspect
@Component
public class LoggingAspect {
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private static String getCurrentTime(){
        return dtf.format(LocalDateTime.now());
    }
    @Before("@annotation(org.t1.annotations.LogBefore)")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println(getCurrentTime() + ": Получил запрос в метод " + joinPoint.getSignature().toShortString());
    }

    @AfterThrowing(pointcut = "@annotation(org.t1.annotations.LogAfterThrowing)", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        System.out.println(getCurrentTime() + ": При выполнении запроса запроса "
                + joinPoint.getSignature().toShortString() + " было выброшено исключение: " + ex.getMessage());
    }

    @AfterReturning(pointcut = "@annotation(org.t1.annotations.LogAfterReturning)", returning = "result")
    public void logAfterReturn(JoinPoint joinPoint, Object result) {
        if(result instanceof List<?> res) {
            System.out.println(getCurrentTime() + ": После выполнения метод "
                    + joinPoint.getSignature().toShortString() + " вернул " + res);
        }
    }

    @Around("@annotation(org.t1.annotations.LogAround)")
    public Object logBeforeAfter(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println(getCurrentTime() + ": Получил запрос в метод " + joinPoint.getSignature().toShortString());
        Object result = joinPoint.proceed();
        System.out.println(getCurrentTime() + ": Получил ответ из метода " + joinPoint.getSignature().toShortString() +
                ", результат " + result);
        return result;
    }
}

