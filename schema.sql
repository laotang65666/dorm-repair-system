-- =============================================================
--  宿舍报修管理系统 - 数据库建表脚本
--  数据库：MySQL 8.x
--  字符集：utf8mb4
--
--  使用方法（任选其一）：
--    1) 命令行：mysql -u root -p < schema.sql
--    2) Navicat / DataGrip：打开本文件，全选执行
--
--  演示账号（密码均为 123456）：
--    管理员 admin     学生 student01     维修员 worker01
-- =============================================================

-- 建库（如已存在则先删除，方便重复执行）
DROP DATABASE IF EXISTS dorm_repair;
CREATE DATABASE dorm_repair DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE dorm_repair;

-- -------------------------------------------------------------
-- 1. 用户表 sys_user
-- -------------------------------------------------------------
CREATE TABLE sys_user (
    id            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    username      VARCHAR(50)  NOT NULL                COMMENT '用户名',
    password      VARCHAR(100) NOT NULL                COMMENT '密码(BCrypt加密)',
    real_name     VARCHAR(50)  NOT NULL                COMMENT '姓名',
    role          VARCHAR(20)  NOT NULL                COMMENT '角色: student学生/worker维修员/admin管理员',
    gender        VARCHAR(10)  DEFAULT NULL            COMMENT '性别: 男/女',
    phone         VARCHAR(20)  DEFAULT NULL            COMMENT '手机号',
    dormitory_id  BIGINT       DEFAULT NULL            COMMENT '所在宿舍ID(仅学生)',
    status        TINYINT      NOT NULL DEFAULT 1      COMMENT '状态: 1启用/0禁用',
    create_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- -------------------------------------------------------------
-- 2. 宿舍表 dormitory
-- -------------------------------------------------------------
CREATE TABLE dormitory (
    id          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '宿舍ID',
    building_no VARCHAR(20) NOT NULL                COMMENT '楼栋号',
    room_no     VARCHAR(20) NOT NULL                COMMENT '房间号',
    floor       INT         DEFAULT NULL            COMMENT '楼层',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宿舍表';

-- -------------------------------------------------------------
-- 3. 报修类别表 repair_category
-- -------------------------------------------------------------
CREATE TABLE repair_category (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '类别ID',
    name        VARCHAR(50)  NOT NULL                COMMENT '类别名称',
    description VARCHAR(200) DEFAULT NULL            COMMENT '类别描述',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报修类别表';

-- -------------------------------------------------------------
-- 4. 报修单表 repair_order
-- -------------------------------------------------------------
CREATE TABLE repair_order (
    id            BIGINT        NOT NULL AUTO_INCREMENT COMMENT '报修单ID',
    order_no      VARCHAR(30)   NOT NULL                COMMENT '报修单编号(如BX20240829001)',
    user_id       BIGINT        NOT NULL                COMMENT '报修人ID(学生)',
    category_id   BIGINT        NOT NULL                COMMENT '报修类别ID',
    dormitory_id  BIGINT        NOT NULL                COMMENT '宿舍ID',
    title         VARCHAR(100)  NOT NULL                COMMENT '报修标题',
    description   VARCHAR(500)  DEFAULT NULL            COMMENT '报修描述',
    image_urls    VARCHAR(1000) DEFAULT NULL            COMMENT '图片地址(多图逗号分隔)',
    worker_id     BIGINT        DEFAULT NULL            COMMENT '维修员ID(派单后填充)',
    status        TINYINT       NOT NULL DEFAULT 0      COMMENT '状态: 0待派单/1已派单/2维修中/3已完成/4已评价/5已取消',
    create_time   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '报修时间',
    assign_time   DATETIME      DEFAULT NULL            COMMENT '派单时间',
    finish_time   DATETIME      DEFAULT NULL            COMMENT '完成时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_order_no (order_no),
    KEY idx_user (user_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报修单表';

-- -------------------------------------------------------------
-- 5. 维修进度表 repair_progress
-- -------------------------------------------------------------
CREATE TABLE repair_progress (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '进度ID',
    order_id    BIGINT       NOT NULL                COMMENT '报修单ID',
    content     VARCHAR(300) NOT NULL                COMMENT '进度内容',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
    PRIMARY KEY (id),
    KEY idx_order (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='维修进度表';

-- -------------------------------------------------------------
-- 6. 评价表 evaluation
-- -------------------------------------------------------------
CREATE TABLE evaluation (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '评价ID',
    order_id    BIGINT       NOT NULL                COMMENT '报修单ID',
    user_id     BIGINT       NOT NULL                COMMENT '评价人ID(学生)',
    rating      TINYINT      NOT NULL                COMMENT '评分(1~5)',
    content     VARCHAR(300) DEFAULT NULL            COMMENT '评价内容',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评价时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_order (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评价表';

-- -------------------------------------------------------------
-- 7. 通知表 notification
-- -------------------------------------------------------------
CREATE TABLE notification (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '通知ID',
    user_id     BIGINT       NOT NULL                COMMENT '接收人ID',
    title       VARCHAR(100) NOT NULL                COMMENT '通知标题',
    content     VARCHAR(300) DEFAULT NULL            COMMENT '通知内容',
    is_read     TINYINT      NOT NULL DEFAULT 0      COMMENT '是否已读: 1是/0否',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '通知时间',
    PRIMARY KEY (id),
    KEY idx_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知表';

-- -------------------------------------------------------------
-- 8. 操作日志表 operate_log（AOP 切面自动写入）
-- -------------------------------------------------------------
CREATE TABLE operate_log (
    id               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    operate_user_id  BIGINT       DEFAULT NULL            COMMENT '操作人ID',
    operate_time     DATETIME     DEFAULT NULL            COMMENT '操作时间',
    class_name       VARCHAR(200) DEFAULT NULL            COMMENT '被调用的类名',
    method_name      VARCHAR(100) DEFAULT NULL            COMMENT '被调用的方法名',
    method_params    VARCHAR(1000) DEFAULT NULL           COMMENT '方法参数',
    return_value     VARCHAR(1000) DEFAULT NULL           COMMENT '方法返回值',
    cost_time        BIGINT       DEFAULT NULL            COMMENT '耗时(毫秒)',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- =============================================================
--  外键约束（放在建表之后统一添加，避免建表顺序问题）
-- =============================================================
ALTER TABLE sys_user
    ADD CONSTRAINT fk_user_dormitory FOREIGN KEY (dormitory_id) REFERENCES dormitory(id);

ALTER TABLE repair_order
    ADD CONSTRAINT fk_order_user    FOREIGN KEY (user_id)     REFERENCES sys_user(id),
    ADD CONSTRAINT fk_order_worker  FOREIGN KEY (worker_id)   REFERENCES sys_user(id),
    ADD CONSTRAINT fk_order_category FOREIGN KEY (category_id) REFERENCES repair_category(id),
    ADD CONSTRAINT fk_order_dorm    FOREIGN KEY (dormitory_id) REFERENCES dormitory(id);

ALTER TABLE repair_progress
    ADD CONSTRAINT fk_progress_order FOREIGN KEY (order_id) REFERENCES repair_order(id);

ALTER TABLE evaluation
    ADD CONSTRAINT fk_eval_order FOREIGN KEY (order_id) REFERENCES repair_order(id),
    ADD CONSTRAINT fk_eval_user  FOREIGN KEY (user_id)  REFERENCES sys_user(id);

ALTER TABLE notification
    ADD CONSTRAINT fk_notify_user FOREIGN KEY (user_id) REFERENCES sys_user(id);

-- =============================================================
--  初始化数据（种子数据）
-- =============================================================

-- 宿舍
INSERT INTO dormitory (building_no, room_no, floor) VALUES
('1号楼', '101', 1),
('1号楼', '102', 1),
('2号楼', '201', 2),
('3号楼', '301', 3);

-- 报修类别
INSERT INTO repair_category (name, description) VALUES
('水电报修', '水龙头、下水道、灯具、插座等'),
('门窗报修', '门锁、窗户、窗帘等'),
('网络报修', '网线、路由器、网络接口等'),
('家具报修', '桌椅、床铺、衣柜等'),
('其他', '未分类的报修事项');

-- 用户（密码统一为 123456，已用 BCrypt 加密）
-- 加密后的 123456：$2a$10$MhlJ945xcwbWqr3nGKYvB.W.tc8KPTPwTCGKt95Zua5m9Qb9vhtRK
INSERT INTO sys_user (username, password, real_name, role, phone, dormitory_id, status) VALUES
('admin',     '$2a$10$MhlJ945xcwbWqr3nGKYvB.W.tc8KPTPwTCGKt95Zua5m9Qb9vhtRK', '系统管理员', 'admin',  '13800000001', NULL, 1),
('worker01',  '$2a$10$MhlJ945xcwbWqr3nGKYvB.W.tc8KPTPwTCGKt95Zua5m9Qb9vhtRK', '维修员小张', 'worker', '13800000002', NULL, 1),
('student01', '$2a$10$MhlJ945xcwbWqr3nGKYvB.W.tc8KPTPwTCGKt95Zua5m9Qb9vhtRK', '学生小李',   'student','13800000003', 1,    1);

-- 完成提示
SELECT '数据库 dorm_repair 建表完成' AS result;
