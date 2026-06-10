package org.yjx.pollservice.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yjx.pollservice.service.UserService;

@RestController
@RequestMapping("/api/admin/user")
@Tag(name = "管理员模块")
public class AdminController {



}
