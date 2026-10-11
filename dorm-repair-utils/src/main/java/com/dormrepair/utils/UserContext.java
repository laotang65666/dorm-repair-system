package com.dormrepair.utils;

import com.dormrepair.pojo.User;

/**
 * 当前登录用户的上下文（基于 ThreadLocal）。
 *
 * ThreadLocal 为每个线程维护独立的副本：Spring 每个请求用一个线程处理，
 * 所以可以用 ThreadLocal 存「当前请求对应的登录用户」，请求结束再清掉，各请求互不干扰。
 */
public final class UserContext {

    private UserContext() {}

    private static final ThreadLocal<User> HOLDER = new ThreadLocal<>();

    /** 写入当前登录用户 */
    public static void set(User user) {
        HOLDER.set(user);
    }

    /** 读取当前登录用户（未登录返回 null） */
    public static User get() {
        return HOLDER.get();
    }

    /** 清除当前线程上下文，防止线程池复用导致串号或内存泄漏 */
    public static void clear() {
        HOLDER.remove();
    }
}
