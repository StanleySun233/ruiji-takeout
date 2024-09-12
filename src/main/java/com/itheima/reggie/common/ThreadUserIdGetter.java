package com.itheima.reggie.common;

public class ThreadUserIdGetter {
    private static ThreadLocal<String> threadLocal= new ThreadLocal<>();

    public static void setCurrentId(String id) {
        threadLocal.set(id);
    }

    public static String getCurrentId() {
        return threadLocal.get();
    }

}
