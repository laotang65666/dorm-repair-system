package com.dormrepair.mapper;

import com.dormrepair.pojo.OperateLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作日志数据访问层。
 */
@Mapper
public interface OperateLogMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into operate_log (operate_user_id, operate_time, class_name, method_name, method_params, return_value, cost_time) " +
            "values (#{operateUserId}, #{operateTime}, #{className}, #{methodName}, #{methodParams}, #{returnValue}, #{costTime})")
    void insert(OperateLog operateLog);

    @Select("select count(*) from operate_log")
    Long count();

    @Select("select * from operate_log order by operate_time desc limit #{start}, #{pageSize}")
    List<OperateLog> list(@Param("start") int start, @Param("pageSize") int pageSize);
}
