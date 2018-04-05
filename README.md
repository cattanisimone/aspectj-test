## Introduction

* **Tracking** - an aspect managing the tracking values (tenant, trackId, eventually sessionId)
* **Time** - enrich and validate timestamp (and lock) parameters
* **Monitoring** - log (INFO level) outcome and elapsed time of a subset of selected methods
* **Auditing** - log (INFO level) on every rest call and agent process callback of the user performing the request

## Coordination

Tracking > Time > Auditing > Monitoring

## Format

Every logs line contains

* time reference
* log level
* thread id
* logger class 
* trackId, if found in context
* tenant, if found in context
* message

```
// log structure
YYYY-MM-DD hh:ii:ss,mmm LEVEL [thread] LoggerClass - [track_id=%s, tenant=%s] - message

// log example
2018-04-03 08:46:47,247 INFO  [http-nio-8080-exec-1] i.c.a.a.c.MonitoringAspect - [track_id=820ddd4e-d941-41f6-806e-cefbe5be635b - tenant=yy] - [class=Model - method=sum - handle=model_class - operation=sum - tenant=yy - outcome=success - elapsed=104]
```

## Details

### Track

The idea is to automatically manage track id injection and propagation

- add track id if missing on each service call ~~and agent process message~~
- extract track id from the context and inject it in client external call and backbone notification
- the previous behaviour cover probably the 95% of the cases, the utility allow track extraction from the context from applicative services if required

Tenant (operative tenant, not user tenant) is stored in MDC context in order to log it in every successive log

> **Todo**
>
> * approve parameters handling 
>   * annotation based (@Tenant, @TrackId)
>   * convention based (tenant, trackId)
> * check MDC behaviour thread-safeness 

### Time

- ***ts*** - enrichment `ts ?: now()` - validation `ts >= 0`
- ***lock*** - enrichment `lock ?: ts ` - validation `lock >= 0`

Generic parameter validations (username != null) should be done using validation annotation

### Monitoring

Produce info logging, reporting performance statistic about the method execution. The scope of monitoring is to log elapsed time and outcome, not to completly trace the method call

```bash
// message structure
[ class=%s - method=%s - handle=%s - operation=%s - outcome=%s - elapsed=%s ]

// message example
[ class=Model - method=sum - handle=model - operation=sum - outcome=success - elapsed=104]
```

> **Todo**
>
> * approve annotation based notation
> * evaluate if related with audit
> * evaluate if different on agent process and controller apis

#### Monitoring metrics

Framework package can automatically produce prometheus metrics `monitoring_duration_seconds` using as labels *handle*, *operation* and *outcome*

> **Todo**
>
> * APM framework, probably we will wait elastic solution
> * until elastic solution, we can produce simple metrics
> * evaluate if use logs, metrics or both

### Auditing

It is a monitoring subaspect. According to a defined pointcut, it enrich monitoring data with the user identification data and provide standard logging feature

> **Todo**
>
> - approve parameters handling 
>   - annotation based (@UserName, @UserGroups, @UserTenant)
>   - convention based (executionUsername, executionGroups, executionTenant)

#### Auditing controllers

Framework pacake provide controllers auditing aspect, providing api/method information in addition to the standard auditing.

```bash
// message structure
[ username=%s - usergroups=%s - usertenant=%s - method=%s - path=%s - outcome=%s - elapsed=%d ]

// message example
[ username=admin - usergroups=admin_write - usertenant=yy - method=GET - path=/aspect - outcome=success - elapsed=180 ]
```



------------

## Open point

### Auditing

fare auditing del ts quando definito??
