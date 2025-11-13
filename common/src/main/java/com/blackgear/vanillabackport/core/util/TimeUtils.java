package com.blackgear.vanillabackport.core.util;

import net.minecraft.util.valueproviders.UniformInt;

import java.util.concurrent.TimeUnit;

public class TimeUtils {
    public static final long NANOSECONDS_PER_SECOND = TimeUnit.SECONDS.toNanos(1L);
    public static final long NANOSECONDS_PER_MILLISECOND = TimeUnit.MILLISECONDS.toNanos(1L);
    public static final long MILLISECONDS_PER_SECOND = TimeUnit.SECONDS.toMillis(1L);
    public static final long SECONDS_PER_HOUR = TimeUnit.HOURS.toSeconds(1L);
    public static final int SECONDS_PER_MINUTE = (int) TimeUnit.MINUTES.toSeconds(1L);

    public static UniformInt rangeOfSeconds(int from, int to) {
        return UniformInt.of(from * 20, to * 20);
    }
}
