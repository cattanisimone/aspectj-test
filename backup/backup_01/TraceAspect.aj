package it.cattanisimone.java.mvn.aspectjfunctional.library.aspect;

import org.apache.log4j.NDC;
import org.slf4j.Logger;
import org.slf4j.MDC;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static org.slf4j.LoggerFactory.getLogger;

public aspect TraceAspect {

    private static final String MDC_LOCATION_KEY = "location";

    private Logger logger = getLogger(TraceAspect.class);

    pointcut traced(): execution(* *.*(..)); // && !within(it.cattanisimone.java.mvn.aspectjfunctional.library.*);

    before() : traced() {
        logger.info("enter");
    }

    Object around() : traced() {

        NDC.push(MDC.get(MDC_LOCATION_KEY));
        MDC.put(MDC_LOCATION_KEY, format("%s.%s", thisJoinPointStaticPart.getSignature().getDeclaringType().getSimpleName(), thisJoinPointStaticPart.getSignature().getName()));
        long start = currentTimeMillis();

        try{
            return proceed();
        } finally {

            logger.info(format("tracing - " + thisJoinPointStaticPart.getSignature().getName() + ", elapsed %sms", (currentTimeMillis() - start)));
            MDC.put(MDC_LOCATION_KEY, NDC.pop());

        }
    }

}
