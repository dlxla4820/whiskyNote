package develop.whiskyNote.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

@Component
@Aspect
@Slf4j
public class LogAspect {
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controller() {}
    @Before("controller()")
    public void beforeRequest(JoinPoint joinPoint) {
        log.info("Start request: {}", joinPoint.getSignature().toShortString());
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            String paramName = parameters[i].getName();
            Object arg = args[i];
            log.info("\t{}: {}", paramName, Objects.toString(arg, "null"));
        }
    }
    @AfterReturning(pointcut = "controller()", returning = "returnValue")
    public void afterReturningLogging(JoinPoint joinPoint, Object returnValue) {
        log.info("End request: {} \tReturn value: {}", joinPoint.getSignature().toShortString(), returnValue);
    }
}
