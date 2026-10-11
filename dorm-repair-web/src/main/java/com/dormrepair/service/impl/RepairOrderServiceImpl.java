package com.dormrepair.service.impl;

import com.dormrepair.exception.BusinessException;
import com.dormrepair.mapper.EvaluationMapper;
import com.dormrepair.mapper.RepairOrderMapper;
import com.dormrepair.mapper.RepairProgressMapper;
import com.dormrepair.mapper.UserMapper;
import com.dormrepair.pojo.Evaluation;
import com.dormrepair.pojo.PageResult;
import com.dormrepair.pojo.RepairOrder;
import com.dormrepair.pojo.RepairOrderQuery;
import com.dormrepair.pojo.RepairProgress;
import com.dormrepair.pojo.User;
import com.dormrepair.service.NotificationService;
import com.dormrepair.service.RepairOrderService;
import com.dormrepair.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RepairOrderServiceImpl implements RepairOrderService {

    @Autowired
    private RepairOrderMapper repairOrderMapper;

    @Autowired
    private RepairProgressMapper repairProgressMapper;

    @Autowired
    private EvaluationMapper evaluationMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationService notificationService;

    @Override
    public RepairOrder create(RepairOrder order) {
        User current = currentUser();
        if (order.getCategoryId() == null) {
            throw new BusinessException("请选择报修类别");
        }
        if (order.getDormitoryId() == null) {
            throw new BusinessException("请选择宿舍");
        }
        if (order.getTitle() == null || order.getTitle().isBlank()) {
            throw new BusinessException("请输入报修标题");
        }
        // 报修人从当前登录用户取，不信任前端传的值（防止冒充他人报修）
        order.setId(null);
        order.setUserId(current.getId());
        order.setOrderNo(generateOrderNo());
        order.setStatus(0);            // 0 待派单
        order.setWorkerId(null);
        order.setCreateTime(LocalDateTime.now());
        repairOrderMapper.insert(order);
        return order;
    }

    @Override
    public PageResult<RepairOrder> page(RepairOrderQuery query) {
        Long total = repairOrderMapper.count(query);
        List<RepairOrder> rows = repairOrderMapper.list(query);
        return new PageResult<>(total, rows);
    }

    @Override
    public RepairOrder detail(Long id) {
        RepairOrder order = repairOrderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("报修单不存在");
        }
        order.setProgressList(repairProgressMapper.listByOrderId(id));
        order.setEvaluation(evaluationMapper.selectByOrderId(id));
        return order;
    }

    @Override
    public RepairOrder assign(Long orderId, Long workerId) {
        RepairOrder order = requireOrder(orderId);
        if (order.getStatus() != 0) {
            throw new BusinessException("只有待派单状态的报修单才能派单");
        }
        User worker = userMapper.selectById(workerId);
        if (worker == null || !"worker".equals(worker.getRole())) {
            throw new BusinessException("维修员不存在");
        }
        repairOrderMapper.updateAssign(orderId, workerId, LocalDateTime.now());
        // 派单后通知维修员
        notificationService.createNotification(workerId, "新维修任务", "你有新的报修任务：" + order.getTitle());
        return repairOrderMapper.selectById(orderId);
    }

    @Override
    public RepairOrder updateStatus(Long orderId, Integer status) {
        RepairOrder order = requireOrder(orderId);
        requireWorkerOwner(order);

        LocalDateTime finishTime = null;
        if (status != null && status == 2) {
            if (order.getStatus() != 1) {
                throw new BusinessException("只有已派单状态才能开始维修");
            }
        } else if (status != null && status == 3) {
            if (order.getStatus() != 2) {
                throw new BusinessException("只有维修中状态才能完成");
            }
            finishTime = LocalDateTime.now();
        } else {
            throw new BusinessException("非法的状态值");
        }

        repairOrderMapper.updateStatus(orderId, status, finishTime);
        if (status == 3) {
            notificationService.createNotification(order.getUserId(), "维修完成", "你的报修已完成：" + order.getTitle());
        }
        return repairOrderMapper.selectById(orderId);
    }

    @Override
    public RepairProgress addProgress(Long orderId, String content) {
        RepairOrder order = requireOrder(orderId);
        requireWorkerOwner(order);
        if (content == null || content.isBlank()) {
            throw new BusinessException("进度内容不能为空");
        }
        RepairProgress progress = new RepairProgress();
        progress.setOrderId(orderId);
        progress.setContent(content);
        progress.setCreateTime(LocalDateTime.now());
        repairProgressMapper.insert(progress);
        return progress;
    }

    @Override
    public RepairOrder cancel(Long orderId) {
        RepairOrder order = requireOrder(orderId);
        User current = currentUser();
        if (!current.getId().equals(order.getUserId())) {
            throw new BusinessException("只能取消自己的报修单");
        }
        if (order.getStatus() != 0) {
            throw new BusinessException("只有待派单状态才能取消");
        }
        repairOrderMapper.updateStatus(orderId, 5, null);
        return repairOrderMapper.selectById(orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // 插入评价 + 改状态 两步要么都成功，要么都回滚
    public Evaluation evaluate(Long orderId, Integer rating, String content) {
        RepairOrder order = requireOrder(orderId);
        User current = currentUser();
        if (!current.getId().equals(order.getUserId())) {
            throw new BusinessException("只能评价自己的报修单");
        }
        if (order.getStatus() != 3) {
            throw new BusinessException("只有已完成的报修单才能评价");
        }
        if (rating == null || rating < 1 || rating > 5) {
            throw new BusinessException("评分需在1-5之间");
        }

        Evaluation evaluation = new Evaluation();
        evaluation.setOrderId(orderId);
        evaluation.setUserId(current.getId());
        evaluation.setRating(rating);
        evaluation.setContent(content);
        evaluation.setCreateTime(LocalDateTime.now());
        evaluationMapper.insert(evaluation);

        repairOrderMapper.updateStatus(orderId, 4, order.getFinishTime());
        return evaluation;
    }

    @Override
    public Map<String, Long> stats() {
        List<Map<String, Object>> rows = repairOrderMapper.countByStatus();
        Map<String, Long> result = new HashMap<>();
        long total = 0;
        for (Map<String, Object> row : rows) {
            Object status = row.get("status");
            Object cnt = row.get("cnt");
            long count = cnt == null ? 0 : ((Number) cnt).longValue();
            result.put(String.valueOf(status), count);
            total += count;
        }
        result.put("total", total);
        return result;
    }

    /** 生成报修单编号，如 BX20261011123456 */
    private String generateOrderNo() {
        return "BX" + System.currentTimeMillis();
    }

    /** 取当前登录用户（未登录抛异常） */
    private User currentUser() {
        User user = UserContext.get();
        if (user == null) {
            throw new BusinessException("未登录");
        }
        return user;
    }

    /** 查报修单，不存在抛异常 */
    private RepairOrder requireOrder(Long orderId) {
        RepairOrder order = repairOrderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("报修单不存在");
        }
        return order;
    }

    /** 校验当前登录用户是该报修单的维修员 */
    private void requireWorkerOwner(RepairOrder order) {
        User current = currentUser();
        if (!current.getId().equals(order.getWorkerId())) {
            throw new BusinessException("只能处理分配给自己的报修单");
        }
    }
}
