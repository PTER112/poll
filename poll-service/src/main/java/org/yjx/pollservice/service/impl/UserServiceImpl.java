package org.yjx.pollservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.yjx.pollpojo.dto.LoginDTO;
import org.yjx.pollpojo.dto.MeDTO;
import org.yjx.pollpojo.dto.RegisterDTO;
import org.yjx.pollpojo.entity.User;
import org.yjx.pollpojo.enums.RoleEnum;
import org.yjx.pollpojo.enums.StatusEnum;
import org.yjx.pollpojo.exception.BusinessException;
import org.yjx.pollpojo.vo.MeVO;
import org.yjx.pollservice.context.UserContext;
import org.yjx.pollservice.mapper.UserMapper;
import org.yjx.pollservice.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     *  注册模块
     * @param dto
     */
    @Override
    public void register(RegisterDTO dto) {
        // 1. 校验用户名非空
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            throw new BusinessException("用户名不能为空", 400);
        }
        // 2. 校验密码非空
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            throw new BusinessException("密码不能为空", 400);
        }

        // 3. 校验用户名唯一
        User exist = userMapper.select(dto.getUsername());
        if (exist != null) {
            throw new BusinessException("用户名已存在", 400);
        }

        // 4. BCrypt 加密密码，入库
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
        user.setRole(RoleEnum.USER.getCode());
        user.setStatus(StatusEnum.NORMAL.getCode());
        userMapper.insert(user);
    }

    /**
     * 登录模块
     * @param dto
     */
    @Override
    public String login(LoginDTO dto)  {
        //业务逻辑：首先要校验用户名和密码是否为空，还要校验用户名和密码是否在数据库中存在，这俩统一返回用户名或者密码错误，
        if(dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            throw new BusinessException("用户名不能为空", 400);
        }
        if(dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            throw new BusinessException("密码不能为空", 400);
        }
        // 2. 校验用户名是否存在
        User user= userMapper.select(dto.getUsername());
        if(user==null) {
            throw new BusinessException("用户不存在", 400);
        }
        // 3. 校验密码是否正确
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        if(!encoder.matches(dto.getPassword(),user.getPassword())) {
            throw new BusinessException("用户名或密码错误", 400);
        }
        if(user.getStatus()==StatusEnum.DISABLED.getCode()){
            throw new BusinessException("用户已被禁用", 400);
        }
        // 4. 把用户信息存储进redis中，返回一个token，token用uuid生成
        String token= UUID.randomUUID().toString();
        // 5. 把token和用户信息存储进redis中
        HashMap<String, Object> map=new HashMap<>();
        map.put("username",user.getUsername());
        map.put("id",user.getId());
        map.put("role",user.getRole());
        String json;
        try {
            json=new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        // 生成新 token 之后，存 Redis 之前，加这两行：

        // ① 查用户有没有旧 token
        String oldToken = stringRedisTemplate.opsForValue().get("user:token:" + user.getId());
        // ② 有就删掉
        if (oldToken != null) {
            stringRedisTemplate.delete("token:" + oldToken);
        }

        stringRedisTemplate.opsForValue().set("token:"+token,json, 24, TimeUnit.HOURS);
        stringRedisTemplate.opsForValue().set("user:token:"+user.getId(),token,24, TimeUnit.HOURS);

        return token;
    }

    /**
     * 退出登录模块
     * @param token
     */
    @Override
    public void logout(String token) {
        // 1. 用 token 去 Redis 查用户 JSON
        String userJson = stringRedisTemplate.opsForValue().get("token:" + token);
        if (userJson == null) {
            throw new BusinessException("未登录", 401);
        }

        // 2. 反序列化，拿到 userId
        Map<String, Object> userMap;
        try {
            userMap = new ObjectMapper().readValue(userJson, Map.class);
        } catch (Exception e) {
            throw new BusinessException("系统错误", 500);
        }
        int userId = (int) userMap.get("id");

        // 3. 删两条 key
        stringRedisTemplate.delete("token:" + token);
        stringRedisTemplate.delete("user:token:" + userId);
        
    }

    /**
     * 获取用户信息模块
     * @param token
     * @return
     */
    @Override
    public MeVO me(String token){
        MeVO meVo=new MeVO();
        String userJson = stringRedisTemplate.opsForValue().get("token:" + token);
        if (userJson == null) {
            throw new BusinessException("用户未登录", 401);
        }
        // 2. 反序列化，拿到 userId
        Map<String, Object> userMap;
        try {
            userMap = new ObjectMapper().readValue(userJson, Map.class);
        } catch (Exception e) {
            throw new BusinessException("系统错误", 500);
        }
        int userId = (int) userMap.get("id");
        meVo.setId(userId);
        meVo.setUsername(userMap.get("username").toString());
        meVo.setRole(userMap.get("role").toString());
        return meVo;
    }

    /**
     * 更新用户信息模块
     * @param meDto
     */
    @Override
    public void update(MeDTO meDto) {
        MeVO currentUser = UserContext.getCurrentUser();      // ① 拿当前用户
        User user = userMapper.selectById(currentUser.getId()); // ② 查完整用户信息

        // ③ 改用户名（如果传了）
        if (meDto.getUsername() != null && !meDto.getUsername().trim().isEmpty()) {
            // 校验唯一（排除自己）
            User exist = userMapper.select(meDto.getUsername());
            if (exist != null && exist.getId() != currentUser.getId()) {
                throw new BusinessException("用户名已存在", 400);
            }
            user.setUsername(meDto.getUsername());
        }

        // ④ 改密码（如果传了）
        BCryptPasswordEncoder encoder = null;
        if (meDto.getOldpassword() != null && meDto.getNewpassword() != null) {
            encoder = new BCryptPasswordEncoder();
            if (!encoder.matches(meDto.getOldpassword(), user.getPassword())) {
                throw new BusinessException("旧密码错误", 400);
            }
            user.setPassword(encoder.encode(meDto.getNewpassword()));
        }


        // ⑤ 更新数据库
        userMapper.updateById(user);

        // ⑥ 更新 Redis 中的用户 JSON
        // 通过 userId 拿到 token
        String token = stringRedisTemplate.opsForValue().get("user:token:" + currentUser.getId());

        // 更新 Redis 中的用户 JSON
        if (token != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", currentUser.getId());
            map.put("username", user.getUsername());  // 可能已改
            map.put("role", user.getRole());
            String json ;
            try {
                json = new ObjectMapper().writeValueAsString(map);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            stringRedisTemplate.opsForValue().set("token:" + token, json, 24, TimeUnit.HOURS);
        }


    }

}
