package com.dormrepair.mapper;

import com.dormrepair.pojo.User;
import com.dormrepair.pojo.UserQuery;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户数据访问层。
 * 简单 CRUD 用注解；分页列表/动态修改用 XML（见 UserMapper.xml）。
 */
@Mapper
public interface UserMapper {

    /** 登录：按用户名查用户（含密码，仅登录校验用） */
    @Select("select * from sys_user where username = #{username}")
    User selectByUsername(String username);

    /** 按 id 查用户（不含密码） */
    @Select("select id, username, real_name, role, gender, phone, dormitory_id, status, create_time from sys_user where id = #{id}")
    User selectById(Long id);

    /** 按角色查用户列表（派单下拉用，不含密码） */
    @Select("select id, username, real_name, role, gender, phone, dormitory_id, status, create_time from sys_user where role = #{role}")
    List<User> listByRole(String role);

    /** 分页列表（XML 动态条件 + LIMIT） */
    List<User> list(UserQuery query);

    /** 总数（XML） */
    Long count(UserQuery query);

    /** 新增（返回自增主键） */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into sys_user (username, password, real_name, role, gender, phone, dormitory_id, status, create_time) " +
            "values (#{username}, #{password}, #{realName}, #{role}, #{gender}, #{phone}, #{dormitoryId}, #{status}, #{createTime})")
    void insert(User user);

    /** 修改（XML 动态更新） */
    void update(User user);

    /** 删除 */
    @Delete("delete from sys_user where id = #{id}")
    void deleteById(Long id);
}
