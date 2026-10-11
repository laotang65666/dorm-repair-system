package com.dormrepair.mapper;

import com.dormrepair.pojo.RepairProgress;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 维修进度数据访问层。
 */
@Mapper
public interface RepairProgressMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into repair_progress (order_id, content, create_time) values (#{orderId}, #{content}, #{createTime})")
    void insert(RepairProgress progress);

    @Select("select * from repair_progress where order_id = #{orderId} order by create_time asc")
    List<RepairProgress> listByOrderId(Long orderId);
}
