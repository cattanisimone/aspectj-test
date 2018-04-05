package it.cattanisimone.aspecj.aspects.core.pointcut;

public abstract aspect Pointcuts {

    public pointcut woven(): !within(it.cattanisimone.aspectj.pure.aspects..*);

    public pointcut all(): execution(* *.*(..)) && woven();

}
