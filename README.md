# 宿舍报修管理系统

基于 **Spring Boot 4 + MyBatis + MySQL** 的多模块 Maven 后端，仿照参考项目 tlias 的分层架构，实现「学生报修 → 管理员派单 → 维修员处理 → 学生评价」的完整业务流程。

## 技术栈

| 层 | 技术 |
|---|---|
| 框架 | Spring Boot 4.0.8（Java 21） |
| ORM | MyBatis 4.0.1（注解 + XML 映射） |
| 数据库 | MySQL 8.x |
| 认证 | JWT（jjwt 0.13.0）+ 拦截器 + `@RequireRole` |
| 日志 | AOP 切面 + `@Log` 注解 → `operate_log` 表 |
| 上传 | 阿里云 OSS SDK v2（密钥走环境变量） |
| 密码 | BCrypt（spring-security-crypto） |

## 多模块结构

```
后端/
├── pom.xml                    # 父工程（packaging=pom，锁定 Spring Boot 版本）
├── dorm-repair-pojo/          # 实体类 + Result/PageResult 等通用返回类
├── dorm-repair-utils/         # JwtUtils / UserContext / 阿里云 OSS
└── dorm-repair-web/           # Controller / Service / Mapper / 配置 / 切面 / 异常
```

## 如何运行

### 1. 准备数据库

执行 `C:\Users\whatcanisayman\Desktop\dorm-repair-system\sql\schema.sql`，会创建 `dorm_repair` 库和 8 张表，并插入演示账号（admin / worker01 / student01，密码均为 123456）。

### 2. 数据库 / OSS 配置

在 `dorm-repair-web/src/main/resources/application.yml` 中：

- 数据库：`localhost:3306/dorm_repair`，用户名 root，密码默认 `879269`（可用环境变量 `DB_PASSWORD` 覆盖）。
- OSS：`endpoint` / `bucketName` / `region` 已配好；**AccessKeyId / AccessKeySecret 通过环境变量 `OSS_ACCESS_KEY_ID` / `OSS_ACCESS_KEY_SECRET` 注入**，不要写进代码。

### 3. 启动

用 IDEA 打开 `后端` 目录（或直接打开根 `pom.xml`），运行 `dorm-repair-web` 模块的 `DormRepairApplication`，后端启动在 **8080** 端口。

命令行方式（在 `后端` 目录）：

```bash
mvn -pl dorm-repair-web -am spring-boot:run
```

## 接口概览

统一返回格式 `{code: 1成功/0失败, msg, data}`，分页返回 `{total, rows}`。

| 模块 | 接口 |
|---|---|
| 认证 | `POST /login`、`POST /user/register` |
| 用户 | `GET /user/page`、`GET /user/list?role=`、`POST /user/add`、`PUT /user`、`DELETE /user/{id}` |
| 宿舍 | `GET /dormitory/list`、`POST /dormitory/add`、`PUT /dormitory`、`DELETE /dormitory/{id}` |
| 类别 | `GET /category/list`、`POST /category/add`、`PUT /category`、`DELETE /category/{id}` |
| 报修 | `POST /repair/create`、`GET /repair/list`、`GET /repair/detail`、`GET /repair/stats`、`POST /repair/assign`、`POST /repair/status`、`POST /repair/progress`、`POST /repair/cancel`、`POST /repair/evaluate` |
| 通知 | `GET /notification/list`、`GET /notification/unreadCount`、`POST /notification/read`、`POST /notification/readAll` |
| 上传 | `POST /upload` |
| 日志 | `GET /log/page` |

## 状态流转

```
0 待派单 → 1 已派单 → 2 维修中 → 3 已完成 → 4 已评价
                         └──（仅状态 0 可）5 已取消
```

## 前端部署

前端已打包为静态资源，与 Nginx 一起放在本仓库的 `nginx-1.22.1/` 目录，默认端口 **81**（80 端口留给其它项目）。

- `nginx-1.22.1/html/`：前端打包产物（`index.html` + `assets/`）
- `nginx-1.22.1/conf/nginx.conf`：已配置 Vue Router history 模式回退（`try_files`）+ `/api` 反向代理到后端 8080

### 启动方式

1. 先启动后端（8080）。
2. 在 `nginx-1.22.1` 目录运行 `nginx.exe`。

浏览器访问 **http://localhost:81/** 即可，演示账号见上文。

> 常用命令（在 `nginx-1.22.1` 目录下）：`nginx.exe -s reload` 重载配置、`nginx.exe -s stop` 停止。
