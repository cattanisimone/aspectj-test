package it.cattanisimone.aspecj.aspects.core.coordination;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclarePrecedence;

@Aspect
@DeclarePrecedence("*..TrackingAspect+, *..TimeAspect+, *..MonitoringAspect+")
public class CoordinationSortingAspect {}
