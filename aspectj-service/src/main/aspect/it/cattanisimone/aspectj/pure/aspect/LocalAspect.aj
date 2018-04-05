package it.cattanisimone.aspectj.pure.aspect;

import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public aspect LocalAspect {

    private Logger logger = getLogger(LocalAspect.class);

    public pointcut traced(): execution(* *.*(..));

    before() : traced() {
//        logger.info("method called");
    }

}
