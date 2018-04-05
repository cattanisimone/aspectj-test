package it.cattanisimone.aspectj.aspects.framework.audit;

import io.prometheus.client.Summary;
import it.cattanisimone.aspecj.aspects.core.AuditingAspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Annotation;

import static it.cattanisimone.aspectj.aspects.foundation.utils.LogUtils.format;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.tuple.Pair.of;

@Aspect
public class ControllerAuditingAspect extends AuditingAspect {

    Summary duration = Summary.build()
            .name("service_call_duration_seconds")
            .labelNames("tenant", "method", "path", "outcome")
            .help("Duration in seconds of metrics extraction by tenant")
            .register();

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    protected void observed() {}

    @Override
    protected void audit(JoinPoint joinPoint, String username, String usergroups, String usertenant, String tenant, String outcome, Long elapsed) {

        RequestMethod method = null;
        String path = "";

        if(joinPoint.getSignature() instanceof MethodSignature) {
            path += head(((RequestMapping) joinPoint.getSignature().getDeclaringType().getAnnotation(RequestMapping.class)).path());
            path += head(((RequestMapping) joinPoint.getSignature().getDeclaringType().getAnnotation(RequestMapping.class)).value());

            Annotation[] annotations = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotations();
            for (Annotation annotation : annotations)
                if (annotation instanceof RequestMapping){
                    path += head(((RequestMapping) annotation).path());
                    path += head(((RequestMapping) annotation).value());
                    method = head(((RequestMapping) annotation).method());
                } else if(annotation instanceof GetMapping){
                    path += head(((GetMapping) annotation).path());
                    path += head(((GetMapping) annotation).value());
                    method = RequestMethod.GET;
                } else if(annotation instanceof PostMapping){
                    path += head(((PostMapping) annotation).path());
                    path += head(((PostMapping) annotation).value());
                    method = RequestMethod.POST;
                } else if(annotation instanceof PutMapping){
                    path += head(((PutMapping) annotation).path());
                    path += head(((PutMapping) annotation).value());
                    method = RequestMethod.PUT;
                } else if(annotation instanceof PatchMapping){
                    path += head(((PatchMapping) annotation).path());
                    path += head(((PatchMapping) annotation).value());
                    method = RequestMethod.PATCH;
                }
        }

        logger.info(format(
                of("username", username),
                of("usergroups", usergroups),
                of("usertenant", usertenant),
                of("method", method),
                of("path", path),
                of("outcome", outcome),
                of("elapsed", elapsed)));

        duration.labels(tenant, valueOf(method), path, outcome).observe(elapsed / 1000d);
    }

    private static <T> T head(T[] values){
        return values.length > 0 ? values[0] : null;
    }

    private static String head(String[] values){
        return values.length > 0 ? values[0] : "";
    }

}
