package cloudcomputinginha.demo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.*;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class MethodLoggingAspect {

    @Pointcut("execution(* cloudcomputinginha.demo..*(..))")
    public void applicationPackagePointcut() {}

    @Around("applicationPackagePointcut()")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        log.info("➡️ 진입: {}.{}() args = {}",
            joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName(),
            joinPoint.getArgs());

        Object result = joinPoint.proceed();

        log.info("✅ 반환: {}.{}() result = {}",
            joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName(),
            result);
        log.info("⏱️ 실행 시간: {} ms",
            System.currentTimeMillis() - start);

        return result;
    }
}