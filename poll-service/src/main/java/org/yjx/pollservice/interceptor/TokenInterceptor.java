package org.yjx.pollservice.interceptor;

import jakarta.servlet.http.HttpServletRequest;  // Spring 封装的请求对象，包含 Header、Body 等
import jakarta.servlet.http.HttpServletResponse; // Spring 封装的响应对象，用来往前端写数据
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;    // 声明本类由 Spring 管理（这样才能注入其他 Bean）
import org.springframework.web.servlet.HandlerInterceptor; // 拦截器接口，必须实现它
import org.yjx.pollpojo.exception.BusinessException;
import org.yjx.pollpojo.vo.MeVO;                  // 用户信息 VO（id、username、role）
import org.yjx.pollservice.context.UserContext;    // ThreadLocal 工具箱，暂存当前用户
import org.yjx.pollservice.service.UserService;    // 查 Redis 拿用户信息

/**
 * Token 拦截器
 * 请求进入 Controller 之前执行，校验用户是否登录
 */
@Component // ← 让 Spring 管理这个类，这样 @Autowired 才能生效
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired // ← 注入 UserService，内部调 me(token) 去 Redis 查用户信息
    private UserService userService;

    /**
     * 前置拦截：请求到达 Controller 之前执行
     *
     * @param request  请求对象，可以从里面拿 Header、参数等
     * @param response 响应对象，拦截时手动往里面写 JSON 返回给前端
     * @param handler  被拦截的目标（一般是 Controller 方法）
     * @return true = 放行，请求继续走到 Controller
     *         false = 拦截，请求到此终止，Controller 收不到
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        // ① 从请求头里取出 token（前端登录后每次请求都在 Header 里带这个值）
        String token = request.getHeader("token");

        // ② 没有 token → 直接返回 401，不放行
        //    null = Header 里根本没这一行，empty = 写了但值为空字符串
        if (token == null || token.trim().isEmpty()) {
            response.setStatus(401);                               // HTTP 状态码 401（未授权）
            response.setContentType("application/json;charset=UTF-8"); // 告诉浏览器返回的是 JSON
            response.getWriter().write("{\"code\":401,\"msg\":\"未登录\",\"data\":null}"); // 手动写 JSON
            return false; // false = 拦截，Controller 不会执行
        }

        // ③ 有 token → 去 Redis 查用户信息
        //    userService.me(token) 的逻辑：用 token 拼成 key → Redis 查 JSON → 反序列化成 MeVO
        try {
            MeVO user = userService.me(token);          // 调 Service 查 Redis
            UserContext.setCurrentUser(user);           // 存进 ThreadLocal，后续 Controller 可以用
            return true;                                // true = 放行，请求继续走到 Controller
        } catch (BusinessException e) {
            // ④ token 非法或已过期（Redis 里没这条数据，Service 会抛 BusinessException）
            //    同样返回 401
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"未登录\",\"data\":null}");
            return false;
        }
    }

    /**
     * 后置清理：请求全部处理完之后执行（无论成功还是失败都会走这里）
     * 必须清 ThreadLocal，防止线程池复用线程时读到上一个请求的残留数据（串号）
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        UserContext.remove(); // 清空当前线程的 ThreadLocal
    }
}
