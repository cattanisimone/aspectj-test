package it.cattanisimone.aspecj.aspects.core;

import it.cattanisimone.aspecj.aspects.core.pointcut.Pointcuts;
import it.cattanisimone.aspectj.aspects.foundation.annotation.Tenant;
import it.cattanisimone.aspectj.aspects.foundation.annotation.TrackId;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static it.cattanisimone.aspectj.aspects.foundation.utils.TrackUtils.MDC_TENANT_KEY;
import static it.cattanisimone.aspectj.aspects.foundation.utils.TrackUtils.MDC_TRACK_KEY;
import static it.cattanisimone.aspectj.aspects.foundation.utils.TrackUtils.genereateTrackId;

@Aspect
public abstract class TrackingAspect extends Pointcuts {

    @Pointcut
    public abstract void tracked();

    @Pointcut("all() && tracked()")
    private void calls(){}

    @Around("calls()")
    public Object track(ProceedingJoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs();
        String trackId = genereateTrackId();
        String tenant = null;

        if(joinPoint.getSignature() instanceof MethodSignature) {
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            Class[] types = method.getParameterTypes();
            Annotation[][] annotations = method.getParameterAnnotations();

            for(int i = 0; i < args.length; i++)
                if(types[i].isAssignableFrom(String.class))
                    for (Annotation annotation : annotations[i])
                        if (annotation instanceof TrackId) {
                            if (args[i] == null)
                                args[i] = trackId;
                            else
                                trackId = (String) args[i];
                        } else if(annotation instanceof Tenant) {
                            tenant = (String) args[i];
                        }
        }

        MDC.put(MDC_TRACK_KEY, trackId);
        MDC.put(MDC_TENANT_KEY, tenant);
        try{
            return joinPoint.proceed(args);
        } finally {
            MDC.remove(MDC_TENANT_KEY);
            MDC.remove(MDC_TRACK_KEY);
        }

    }

}
