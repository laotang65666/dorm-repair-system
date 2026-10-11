package com.dormrepair.service.impl;

import com.dormrepair.mapper.OperateLogMapper;
import com.dormrepair.pojo.OperateLog;
import com.dormrepair.pojo.PageResult;
import com.dormrepair.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Override
    public PageResult<OperateLog> page(int page, int pageSize) {
        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 10;
        Long total = operateLogMapper.count();
        int start = (page - 1) * pageSize;
        List<OperateLog> rows = operateLogMapper.list(start, pageSize);
        return new PageResult<>(total, rows);
    }
}
