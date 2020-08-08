package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Select("select * from CREDENTIALS")
    @Results({
            @Result(id = true, property = "id", column = "credentialid"),
            @Result(property = "password", column = "password"),
            @Result(property = "username", column = "username"),
            @Result(property = "key", column = "key"),
            @Result(property = "user", column = "userid",
                    one = @One(select = "com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper.findById"))
    })
    public List<Credentials> findAll();

    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{id} and userid = #{user_id}")
    @Results({
            @Result(id = true, property = "id", column = "credentialid"),
            @Result(property = "password", column = "password"),
            @Result(property = "username", column = "username"),
            @Result(property = "key", column = "key"),
            @Result(property = "url", column = "url"),
            @Result(property = "user", column = "userid",
                    one = @One(select = "com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper.findById"))
    })
    public Credentials findByIdAndUserId(@Param("id") long id, @Param("user_id") long userid);

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{user_id}")
    @Results({
            @Result(id = true, property = "id", column = "credentialid"),
            @Result(property = "password", column = "password"),
            @Result(property = "username", column = "username"),
            @Result(property = "key", column = "key"),
            @Result(property = "url", column = "url"),
            @Result(property = "user", column = "userid",
                    one = @One(select = "com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper.findById"))
    })
    public List<Credentials> findAllByUserId(@Param("user_id") long userid);

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{user_id} and url= #{defaultUrl}")
    @Results({
            @Result(id = true, property = "id", column = "credentialid"),
            @Result(property = "password", column = "password"),
            @Result(property = "username", column = "username"),
            @Result(property = "key", column = "key"),
            @Result(property = "url", column = "url"),
            @Result(property = "user", column = "userid",
                    one = @One(select = "com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper.findById"))
    })
    public Credentials findDefaultByUserId(@Param("user_id") long userid, @Param("defaultUrl") String defaultUrl);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{id}")
    public int deleteById(@Param("id") long id);

    @Insert("INSERT INTO CREDENTIALS(url, username, `key`, password, userid) " +
            " VALUES (#{url}, #{username}, #{key}, #{password}, #{user.userId})")
    @Options(useGeneratedKeys = true, keyProperty = "id",keyColumn = "credentialid")
    public void insert(Credentials credentials);

    @Update("Update CREDENTIALS set url=#{url}, " +
            " username=#{username}, `key`=#{key}, password=#{password} where credentialid=#{id}")
    public int update(Credentials credentials);
}
