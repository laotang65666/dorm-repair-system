package com.dormrepair.service.impl;

import com.dormrepair.exception.BusinessException;
import com.dormrepair.mapper.DormitoryMapper;
import com.dormrepair.pojo.Dormitory;
import com.dormrepair.service.DormitoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DormitoryServiceImpl implements DormitoryService {

    @Autowired
    private DormitoryMapper dormitoryMapper;

    @Override
    public List<Dormitory> list() {
        return dormitoryMapper.list();
    }

    @Override
    public Dormitory add(Dormitory dormitory) {
        validate(dormitory);
        dormitoryMapper.insert(dormitory);
        return dormitory;
    }

    @Override
    public Dormitory update(Dormitory dormitory) {
        if (dormitory.getId() == null || dormitoryMapper.selectById(dormitory.getId()) == null) {
            throw new BusinessException("宿舍不存在");
        }
        validate(dormitory);
        dormitoryMapper.update(dormitory);
        return dormitoryMapper.selectById(dormitory.getId());
    }

    @Override
    public void delete(Long id) {
        if (dormitoryMapper.selectById(id) == null) {
            throw new BusinessException("宿舍不存在");
        }
        dormitoryMapper.deleteById(id);
    }

    private void validate(Dormitory dormitory) {
        if (dormitory.getBuildingNo() == null || dormitory.getBuildingNo().isBlank()) {
            throw new BusinessException("楼栋号不能为空");
        }
        if (dormitory.getRoomNo() == null || dormitory.getRoomNo().isBlank()) {
            throw new BusinessException("房间号不能为空");
        }
    }
}
