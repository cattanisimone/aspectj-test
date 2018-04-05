package it.cattanisimone.aspectj.pure.aspects;

import it.cattanisimone.aspecj.aspects.core.pointcut.Pointcuts;
import it.cattanisimone.aspectj.aspects.foundation.utils.LogUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;

import static java.lang.String.format;
import static org.apache.commons.lang3.tuple.Pair.of;
import static org.slf4j.LoggerFactory.getLogger;

public aspect DebuggingAspect extends Pointcuts {

//    private Logger logger = getLogger(DebuggingAspect.class);
//
//    Object around() : Pointcuts.all() {
//
//        long start = currentTimeMillis();
//
//        try{
//            return proceed();
//        } finally {
//
//            Stream<Pair<String, Object>> trace = Stream.of(
//                    of("type", "debug"),
//                    of("class", thisJoinPointStaticPart.getSignature().getDeclaringType().getSimpleName()),
//                    of("method", thisJoinPointStaticPart.getSignature().getName())
//            );
//
//            if(thisJoinPointStaticPart.getSignature() instanceof MethodSignature){
//                MethodSignature signature = (MethodSignature) thisJoinPointStaticPart.getSignature();
//                Stream<Pair<String, Object>> args = IntStream.range(0, signature.getParameterNames().length)
//                        .mapToObj(i -> of(format("param_%s", signature.getParameterNames()[i]), thisJoinPoint.getArgs()[i]));
//                trace = concat(trace, args);
//            }
//
//            Stream<Pair<String, Object>> stats = Stream.of(of("elapsed", (currentTimeMillis() - start)));
//            trace = concat(trace, stats);
//
//            logger.info(LogUtils.format(trace));
//
//        }
//    }


}
