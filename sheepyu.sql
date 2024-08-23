/*
 Navicat Premium Dump SQL

 Source Server         : local@mysql01
 Source Server Type    : MySQL
 Source Server Version : 80028 (8.0.28)
 Source Host           : localhost:3001
 Source Schema         : sheepyu

 Target Server Type    : MySQL
 Target Server Version : 80028 (8.0.28)
 File Encoding         : 65001

 Date: 23/08/2024 14:34:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for QRTZ_BLOB_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_BLOB_TRIGGERS`;
CREATE TABLE `QRTZ_BLOB_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `BLOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `SCHED_NAME`(`SCHED_NAME` ASC, `TRIGGER_NAME` ASC, `TRIGGER_GROUP` ASC) USING BTREE,
  CONSTRAINT `QRTZ_BLOB_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_CALENDARS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CALENDARS`;
CREATE TABLE `QRTZ_CALENDARS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `CALENDAR_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_CRON_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CRON_TRIGGERS`;
CREATE TABLE `QRTZ_CRON_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `CRON_EXPRESSION` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TIME_ZONE_ID` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `QRTZ_CRON_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_FIRED_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_FIRED_TRIGGERS`;
CREATE TABLE `QRTZ_FIRED_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `ENTRY_ID` varchar(95) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `INSTANCE_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `FIRED_TIME` bigint NOT NULL,
  `SCHED_TIME` bigint NOT NULL,
  `PRIORITY` int NOT NULL,
  `STATE` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `JOB_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `JOB_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `ENTRY_ID`) USING BTREE,
  INDEX `IDX_QRTZ_FT_TRIG_INST_NAME`(`SCHED_NAME` ASC, `INSTANCE_NAME` ASC) USING BTREE,
  INDEX `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY`(`SCHED_NAME` ASC, `INSTANCE_NAME` ASC, `REQUESTS_RECOVERY` ASC) USING BTREE,
  INDEX `IDX_QRTZ_FT_J_G`(`SCHED_NAME` ASC, `JOB_NAME` ASC, `JOB_GROUP` ASC) USING BTREE,
  INDEX `IDX_QRTZ_FT_JG`(`SCHED_NAME` ASC, `JOB_GROUP` ASC) USING BTREE,
  INDEX `IDX_QRTZ_FT_T_G`(`SCHED_NAME` ASC, `TRIGGER_NAME` ASC, `TRIGGER_GROUP` ASC) USING BTREE,
  INDEX `IDX_QRTZ_FT_TG`(`SCHED_NAME` ASC, `TRIGGER_GROUP` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_JOB_DETAILS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_JOB_DETAILS`;
CREATE TABLE `QRTZ_JOB_DETAILS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `JOB_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `JOB_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `IS_DURABLE` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `IS_UPDATE_DATA` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `JOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_J_REQ_RECOVERY`(`SCHED_NAME` ASC, `REQUESTS_RECOVERY` ASC) USING BTREE,
  INDEX `IDX_QRTZ_J_GRP`(`SCHED_NAME` ASC, `JOB_GROUP` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_LOCKS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_LOCKS`;
CREATE TABLE `QRTZ_LOCKS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `LOCK_NAME` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `LOCK_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;
CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_SCHEDULER_STATE
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SCHEDULER_STATE`;
CREATE TABLE `QRTZ_SCHEDULER_STATE`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `INSTANCE_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `LAST_CHECKIN_TIME` bigint NOT NULL,
  `CHECKIN_INTERVAL` bigint NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_SIMPLE_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPLE_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPLE_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `REPEAT_COUNT` bigint NOT NULL,
  `REPEAT_INTERVAL` bigint NOT NULL,
  `TIMES_TRIGGERED` bigint NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `QRTZ_SIMPLE_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_SIMPROP_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPROP_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPROP_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `STR_PROP_1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `STR_PROP_2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `STR_PROP_3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `INT_PROP_1` int NULL DEFAULT NULL,
  `INT_PROP_2` int NULL DEFAULT NULL,
  `LONG_PROP_1` bigint NULL DEFAULT NULL,
  `LONG_PROP_2` bigint NULL DEFAULT NULL,
  `DEC_PROP_1` decimal(13, 4) NULL DEFAULT NULL,
  `DEC_PROP_2` decimal(13, 4) NULL DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `QRTZ_SIMPROP_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_TRIGGERS`;
CREATE TABLE `QRTZ_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `JOB_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `JOB_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint NULL DEFAULT NULL,
  `PREV_FIRE_TIME` bigint NULL DEFAULT NULL,
  `PRIORITY` int NULL DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_TYPE` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `START_TIME` bigint NOT NULL,
  `END_TIME` bigint NULL DEFAULT NULL,
  `CALENDAR_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `MISFIRE_INSTR` smallint NULL DEFAULT NULL,
  `JOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_J`(`SCHED_NAME` ASC, `JOB_NAME` ASC, `JOB_GROUP` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_JG`(`SCHED_NAME` ASC, `JOB_GROUP` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_C`(`SCHED_NAME` ASC, `CALENDAR_NAME` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_G`(`SCHED_NAME` ASC, `TRIGGER_GROUP` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_STATE`(`SCHED_NAME` ASC, `TRIGGER_STATE` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_N_STATE`(`SCHED_NAME` ASC, `TRIGGER_NAME` ASC, `TRIGGER_GROUP` ASC, `TRIGGER_STATE` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_N_G_STATE`(`SCHED_NAME` ASC, `TRIGGER_GROUP` ASC, `TRIGGER_STATE` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_NEXT_FIRE_TIME`(`SCHED_NAME` ASC, `NEXT_FIRE_TIME` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST`(`SCHED_NAME` ASC, `TRIGGER_STATE` ASC, `NEXT_FIRE_TIME` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_MISFIRE`(`SCHED_NAME` ASC, `MISFIRE_INSTR` ASC, `NEXT_FIRE_TIME` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE`(`SCHED_NAME` ASC, `MISFIRE_INSTR` ASC, `NEXT_FIRE_TIME` ASC, `TRIGGER_STATE` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP`(`SCHED_NAME` ASC, `MISFIRE_INSTR` ASC, `NEXT_FIRE_TIME` ASC, `TRIGGER_GROUP` ASC, `TRIGGER_STATE` ASC) USING BTREE,
  CONSTRAINT `QRTZ_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `QRTZ_JOB_DETAILS` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_access_log
-- ----------------------------
DROP TABLE IF EXISTS `system_access_log`;
CREATE TABLE `system_access_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户编号',
  `user_type` tinyint NOT NULL COMMENT '用户类型',
  `login_type` tinyint NOT NULL COMMENT '登录类型: 用户名+密码, 邮箱登录...',
  `login_result` tinyint NOT NULL COMMENT '登录结果: 密码错误, 验证码错误...',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '用户账号',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `user_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户 IP',
  `user_agent` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '浏览器 UA',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统访问记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_api_log
-- ----------------------------
DROP TABLE IF EXISTS `system_api_log`;
CREATE TABLE `system_api_log`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` int NOT NULL DEFAULT 0 COMMENT '用户编号',
  `user_type` tinyint NOT NULL DEFAULT 0 COMMENT '用户类型',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作名',
  `type` int NOT NULL COMMENT '操作类型, 查询, 删除...',
  `request_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '请求方法名',
  `request_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '请求地址',
  `request_params` varchar(8000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '请求参数',
  `user_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户 IP',
  `duration` int NOT NULL COMMENT '执行时长',
  `result_code` int NULL DEFAULT 0 COMMENT '结果码',
  `result_data` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '结果数据',
  `status` tinyint NOT NULL COMMENT '正常 or 异常',
  `exception_time` datetime NULL DEFAULT NULL COMMENT '异常发生时间',
  `exception_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '异常类ex.getClass()',
  `exception_root_cause_message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '异常导致的根消息\n .ExceptionUtil#getRootCauseMessage(Throwable)}',
  `exception_stack_trace_full` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '异常的栈轨迹\n完整信息',
  `exception_stack_trace_crucial` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '异常的关键信息',
  `process_status` tinyint NULL DEFAULT NULL COMMENT '处理状态',
  `process_time` datetime NULL DEFAULT NULL COMMENT '处理时间',
  `process_user_id` int NULL DEFAULT NULL COMMENT '处理用户编号',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统异常日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_codegen_column
-- ----------------------------
DROP TABLE IF EXISTS `system_codegen_column`;
CREATE TABLE `system_codegen_column`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` bigint NOT NULL COMMENT 'tableId',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字段名',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字段类型',
  `comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字段描述',
  `nullable` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否允许为空',
  `primary_key` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否主键',
  `auto_increment` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否自增',
  `java_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Java 属性类型',
  `java_field` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Java 属性名',
  `dict_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '字典类型',
  `example` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '数据示例',
  `create_operation` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否为 Create 创建操作的字段',
  `update_operation` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否为 Update 更新操作的字段',
  `query_operation` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否为 List 查询操作的字段',
  `query_condition` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '=' COMMENT 'List 查询操作的条件类型',
  `list_operation_result` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否为 List 查询操作的返回字段',
  `form_show_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'text' COMMENT '表单显示类型',
  `quick_search` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否为快速搜索字段',
  `sort` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否按照此字段排序, 如果是, 那么是asc还是desc',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '代码生成表字段定义' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_codegen_table
-- ----------------------------
DROP TABLE IF EXISTS `system_codegen_table`;
CREATE TABLE `system_codegen_table`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `scene` tinyint NOT NULL DEFAULT 1 COMMENT '生成场景, 0管理端, 1用户端',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '表描述',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `module_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '模块名',
  `business_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '业务名',
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '类名称',
  `class_comment` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '类描述',
  `author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '作者',
  `require_list` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否需要list接口',
  `require_page` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否需要page接口',
  `require_export` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否需要excel导出接口',
  `require_import` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否需要excel导入接口',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '代码生成表定义' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_config
-- ----------------------------
DROP TABLE IF EXISTS `system_config`;
CREATE TABLE `system_config`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '参数键值',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '参数配置表' ROW_FORMAT = Dynamic;

INSERT INTO `system_config` VALUES (1, '验证码开关', 'captcha.enable', 'false', '系统验证码的开关', 'admin', '2023-01-21 06:48:12', 'admin', '2023-10-20 16:38:25', b'0');
INSERT INTO `system_config` VALUES (2, '默认密码', 'password.default', '12345678', '重置系统用户时的默认密码', 'admin', '2023-01-23 20:00:48', NULL, '2023-01-23 20:00:48', b'0');
INSERT INTO `system_config` VALUES (3, '文件上传实现', 'file-upload.default', 'LOCAL', '默认的文件上传实现, 修改立马生效\nLOCAL: 本地上传\nALIYUN: 阿里云\nQCLOUD: 腾讯', 'admin', '2023-01-25 09:12:09', 'admin', '2023-03-17 20:24:41', b'0');
INSERT INTO `system_config` VALUES (4, '消息发送实现', 'sms-sender.default', 'EMAIL', '默认的消息发送实现', 'admin', '2023-01-25 09:25:25', '', '2023-01-26 01:01:09', b'0');
INSERT INTO `system_config` VALUES (5, '文件分片大小', 'file-upload.part-size', '10', '默认文件分片大小(单位M)', '', '2023-01-26 01:02:20', '', '2023-01-26 01:02:40', b'0');

-- ----------------------------
-- Table structure for system_dept
-- ----------------------------
DROP TABLE IF EXISTS `system_dept`;
CREATE TABLE `system_dept`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '部门名称',
  `parent_id` bigint NOT NULL DEFAULT 0 COMMENT '父部门id',
  `sort` int NOT NULL DEFAULT 0 COMMENT '显示顺序',
  `type` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '类型:(0部门,1:职位)',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

INSERT INTO `system_dept` VALUES (1, '总部', 0, 0, 0, '', '3029944576@qq.com', 'admin', '2023-01-30 12:10:33', 'admin', '2023-09-26 21:48:01', b'0');
INSERT INTO `system_dept` VALUES (7, '访客', 1, 0, 1, NULL, NULL, 'admin', '2023-09-24 23:03:02', 'admin', '2023-09-27 09:53:37', b'0');
INSERT INTO `system_dept` VALUES (24, '测试分部', 1, 0, 0, NULL, NULL, 'admin', '2023-10-20 16:45:37', NULL, '2023-10-20 16:45:37', b'0');

-- ----------------------------
-- Table structure for system_dept_query_dept
-- ----------------------------
DROP TABLE IF EXISTS `system_dept_query_dept`;
CREATE TABLE `system_dept_query_dept`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `source_dept_id` bigint NOT NULL COMMENT '被查询的部门id',
  `target_dept_id` bigint NOT NULL COMMENT '查询操作者部门id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '部门之间的查询权限控制表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_dept_role
-- ----------------------------
DROP TABLE IF EXISTS `system_dept_role`;
CREATE TABLE `system_dept_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `dept_id` bigint NOT NULL COMMENT '部门职位ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '部门职位和角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `system_dict_data`;
CREATE TABLE `system_dict_data`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典类型',
  `sort` int NOT NULL DEFAULT 0 COMMENT '字典排序',
  `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '字典标签',
  `value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '字典键值',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '通用状态',
  `color_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '颜色类型: primary, info, warning, success, error',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_dict_type`(`dict_type` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '字典数据表' ROW_FORMAT = Dynamic;

INSERT INTO `system_dict_data` VALUES (6, 'common_status', 2, '启用', '1', 1, '', '', 'admin', '2023-01-29 17:41:16', 'admin', '2023-03-13 11:33:34', b'0');
INSERT INTO `system_dict_data` VALUES (7, 'common_status', 3, '禁用', '0', 1, 'danger', '', 'admin', '2023-01-29 17:41:27', 'admin', '2023-03-13 11:34:39', b'0');
INSERT INTO `system_dict_data` VALUES (8, 'system_menu_type', 0, '目录', '1', 1, 'success', '菜单类型为目录', 'admin', '2023-02-13 14:57:41', NULL, '2023-02-13 14:57:41', b'0');
INSERT INTO `system_dict_data` VALUES (9, 'system_menu_type', 1, '菜单', '2', 1, '', '菜单项', 'admin', '2023-02-13 14:58:17', 'admin', '2023-03-13 16:36:58', b'0');
INSERT INTO `system_dict_data` VALUES (10, 'system_menu_type', 2, '按钮', '3', 1, 'info', '按钮', 'admin', '2023-02-13 14:58:34', 'admin', '2023-03-13 16:37:03', b'0');
INSERT INTO `system_dict_data` VALUES (11, 'system_codegen_scene', 0, '管理端', '1', 1, '', '', 'admin', '2023-03-09 16:19:01', NULL, '2023-03-09 08:21:24', b'0');
INSERT INTO `system_dict_data` VALUES (12, 'system_codegen_scene', 0, '用户端', '2', 1, 'success', '', 'admin', '2023-03-09 16:19:18', NULL, '2023-03-09 16:19:18', b'0');
INSERT INTO `system_dict_data` VALUES (13, 'common_boolean_status', 0, '是', 'true', 1, 'success', '', 'admin', '2023-03-09 16:43:36', NULL, '2023-03-09 16:43:36', b'0');
INSERT INTO `system_dict_data` VALUES (14, 'common_boolean_status', 0, '否', 'false', 1, 'danger', '', 'admin', '2023-03-09 16:44:00', NULL, '2023-03-09 16:44:00', b'0');
INSERT INTO `system_dict_data` VALUES (15, 'sex', 0, '男', '0', 1, '', '', 'admin', '2023-03-10 21:57:14', NULL, '2023-03-10 21:57:14', b'0');
INSERT INTO `system_dict_data` VALUES (16, 'sex', 0, '女', '1', 1, 'danger', '', 'admin', '2023-03-10 21:57:29', NULL, '2023-03-10 21:57:29', b'0');
INSERT INTO `system_dict_data` VALUES (19, 'system_user_type', 0, '用户端', '1', 1, 'success', NULL, 'admin', '2023-03-13 16:12:51', NULL, '2023-03-13 16:12:51', b'0');
INSERT INTO `system_dict_data` VALUES (20, 'system_user_type', 0, '管理端', '2', 1, 'danger', NULL, 'admin', '2023-03-13 16:15:31', NULL, '2023-03-13 16:15:31', b'0');
INSERT INTO `system_dict_data` VALUES (21, 'api_log_process_status', 0, '已处理', '1', 1, 'success', NULL, 'admin', '2023-03-15 15:12:38', NULL, '2023-03-15 15:12:38', b'0');
INSERT INTO `system_dict_data` VALUES (22, 'api_log_process_status', 0, '未处理', '0', 1, 'warning', NULL, 'admin', '2023-03-15 15:12:53', NULL, '2023-03-15 15:12:53', b'0');
INSERT INTO `system_dict_data` VALUES (23, 'api_log_status', 0, '正常', '1', 1, 'success', NULL, 'admin', '2023-03-15 15:13:18', NULL, '2023-03-15 15:13:18', b'0');
INSERT INTO `system_dict_data` VALUES (24, 'api_log_status', 0, '异常', '0', 1, 'danger', NULL, 'admin', '2023-03-15 15:13:27', NULL, '2023-03-15 15:13:27', b'0');
INSERT INTO `system_dict_data` VALUES (25, 'login_result', 0, '成功', '0', 1, 'success', NULL, 'admin', '2023-03-15 15:14:05', NULL, '2023-03-15 15:14:05', b'0');
INSERT INTO `system_dict_data` VALUES (26, 'login_result', 0, '账号或密码错误', '10', 1, 'danger', NULL, 'admin', '2023-03-15 15:14:28', NULL, '2023-03-15 15:14:28', b'0');
INSERT INTO `system_dict_data` VALUES (27, 'login_result', 0, '用户被禁用', '20', 1, 'danger', NULL, 'admin', '2023-03-15 15:14:42', NULL, '2023-03-15 15:14:42', b'0');
INSERT INTO `system_dict_data` VALUES (28, 'login_result', 0, '验证码错误', '30', 1, 'danger', NULL, 'admin', '2023-03-15 15:14:52', NULL, '2023-03-15 15:14:52', b'0');
INSERT INTO `system_dict_data` VALUES (29, 'login_type', 0, '账号登录', '100', 1, 'info', NULL, 'admin', '2023-03-15 15:15:35', NULL, '2023-03-15 15:15:35', b'0');
INSERT INTO `system_dict_data` VALUES (30, 'login_type', 0, '社交登录', '101', 1, 'info', NULL, 'admin', '2023-03-15 15:15:59', NULL, '2023-03-15 15:15:59', b'0');
INSERT INTO `system_dict_data` VALUES (31, 'login_type', 0, '手机登陆', '103', 1, 'info', NULL, 'admin', '2023-03-15 15:16:25', NULL, '2023-03-15 15:16:25', b'0');
INSERT INTO `system_dict_data` VALUES (32, 'login_type', 0, '邮箱登录', '104', 1, 'info', NULL, 'admin', '2023-03-15 15:16:42', NULL, '2023-03-15 15:16:42', b'0');
INSERT INTO `system_dict_data` VALUES (33, 'operate_type', 0, '查询', '1', 1, 'info', NULL, 'admin', '2023-03-15 15:26:30', NULL, '2023-03-15 15:26:30', b'0');
INSERT INTO `system_dict_data` VALUES (34, 'operate_type', 0, '新增', '2', 1, '', NULL, 'admin', '2023-03-15 15:26:42', NULL, '2023-03-15 15:26:42', b'0');
INSERT INTO `system_dict_data` VALUES (35, 'operate_type', 0, '修改', '3', 1, '', NULL, 'admin', '2023-03-15 15:26:51', NULL, '2023-03-15 15:26:51', b'0');
INSERT INTO `system_dict_data` VALUES (36, 'operate_type', 0, '删除', '4', 1, 'danger', NULL, 'admin', '2023-03-15 15:27:03', NULL, '2023-03-15 15:27:03', b'0');
INSERT INTO `system_dict_data` VALUES (37, 'operate_type', 0, '导出', '5', 1, '', NULL, 'admin', '2023-03-15 15:27:28', NULL, '2023-03-15 15:27:28', b'0');
INSERT INTO `system_dict_data` VALUES (38, 'operate_type', 0, '导入', '6', 1, '', NULL, 'admin', '2023-03-15 15:27:39', NULL, '2023-03-15 15:27:39', b'0');
INSERT INTO `system_dict_data` VALUES (39, 'operate_type', 0, '代码生成', '7', 1, '', NULL, 'admin', '2023-03-15 15:27:52', NULL, '2023-03-15 15:27:52', b'0');
INSERT INTO `system_dict_data` VALUES (40, 'operate_type', 0, '下载', '8', 1, 'info', NULL, 'admin', '2023-03-15 15:28:03', NULL, '2023-03-15 15:28:03', b'0');
INSERT INTO `system_dict_data` VALUES (41, 'operate_type', 0, '其他', '0', 1, 'info', NULL, 'admin', '2023-03-15 15:28:12', NULL, '2023-03-15 15:28:12', b'0');
INSERT INTO `system_dict_data` VALUES (42, 'system_job_status', 0, '初始化', '0', 1, '', NULL, 'admin', '2023-03-15 16:48:29', NULL, '2023-03-15 16:48:29', b'0');
INSERT INTO `system_dict_data` VALUES (43, 'system_job_status', 0, '已开启', '1', 1, 'success', NULL, 'admin', '2023-03-15 16:48:40', NULL, '2023-03-15 16:48:40', b'0');
INSERT INTO `system_dict_data` VALUES (44, 'system_job_status', 0, '已暂停', '2', 1, 'warning', NULL, 'admin', '2023-03-15 16:48:51', NULL, '2023-03-15 16:48:51', b'0');
INSERT INTO `system_dict_data` VALUES (45, 'system_jog_log_status', 0, '成功', '1', 1, 'success', NULL, 'admin', '2023-03-15 16:49:15', NULL, '2023-03-15 16:49:15', b'0');
INSERT INTO `system_dict_data` VALUES (46, 'system_jog_log_status', 0, '失败', '0', 1, 'danger', NULL, 'admin', '2023-03-15 16:49:24', NULL, '2023-03-15 16:49:24', b'0');
INSERT INTO `system_dict_data` VALUES (47, 'system_file_complete', 0, '完成', '1', 1, 'success', NULL, 'admin', '2023-03-15 17:59:09', NULL, '2023-03-15 17:59:09', b'0');
INSERT INTO `system_dict_data` VALUES (48, 'system_file_complete', 0, '未完成', '0', 1, 'warning', NULL, 'admin', '2023-03-15 17:59:20', NULL, '2023-03-15 17:59:20', b'0');
INSERT INTO `system_dict_data` VALUES (49, 'file_type', 0, '图片', 'image/jpeg', 1, 'info', NULL, 'admin', '2023-03-17 09:57:09', NULL, '2023-03-17 09:57:09', b'0');
INSERT INTO `system_dict_data` VALUES (50, 'file_type', 0, '图片', 'image/png', 1, 'info', NULL, 'admin', '2023-03-17 09:57:50', NULL, '2023-03-17 09:57:50', b'0');
INSERT INTO `system_dict_data` VALUES (51, 'file_type', 0, '图片', 'image/gif', 1, 'info', NULL, 'admin', '2023-03-17 09:58:04', NULL, '2023-03-17 09:58:04', b'0');
INSERT INTO `system_dict_data` VALUES (52, 'file_type', 0, 'word', 'application/msword', 1, '', NULL, 'admin', '2023-03-17 09:59:24', NULL, '2023-03-17 09:59:24', b'0');
INSERT INTO `system_dict_data` VALUES (53, 'file_type', 0, 'word', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', 1, '', NULL, 'admin', '2023-03-17 09:59:40', NULL, '2023-03-17 09:59:40', b'0');
INSERT INTO `system_dict_data` VALUES (54, 'file_type', 0, '音频', 'audio/mpeg', 1, '', NULL, 'admin', '2023-03-17 10:01:14', NULL, '2023-03-17 10:01:14', b'0');
INSERT INTO `system_dict_data` VALUES (55, 'file_type', 0, '视频', 'video/mpeg', 1, '', NULL, 'admin', '2023-03-17 10:01:33', NULL, '2023-03-17 10:01:33', b'0');
INSERT INTO `system_dict_data` VALUES (56, 'file_type', 0, 'PDF', 'application/pdf', 1, 'info', NULL, 'admin', '2023-03-17 10:01:53', NULL, '2023-03-17 10:01:53', b'0');
INSERT INTO `system_dict_data` VALUES (57, 'file_type', 0, 'ppt', 'application/vnd.ms-powerpoint', 1, 'warning', NULL, 'admin', '2023-03-17 10:02:21', NULL, '2023-03-17 10:02:21', b'0');
INSERT INTO `system_dict_data` VALUES (58, 'file_type', 0, 'ppt', 'application/vnd.openxmlformats-officedocument.presentationml.presentation', 1, 'warning', NULL, 'admin', '2023-03-17 10:02:38', NULL, '2023-03-17 10:02:38', b'0');
INSERT INTO `system_dict_data` VALUES (59, 'file_type', 0, '压缩包', 'application/x-rar-compressed', 1, 'danger', NULL, 'admin', '2023-03-17 10:03:12', NULL, '2023-03-17 10:03:12', b'0');
INSERT INTO `system_dict_data` VALUES (60, 'file_type', 0, '压缩包', 'application/x-tar', 1, 'danger', NULL, 'admin', '2023-03-17 10:03:36', NULL, '2023-03-17 10:03:36', b'0');
INSERT INTO `system_dict_data` VALUES (61, 'file_type', 0, 'excel', 'application/vnd.ms-excel', 1, 'success', NULL, 'admin', '2023-03-17 10:04:01', NULL, '2023-03-17 10:04:01', b'0');
INSERT INTO `system_dict_data` VALUES (62, 'file_type', 0, 'excel', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', 1, 'success', NULL, 'admin', '2023-03-17 10:04:11', NULL, '2023-03-17 10:04:11', b'0');
INSERT INTO `system_dict_data` VALUES (63, 'file_type', 0, '压缩包', 'application/zip', 1, 'danger', NULL, 'admin', '2023-03-17 10:04:28', NULL, '2023-03-17 10:04:28', b'0');
INSERT INTO `system_dict_data` VALUES (64, 'file_type', 0, '压缩包', 'application/x-7z-compressed', 1, 'danger', NULL, 'admin', '2023-03-17 10:04:41', NULL, '2023-03-17 10:04:41', b'0');
INSERT INTO `system_dict_data` VALUES (65, 'file_type', 0, '视频', 'video/mp4', 1, '', NULL, 'admin', '2023-03-17 10:05:22', NULL, '2023-03-17 10:05:22', b'0');
INSERT INTO `system_dict_data` VALUES (66, 'system_dept_type', 0, '部门', '0', 1, '', NULL, 'admin', '2023-09-24 22:38:06', 'admin', '2023-10-19 20:53:58', b'0');
INSERT INTO `system_dict_data` VALUES (67, 'system_dept_type', 1, '职位', '1', 1, 'info', NULL, 'admin', '2023-09-24 22:38:29', 'admin', '2023-10-19 20:54:04', b'0');
INSERT INTO `system_dict_data` VALUES (70, 'login_type', 0, 'RefreshToken', '105', 1, 'info', NULL, 'admin', '2023-09-30 15:29:49', NULL, '2023-09-30 15:29:49', b'0');

-- ----------------------------
-- Table structure for system_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `system_dict_type`;
CREATE TABLE `system_dict_type`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典类型: 模块_字段下划线命名',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '字典名称',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '开启状态',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_type`(`type` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '字典类型表' ROW_FORMAT = Dynamic;

INSERT INTO `system_dict_type` VALUES (6, 'common_status', '通用状态', 1, '启用或禁用', 'admin', '2023-01-29 17:40:28', NULL, '2023-01-29 17:40:28', b'0');
INSERT INTO `system_dict_type` VALUES (7, 'system_menu_type', '菜单资源类型', 1, '菜单资源类型', 'admin', '2023-02-13 14:55:33', NULL, '2023-02-13 07:00:13', b'0');
INSERT INTO `system_dict_type` VALUES (8, 'system_codegen_scene', '代码生成场景', 1, '', 'admin', '2023-03-09 16:18:19', NULL, '2023-03-09 16:18:19', b'0');
INSERT INTO `system_dict_type` VALUES (9, 'common_boolean_status', '通用判断状态(是否)', 1, '', 'admin', '2023-03-09 16:40:32', NULL, '2023-03-09 16:40:32', b'0');
INSERT INTO `system_dict_type` VALUES (10, 'sex', '性别', 1, NULL, 'admin', '2023-03-10 21:56:45', 'admin', '2023-03-12 20:42:31', b'0');
INSERT INTO `system_dict_type` VALUES (15, 'system_user_type', '系统用户类型', 1, NULL, 'admin', '2023-03-13 16:11:47', NULL, '2023-03-13 16:11:47', b'0');
INSERT INTO `system_dict_type` VALUES (18, 'login_type', '登录方式/类型', 1, NULL, 'admin', '2023-03-15 15:10:15', NULL, '2023-03-15 15:10:15', b'0');
INSERT INTO `system_dict_type` VALUES (19, 'login_result', '登录结果', 1, NULL, 'admin', '2023-03-15 15:10:32', NULL, '2023-03-15 15:10:32', b'0');
INSERT INTO `system_dict_type` VALUES (20, 'api_log_status', 'api日志状态', 1, '调用api后结果是否有异常', 'admin', '2023-03-15 15:11:14', 'admin', '2023-03-15 15:11:32', b'0');
INSERT INTO `system_dict_type` VALUES (21, 'api_log_process_status', 'api异常日志处理状态', 1, NULL, 'admin', '2023-03-15 15:12:12', NULL, '2023-03-15 15:12:12', b'0');
INSERT INTO `system_dict_type` VALUES (22, 'operate_type', 'api操作类型', 1, NULL, 'admin', '2023-03-15 15:26:04', NULL, '2023-03-15 15:26:04', b'0');
INSERT INTO `system_dict_type` VALUES (24, 'system_job_status', '定时任务状态', 1, NULL, 'admin', '2023-03-15 16:47:04', NULL, '2023-03-15 16:47:04', b'0');
INSERT INTO `system_dict_type` VALUES (25, 'system_jog_log_status', '定时任务日志状态', 1, NULL, 'admin', '2023-03-15 16:47:25', NULL, '2023-03-15 16:47:25', b'0');
INSERT INTO `system_dict_type` VALUES (26, 'system_file_complete', '文件上传完成状态', 1, NULL, 'admin', '2023-03-15 17:57:15', NULL, '2023-03-15 17:57:15', b'0');
INSERT INTO `system_dict_type` VALUES (27, 'file_type', '文件MIME类型', 1, NULL, 'admin', '2023-03-17 09:55:17', 'admin', '2023-12-15 18:22:43', b'0');
INSERT INTO `system_dict_type` VALUES (28, 'system_dept_type', '系统部门类型', 1, NULL, 'admin', '2023-09-24 22:37:29', NULL, '2023-09-24 22:37:29', b'0');

-- ----------------------------
-- Table structure for system_file
-- ----------------------------
DROP TABLE IF EXISTS `system_file`;
CREATE TABLE `system_file`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '文件编号',
  `upload_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '兼容s3协议唯一上传id',
  `part_index` int NOT NULL DEFAULT 0 COMMENT '如果是分片上传, 当前上传了几块, 1代表上传了一块',
  `filename` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件名',
  `md5` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件md5',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件 URL',
  `mime_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'MIME类型',
  `size` bigint NULL DEFAULT NULL COMMENT '文件大小, 字节',
  `domain` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件上传地域, 例如: http://localhost',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件上传相对路径, 例如: /2022/08/xxx.jpg',
  `complete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否完成',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_upload_id`(`upload_id` ASC) USING BTREE,
  UNIQUE INDEX `idx_md5`(`md5` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_file_part
-- ----------------------------
DROP TABLE IF EXISTS `system_file_part`;
CREATE TABLE `system_file_part`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `upload_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '一次上传唯一id',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '相对路径',
  `md5` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件部分的md5',
  `part_index` int UNSIGNED NOT NULL COMMENT '当前是第几部分',
  `size` int UNSIGNED NOT NULL COMMENT '文件部分大小(字节)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_upload_idx`(`upload_id` ASC, `part_index` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_job
-- ----------------------------
DROP TABLE IF EXISTS `system_job`;
CREATE TABLE `system_job`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务编号',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '任务名称',
  `status` tinyint NOT NULL COMMENT '任务状态',
  `handler_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '处理器的名字',
  `handler_param` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '处理器的参数',
  `cron` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'CRON 表达式',
  `retry_count` int NOT NULL DEFAULT 0 COMMENT '重试次数',
  `retry_interval` int NOT NULL DEFAULT 0 COMMENT '重试间隔',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '定时任务表' ROW_FORMAT = Dynamic;

INSERT INTO `system_job` VALUES (1, '测试定时任务', 2, 'testTask', '测试参数xxx', '0/2 * * * * ?', 2, 2, 'admin', '2023-01-17 15:17:28', 'admin', '2023-12-26 10:19:14', b'0');
INSERT INTO `system_job` VALUES (2, '权限缓存刷新', 2, 'permissionRefreshTask', NULL, '0 0/30 * * * ?', 1, 5, 'admin', '2023-09-25 09:13:00', 'admin', '2023-09-25 09:38:41', b'0');

-- ----------------------------
-- Table structure for system_job_log
-- ----------------------------
DROP TABLE IF EXISTS `system_job_log`;
CREATE TABLE `system_job_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志编号',
  `job_id` bigint NOT NULL COMMENT '任务编号',
  `handler_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '处理器的名字',
  `handler_param` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '处理器的参数',
  `retry_count` int NULL DEFAULT 1 COMMENT '重试了多少次',
  `begin_time` datetime NULL DEFAULT NULL COMMENT '开始执行时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束执行时间',
  `duration` int NULL DEFAULT NULL COMMENT '执行时长',
  `status` tinyint NULL DEFAULT NULL COMMENT '任务状态1成功, 0失败',
  `result` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '结果数据',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '定时任务日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_menu
-- ----------------------------
DROP TABLE IF EXISTS `system_menu`;
CREATE TABLE `system_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单名称',
  `permission` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '权限标识',
  `type` tinyint NOT NULL COMMENT '菜单类型',
  `sort` int NOT NULL DEFAULT 0 COMMENT '显示顺序',
  `parent_id` bigint NOT NULL DEFAULT 0 COMMENT '父菜单ID',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '路由地址',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '组件路径',
  `status` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否启用',
  `keep_alive` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否缓存',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

INSERT INTO `system_menu` VALUES (1, '系统管理', '', 1, 10, 0, 'system', 'fa fa-windows', '', b'1', b'0', 'admin', '2023-01-30 16:32:59', 'admin', '2023-09-27 11:42:44', b'0');
INSERT INTO `system_menu` VALUES (2, '菜单管理', 'system:menu:query', 2, 0, 1, 'menu', 'el-icon-Menu', 'system/menu/index', b'1', b'1', 'admin', '2023-01-30 16:34:56', 'admin', '2023-03-16 10:56:45', b'0');
INSERT INTO `system_menu` VALUES (3, '新增菜单', 'system:menu:create', 3, 0, 2, '', '', '', b'1', b'1', 'admin', '2023-01-30 16:36:34', 'admin', '2023-09-25 14:26:20', b'0');
INSERT INTO `system_menu` VALUES (4, '删除菜单', 'system:menu:delete', 3, 0, 2, '', '', '', b'1', b'1', 'admin', '2023-01-30 16:37:05', 'admin', '2023-09-25 14:26:22', b'0');
INSERT INTO `system_menu` VALUES (5, '修改菜单', 'system:menu:update', 3, 0, 2, '', '', '', b'1', b'1', 'admin', '2023-01-30 16:37:14', 'admin', '2023-09-25 14:26:23', b'0');
INSERT INTO `system_menu` VALUES (7, '代码生成', 'system:codegen:query', 2, 10, 1, 'codegen', 'fa fa-copy', 'system/codegen/index', b'1', b'1', 'admin', '2023-02-26 09:56:23', 'admin', '2023-03-14 16:21:54', b'0');
INSERT INTO `system_menu` VALUES (9, '新增代码模板', 'system:codegen:create', 3, 0, 7, '', '#', '', b'1', b'0', 'admin', '2023-02-26 11:01:15', 'admin', '2023-02-26 14:54:09', b'0');
INSERT INTO `system_menu` VALUES (10, '修改代码模板', 'system:codegen:update', 3, 0, 7, '', '#', '', b'1', b'0', 'admin', '2023-02-26 11:02:04', 'admin', '2023-02-26 14:55:08', b'0');
INSERT INTO `system_menu` VALUES (11, '删除代码模板', 'system:codegen:delete', 3, 0, 7, '', '#', '', b'1', b'0', 'admin', '2023-02-26 11:02:31', 'admin', '2023-02-26 14:55:11', b'0');
INSERT INTO `system_menu` VALUES (12, '控制台', 'dashboard', 2, -1, 0, 'dashboard', 'fa fa-dashboard', 'dashboard', b'1', b'1', 'admin', '2023-02-26 11:17:04', 'admin', '2023-02-26 14:21:20', b'0');
INSERT INTO `system_menu` VALUES (14, '用户管理', 'system:user:query', 2, 1, 1, 'user', 'el-icon-Avatar', 'system/user/index', b'1', b'1', 'admin', '2023-03-03 21:28:22', 'admin', '2023-09-25 14:17:49', b'0');
INSERT INTO `system_menu` VALUES (15, '测试1', 'system:demo:query', 2, 99, 1, 'demo', 'fa fa-stethoscope', 'system/demo/index', b'0', b'1', 'admin', '2023-03-08 15:18:16', 'admin', '2023-09-25 16:34:05', b'0');
INSERT INTO `system_menu` VALUES (16, '添加测试', 'system:demo:create', 3, 0, 15, '', '#', '', b'0', b'1', 'admin', '2023-03-10 21:30:16', 'admin', '2023-10-13 12:21:27', b'0');
INSERT INTO `system_menu` VALUES (17, '修改测试', 'system:demo:update', 3, 0, 15, '', '#', '', b'0', b'1', 'admin', '2023-03-10 21:30:39', NULL, '2023-09-25 16:34:05', b'0');
INSERT INTO `system_menu` VALUES (18, '删除测试', 'system:demo:delete', 3, 0, 15, '', '#', '', b'0', b'1', 'admin', '2023-03-10 21:31:01', NULL, '2023-09-25 16:34:05', b'0');
INSERT INTO `system_menu` VALUES (19, '导入测试', 'system:demo:import', 3, 0, 15, '', '#', '', b'0', b'1', 'admin', '2023-03-10 21:31:55', NULL, '2023-09-25 16:34:05', b'0');
INSERT INTO `system_menu` VALUES (20, '导出测试', 'system:demo:export', 3, 0, 15, '', '#', '', b'0', b'1', 'admin', '2023-03-10 21:32:07', NULL, '2023-09-25 16:34:05', b'0');
INSERT INTO `system_menu` VALUES (21, '生成代码', 'system:codegen:generate', 3, 0, 7, '', '#', '', b'1', b'1', 'admin', '2023-03-10 21:36:05', NULL, '2023-03-10 21:36:05', b'0');
INSERT INTO `system_menu` VALUES (27, '字典管理', 'system:dict:query', 2, 4, 1, 'dict', 'el-icon-Collection', 'system/dict/index', b'1', b'1', 'admin', '2023-03-12 10:05:37', 'admin', '2023-09-26 18:30:26', b'0');
INSERT INTO `system_menu` VALUES (28, '删除字典', 'system:dict:delete', 3, 0, 27, '', '#', '', b'1', b'1', 'admin', '2023-03-12 10:06:10', NULL, '2023-03-12 10:06:10', b'0');
INSERT INTO `system_menu` VALUES (29, '修改字典', 'system:dict:update', 3, 0, 27, '', '#', '', b'1', b'1', 'admin', '2023-03-12 10:06:28', NULL, '2023-03-12 10:06:28', b'0');
INSERT INTO `system_menu` VALUES (30, '新增字典', 'system:dict:create', 3, 0, 27, '', '#', '', b'1', b'1', 'admin', '2023-03-12 10:06:43', NULL, '2023-03-12 10:06:43', b'0');
INSERT INTO `system_menu` VALUES (31, '部门/职位', 'system:dept:query', 2, 2, 1, 'dept', 'fa fa-sitemap', 'system/dept/index', b'1', b'1', 'admin', '2023-03-13 16:35:14', 'admin', '2023-10-13 12:07:12', b'0');
INSERT INTO `system_menu` VALUES (32, '添加部门/职位', 'system:dept:create', 3, 0, 31, '', '#', '', b'1', b'1', 'admin', '2023-03-13 16:36:05', 'admin', '2023-10-13 12:07:46', b'0');
INSERT INTO `system_menu` VALUES (33, '删除部门/职位', 'system:dept:delete', 3, 0, 31, '', '#', '', b'1', b'1', 'admin', '2023-03-13 16:36:28', 'admin', '2023-10-13 12:07:49', b'0');
INSERT INTO `system_menu` VALUES (34, '修改部门/职位', 'system:dept:update', 3, 0, 31, '', '#', '', b'1', b'1', 'admin', '2023-03-13 16:36:44', 'admin', '2023-10-13 12:08:07', b'0');
INSERT INTO `system_menu` VALUES (39, '角色管理', 'system:role:query', 2, 3, 1, 'role', 'fa fa-group', 'system/role/index', b'1', b'1', 'admin', '2023-03-13 18:44:10', 'admin', '2023-09-26 18:32:33', b'0');
INSERT INTO `system_menu` VALUES (40, '添加角色', 'system:role:create', 3, 0, 39, '', '#', '', b'1', b'1', 'admin', '2023-03-13 18:44:33', 'admin', '2023-03-13 18:45:10', b'0');
INSERT INTO `system_menu` VALUES (41, '删除角色', 'system:role:delete', 3, 0, 39, '', '#', '', b'1', b'1', 'admin', '2023-03-13 18:44:50', 'admin', '2023-03-13 18:45:15', b'0');
INSERT INTO `system_menu` VALUES (42, '修改角色', 'system:role:update', 3, 0, 39, '', '#', '', b'1', b'1', 'admin', '2023-03-13 18:45:05', NULL, '2023-03-13 18:45:05', b'0');
INSERT INTO `system_menu` VALUES (43, '分配菜单', 'system:menu:assign', 3, 0, 2, '', '#', '', b'1', b'1', 'admin', '2023-03-13 19:04:41', NULL, '2023-03-13 19:04:41', b'0');
INSERT INTO `system_menu` VALUES (44, '创建用户', 'system:user:create', 3, 0, 14, '', '#', '', b'1', b'1', 'admin', '2023-03-14 16:17:38', NULL, '2023-03-14 16:17:38', b'0');
INSERT INTO `system_menu` VALUES (45, '修改用户', 'system:user:update', 3, 0, 14, '', '#', '', b'1', b'1', 'admin', '2023-03-14 16:18:01', NULL, '2023-03-14 16:18:01', b'0');
INSERT INTO `system_menu` VALUES (46, '删除用户', 'system:user:delete', 3, 0, 14, '', '#', '', b'1', b'1', 'admin', '2023-03-14 16:18:20', NULL, '2023-03-14 16:18:20', b'0');
INSERT INTO `system_menu` VALUES (47, '导出用户', 'system:user:export', 3, 0, 14, '', '#', '', b'1', b'1', 'admin', '2023-03-14 16:18:33', NULL, '2023-03-14 16:18:33', b'0');
INSERT INTO `system_menu` VALUES (48, '分配角色', 'system:role:assign', 3, 0, 39, '', '#', '', b'1', b'1', 'admin', '2023-03-14 17:45:00', NULL, '2023-03-14 17:45:00', b'0');
INSERT INTO `system_menu` VALUES (49, '重置用户密码', 'system:user:reset-password', 3, 0, 14, '', '#', '', b'1', b'1', 'admin', '2023-03-14 17:46:08', NULL, '2023-03-14 17:46:08', b'0');
INSERT INTO `system_menu` VALUES (50, '日志管理', '', 1, 10, 1, 'log', 'fa fa-list-alt', '', b'1', b'1', 'admin', '2023-03-15 18:04:15', 'admin', '2023-03-15 18:04:31', b'0');
INSERT INTO `system_menu` VALUES (51, '访问日志', 'system:log-access:query', 2, 0, 50, 'access', 'fa fa-universal-access', 'system/log/access', b'1', b'1', 'admin', '2023-03-15 18:06:14', 'admin', '2023-03-18 09:06:15', b'0');
INSERT INTO `system_menu` VALUES (52, 'API日志', 'system:log-api:query', 2, 0, 50, 'api', 'fa fa-connectdevelop', 'system/log/api', b'1', b'1', 'admin', '2023-03-15 18:06:40', 'admin', '2023-03-18 09:07:41', b'0');
INSERT INTO `system_menu` VALUES (53, '定时任务', 'system:job:query', 2, 10, 1, 'job', 'fa fa-tasks', 'system/job/index', b'1', b'1', 'admin', '2023-03-15 18:07:58', 'admin', '2023-03-16 14:59:05', b'0');
INSERT INTO `system_menu` VALUES (54, '创建定时任务', 'system:job:create', 3, 0, 53, '', '#', '', b'1', b'1', 'admin', '2023-03-15 18:08:29', NULL, '2023-03-15 18:08:29', b'0');
INSERT INTO `system_menu` VALUES (55, '修改定时任务', 'system:job:update', 3, 0, 53, '', '#', '', b'1', b'1', 'admin', '2023-03-15 18:08:43', NULL, '2023-03-15 18:08:43', b'0');
INSERT INTO `system_menu` VALUES (56, '删除定时任务', 'system:job:delete', 3, 0, 53, '', '#', '', b'1', b'1', 'admin', '2023-03-15 18:09:00', NULL, '2023-03-15 18:09:00', b'0');
INSERT INTO `system_menu` VALUES (57, '系统配置', 'system:config:query', 2, 10, 1, 'config', 'el-icon-Setting', 'system/config/index', b'1', b'1', 'admin', '2023-03-15 18:11:03', 'admin', '2023-03-16 14:49:09', b'0');
INSERT INTO `system_menu` VALUES (58, '文件管理', 'system:file:query', 2, 10, 1, 'file', 'el-icon-Files', 'system/file/index', b'1', b'1', 'admin', '2023-03-15 18:11:59', NULL, '2023-12-26 02:05:22', b'0');
INSERT INTO `system_menu` VALUES (59, '添加配置', 'system:config:create', 3, 0, 57, '', '#', '', b'1', b'1', 'admin', '2023-03-15 18:12:53', NULL, '2023-03-15 18:12:53', b'0');
INSERT INTO `system_menu` VALUES (60, '修改配置', 'system:config:update', 3, 0, 57, '', '#', '', b'1', b'1', 'admin', '2023-03-15 18:13:07', NULL, '2023-03-15 18:13:07', b'0');
INSERT INTO `system_menu` VALUES (61, '删除配置', 'system:config:delete', 3, 0, 57, '', '#', '', b'1', b'1', 'admin', '2023-03-15 18:13:23', NULL, '2023-03-15 18:13:23', b'0');
INSERT INTO `system_menu` VALUES (62, '删除文件', 'system:file:delete', 3, 0, 58, '', '#', '', b'1', b'1', 'admin', '2023-03-15 18:13:46', NULL, '2023-12-26 02:05:22', b'0');
INSERT INTO `system_menu` VALUES (63, '下载文件', 'system:file:download', 3, 0, 58, '', '#', '', b'1', b'1', 'admin', '2023-03-15 18:14:05', NULL, '2023-12-26 02:05:22', b'0');
INSERT INTO `system_menu` VALUES (64, '处理api错误日志', 'sytem:log-api:update', 3, 0, 52, '', '#', '', b'1', b'1', 'admin', '2023-03-16 12:00:19', NULL, '2023-03-16 12:00:19', b'0');
INSERT INTO `system_menu` VALUES (65, '上传文件', 'system:file:create', 3, 0, 58, '', '#', '', b'1', b'1', 'admin', '2023-03-17 15:29:09', NULL, '2023-12-26 02:05:22', b'0');
INSERT INTO `system_menu` VALUES (66, '查询菜单', 'system:menu:query', 3, 0, 2, '', '', '', b'1', b'1', 'admin', '2023-09-27 09:40:29', 'admin', '2023-10-20 17:02:41', b'0');
INSERT INTO `system_menu` VALUES (67, '查询用户', 'system:user:query', 3, 0, 14, '', '', '', b'1', b'1', 'admin', '2023-09-27 09:40:57', NULL, '2023-09-27 09:40:57', b'0');
INSERT INTO `system_menu` VALUES (68, '查询部门/职位', 'system:dept:query', 3, 0, 31, '', '', '', b'1', b'1', 'admin', '2023-09-27 09:41:17', 'admin', '2023-10-13 12:08:12', b'0');
INSERT INTO `system_menu` VALUES (69, '查询角色', 'system:role:query', 3, 0, 39, '', '', '', b'1', b'1', 'admin', '2023-09-27 09:41:35', NULL, '2023-09-27 09:41:35', b'0');
INSERT INTO `system_menu` VALUES (85, '查询字典', 'system:dict:query', 3, 0, 27, '', '', '', b'1', b'1', 'admin', '2023-09-27 18:32:10', NULL, '2023-09-27 18:32:10', b'0');

-- ----------------------------
-- Table structure for system_role
-- ----------------------------
DROP TABLE IF EXISTS `system_role`;
CREATE TABLE `system_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `dept_id` bigint UNSIGNED NOT NULL COMMENT '部门id',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色权限字符串',
  `sort` int NOT NULL COMMENT '显示顺序',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色信息表' ROW_FORMAT = Dynamic;

INSERT INTO `system_role` VALUES (1, 1, '超级管理员', 'super_admin', 0, '拥有所有权限..', 'admin', '2023-01-31 11:28:08', 'admin', '2023-10-13 04:01:28', b'0');
INSERT INTO `system_role` VALUES (2, 1, '普通角色', 'common', 0, '只有查询权限', 'admin', '2023-01-31 08:32:27', 'admin', '2023-10-13 04:01:28', b'0');
INSERT INTO `system_role` VALUES (6, 1, '部门管理者基本权限', 'dept_manager_basic', 3, NULL, 'admin', '2023-09-25 18:29:47', NULL, '2023-10-13 04:01:28', b'0');

-- ----------------------------
-- Table structure for system_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `system_role_menu`;
CREATE TABLE `system_role_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增编号',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = Dynamic;

INSERT INTO `system_role_menu` VALUES (15, 2, 12);
INSERT INTO `system_role_menu` VALUES (20, 6, 32);
INSERT INTO `system_role_menu` VALUES (21, 6, 33);
INSERT INTO `system_role_menu` VALUES (23, 6, 39);
INSERT INTO `system_role_menu` VALUES (24, 6, 40);
INSERT INTO `system_role_menu` VALUES (25, 6, 41);
INSERT INTO `system_role_menu` VALUES (26, 6, 42);
INSERT INTO `system_role_menu` VALUES (27, 6, 12);
INSERT INTO `system_role_menu` VALUES (28, 6, 44);
INSERT INTO `system_role_menu` VALUES (29, 6, 45);
INSERT INTO `system_role_menu` VALUES (30, 6, 14);
INSERT INTO `system_role_menu` VALUES (31, 6, 46);
INSERT INTO `system_role_menu` VALUES (32, 6, 47);
INSERT INTO `system_role_menu` VALUES (34, 6, 49);
INSERT INTO `system_role_menu` VALUES (35, 6, 31);
INSERT INTO `system_role_menu` VALUES (36, 6, 1);
INSERT INTO `system_role_menu` VALUES (37, 6, 43);
INSERT INTO `system_role_menu` VALUES (71, 6, 66);
INSERT INTO `system_role_menu` VALUES (72, 6, 48);
INSERT INTO `system_role_menu` VALUES (73, 6, 34);

-- ----------------------------
-- Table structure for system_user
-- ----------------------------
DROP TABLE IF EXISTS `system_user`;
CREATE TABLE `system_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户账号',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '密码',
  `nickname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户昵称',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '用户邮箱',
  `mobile` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '手机号码',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '头像地址',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '帐号状态（1正常 0停用）',
  `type` int NOT NULL COMMENT '1会员,2管理员',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `login_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '最后登录IP',
  `login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_username`(`username` ASC) USING BTREE,
  INDEX `idx_email`(`email` ASC) USING BTREE,
  INDEX `idx_mobile`(`mobile` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

INSERT INTO `system_user` VALUES (1, 'admin', '$2a$10$ruiKtNKXEXxHYO6ZQQNwmO3m6moo0d2yTHhBwHb5JCay0qRYhOBgW', 'sheepyu', '3029944576@qq.com', '18374758888', 'http://localhost:18080/file/2023/12/15/81fb71a0e898449a8fa42ad807f53101.jpg', 1, 2, '我是管理员,嘿嘿', '127.0.0.1', '2024-07-22 10:58:09', 'admin', '2023-01-19 14:59:42', 'null', '2024-07-22 10:58:09', b'0');
INSERT INTO `system_user` VALUES (2, 'sheepyu_CFq03WGoKm', '$2a$10$P6AXMyJm0Q3E9Li2f2ts6OBK0uyPUPo/Wlm7U4mUy019TuxoXnjLe', '小黑', '1126882717@qq.com', '18311113333', 'https://thirdwx.qlogo.cn/mmopen/vi_32/XF9G5zQOXaLVgO739DoicP1ibNtDSl52swglcDOiclNcNtzLicpYcrXjKBsibiaMkxY9MrQ13vdBiaGjM2EjGhVI24m7Q/132', 1, 1, NULL, '127.0.0.1', '2023-01-22 17:34:40', '', '2023-01-22 09:31:17', 'admin', '2023-01-22 17:34:40', b'0');
INSERT INTO `system_user` VALUES (3, 'xiaohei', '$2a$10$ofi1MJj4cyiwN4yWOQAz0eqxDZPosBo0czyrou8ty6vBAVrT4NySi', 'heihei', 'xxx@163.com', '19507414588', 'http://localhost:18080/file/2023/10/20/06dc05fdbc0e497e8c07195ee13c7f5f.png', 1, 2, '', '127.0.0.1', '2023-12-15 17:26:22', 'admin', '2023-01-31 16:45:10', 'xiaohei', '2023-12-15 17:26:52', b'0');

-- ----------------------------
-- Table structure for system_user_dept
-- ----------------------------
DROP TABLE IF EXISTS `system_user_dept`;
CREATE TABLE `system_user_dept`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户和部门关联表' ROW_FORMAT = Dynamic;

INSERT INTO `system_user_dept` VALUES (1, 1, 1);
INSERT INTO `system_user_dept` VALUES (2, 3, 24);

-- ----------------------------
-- Table structure for system_user_role
-- ----------------------------
DROP TABLE IF EXISTS `system_user_role`;
CREATE TABLE `system_user_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增编号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户和角色关联表' ROW_FORMAT = Dynamic;

INSERT INTO `system_user_role` VALUES (1, 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
