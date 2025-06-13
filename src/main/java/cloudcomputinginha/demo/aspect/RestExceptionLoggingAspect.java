package cloudcomputinginha.demo.aspect;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
public class RestExceptionLoggingAspect {

    private static final Set<String> SENSITIVE_HEADERS = Set.of(
        "authorization", "cookie", "x-api-key", "x-auth-token"
    );
    private static final Set<String> SENSITIVE_PARAMS = Set.of(
        "password", "token", "secret"
    );

    @AfterThrowing(pointcut = "within(@org.springframework.web.bind.annotation.RestController *)", throwing = "ex")
    public void logRestException(JoinPoint joinPoint, Throwable ex) {



        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            log.error("""
            ❌ REST API 예외:
            URI: {}
            Method: {}
            Params: {}
            Headers: {}
            Exception: {}
            """,
                request.getRequestURI(),
                request.getMethod(),
                filterSensitiveParams(request.getParameterMap()),
                Collections.list(request.getHeaderNames()).stream()
                    .filter(h -> !SENSITIVE_HEADERS.contains(h.toLowerCase()))
                    .collect(Collectors.toMap(h -> h, request::getHeader)),
                ex.getMessage(), ex
            );
        }
    }
    private Map<String, String[]> filterSensitiveParams(Map<String, String[]> params) {
            return params.entrySet().stream()
                    .filter(entry -> !SENSITIVE_PARAMS.contains(entry.getKey().toLowerCase()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
}