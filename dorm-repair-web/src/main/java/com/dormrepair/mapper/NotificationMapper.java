package com.dormrepair.mapper;

import com.dormrepair.pojo.Notification;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 通知数据访问层。
 */
@Mapper
public interface NotificationMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into notification (user_id, title, content, is_read, create_time) " +
            "values (#{userId}, #{title}, #{content}, #{isRead}, #{createTime})")
    void insert(Notification notification);

    @Select("select * from notification where user_id = #{userId} order by create_time desc")
    List<Notification> listByUserId(Long userId);

    @Select("select count(*) from notification where user_id = #{userId} and is_read = 0")
    Long countUnread(Long userId);

    @Update("update notification set is_read = 1 where id = #{id} and user_id = #{userId}")
    void markRead(@Param("userId") Long userId, @Param("id") Long id);

    @Update("update notification set is_read = 1 where user_id = #{userId}")
    void markAllRead(Long userId);
}
