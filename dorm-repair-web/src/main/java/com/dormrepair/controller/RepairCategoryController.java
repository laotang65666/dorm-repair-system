package com.dormrepair.controller;

import com.dormrepair.anno.Log;
import com.dormrepair.anno.RequireRole;
import com.dormrepair.pojo.RepairCategory;
import com.dormrepair.pojo.Result;
import com.dormrepair.service.RepairCategoryService;
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
 * 报修类别管理接口。
 */
@RestController
@RequestMapping("/category")
public class RepairCategoryController {

    @Autowired
    private RepairCategoryService repairCategoryService;

    /** 查询所有类别（所有登录用户可见）：GET /category/list */
    @GetMapping("/list")
    public Result list() {
        return Result.success(repairCategoryService.list());
    }

    /** 新增（管理员）：POST /category/add */
    @Log
    @RequireRole("admin")
    @PostMapping("/add")
    public Result add(@RequestBody RepairCategory category) {
        return Result.success(repairCategoryService.add(category));
    }

    /** 修改（管理员）：PUT /category */
    @Log
    @RequireRole("admin")
    @PutMapping
    public Result update(@RequestBody RepairCategory category) {
        return Result.success(repairCategoryService.update(category));
    }

    /** 删除（管理员）：DELETE /category/{id} */
    @Log
    @RequireRole("admin")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        repairCategoryService.delete(id);
        return Result.success();
    }
}
