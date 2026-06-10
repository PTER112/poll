package org.yjx.pollservice.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.yjx.pollpojo.exception.BusinessException;
import org.yjx.pollpojo.vo.MeVO;
import org.yjx.pollservice.context.UserContext;

@Aspect        // 告诉 Spring：这是切面类
@Component     // 让 Spring 管理
public class AdminAspect {

    @Before("@annotation(org.yjx.pollpojo.annotation.RequireAdmin)")   // 拦截带 @RequireAdmin 的方法
    public void checkAdmin() {
        MeVO user = UserContext.getCurrentUser();
        if (!"admin".equals(user.getRole())) {
            throw new BusinessException("无权限", 403);
        }
    }
}


