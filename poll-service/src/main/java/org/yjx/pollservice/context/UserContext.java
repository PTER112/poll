package org.yjx.pollservice.context;

import org.yjx.pollpojo.vo.MeVO;

public class UserContext {
    // ThreadLocal：每个线程独立的存储空间
    private static final ThreadLocal<MeVO> threadLocal = new ThreadLocal<>();

    // 存
    public static void setCurrentUser(MeVO user) {
        threadLocal.set(user);
    }

    // 取
    public static MeVO getCurrentUser() {
        return threadLocal.get();
    }

    // 清（请求结束后必须调，防止内存泄漏）
    public static void remove() {
        threadLocal.remove();
    }

}
