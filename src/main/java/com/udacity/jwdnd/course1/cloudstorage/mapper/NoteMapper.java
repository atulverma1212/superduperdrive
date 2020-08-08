package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Optional;

@Mapper
public interface NoteMapper {

    @Select("select * from NOTES WHERE userid = #{userId}")
    @Results({
            @Result(id = true, property = "id", column = "noteid"),
            @Result(property = "description", column = "notedescription"),
            @Result(property = "title", column = "notetitle"),
            @Result(property = "user", column = "userid",
            one = @One(select = "com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper.findById"))
    })
    public List<Note> findAllByUserId(@Param("userId") int userId);

    @Select("SELECT * FROM NOTES WHERE noteid = #{noteId} and userid = #{userId}")
    @Results({
            @Result(id = true, property = "id", column = "noteid"),
            @Result(property = "description", column = "notedescription"),
            @Result(property = "title", column = "notetitle"),
            @Result(property = "user", column = "userid",
                    one = @One(select = "com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper.findById"))
    })
    public Optional<Note> findByIdAndUserId(@Param("noteId") long id, @Param("userId") int userId);

    @Delete("DELETE FROM NOTES WHERE noteid = #{id}")
    public void deleteById(@Param("id") long id);

    @Insert("INSERT INTO NOTES(notetitle, notedescription, userid) " +
            " VALUES (#{title}, #{description}, #{user.userId})")
    @Options(useGeneratedKeys = true, keyProperty = "id",keyColumn = "noteid")
    public void insert(Note note);

    @Update("Update NOTES set notetitle=#{title}, " +
            " notedescription=#{description} where noteid=#{id}")
    public int update(Note note);
}
