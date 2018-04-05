package it.cattanisimone.aspecj.aspects.core.coordination;

import it.cattanisimone.aspecj.aspects.core.pointcut.Pointcuts;

public aspect CoordinationSortingAspect extends Pointcuts {
    declare precedence: *..TestingAspect, *..TrackingAspect, *..MonitoringAspect, *..DebuggingAspect;
}
