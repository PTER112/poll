package org.yjx.pollservice.service;

import org.yjx.pollpojo.dto.LoginDTO;
import org.yjx.pollpojo.dto.MeDTO;
import org.yjx.pollpojo.dto.RegisterDTO;
import org.yjx.pollpojo.vo.MeVO;

public interface UserService {
    /**
     *  注册模块
     * @param registerDTO
     */
    void register(RegisterDTO registerDTO);

    /**
     * 登录模块
     * @param dto
     */
    String login(LoginDTO dto);

    /**
     * 退出登录模块
     * @param token
     */
    void logout(String token);

    /**
     * 获取用户信息模块
     * @param token
     * @return
     */
    MeVO me(String token) ;

    /**
     * 更新用户信息模块
     * @param meDto
     */
    void update(MeDTO meDto);
}
