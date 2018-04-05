package it.cattanisimone.aspectj.aspects.foundation.utils;

import org.slf4j.MDC;

import java.util.Optional;

import static java.util.UUID.randomUUID;

public class TrackUtils {

    public static final String MDC_TRACK_KEY = "track_id";
    public static final String MDC_TENANT_KEY = "tenant";

    public static String genereateTrackId(){
        return randomUUID().toString();
    }

    public static String extractTrackId(){
        return Optional
                .ofNullable(MDC.get(MDC_TRACK_KEY))
                .orElseGet(TrackUtils::genereateTrackId);
    }

}
