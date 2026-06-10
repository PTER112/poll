package org.yjx.pollservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yjx.pollpojo.dto.LoginDTO;
import org.yjx.pollpojo.dto.MeDTO;
import org.yjx.pollpojo.dto.RegisterDTO;
import org.yjx.pollpojo.result.Result;
import org.yjx.pollpojo.vo.MeVO;
import org.yjx.pollservice.service.UserService;

@RestController
@RequestMapping("/api/user")
@Tag(name = "用户模块")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 注册模块
     * @param dto
     * @return
     */
    @PostMapping("/register")
    @Operation(summary = "注册模块")
    public Result<Void> register(@RequestBody RegisterDTO dto) {
        userService.register(dto);
        return Result.success();
    }

    /**
     * 登录模块
     * @param dto
     * @return
     */
    @PostMapping("/login")
    @Operation(summary = "登录模块")
    public Result<String> login(@RequestBody LoginDTO dto) {
        String token = userService.login(dto);
        return Result.success(token);
    }

    /**
     * 退出登录模块
     * @return
     */
    @PostMapping("/logout")
    @Operation(summary = "退出登录模块")
    public Result<Void> logout(@RequestHeader("token") String token) {
        userService.logout(token);
        return Result.success();
    }

    /**
     * 获取用户信息模块
     * @param token
     * @return
     */
    @GetMapping("/me")
    @Operation(summary = "获取用户信息模块")
    public Result<MeVO> me(@RequestHeader("token") String token) {
        MeVO meVo=userService.me(token);
        return Result.success(meVo);
    }


    /**
     * 更新用户信息模块
     * @param meDto
     * @return
     */
    @PutMapping("/update")
    @Operation(summary = "更新用户信息模块")
    public Result<Void> update(@RequestBody MeDTO meDto) {
        userService.update(meDto);
        return Result.success();
    }
}
