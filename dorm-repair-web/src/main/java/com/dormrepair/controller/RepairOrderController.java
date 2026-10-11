package com.dormrepair.controller;

import com.dormrepair.anno.Log;
import com.dormrepair.anno.RequireRole;
import com.dormrepair.pojo.RepairOrder;
import com.dormrepair.pojo.RepairOrderQuery;
import com.dormrepair.pojo.Result;
import com.dormrepair.service.RepairOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 报修单管理接口。
 */
@RestController
@RequestMapping("/repair")
public class RepairOrderController {

    @Autowired
    private RepairOrderService repairOrderService;

    /** 提交报修（学生）：POST /repair/create */
    @PostMapping("/create")
    public Result create(@RequestBody RepairOrder order) {
        return Result.success(repairOrderService.create(order));
    }

    /** 分页查询：GET /repair/list?page=1&pageSize=10&userId=&workerId=&status= */
    @GetMapping("/list")
    public Result list(RepairOrderQuery query) {
        return Result.success(repairOrderService.page(query));
    }

    /** 详情（含进度 + 评价）：GET /repair/detail?id= */
    @GetMapping("/detail")
    public Result detail(@RequestParam Long id) {
        return Result.success(repairOrderService.detail(id));
    }

    /** 按状态统计（管理员数据概览）：GET /repair/stats */
    @RequireRole("admin")
    @GetMapping("/stats")
    public Result stats() {
        return Result.success(repairOrderService.stats());
    }

    /** 派单（管理员）：POST /repair/assign?orderId=&workerId= */
    @Log
    @RequireRole("admin")
    @PostMapping("/assign")
    public Result assign(@RequestParam Long orderId, @RequestParam Long workerId) {
        return Result.success(repairOrderService.assign(orderId, workerId));
    }

    /** 更新状态（维修员）：POST /repair/status?orderId=&status= */
    @PostMapping("/status")
    public Result status(@RequestParam Long orderId, @RequestParam Integer status) {
        return Result.success(repairOrderService.updateStatus(orderId, status));
    }

    /** 新增进度（维修员）：POST /repair/progress?orderId=&content= */
    @PostMapping("/progress")
    public Result progress(@RequestParam Long orderId, @RequestParam String content) {
        return Result.success(repairOrderService.addProgress(orderId, content));
    }

    /** 取消报修（学生本人）：POST /repair/cancel?orderId= */
    @PostMapping("/cancel")
    public Result cancel(@RequestParam Long orderId) {
        return Result.success(repairOrderService.cancel(orderId));
    }

    /** 评价（学生本人）：POST /repair/evaluate?orderId=&rating=&content= */
    @PostMapping("/evaluate")
    public Result evaluate(@RequestParam Long orderId,
                           @RequestParam Integer rating,
                           @RequestParam(required = false) String content) {
        return Result.success(repairOrderService.evaluate(orderId, rating, content));
    }
}
