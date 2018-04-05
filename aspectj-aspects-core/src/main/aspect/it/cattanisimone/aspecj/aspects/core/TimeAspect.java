package it.cattanisimone.aspecj.aspects.core;

import it.cattanisimone.aspecj.aspects.core.pointcut.Pointcuts;
import it.cattanisimone.aspectj.aspects.foundation.annotation.time.Lock;
import it.cattanisimone.aspectj.aspects.foundation.annotation.time.Ts;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.joda.time.DateTime;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Aspect
public abstract class TimeAspect extends Pointcuts {

    @Pointcut
    public abstract void enriched();

    @Pointcut("all() && enriched()")
    private void calls(){}

    @Around("calls()")
    public Object timestamp(ProceedingJoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs();
        Long now = DateTime.now().getMillis();

        if(joinPoint.getSignature() instanceof MethodSignature) {
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            Class[] types = method.getParameterTypes();
            Annotation[][] annotations = method.getParameterAnnotations();

            int lock = -1;
            int ts = -1;

            for(int i = 0; i < args.length; i++)
                if(types[i].isAssignableFrom(Long.class))
                    for (Annotation annotation : annotations[i])
                        if (annotation instanceof Ts && args[i] == null)
                            ts = i;
                        else if (annotation instanceof Lock && args[i] == null)
                            lock = i;

            if(ts > 0 && args[ts] == null) args[ts] = now;
            if(lock > 0 && args[lock] == null) args[lock] = args[ts];
        }

        return joinPoint.proceed(args);
    }

}
