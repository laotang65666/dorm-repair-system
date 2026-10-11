package com.dormrepair.controller;

import com.dormrepair.anno.RequireRole;
import com.dormrepair.pojo.Result;
import com.dormrepair.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 操作日志查询接口（仅管理员）。
 */
@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LogService logService;

    /** 分页查询操作日志：GET /log/page?page=1&pageSize=10 */
    @RequireRole("admin")
    @GetMapping("/page")
    public Result page(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(logService.page(page, pageSize));
    }
}
