package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from USERS")
    public List<User> findAll();

    @Select("select * from USERS WHERE username = #{username}")
    public User findByUsername(@Param("username") String username);

    @Select("SELECT * FROM USERS WHERE userid = #{userId}")
    public User findById(@Param("userId") long id);

    @Delete("DELETE FROM USERS WHERE userid = #{userId}")
    public int deleteById(@Param("userId") long id);

    @Insert("INSERT INTO USERS(username, salt, firstname, lastname, password) " +
            " VALUES (#{username}, #{salt}, #{firstName}, #{lastName}, #{password})")
    @Options(useGeneratedKeys = true, keyProperty = "userId",keyColumn = "userid")
    public void insert(User user);

    @Update("Update USERS set firstname=#{firstName}, " +
            " lastname=#{lastName}, password=#{password}, salt=#{salt}, username=#{username} where userid=#{userId}")
    public int update(User user);

}
