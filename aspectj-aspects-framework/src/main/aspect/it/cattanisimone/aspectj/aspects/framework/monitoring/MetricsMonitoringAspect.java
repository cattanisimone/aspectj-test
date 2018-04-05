package it.cattanisimone.aspectj.aspects.framework.monitoring;

import io.prometheus.client.Summary;
import it.cattanisimone.aspecj.aspects.core.MonitoringAspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class MetricsMonitoringAspect extends MonitoringAspect {

    Summary duration = Summary.build()
            .name("monitoring_duration_seconds")
            .labelNames("tenant", "handle", "operation", "outcome")
            .help("Duration in seconds of metrics extraction by tenant")
            .register();

    @Override
    protected void register(JoinPoint joinPoint, String handle, String operation, String tenant, String outcome, Long elapsed) {
        super.register(joinPoint, handle, operation, tenant, outcome, elapsed);

        duration.labels(tenant, handle, operation, outcome).observe(elapsed / 1000d);

    }
}
