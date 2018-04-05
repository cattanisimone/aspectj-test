package it.cattanisimone.aspecj.aspects.core;

import it.cattanisimone.aspectj.aspects.foundation.annotation.audit.UserGroups;
import it.cattanisimone.aspectj.aspects.foundation.annotation.audit.UserName;
import it.cattanisimone.aspectj.aspects.foundation.annotation.audit.UserTenant;
import it.cattanisimone.aspectj.aspects.foundation.annotation.time.Lock;
import it.cattanisimone.aspectj.aspects.foundation.annotation.time.Ts;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import static it.cattanisimone.aspectj.aspects.foundation.utils.LogUtils.format;
import static java.lang.String.join;
import static org.apache.commons.lang3.tuple.Pair.of;
import static org.slf4j.LoggerFactory.getLogger;

@Aspect
public abstract class AuditingAspect extends MonitoringAspect {

    protected Logger logger = getLogger(AuditingAspect.class);

    @Override
    protected final void register(JoinPoint joinPoint, String handle, String operation, String tenant, String outcome, Long elapsed) {

        Object[] args = joinPoint.getArgs();
        String username = "";
        String usergroups = "";
        String usertenant = "";

        if(joinPoint.getSignature() instanceof MethodSignature) {
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            Class[] types = method.getParameterTypes();
            Annotation[][] annotations = method.getParameterAnnotations();

            for(int i = 0; i < args.length; i++)
                if(types[i].isAssignableFrom(String.class)) {
                    for (Annotation annotation : annotations[i])
                        if (annotation instanceof UserName)
                            username = (String) args[i];
                        else if (annotation instanceof UserTenant)
                            usertenant = (String) args[i];
                } else if(types[i].isAssignableFrom(List.class)) {
                    for (Annotation annotation : annotations[i])
                        if (annotation instanceof UserGroups)
                            usergroups = join(",", (List) args[i]);
                }
        }

        audit(joinPoint, username, usergroups, usertenant, tenant, outcome, elapsed);
    }

    protected void audit(JoinPoint joinPoint, String username, String usergroups, String usertenant, String tenant, String outcome, Long elapsed) {
        logger.info(format(
                of("username", username),
                of("usergroups", usergroups),
                of("usertenant", usertenant),
                of("outcome", outcome),
                of("elapsed", elapsed)));
    }

}
