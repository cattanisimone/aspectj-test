package it.cattanisimone.aspectj.pure.service.controller;

import it.cattanisimone.aspectj.aspects.foundation.annotation.audit.UserGroups;
import it.cattanisimone.aspectj.aspects.foundation.annotation.audit.UserName;
import it.cattanisimone.aspectj.aspects.foundation.annotation.audit.UserTenant;
import it.cattanisimone.aspectj.aspects.foundation.annotation.time.Ts;
import it.cattanisimone.aspectj.aspects.foundation.annotation.track.Tenant;
import it.cattanisimone.aspectj.aspects.foundation.annotation.track.TrackId;
import it.cattanisimone.aspectj.pure.service.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Validated
@RequestMapping(produces = "application/json", value = "/aspect")
public class TestController {

    @Autowired
    private TestService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Map api(@RequestHeader(value = "X-Tenant") @Tenant @UserTenant String tenant,
                   @RequestHeader(value = "X-Consumer-Username") @UserName String username,
                   @RequestHeader(value = "X-Consumer-Groups") @UserGroups List<String> groups,
                   @RequestParam(value = "a") @Min(5) Integer a,
                   @RequestParam(value = "track", required = false) @TrackId String b,
                   @RequestParam(value = "ts", required = false) @Ts Long ts){

        Map<String, Object> data = new HashMap<>();
        data.put("a", "" + a.toString());
        data.put("ts", "" + ts.toString());
        data.put("track", "" + b);

        service.method(ts, b);

        return data;
    }

}
