package it.cattanisimone.aspectj.aspects.framework.tracking;

import it.cattanisimone.aspecj.aspects.core.TrackingAspect;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ControllerTrackingAspect extends TrackingAspect {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void tracked() {}

}
