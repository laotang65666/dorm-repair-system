package com.dormrepair.service;

import com.dormrepair.pojo.Dormitory;

import java.util.List;

/**
 * 宿舍业务逻辑接口
 */
public interface DormitoryService {

    List<Dormitory> list();

    Dormitory add(Dormitory dormitory);

    Dormitory update(Dormitory dormitory);

    void delete(Long id);
}
