package it.cattanisimone.java.mvn.aspectjfunctional.library.aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;

public aspect AuditAspect {

    private Logger logger = LoggerFactory.getLogger(AuditAspect.class);

    pointcut controller(): within(@org.springframework.web.bind.annotation.RestController *);
    pointcut traced(): execution(* *.*(..)) && !within(TrackAspect) && !within(TraceAspect) && !within(AuditAspect);

    pointcut audited(): traced() && controller();

    Object around() : audited() {
        try{
            return proceed();
        } finally {
            logger.info(format("auditing - " + thisJoinPointStaticPart.getSignature().getName() + ", response=200"));
        }
    }

}
