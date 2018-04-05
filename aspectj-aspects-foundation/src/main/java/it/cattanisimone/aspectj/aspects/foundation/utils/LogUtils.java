package it.cattanisimone.aspectj.aspects.foundation.utils;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public class LogUtils {

    private static final String LOG_FORMAT = "[%s]";
    private static final String LOG_FORMAT_ARG = "%s=%s";
    private static final String LOG_FORMAT_ARG_SEPARATOR = " - ";

    private static String key(String original){
        return original.toLowerCase()
                .replace(".", "_")
                .replace("-", "_")
                .replace("=", ":");
    }

    private static String value(Object original){
        return String.valueOf(original)
                .replace("-", "")
                .replace("=", ":");
    }

    public static String format(Pair<String, Object>... args){
        return format(Arrays.stream(args));
    }

    public static String format(Stream<Pair<String, Object>> args){
        return String.format(LOG_FORMAT, args
                .map(arg -> String.format(LOG_FORMAT_ARG, key(arg.getKey()), value(arg.getValue())))
                .collect(joining(LOG_FORMAT_ARG_SEPARATOR)));
    }

}