package it.cattanisimone.aspectj.aspects.framework.time;

import it.cattanisimone.aspecj.aspects.core.TimeAspect;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ControllerTimeAspect extends TimeAspect {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void enriched() {}

}
