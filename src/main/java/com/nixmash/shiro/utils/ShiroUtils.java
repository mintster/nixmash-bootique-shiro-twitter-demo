package com.nixmash.shiro.utils;

import java.util.Arrays;
import java.util.List;

public class ShiroUtils {

    public static boolean isInTestingMode() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        List<StackTraceElement> list = Arrays.asList(stackTrace);
        for (StackTraceElement element : list) {
            if (element.getClassName().startsWith("org.junit.")) {
                return true;
            }
        }
        return false;
    }
}
