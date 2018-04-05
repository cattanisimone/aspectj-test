package it.cattanisimone.aspecj.aspects.core.pointcut;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public abstract class PointcutsTest {

    @Pointcut("!within(it.cattanisimone.aspectj.pure.aspects..*)")
    protected void woven(){}

    @Pointcut("execution(* *.*(..)) && woven()")
    protected void all(){}

}
