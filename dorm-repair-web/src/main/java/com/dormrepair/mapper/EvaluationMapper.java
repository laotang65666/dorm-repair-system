package com.dormrepair.mapper;

import com.dormrepair.pojo.Evaluation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
 * 评价数据访问层。
 */
@Mapper
public interface EvaluationMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into evaluation (order_id, user_id, rating, content, create_time) " +
            "values (#{orderId}, #{userId}, #{rating}, #{content}, #{createTime})")
    void insert(Evaluation evaluation);

    @Select("select * from evaluation where order_id = #{orderId}")
    Evaluation selectByOrderId(Long orderId);
}
