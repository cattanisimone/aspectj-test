package it.cattanisimone.java.mvn.aspectjfunctional.library.aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import static it.cattanisimone.java.mvn.aspectjfunctional.library.utils.TrackUtils.genereateTrackId;

public aspect TrackAspect {

    private Logger logger = LoggerFactory.getLogger(TrackAspect.class);

    pointcut traced(): execution(* *.*(..)) && !within(TrackAspect) && !within(TraceAspect) && !within(AuditAspect);
    pointcut controller(): within(@org.springframework.web.bind.annotation.RestController *);

    pointcut calls(): traced() && controller();

    before() : call() {
        System.out.println("arrivo");
    }

    Object around() : calls() {

        MDC.put("trackId", genereateTrackId());

        try {

            return proceed();

        } finally {
            MDC.remove("trackId");
        }
    }

}
