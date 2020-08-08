package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entity.DbFile;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FileMapper {
    @Select("select * from FILES WHERE userid = #{userid}")
    @Results({
            @Result(id = true, property = "id", column = "fileid"),
            @Result(property = "contentType", column = "contenttype"),
            @Result(property = "size", column = "filesize"),
            @Result(property = "data", column = "filedata"),
            @Result(property = "name", column = "filename"),
            @Result(property = "user", column = "userid",
                    one = @One(select = "com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper.findById"))
    })
    public List<DbFile> findAllUserFiles(@Param("userid") int userid);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileid} and userid = #{userid}")
    @Results({
            @Result(id = true, property = "id", column = "fileid"),
            @Result(property = "contentType", column = "contenttype"),
            @Result(property = "size", column = "filesize"),
            @Result(property = "data", column = "filedata"),
            @Result(property = "name", column = "filename"),
            @Result(property = "user", column = "userid",
                    one = @One(select = "com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper.findById"))
    })
    public Optional<DbFile> findByIdAndUserId(@Param("fileid") long id, @Param("userid") int userid);

    @Select("SELECT * FROM FILES WHERE filename = #{name} and userid = #{userid}")
    @Results({
            @Result(id = true, property = "id", column = "fileid"),
            @Result(property = "contentType", column = "contenttype"),
            @Result(property = "size", column = "filesize"),
            @Result(property = "data", column = "filedata"),
            @Result(property = "name", column = "filename"),
            @Result(property = "user", column = "userid",
                    one = @One(select = "com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper.findById"))
    })
    public Optional<DbFile> findByNameAndUserId(@Param("name") String name, @Param("userid") int userid);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileid}")
    public void deleteById(@Param("fileid") long id);

    @Insert("INSERT INTO FILES(filename, contenttype, filesize, userid, filedata) " +
            " VALUES (#{name}, #{contentType}, #{size}, #{user.userId}, #{data})")
    @Options(useGeneratedKeys = true, keyProperty = "id",keyColumn = "fileid")
    public void insert(DbFile file);

    @Update("Update FILES set filename=#{name}, " +
            " contenttype=#{contentType}, filesize=#{size}, userid=#{user.userId}, filedata=#{data} where fileId=#{id}")
    public int update(DbFile file);
}
