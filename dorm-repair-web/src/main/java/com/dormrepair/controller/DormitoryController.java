package com.dormrepair.controller;

import com.dormrepair.anno.Log;
import com.dormrepair.anno.RequireRole;
import com.dormrepair.pojo.Dormitory;
import com.dormrepair.pojo.Result;
import com.dormrepair.service.DormitoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 宿舍管理接口。
 */
@RestController
@RequestMapping("/dormitory")
public class DormitoryController {

    @Autowired
    private DormitoryService dormitoryService;

    /** 查询所有宿舍（所有登录用户可见）：GET /dormitory/list */
    @GetMapping("/list")
    public Result list() {
        return Result.success(dormitoryService.list());
    }

    /** 新增（管理员）：POST /dormitory/add */
    @Log
    @RequireRole("admin")
    @PostMapping("/add")
    public Result add(@RequestBody Dormitory dormitory) {
        return Result.success(dormitoryService.add(dormitory));
    }

    /** 修改（管理员）：PUT /dormitory */
    @Log
    @RequireRole("admin")
    @PutMapping
    public Result update(@RequestBody Dormitory dormitory) {
        return Result.success(dormitoryService.update(dormitory));
    }

    /** 删除（管理员）：DELETE /dormitory/{id} */
    @Log
    @RequireRole("admin")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        dormitoryService.delete(id);
        return Result.success();
    }
}
