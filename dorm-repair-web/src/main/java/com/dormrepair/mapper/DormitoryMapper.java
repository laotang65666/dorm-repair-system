package com.dormrepair.mapper;

import com.dormrepair.pojo.Dormitory;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 宿舍数据访问层。
 */
@Mapper
public interface DormitoryMapper {

    @Select("select * from dormitory order by building_no, room_no")
    List<Dormitory> list();

    @Select("select * from dormitory where id = #{id}")
    Dormitory selectById(Long id);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into dormitory (building_no, room_no, floor) values (#{buildingNo}, #{roomNo}, #{floor})")
    void insert(Dormitory dormitory);

    @Update("update dormitory set building_no = #{buildingNo}, room_no = #{roomNo}, floor = #{floor} where id = #{id}")
    void update(Dormitory dormitory);

    @Delete("delete from dormitory where id = #{id}")
    void deleteById(Long id);
}
