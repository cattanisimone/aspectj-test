package it.cattanisimone.aspectj.pure.aspects;

import it.cattanisimone.aspectj.aspects.foundation.annotation.TrackId;
import it.cattanisimone.aspecj.aspects.core.pointcut.Pointcuts;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static it.cattanisimone.aspectj.aspects.foundation.utils.TrackUtils.MDC_TRACK_KEY;
import static it.cattanisimone.aspectj.aspects.foundation.utils.TrackUtils.genereateTrackId;

public aspect TrackingAspect extends Pointcuts {

    pointcut controller(): within(@org.springframework.web.bind.annotation.RestController *);

    pointcut calls(): Pointcuts.all() && controller();

    before() : execution(* *.*(..)) && controller() {

        Object[] args = thisJoinPoint.getArgs();

        if(thisJoinPoint.getSignature() instanceof MethodSignature) {
            Method method = ((MethodSignature) thisJoinPoint.getSignature()).getMethod();
            Class[] types = method.getParameterTypes();
            Annotation[][] annotations = method.getParameterAnnotations();

            for(int i = 0; i < args.length; i++)
                if(types[i].isAssignableFrom(String.class))
                    for (Annotation annotation : annotations[i])
                        if (annotation instanceof TrackId && args[i] == null)
                            args[i] = genereateTrackId();
        }

//        proceed(args);

        System.out.println(thisJoinPoint);
        System.out.println(thisJoinPointStaticPart);
        System.out.println(thisEnclosingJoinPointStaticPart);
        System.out.println("test");
    }

    Object around() : calls() {
        MDC.put(MDC_TRACK_KEY, genereateTrackId());
        try {
            return proceed();
        } finally {
            MDC.remove(MDC_TRACK_KEY);
        }
    }

}
