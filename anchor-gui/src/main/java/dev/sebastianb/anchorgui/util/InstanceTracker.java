package dev.sebastianb.anchorgui.util;

public class InstanceTracker {
    private static final ThreadLocal<Object> lastCaller = new ThreadLocal<>();

    public static void setCaller(Object caller) {
        lastCaller.set(caller);
    }

    public static Object getCaller() {
        return lastCaller.get();
    }
}
