package com.dormrepair.service;

import com.dormrepair.pojo.RepairCategory;

import java.util.List;

/**
 * 报修类别业务逻辑接口
 */
public interface RepairCategoryService {

    List<RepairCategory> list();

    RepairCategory add(RepairCategory category);

    RepairCategory update(RepairCategory category);

    void delete(Long id);
}
