package com.dormrepair.service.impl;

import com.dormrepair.exception.BusinessException;
import com.dormrepair.mapper.RepairCategoryMapper;
import com.dormrepair.pojo.RepairCategory;
import com.dormrepair.service.RepairCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepairCategoryServiceImpl implements RepairCategoryService {

    @Autowired
    private RepairCategoryMapper repairCategoryMapper;

    @Override
    public List<RepairCategory> list() {
        return repairCategoryMapper.list();
    }

    @Override
    public RepairCategory add(RepairCategory category) {
        if (category.getName() == null || category.getName().isBlank()) {
            throw new BusinessException("类别名称不能为空");
        }
        repairCategoryMapper.insert(category);
        return category;
    }

    @Override
    public RepairCategory update(RepairCategory category) {
        if (category.getId() == null || repairCategoryMapper.selectById(category.getId()) == null) {
            throw new BusinessException("类别不存在");
        }
        if (category.getName() == null || category.getName().isBlank()) {
            throw new BusinessException("类别名称不能为空");
        }
        repairCategoryMapper.update(category);
        return repairCategoryMapper.selectById(category.getId());
    }

    @Override
    public void delete(Long id) {
        if (repairCategoryMapper.selectById(id) == null) {
            throw new BusinessException("类别不存在");
        }
        repairCategoryMapper.deleteById(id);
    }
}
