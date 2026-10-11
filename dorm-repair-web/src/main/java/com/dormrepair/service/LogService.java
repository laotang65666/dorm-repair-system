package com.dormrepair.service;

import com.dormrepair.pojo.OperateLog;
import com.dormrepair.pojo.PageResult;

/**
 * 操作日志业务逻辑接口
 */
public interface LogService {

    /** 分页查询操作日志 */
    PageResult<OperateLog> page(int page, int pageSize);
}
