package org.g2.dynamic.jdbc.context;

import java.util.LinkedList;

/**
 * @author wuwenxi 2023-02-10
 */
public class DataRoutHolder {

    private static ThreadLocal<LinkedList<String>> DS_ROUT = new InheritableThreadLocal<>();

    private DataRoutHolder() {
    }

    public static void set(String dsName) {
        LinkedList<String> routs = DS_ROUT.get();
        if (routs == null) {
            routs = new LinkedList<>();
            DS_ROUT.set(routs);
        }
        routs.add(dsName);
    }

    public static String get() {
        LinkedList<String> routs = DS_ROUT.get();
        return routs == null || routs.size() == 0 ? null : routs.getLast();
    }

    public static void clear() {
        LinkedList<String> routs = DS_ROUT.get();
        if (routs != null) {
            routs.removeLast();
        }
        if (routs != null && routs.size() == 0) {
            DS_ROUT.remove();
        }
    }
}
