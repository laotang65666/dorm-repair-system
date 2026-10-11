package com.dormrepair.mapper;

import com.dormrepair.pojo.RepairOrder;
import com.dormrepair.pojo.RepairOrderQuery;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 报修单数据访问层。
 * 列表/详情（JOIN 出名称）用 XML（见 RepairOrderMapper.xml），增改用注解。
 */
@Mapper
public interface RepairOrderMapper {

    /** 新增报修单（返回自增主键） */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into repair_order (order_no, user_id, category_id, dormitory_id, title, description, image_urls, worker_id, status, create_time) " +
            "values (#{orderNo}, #{userId}, #{categoryId}, #{dormitoryId}, #{title}, #{description}, #{imageUrls}, #{workerId}, #{status}, #{createTime})")
    void insert(RepairOrder order);

    /** 分页列表（XML：JOIN + 动态条件 + LIMIT） */
    List<RepairOrder> list(RepairOrderQuery query);

    /** 总数（XML） */
    Long count(RepairOrderQuery query);

    /** 按 id 查单条（XML：JOIN 出名称） */
    RepairOrder selectById(Long id);

    /** 派单：填充维修员 + 派单时间，状态改为已派单(1) */
    @Update("update repair_order set worker_id = #{workerId}, assign_time = #{assignTime}, status = 1 where id = #{id}")
    void updateAssign(@Param("id") Long id, @Param("workerId") Long workerId, @Param("assignTime") LocalDateTime assignTime);

    /** 更新状态（维修中/已完成/已评价/已取消），完成时带 finishTime */
    @Update("update repair_order set status = #{status}, finish_time = #{finishTime} where id = #{id}")
    void updateStatus(@Param("id") Long id, @Param("status") Integer status, @Param("finishTime") LocalDateTime finishTime);

    /** 按状态统计数量（管理员数据概览用） */
    @Select("select status, count(*) as cnt from repair_order group by status")
    List<Map<String, Object>> countByStatus();
}
