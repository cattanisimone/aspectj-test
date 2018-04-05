package it.cattanisimone.aspectj.pure.aspects;

import it.cattanisimone.aspectj.aspects.foundation.annotation.Monitored;
import it.cattanisimone.aspecj.aspects.core.pointcut.Pointcuts;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.MDC;

import java.util.Optional;

import static it.cattanisimone.aspectj.aspects.foundation.utils.LogUtils.format;
import static java.lang.String.valueOf;
import static java.lang.System.currentTimeMillis;
import static org.apache.commons.lang3.tuple.Pair.of;
import static org.slf4j.LoggerFactory.getLogger;

public aspect MonitoringAspect extends Pointcuts {

    public static final String MDC_OUTCOME_KEY = "outcome";

    private Logger logger = getLogger(MonitoringAspect.class);

    private pointcut annotated(): @annotation(it.cattanisimone.aspectj.pure.aspects.annotation.Monitored);
    public  pointcut monitored(): Pointcuts.all() && annotated();

    after() returning : monitored() {
        MDC.put(MDC_OUTCOME_KEY, "success");
    }

    after() throwing : monitored() {
        MDC.put(MDC_OUTCOME_KEY, "failure");
    }

    Object around() : monitored() {
        long start = currentTimeMillis();
        try{
            return proceed();
        } finally {

            String handle = String.format("%s.%s", thisJoinPointStaticPart.getSignature().getDeclaringType().getSimpleName(), thisJoinPointStaticPart.getSignature().getName());
            if(thisJoinPointStaticPart.getSignature() instanceof MethodSignature){
                handle = Optional.of(((MethodSignature) thisJoinPointStaticPart.getSignature()).getMethod().getAnnotation(Monitored.class).handle())
                        .filter(h -> !h.equals(""))
                        .orElse(handle);
            }

            logger.info(format(
                    of("class", thisJoinPointStaticPart.getSignature().getDeclaringType().getSimpleName()),
                    of("method", thisJoinPointStaticPart.getSignature().getName()),
                    of("handle", handle),
                    of("outcome", MDC.get(MDC_OUTCOME_KEY)),
                    of("elapsed", valueOf((currentTimeMillis() - start)))));

            MDC.remove(MDC_OUTCOME_KEY);
        }
    }

}
