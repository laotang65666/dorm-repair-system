package com.dormrepair.service;

import com.dormrepair.pojo.Evaluation;
import com.dormrepair.pojo.PageResult;
import com.dormrepair.pojo.RepairOrder;
import com.dormrepair.pojo.RepairOrderQuery;
import com.dormrepair.pojo.RepairProgress;

import java.util.Map;

/**
 * 报修单业务逻辑接口
 */
public interface RepairOrderService {

    /** 学生提交报修 */
    RepairOrder create(RepairOrder order);

    /** 分页查询报修单（支持按报修人/维修员/状态过滤） */
    PageResult<RepairOrder> page(RepairOrderQuery query);

    /** 报修单详情（含进度列表 + 评价） */
    RepairOrder detail(Long id);

    /** 管理员派单（指派维修员） */
    RepairOrder assign(Long orderId, Long workerId);

    /** 维修员更新状态（开始维修/完成） */
    RepairOrder updateStatus(Long orderId, Integer status);

    /** 维修员新增进度记录 */
    RepairProgress addProgress(Long orderId, String content);

    /** 学生取消报修（仅待派单状态） */
    RepairOrder cancel(Long orderId);

    /** 学生评价维修结果 */
    Evaluation evaluate(Long orderId, Integer rating, String content);

    /** 按状态统计数量（管理员数据概览用，返回 {status: count, ..., total}） */
    Map<String, Long> stats();
}
