package it.cattanisimone.aspectj.pure.service.service.model;

import it.cattanisimone.aspectj.aspects.foundation.annotation.Monitored;

public class Model {

    @Monitored(handle = "model_class", operation = "sum")
    public Integer sum(Integer a, Integer b){
        try {
            if(a == 42)
                throw new IllegalStateException("");
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return a + b;
    }

}
