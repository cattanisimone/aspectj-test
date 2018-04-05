package it.cattanisimone.aspectj.pure.service.service;

import io.prometheus.client.Counter;
import it.cattanisimone.aspectj.aspects.foundation.annotation.Monitored;
import it.cattanisimone.aspectj.pure.service.service.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static it.cattanisimone.aspectj.aspects.foundation.utils.LogUtils.format;
import static org.apache.commons.lang3.tuple.Pair.of;

@Service
public class TestService {

    private final Counter test;

    Logger logger = LoggerFactory.getLogger(TestService.class);

    private final Model model;

    public TestService() {
        model = new Model();
        test = Counter.build()
                .name("test")
                .help("help string")
                .register();
    }

    @Monitored
    public Integer method(Long ts, String trackId){

        test.inc();

        logger.warn(format(
                of("operation", "update"),
                of("ts", ts.toString()),
                of("ignored", "title"),
                of("trackid", trackId)));

        return model.sum(new Long(ts / 1000).intValue(), 10);
    }

}
