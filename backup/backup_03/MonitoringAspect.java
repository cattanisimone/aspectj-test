package it.cattanisimone.aspecj.aspects.core;

import it.cattanisimone.aspecj.aspects.core.pointcut.Pointcuts;
import it.cattanisimone.aspectj.aspects.foundation.annotation.Monitored;
import it.cattanisimone.aspectj.aspects.foundation.utils.TrackUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.MDC;

import static it.cattanisimone.aspectj.aspects.foundation.utils.LogUtils.format;
import static it.cattanisimone.aspectj.aspects.foundation.utils.TrackUtils.MDC_TENANT_KEY;
import static org.apache.commons.lang3.tuple.Pair.of;
import static org.joda.time.DateTime.now;
import static org.slf4j.LoggerFactory.getLogger;

@Aspect
public abstract class MonitoringAspect extends Pointcuts {

    public static final String MDC_OUTCOME_KEY = "outcome";

    private Logger logger = getLogger(MonitoringAspect.class);

    @Pointcut("@annotation(it.cattanisimone.aspectj.aspects.foundation.annotation.Monitored)")
    private void annotated(){}

    @Pointcut("all() && annotated()")
    private void monitored(){}

    @AfterReturning("monitored()")
    public void success(){
        MDC.put(MDC_OUTCOME_KEY, "success");
    }

    @AfterThrowing("monitored()")
    public void failure(){
        MDC.put(MDC_OUTCOME_KEY, "failure");
    }

    @Around("monitored()")
    public Object monitor(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = now().getMillis();
        try{
            return joinPoint.proceed();
        } finally {
            long end = now().getMillis();

            String handle = String.format("%s.%s", joinPoint.getStaticPart().getSignature().getDeclaringType().getSimpleName(), joinPoint.getStaticPart().getSignature().getName());
            String operation = "";

            if(joinPoint.getStaticPart().getSignature() instanceof MethodSignature){
                Monitored annotation = ((MethodSignature) joinPoint.getStaticPart().getSignature()).getMethod().getAnnotation(Monitored.class);
                if(annotation != null && !annotation.handle().equals("")){
                    handle = annotation.handle();
                    operation = annotation.operation();
                }
            }

            register(joinPoint, handle, operation, MDC.get(MDC_TENANT_KEY), MDC.get(MDC_OUTCOME_KEY), (end - start));

            MDC.remove(MDC_OUTCOME_KEY);
        }

    }

    protected void register(JoinPoint joinPoint, String handle, String operation, String tenant, String outcome, Long elapsed) {
        logger.info(format(
                of("class", joinPoint.getStaticPart().getSignature().getDeclaringType().getSimpleName()),
                of("method", joinPoint.getStaticPart().getSignature().getName()),
                of("handle", handle),
                of("operation", operation),
                of("tenant", tenant),
                of("outcome", outcome),
                of("elapsed", elapsed)));
    }



}
