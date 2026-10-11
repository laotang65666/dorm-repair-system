package com.dormrepair.mapper;

import com.dormrepair.pojo.RepairCategory;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 报修类别数据访问层。
 */
@Mapper
public interface RepairCategoryMapper {

    @Select("select * from repair_category order by id")
    List<RepairCategory> list();

    @Select("select * from repair_category where id = #{id}")
    RepairCategory selectById(Long id);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into repair_category (name, description) values (#{name}, #{description})")
    void insert(RepairCategory category);

    @Update("update repair_category set name = #{name}, description = #{description} where id = #{id}")
    void update(RepairCategory category);

    @Delete("delete from repair_category where id = #{id}")
    void deleteById(Long id);
}
