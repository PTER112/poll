package org.yjx.pollservice.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.yjx.pollpojo.entity.User;

@Mapper
public interface UserMapper {


    /**
     * 注册用户
     * @param user
     */
    @Insert("insert into user (username, password, role, status) values (#{username}, #{password}, #{role}, #{status})")
    void insert(User user);

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    @Select("select * from user where username = #{username}")
    User select(String username);

    /**
     * 根据用户ID查询用户
     * @param id
     * @return
     */
    @Select("select * from user where id = #{id}")
    User selectById(Integer id);

    /**
     *根据id更新用户信息
     * @param user
     */
    @Update("update user set username = #{username}, password = #{password} where id = #{id}")
    void updateById(User user);
}
