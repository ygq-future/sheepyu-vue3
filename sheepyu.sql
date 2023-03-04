/*
 Navicat Premium Data Transfer

 Source Server         : local@mysql01
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : localhost:3001
 Source Schema         : sheepyu

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 04/03/2023 17:53:44
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of QRTZ_BLOB_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_CALENDARS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CALENDARS`;
CREATE TABLE `QRTZ_CALENDARS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `CALENDAR_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of QRTZ_CALENDARS
-- ----------------------------

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of QRTZ_CRON_TRIGGERS
-- ----------------------------
INSERT INTO `QRTZ_CRON_TRIGGERS` VALUES ('quartzScheduler', 'testTask', 'DEFAULT', '0 5 * * * ?', 'Asia/Shanghai');

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of QRTZ_FIRED_TRIGGERS
-- ----------------------------

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of QRTZ_JOB_DETAILS
-- ----------------------------
INSERT INTO `QRTZ_JOB_DETAILS` VALUES ('quartzScheduler', 'testTask', 'DEFAULT', NULL, 'top.sheepyu.framework.job.core.handler.JobHandlerInvoke', '0', '1', '1', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C770800000010000000027400064A4F425F49447372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B020000787000000000000000027400104A4F425F48414E444C45525F4E414D45740008746573745461736B7800);

-- ----------------------------
-- Table structure for QRTZ_LOCKS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_LOCKS`;
CREATE TABLE `QRTZ_LOCKS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `LOCK_NAME` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `LOCK_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of QRTZ_LOCKS
-- ----------------------------
INSERT INTO `QRTZ_LOCKS` VALUES ('quartzScheduler', 'STATE_ACCESS');
INSERT INTO `QRTZ_LOCKS` VALUES ('quartzScheduler', 'TRIGGER_ACCESS');
INSERT INTO `QRTZ_LOCKS` VALUES ('schedulerFactoryBean', 'TRIGGER_ACCESS');

-- ----------------------------
-- Table structure for QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;
CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of QRTZ_SCHEDULER_STATE
-- ----------------------------
INSERT INTO `QRTZ_SCHEDULER_STATE` VALUES ('quartzScheduler', 'NON_CLUSTERED', 1677923609961, 15000);

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of QRTZ_SIMPLE_TRIGGERS
-- ----------------------------

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of QRTZ_SIMPROP_TRIGGERS
-- ----------------------------

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of QRTZ_TRIGGERS
-- ----------------------------
INSERT INTO `QRTZ_TRIGGERS` VALUES ('quartzScheduler', 'testTask', 'DEFAULT', 'testTask', 'DEFAULT', NULL, 1677924300000, 1677920716986, 5, 'WAITING', 'CRON', 1673940256000, 0, NULL, 0, 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C770800000010000000037400114A4F425F48414E444C45525F504152414D74000FE6B58BE8AF95E58F82E695B07878787400124A4F425F52455452595F494E54455256414C737200116A6176612E6C616E672E496E746567657212E2A0A4F781873802000149000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B02000078700000000274000F4A4F425F52455452595F434F554E5471007E000C7800);

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
) ENGINE = InnoDB AUTO_INCREMENT = 2749 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统访问记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_access_log
-- ----------------------------
INSERT INTO `system_access_log` VALUES (2655, NULL, 1, 100, 10, 'admin', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36', 'admin', '2023-01-20 17:24:04', NULL, '2023-01-20 09:27:42', b'0');
INSERT INTO `system_access_log` VALUES (2656, NULL, 1, 100, 30, 'admin', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36', 'admin', '2023-01-20 17:26:46', NULL, '2023-01-20 09:27:42', b'0');
INSERT INTO `system_access_log` VALUES (2657, 1, 1, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36', 'admin', '2023-01-20 17:27:03', NULL, '2023-01-20 17:27:03', b'0');
INSERT INTO `system_access_log` VALUES (2658, 1, 1, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36', 'admin', '2023-01-20 17:30:07', NULL, '2023-01-20 17:30:07', b'0');
INSERT INTO `system_access_log` VALUES (2659, 1, 1, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36', 'admin', '2023-01-21 16:17:17', NULL, '2023-01-21 16:17:17', b'0');
INSERT INTO `system_access_log` VALUES (2660, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36', 'admin', '2023-01-22 17:17:05', NULL, '2023-01-22 17:17:05', b'0');
INSERT INTO `system_access_log` VALUES (2661, 1, 2, 100, 0, '18374756852', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36', 'admin', '2023-01-22 17:17:26', NULL, '2023-01-22 17:17:26', b'0');
INSERT INTO `system_access_log` VALUES (2662, NULL, 2, 100, 10, '1837475652', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36', 'admin', '2023-01-22 17:17:30', NULL, '2023-01-23 12:55:09', b'0');
INSERT INTO `system_access_log` VALUES (2663, 1, 1, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36', 'admin', '2023-01-22 17:17:45', NULL, '2023-01-22 17:17:45', b'0');
INSERT INTO `system_access_log` VALUES (2664, 1, 1, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36', 'admin', '2023-01-22 17:20:54', NULL, '2023-01-22 17:20:54', b'0');
INSERT INTO `system_access_log` VALUES (2665, 1, 1, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36', 'admin', '2023-01-22 17:21:13', NULL, '2023-01-22 17:21:13', b'0');
INSERT INTO `system_access_log` VALUES (2666, NULL, 1, 100, 10, 'admin', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36', 'admin', '2023-01-22 17:21:36', NULL, '2023-01-23 12:55:12', b'0');
INSERT INTO `system_access_log` VALUES (2667, NULL, 2, 100, 10, '1837475652', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36', 'admin', '2023-01-22 17:21:43', NULL, '2023-01-23 12:55:14', b'0');
INSERT INTO `system_access_log` VALUES (2668, 1, 2, 100, 0, '18374756852', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36', 'admin', '2023-01-22 17:21:48', NULL, '2023-01-22 17:21:48', b'0');
INSERT INTO `system_access_log` VALUES (2669, NULL, 1, 100, 10, '3029944576@qq.com', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36', 'admin', '2023-01-22 17:28:44', NULL, '2023-01-23 12:55:15', b'0');
INSERT INTO `system_access_log` VALUES (2670, NULL, 1, 104, 30, '1126882717@qq.com', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36', 'admin', '2023-01-22 17:33:26', NULL, '2023-01-23 12:55:16', b'0');
INSERT INTO `system_access_log` VALUES (2671, NULL, 1, 100, 10, '', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36', 'admin', '2023-01-22 17:33:32', NULL, '2023-01-23 12:55:18', b'0');
INSERT INTO `system_access_log` VALUES (2672, 2, 1, 104, 0, '1126882717@qq.com', '小黑', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36', 'admin', '2023-01-22 17:34:40', NULL, '2023-01-22 17:34:40', b'0');
INSERT INTO `system_access_log` VALUES (2673, NULL, 2, 100, 10, 'admin', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', 'admin', '2023-02-08 17:30:18', NULL, '2023-02-08 17:30:18', b'0');
INSERT INTO `system_access_log` VALUES (2674, NULL, 2, 100, 10, 'admin', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', 'admin', '2023-02-08 17:31:31', NULL, '2023-02-08 17:31:31', b'0');
INSERT INTO `system_access_log` VALUES (2675, NULL, 2, 100, 10, 'admin', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', 'admin', '2023-02-08 17:32:28', NULL, '2023-02-08 17:32:28', b'0');
INSERT INTO `system_access_log` VALUES (2676, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', 'admin', '2023-02-08 17:34:19', NULL, '2023-02-08 17:34:19', b'0');
INSERT INTO `system_access_log` VALUES (2677, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', 'admin', '2023-02-08 17:34:37', NULL, '2023-02-08 17:34:37', b'0');
INSERT INTO `system_access_log` VALUES (2678, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', 'admin', '2023-02-08 17:35:01', NULL, '2023-02-08 17:35:01', b'0');
INSERT INTO `system_access_log` VALUES (2679, NULL, 2, 100, 30, 'admin', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', 'admin', '2023-02-08 17:36:22', NULL, '2023-02-08 17:36:22', b'0');
INSERT INTO `system_access_log` VALUES (2680, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', 'admin', '2023-02-08 17:36:32', NULL, '2023-02-08 17:36:32', b'0');
INSERT INTO `system_access_log` VALUES (2681, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', 'admin', '2023-02-08 17:40:58', NULL, '2023-02-08 17:40:58', b'0');
INSERT INTO `system_access_log` VALUES (2682, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', NULL, '2023-02-08 17:58:59', NULL, '2023-02-08 17:58:59', b'0');
INSERT INTO `system_access_log` VALUES (2683, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', NULL, '2023-02-08 18:04:22', NULL, '2023-02-08 18:04:22', b'0');
INSERT INTO `system_access_log` VALUES (2684, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', 'admin', '2023-02-08 18:04:37', NULL, '2023-02-08 18:04:37', b'0');
INSERT INTO `system_access_log` VALUES (2685, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', 'admin', '2023-02-08 18:18:10', NULL, '2023-02-08 18:18:10', b'0');
INSERT INTO `system_access_log` VALUES (2686, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', NULL, '2023-02-08 18:38:58', NULL, '2023-02-08 18:38:58', b'0');
INSERT INTO `system_access_log` VALUES (2687, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', NULL, '2023-02-09 13:52:24', NULL, '2023-02-09 13:52:24', b'0');
INSERT INTO `system_access_log` VALUES (2688, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', NULL, '2023-02-09 14:02:43', NULL, '2023-02-09 14:02:43', b'0');
INSERT INTO `system_access_log` VALUES (2689, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', NULL, '2023-02-09 14:04:42', NULL, '2023-02-09 14:04:42', b'0');
INSERT INTO `system_access_log` VALUES (2690, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', NULL, '2023-02-09 14:05:57', NULL, '2023-02-09 14:05:57', b'0');
INSERT INTO `system_access_log` VALUES (2691, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', NULL, '2023-02-09 14:06:27', NULL, '2023-02-09 14:06:27', b'0');
INSERT INTO `system_access_log` VALUES (2692, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', NULL, '2023-02-09 14:06:52', NULL, '2023-02-09 14:06:52', b'0');
INSERT INTO `system_access_log` VALUES (2693, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', NULL, '2023-02-09 14:09:11', NULL, '2023-02-09 14:09:11', b'0');
INSERT INTO `system_access_log` VALUES (2694, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', NULL, '2023-02-09 14:11:12', NULL, '2023-02-09 14:11:12', b'0');
INSERT INTO `system_access_log` VALUES (2695, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', NULL, '2023-02-09 14:13:20', NULL, '2023-02-09 14:13:20', b'0');
INSERT INTO `system_access_log` VALUES (2696, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', NULL, '2023-02-09 14:41:55', NULL, '2023-02-09 14:41:55', b'0');
INSERT INTO `system_access_log` VALUES (2697, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', NULL, '2023-02-09 14:45:29', NULL, '2023-02-09 14:45:29', b'0');
INSERT INTO `system_access_log` VALUES (2698, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', NULL, '2023-02-09 14:51:05', NULL, '2023-02-09 14:51:05', b'0');
INSERT INTO `system_access_log` VALUES (2699, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', NULL, '2023-02-09 14:53:22', NULL, '2023-02-09 14:53:22', b'0');
INSERT INTO `system_access_log` VALUES (2700, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', NULL, '2023-02-09 14:55:00', NULL, '2023-02-09 14:55:00', b'0');
INSERT INTO `system_access_log` VALUES (2701, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', NULL, '2023-02-09 15:27:16', NULL, '2023-02-09 15:27:16', b'0');
INSERT INTO `system_access_log` VALUES (2702, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', NULL, '2023-02-09 15:36:25', NULL, '2023-02-09 15:36:25', b'0');
INSERT INTO `system_access_log` VALUES (2703, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', NULL, '2023-02-09 15:40:30', NULL, '2023-02-09 15:40:30', b'0');
INSERT INTO `system_access_log` VALUES (2704, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', NULL, '2023-02-09 16:03:01', NULL, '2023-02-09 16:03:01', b'0');
INSERT INTO `system_access_log` VALUES (2705, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', NULL, '2023-02-09 16:09:45', NULL, '2023-02-09 16:09:45', b'0');
INSERT INTO `system_access_log` VALUES (2706, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', NULL, '2023-02-09 16:14:30', NULL, '2023-02-09 16:14:30', b'0');
INSERT INTO `system_access_log` VALUES (2707, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', NULL, '2023-02-09 16:16:02', NULL, '2023-02-09 16:16:02', b'0');
INSERT INTO `system_access_log` VALUES (2708, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', NULL, '2023-02-09 16:18:38', NULL, '2023-02-09 16:18:38', b'0');
INSERT INTO `system_access_log` VALUES (2709, NULL, 2, 100, 30, 'admin', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', NULL, '2023-02-09 16:21:28', NULL, '2023-02-09 16:21:28', b'0');
INSERT INTO `system_access_log` VALUES (2710, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', NULL, '2023-02-09 16:21:33', NULL, '2023-02-09 16:21:33', b'0');
INSERT INTO `system_access_log` VALUES (2711, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', NULL, '2023-02-09 16:26:12', NULL, '2023-02-09 16:26:12', b'0');
INSERT INTO `system_access_log` VALUES (2712, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', NULL, '2023-02-09 16:28:43', NULL, '2023-02-09 16:28:43', b'0');
INSERT INTO `system_access_log` VALUES (2713, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36', NULL, '2023-02-10 16:51:24', NULL, '2023-02-10 16:51:24', b'0');
INSERT INTO `system_access_log` VALUES (2714, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.78', NULL, '2023-02-12 16:09:53', NULL, '2023-02-12 16:09:53', b'0');
INSERT INTO `system_access_log` VALUES (2715, NULL, 2, 100, 30, '18374756852', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36', NULL, '2023-02-13 08:49:09', NULL, '2023-02-13 08:49:09', b'0');
INSERT INTO `system_access_log` VALUES (2716, NULL, 2, 100, 10, '18374756852', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36', NULL, '2023-02-13 08:49:15', NULL, '2023-02-13 08:49:15', b'0');
INSERT INTO `system_access_log` VALUES (2717, 1, 2, 100, 0, '3029944576@qq.com', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36', NULL, '2023-02-13 08:49:30', NULL, '2023-02-13 08:49:30', b'0');
INSERT INTO `system_access_log` VALUES (2718, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '8.8.8.8', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.41', NULL, '2023-02-18 11:38:00', NULL, '2023-02-18 11:38:00', b'0');
INSERT INTO `system_access_log` VALUES (2719, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '8.8.8.8', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.41', NULL, '2023-02-18 14:25:36', NULL, '2023-02-18 14:25:36', b'0');
INSERT INTO `system_access_log` VALUES (2720, NULL, 2, 100, 30, 'admin', NULL, '8.8.8.8', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.41', NULL, '2023-02-18 14:26:18', NULL, '2023-02-18 14:26:18', b'0');
INSERT INTO `system_access_log` VALUES (2721, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '8.8.8.8', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.41', NULL, '2023-02-18 14:26:22', NULL, '2023-02-18 14:26:22', b'0');
INSERT INTO `system_access_log` VALUES (2722, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '8.8.8.8', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.41', NULL, '2023-02-18 15:03:29', NULL, '2023-02-18 15:03:29', b'0');
INSERT INTO `system_access_log` VALUES (2723, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '8.8.8.8', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.41', NULL, '2023-02-18 15:42:03', NULL, '2023-02-18 15:42:03', b'0');
INSERT INTO `system_access_log` VALUES (2724, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '8.8.8.8', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.41', NULL, '2023-02-18 16:16:41', NULL, '2023-02-18 16:16:41', b'0');
INSERT INTO `system_access_log` VALUES (2725, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '8.8.8.8', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.41', NULL, '2023-02-18 16:17:51', NULL, '2023-02-18 16:17:51', b'0');
INSERT INTO `system_access_log` VALUES (2726, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '8.8.8.8', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.41', NULL, '2023-02-18 16:18:52', NULL, '2023-02-18 16:18:52', b'0');
INSERT INTO `system_access_log` VALUES (2727, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '8.8.8.8', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.41', NULL, '2023-02-18 16:28:13', NULL, '2023-02-18 16:28:13', b'0');
INSERT INTO `system_access_log` VALUES (2728, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '8.8.8.8', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.50', NULL, '2023-02-22 21:03:29', NULL, '2023-02-22 21:03:29', b'0');
INSERT INTO `system_access_log` VALUES (2729, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '8.8.8.8', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.50', NULL, '2023-02-23 10:28:37', NULL, '2023-02-23 10:28:37', b'0');
INSERT INTO `system_access_log` VALUES (2730, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.50', NULL, '2023-02-23 15:37:18', NULL, '2023-02-23 15:37:18', b'0');
INSERT INTO `system_access_log` VALUES (2731, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36', NULL, '2023-02-23 15:40:17', NULL, '2023-02-23 15:40:17', b'0');
INSERT INTO `system_access_log` VALUES (2732, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Linux; U; Android 12; zh-cn; M2012K10C Build/SP1A.210812.016) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/100.0.4896.127 Mobile Safari/537.36 XiaoMi/MiuiBrowser/17.4.80113 swan-mibrowser', NULL, '2023-02-23 15:44:22', NULL, '2023-02-23 15:44:22', b'0');
INSERT INTO `system_access_log` VALUES (2733, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '8.8.8.8', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.50', NULL, '2023-02-23 16:39:05', NULL, '2023-02-23 16:39:05', b'0');
INSERT INTO `system_access_log` VALUES (2734, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '8.8.8.8', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.50', NULL, '2023-02-24 19:57:13', NULL, '2023-02-24 19:57:13', b'0');
INSERT INTO `system_access_log` VALUES (2735, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '8.8.8.8', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.50', NULL, '2023-02-24 19:58:20', NULL, '2023-02-24 19:58:20', b'0');
INSERT INTO `system_access_log` VALUES (2736, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '8.8.8.8', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.50', NULL, '2023-02-24 19:59:09', NULL, '2023-02-24 19:59:09', b'0');
INSERT INTO `system_access_log` VALUES (2737, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Linux; U; Android 12; zh-cn; M2012K10C Build/SP1A.210812.016) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/100.0.4896.127 Mobile Safari/537.36 XiaoMi/MiuiBrowser/17.4.240216 swan-mibrowser', NULL, '2023-02-25 16:57:54', NULL, '2023-02-25 16:57:54', b'0');
INSERT INTO `system_access_log` VALUES (2738, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36', NULL, '2023-02-25 17:06:36', NULL, '2023-02-25 17:06:36', b'0');
INSERT INTO `system_access_log` VALUES (2739, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36', NULL, '2023-02-26 14:21:29', NULL, '2023-02-26 14:21:29', b'0');
INSERT INTO `system_access_log` VALUES (2740, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36', NULL, '2023-02-26 14:21:50', NULL, '2023-02-26 14:21:50', b'0');
INSERT INTO `system_access_log` VALUES (2741, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36', NULL, '2023-02-26 14:22:18', NULL, '2023-02-26 14:22:18', b'0');
INSERT INTO `system_access_log` VALUES (2742, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '8.8.8.8', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.56', NULL, '2023-02-26 09:36:21', NULL, '2023-02-26 09:36:21', b'0');
INSERT INTO `system_access_log` VALUES (2743, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '8.8.8.8', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.56', NULL, '2023-02-26 11:31:41', NULL, '2023-02-26 11:31:41', b'0');
INSERT INTO `system_access_log` VALUES (2744, NULL, 2, 100, 30, 'admin', NULL, '8.8.8.8', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.57', NULL, '2023-02-28 17:43:32', NULL, '2023-02-28 17:43:32', b'0');
INSERT INTO `system_access_log` VALUES (2745, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '8.8.8.8', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.57', NULL, '2023-02-28 17:43:35', NULL, '2023-02-28 17:43:35', b'0');
INSERT INTO `system_access_log` VALUES (2746, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '8.8.8.8', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.57', NULL, '2023-03-01 17:17:23', NULL, '2023-03-01 17:17:23', b'0');
INSERT INTO `system_access_log` VALUES (2747, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '8.8.8.8', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.57', NULL, '2023-03-01 20:32:06', NULL, '2023-03-01 20:32:06', b'0');
INSERT INTO `system_access_log` VALUES (2748, 1, 2, 100, 0, 'admin', '洋芋_sheepyu', '8.8.8.8', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.57', NULL, '2023-03-03 19:07:56', NULL, '2023-03-03 19:07:56', b'0');

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
  `request_method` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '请求方法名',
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
) ENGINE = InnoDB AUTO_INCREMENT = 1365 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统异常日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_api_log
-- ----------------------------
INSERT INTO `system_api_log` VALUES (1301, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":3,\"name\":\"新增菜单\",\"permission\":\"system:menu:create\",\"type\":3,\"sort\":0,\"parentId\":2,\"path\":\"\",\"icon\":\"\",\"component\":\"\",\"status\":1,\"keepAlive\":1}}', '8.8.8.8', 55, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-24 09:28:17', NULL, '2023-02-24 09:28:17', b'0');
INSERT INTO `system_api_log` VALUES (1302, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":3,\"name\":\"新增菜单\",\"permission\":\"system:menu:create\",\"type\":3,\"sort\":0,\"parentId\":2,\"path\":\"\",\"icon\":\"\",\"component\":\"\",\"status\":1,\"keepAlive\":0}}', '8.8.8.8', 27, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-24 09:34:27', NULL, '2023-02-24 09:34:27', b'0');
INSERT INTO `system_api_log` VALUES (1303, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":3,\"name\":\"新增菜单\",\"permission\":\"system:menu:create\",\"type\":3,\"sort\":0,\"parentId\":2,\"path\":\"\",\"icon\":\"\",\"component\":\"\",\"status\":1,\"keepAlive\":1}}', '8.8.8.8', 20, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-24 09:36:41', NULL, '2023-02-24 09:36:41', b'0');
INSERT INTO `system_api_log` VALUES (1304, 1, 2, '删除系统菜单', 4, 'DELETE', '/admin-api/system/permission/menu/3,4,5', '{\"ids\":\"3,4,5\"}', '8.8.8.8', 75, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-24 18:14:23', NULL, '2023-02-24 18:14:23', b'0');
INSERT INTO `system_api_log` VALUES (1305, 1, 2, '删除系统菜单', 4, 'DELETE', '/admin-api/system/permission/menu/2', '{\"ids\":\"2\"}', '8.8.8.8', 17, 0, '', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-24 19:40:03', NULL, '2023-02-24 19:40:03', b'0');
INSERT INTO `system_api_log` VALUES (1306, 1, 2, '删除系统菜单', 4, 'DELETE', '/admin-api/system/permission/menu/2', '{\"ids\":\"2\"}', '8.8.8.8', 13, 0, '', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-24 19:42:08', NULL, '2023-02-24 19:42:08', b'0');
INSERT INTO `system_api_log` VALUES (1307, 1, 2, '删除系统菜单', 4, 'DELETE', '/admin-api/system/permission/menu/1', '{\"ids\":\"1\"}', '8.8.8.8', 14, 0, '', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-24 19:43:49', NULL, '2023-02-24 19:43:49', b'0');
INSERT INTO `system_api_log` VALUES (1308, 1, 2, '删除系统菜单', 4, 'DELETE', '/admin-api/system/permission/menu/1,2,3,4,5', '{\"ids\":\"1,2,3,4,5\"}', '8.8.8.8', 14, 0, '', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-24 19:45:34', NULL, '2023-02-24 19:45:34', b'0');
INSERT INTO `system_api_log` VALUES (1309, 1, 2, '删除系统菜单', 4, 'DELETE', '/admin-api/system/permission/menu/2', '{\"ids\":\"2\"}', '8.8.8.8', 12, 0, '', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-24 19:53:24', NULL, '2023-02-24 19:53:24', b'0');
INSERT INTO `system_api_log` VALUES (1310, 1, 2, '删除系统菜单', 4, 'DELETE', '/admin-api/system/permission/menu/1', '{\"ids\":\"1\"}', '8.8.8.8', 14, 0, '', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-24 19:53:27', NULL, '2023-02-24 19:53:27', b'0');
INSERT INTO `system_api_log` VALUES (1311, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":3,\"name\":\"新增菜单\",\"permission\":\"system:menu:create\",\"type\":3,\"sort\":0,\"parentId\":2,\"path\":\"\",\"icon\":\"\",\"component\":\"\",\"status\":1,\"keepAlive\":0}}', '8.8.8.8', 43, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-24 19:53:53', NULL, '2023-02-24 19:53:53', b'0');
INSERT INTO `system_api_log` VALUES (1312, 1, 2, '修改字典数据', 3, 'PUT', '/admin-api/system/dict/data', '{\"updateVo\":{\"id\":9,\"sort\":0,\"label\":\"菜单\",\"status\":1,\"colorType\":\"\",\"remark\":\"菜单项\"}}', '8.8.8.8', 54, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-24 19:56:43', NULL, '2023-02-24 19:56:43', b'0');
INSERT INTO `system_api_log` VALUES (1313, 1, 2, '删除系统菜单', 4, 'DELETE', '/admin-api/system/permission/menu/2', '{\"ids\":\"2\"}', '8.8.8.8', 12, 0, '', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-24 19:59:51', NULL, '2023-02-24 19:59:51', b'0');
INSERT INTO `system_api_log` VALUES (1314, 1, 2, '删除系统菜单', 4, 'DELETE', '/admin-api/system/permission/menu/5', '{\"ids\":\"5\"}', '8.8.8.8', 35, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-24 20:00:16', NULL, '2023-02-24 20:00:16', b'0');
INSERT INTO `system_api_log` VALUES (1315, 1, 2, '添加系统菜单', 2, 'POST', '/admin-api/system/permission/menu', '{\"createVo\":{\"name\":\"代码生成\",\"permission\":\"system:codegen:query\",\"type\":2,\"sort\":-1,\"parentId\":1,\"path\":\"codegen\",\"icon\":\"fa fa-copy\",\"component\":\"system/codegen/index\",\"status\":1,\"keepAlive\":1}}', '8.8.8.8', 49, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-26 09:56:23', NULL, '2023-02-26 09:56:23', b'0');
INSERT INTO `system_api_log` VALUES (1316, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":7,\"name\":\"代码生成\",\"permission\":\"system:codegen:query\",\"type\":2,\"sort\":0,\"parentId\":1,\"path\":\"codegen\",\"icon\":\"fa fa-copy\",\"component\":\"system/codegen/index\",\"status\":0,\"keepAlive\":1}}', '8.8.8.8', 28, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-26 10:20:40', NULL, '2023-02-26 10:20:40', b'0');
INSERT INTO `system_api_log` VALUES (1317, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":7,\"name\":\"代码生成\",\"permission\":\"system:codegen:query\",\"type\":2,\"sort\":0,\"parentId\":1,\"path\":\"codegen\",\"icon\":\"fa fa-copy\",\"component\":\"system/codegen/index\",\"status\":1,\"keepAlive\":1}}', '8.8.8.8', 25, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-26 10:26:12', NULL, '2023-02-26 10:26:12', b'0');
INSERT INTO `system_api_log` VALUES (1318, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":7,\"name\":\"代码生成\",\"permission\":\"system:codegen:query\",\"type\":2,\"sort\":0,\"parentId\":1,\"path\":\"codegen\",\"icon\":\"fa fa-copy\",\"component\":\"system/codegen/index\",\"status\":0,\"keepAlive\":1}}', '8.8.8.8', 27, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-26 10:29:43', NULL, '2023-02-26 10:29:43', b'0');
INSERT INTO `system_api_log` VALUES (1319, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":7,\"name\":\"代码生成\",\"permission\":\"system:codegen:query\",\"type\":2,\"sort\":0,\"parentId\":1,\"path\":\"codegen\",\"icon\":\"fa fa-copy\",\"component\":\"system/codegen/index\",\"status\":1,\"keepAlive\":1}}', '8.8.8.8', 21, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-26 10:29:48', NULL, '2023-02-26 10:29:48', b'0');
INSERT INTO `system_api_log` VALUES (1320, 1, 2, '添加系统菜单', 2, 'POST', '/admin-api/system/permission/menu', '{\"createVo\":{\"name\":\"菜单查询\",\"permission\":\"system:menu:query\",\"type\":3,\"sort\":0,\"parentId\":2,\"status\":1,\"keepAlive\":1}}', '8.8.8.8', 18, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-26 11:00:30', NULL, '2023-02-26 11:00:30', b'0');
INSERT INTO `system_api_log` VALUES (1321, 1, 2, '添加系统菜单', 2, 'POST', '/admin-api/system/permission/menu', '{\"createVo\":{\"name\":\"新增代码模板\",\"permission\":\"system:codegen:create\",\"type\":3,\"sort\":0,\"parentId\":7,\"status\":1,\"keepAlive\":1}}', '8.8.8.8', 18, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-26 11:01:15', NULL, '2023-02-26 11:01:15', b'0');
INSERT INTO `system_api_log` VALUES (1322, 1, 2, '添加系统菜单', 2, 'POST', '/admin-api/system/permission/menu', '{\"createVo\":{\"name\":\"修改代码模板\",\"permission\":\"system:codegen:update\",\"type\":3,\"sort\":0,\"parentId\":7,\"status\":1,\"keepAlive\":1}}', '8.8.8.8', 20, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-26 11:02:04', NULL, '2023-02-26 11:02:04', b'0');
INSERT INTO `system_api_log` VALUES (1323, 1, 2, '添加系统菜单', 2, 'POST', '/admin-api/system/permission/menu', '{\"createVo\":{\"name\":\"删除代码模板\",\"permission\":\"system:codegen:delete\",\"type\":3,\"sort\":0,\"parentId\":7,\"status\":1,\"keepAlive\":1}}', '8.8.8.8', 22, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-26 11:02:31', NULL, '2023-02-26 11:02:31', b'0');
INSERT INTO `system_api_log` VALUES (1324, 1, 2, '删除系统菜单', 4, 'DELETE', '/admin-api/system/permission/menu/8', '{\"ids\":\"8\"}', '8.8.8.8', 61, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-26 11:02:38', NULL, '2023-02-26 11:02:38', b'0');
INSERT INTO `system_api_log` VALUES (1325, 1, 2, '添加系统菜单', 2, 'POST', '/admin-api/system/permission/menu', '{\"createVo\":{\"name\":\"控制台\",\"permission\":\"dashboard\",\"type\":2,\"sort\":-1,\"parentId\":0,\"path\":\"dashboard\",\"icon\":\"fa fa-dashboard\",\"component\":\"dashboard\",\"status\":1,\"keepAlive\":1}}', '8.8.8.8', 16, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-26 11:17:04', NULL, '2023-02-26 11:17:04', b'0');
INSERT INTO `system_api_log` VALUES (1326, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":9,\"name\":\"新增代码模板\",\"permission\":\"system:codegen:create\",\"type\":3,\"sort\":0,\"parentId\":7,\"path\":\"\",\"icon\":\"#\",\"status\":1,\"keepAlive\":0}}', '8.8.8.8', 43, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-26 11:59:49', NULL, '2023-02-26 11:59:49', b'0');
INSERT INTO `system_api_log` VALUES (1327, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":10,\"name\":\"修改代码模板\",\"permission\":\"system:codegen:update\",\"type\":3,\"sort\":0,\"parentId\":7,\"path\":\"\",\"icon\":\"#\",\"status\":1,\"keepAlive\":0}}', '8.8.8.8', 26, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-26 11:59:49', NULL, '2023-02-26 11:59:49', b'0');
INSERT INTO `system_api_log` VALUES (1328, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":11,\"name\":\"删除代码模板\",\"permission\":\"system:codegen:delete\",\"type\":3,\"sort\":0,\"parentId\":7,\"path\":\"\",\"icon\":\"#\",\"status\":1,\"keepAlive\":0}}', '8.8.8.8', 24, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-26 11:59:49', NULL, '2023-02-26 11:59:49', b'0');
INSERT INTO `system_api_log` VALUES (1329, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":12,\"name\":\"控制台\",\"permission\":\"dashboard\",\"type\":2,\"sort\":-1,\"parentId\":0,\"path\":\"dashboard\",\"icon\":\"fa fa-dashboard\",\"component\":\"dashboard\",\"status\":1,\"keepAlive\":0}}', '8.8.8.8', 22, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-26 14:21:00', NULL, '2023-02-26 14:21:00', b'0');
INSERT INTO `system_api_log` VALUES (1330, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":12,\"name\":\"控制台\",\"permission\":\"dashboard\",\"type\":2,\"sort\":-1,\"parentId\":0,\"path\":\"dashboard\",\"icon\":\"fa fa-dashboard\",\"component\":\"dashboard\",\"status\":1,\"keepAlive\":1}}', '8.8.8.8', 20, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-26 14:21:20', NULL, '2023-02-26 14:21:20', b'0');
INSERT INTO `system_api_log` VALUES (1331, 1, 2, '删除系统菜单', 4, 'DELETE', '/admin-api/system/permission/menu/1', '{\"ids\":\"1\"}', '8.8.8.8', 28, 0, '', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-26 14:42:17', NULL, '2023-02-26 14:42:17', b'0');
INSERT INTO `system_api_log` VALUES (1332, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":9,\"name\":\"新增代码模板1\",\"permission\":\"system:codegen:create\",\"type\":3,\"sort\":0,\"parentId\":7,\"path\":\"\",\"icon\":\"#\",\"component\":\"\",\"status\":1,\"keepAlive\":0}}', '8.8.8.8', 22, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-26 14:48:21', NULL, '2023-02-26 14:48:21', b'0');
INSERT INTO `system_api_log` VALUES (1333, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":9,\"name\":\"新增代码模板\",\"permission\":\"system:codegen:create\",\"type\":3,\"sort\":0,\"parentId\":7,\"path\":\"\",\"icon\":\"#\",\"component\":\"\",\"status\":1,\"keepAlive\":0}}', '8.8.8.8', 20, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-26 14:51:11', NULL, '2023-02-26 14:51:11', b'0');
INSERT INTO `system_api_log` VALUES (1334, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":9,\"name\":\"新增代码模板1\",\"permission\":\"system:codegen:create\",\"type\":3,\"sort\":0,\"parentId\":7,\"path\":\"\",\"icon\":\"#\",\"component\":\"\",\"status\":1,\"keepAlive\":0}}', '8.8.8.8', 24, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-26 14:53:21', NULL, '2023-02-26 14:53:21', b'0');
INSERT INTO `system_api_log` VALUES (1335, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":9,\"name\":\"新增代码模板\",\"permission\":\"system:codegen:create\",\"type\":3,\"sort\":0,\"parentId\":7,\"path\":\"\",\"icon\":\"#\",\"component\":\"\",\"status\":1,\"keepAlive\":0}}', '8.8.8.8', 21, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-26 14:54:09', NULL, '2023-02-26 14:54:09', b'0');
INSERT INTO `system_api_log` VALUES (1336, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":10,\"name\":\"修改代码模板1\",\"permission\":\"system:codegen:update\",\"type\":3,\"sort\":0,\"parentId\":7,\"path\":\"\",\"icon\":\"#\",\"component\":\"\",\"status\":1,\"keepAlive\":0}}', '8.8.8.8', 21, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-26 14:54:16', NULL, '2023-02-26 14:54:16', b'0');
INSERT INTO `system_api_log` VALUES (1337, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":11,\"name\":\"删除代码模板2\",\"permission\":\"system:codegen:delete\",\"type\":3,\"sort\":0,\"parentId\":7,\"path\":\"\",\"icon\":\"#\",\"component\":\"\",\"status\":1,\"keepAlive\":0}}', '8.8.8.8', 21, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-26 14:54:20', NULL, '2023-02-26 14:54:20', b'0');
INSERT INTO `system_api_log` VALUES (1338, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":10,\"name\":\"修改代码模板\",\"permission\":\"system:codegen:update\",\"type\":3,\"sort\":0,\"parentId\":7,\"path\":\"\",\"icon\":\"#\",\"component\":\"\",\"status\":1,\"keepAlive\":0}}', '8.8.8.8', 21, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-26 14:55:08', NULL, '2023-02-26 14:55:08', b'0');
INSERT INTO `system_api_log` VALUES (1339, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":11,\"name\":\"删除代码模板\",\"permission\":\"system:codegen:delete\",\"type\":3,\"sort\":0,\"parentId\":7,\"path\":\"\",\"icon\":\"#\",\"component\":\"\",\"status\":1,\"keepAlive\":0}}', '8.8.8.8', 21, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-26 14:55:11', NULL, '2023-02-26 14:55:11', b'0');
INSERT INTO `system_api_log` VALUES (1340, 1, 2, '删除系统菜单', 4, 'DELETE', '/admin-api/system/permission/menu/1', '{\"ids\":\"1\"}', '8.8.8.8', 28, 0, '', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-28 17:44:12', NULL, '2023-02-28 17:44:12', b'0');
INSERT INTO `system_api_log` VALUES (1341, 1, 2, '删除系统菜单', 4, 'DELETE', '/admin-api/system/permission/menu/1', '{\"ids\":\"1\"}', '8.8.8.8', 13, 0, '', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-28 17:44:23', NULL, '2023-02-28 17:44:23', b'0');
INSERT INTO `system_api_log` VALUES (1342, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":7,\"name\":\"代码生成\",\"permission\":\"system:codegen:query\",\"type\":2,\"sort\":0,\"parentId\":1,\"path\":\"codegen\",\"icon\":\"fa fa-copy\",\"component\":\"system/codegen/index\",\"status\":1,\"keepAlive\":1}}', '8.8.8.8', 35, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-28 18:28:25', NULL, '2023-02-28 18:28:25', b'0');
INSERT INTO `system_api_log` VALUES (1343, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":7,\"name\":\"代码生成\",\"permission\":\"system:codegen:query\",\"type\":2,\"sort\":0,\"parentId\":1,\"path\":\"codegen\",\"icon\":\"fa fa-copy\",\"component\":\"system/codegen/index\",\"status\":1,\"keepAlive\":0}}', '8.8.8.8', 27, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-28 18:28:26', NULL, '2023-02-28 18:28:26', b'0');
INSERT INTO `system_api_log` VALUES (1344, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":7,\"name\":\"代码生成\",\"permission\":\"system:codegen:query\",\"type\":2,\"sort\":0,\"parentId\":1,\"path\":\"codegen\",\"icon\":\"fa fa-copy\",\"component\":\"system/codegen/index\",\"status\":1,\"keepAlive\":1}}', '8.8.8.8', 24, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-28 18:28:52', NULL, '2023-02-28 18:28:52', b'0');
INSERT INTO `system_api_log` VALUES (1345, 1, 2, '添加系统菜单', 2, 'POST', '/admin-api/system/permission/menu', '{\"createVo\":{\"name\":\"demo\",\"type\":1,\"sort\":0,\"parentId\":7,\"path\":\"xxx\",\"status\":1,\"keepAlive\":1}}', '8.8.8.8', 25, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-28 18:30:24', NULL, '2023-02-28 18:30:24', b'0');
INSERT INTO `system_api_log` VALUES (1346, 1, 2, '删除系统菜单', 4, 'DELETE', '/admin-api/system/permission/menu/13', '{\"ids\":\"13\"}', '8.8.8.8', 43, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-28 18:30:38', NULL, '2023-02-28 18:30:38', b'0');
INSERT INTO `system_api_log` VALUES (1347, 1, 2, '添加系统菜单', 2, 'POST', '/admin-api/system/permission/menu', '{\"createVo\":{\"name\":\"xxx\",\"type\":1,\"sort\":0,\"parentId\":7,\"path\":\"xxx\",\"status\":1,\"keepAlive\":1}}', '8.8.8.8', 28, 0, '', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-28 19:06:55', NULL, '2023-02-28 19:06:55', b'0');
INSERT INTO `system_api_log` VALUES (1348, 1, 2, '添加系统菜单', 2, 'POST', '/admin-api/system/permission/menu', '{\"createVo\":{\"name\":\"xx\",\"permission\":\"xx\",\"type\":3,\"sort\":0,\"parentId\":1,\"status\":1,\"keepAlive\":1}}', '8.8.8.8', 9, 0, '', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-28 19:08:11', NULL, '2023-02-28 19:08:11', b'0');
INSERT INTO `system_api_log` VALUES (1349, 1, 2, '添加系统菜单', 2, 'POST', '/admin-api/system/permission/menu', '{\"createVo\":{\"name\":\"xx\",\"permission\":\"xx\",\"type\":3,\"sort\":0,\"parentId\":1,\"status\":1,\"keepAlive\":1}}', '8.8.8.8', 5, 0, '', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-28 19:09:52', NULL, '2023-02-28 19:09:52', b'0');
INSERT INTO `system_api_log` VALUES (1350, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":3,\"name\":\"新增菜单\",\"permission\":\"system:menu:create\",\"type\":3,\"sort\":0,\"parentId\":2,\"path\":\"\",\"icon\":\"\",\"component\":\"\",\"status\":0,\"keepAlive\":0}}', '8.8.8.8', 29, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-28 19:10:10', NULL, '2023-02-28 19:10:10', b'0');
INSERT INTO `system_api_log` VALUES (1351, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":3,\"name\":\"新增菜单\",\"permission\":\"system:menu:create\",\"type\":3,\"sort\":0,\"parentId\":2,\"path\":\"\",\"icon\":\"\",\"component\":\"\",\"status\":1,\"keepAlive\":0}}', '8.8.8.8', 23, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-02-28 19:10:12', NULL, '2023-02-28 19:10:12', b'0');
INSERT INTO `system_api_log` VALUES (1352, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":1,\"name\":\"系统管理\",\"permission\":\"\",\"type\":1,\"sort\":0,\"parentId\":0,\"path\":\"system\",\"icon\":\"fa fa-windows\",\"component\":\"\",\"status\":1,\"keepAlive\":0}}', '8.8.8.8', 45, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-03-01 21:17:24', NULL, '2023-03-01 21:17:24', b'0');
INSERT INTO `system_api_log` VALUES (1353, 1, 2, '删除系统菜单', 4, 'DELETE', '/admin-api/system/permission/menu/1', '{\"ids\":\"1\"}', '8.8.8.8', 51, 0, '', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-03-01 22:39:15', NULL, '2023-03-01 22:39:15', b'0');
INSERT INTO `system_api_log` VALUES (1354, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":1,\"name\":\"系统管理1\",\"permission\":\"\",\"type\":1,\"sort\":0,\"parentId\":0,\"path\":\"system\",\"icon\":\"fa fa-windows\",\"component\":\"\",\"status\":1,\"keepAlive\":0}}', '8.8.8.8', 16, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-03-01 22:43:18', NULL, '2023-03-01 22:43:18', b'0');
INSERT INTO `system_api_log` VALUES (1355, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":1,\"name\":\"系统管理\",\"permission\":\"\",\"type\":1,\"sort\":0,\"parentId\":0,\"path\":\"system\",\"icon\":\"fa fa-windows\",\"component\":\"\",\"status\":1,\"keepAlive\":0}}', '8.8.8.8', 17, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-03-01 22:43:33', NULL, '2023-03-01 22:43:33', b'0');
INSERT INTO `system_api_log` VALUES (1356, 1, 2, '添加系统菜单', 2, 'POST', '/admin-api/system/permission/menu', '{\"createVo\":{\"name\":\"用户管理\",\"permission\":\"system:user:query\",\"type\":2,\"sort\":0,\"parentId\":1,\"path\":\"user\",\"icon\":\"\",\"component\":\"system/user/index\",\"status\":1,\"keepAlive\":1}}', '8.8.8.8', 38, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-03-03 21:28:22', NULL, '2023-03-03 21:28:22', b'0');
INSERT INTO `system_api_log` VALUES (1357, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":2,\"name\":\"菜单管理\",\"permission\":\"system:menu:query\",\"type\":2,\"sort\":0,\"parentId\":1,\"path\":\"menu\",\"icon\":\"el-icon-Menu\",\"component\":\"system/menu/index\",\"status\":1,\"keepAlive\":1}}', '8.8.8.8', 23, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-03-04 10:06:32', NULL, '2023-03-04 10:06:32', b'0');
INSERT INTO `system_api_log` VALUES (1358, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":3,\"name\":\"新增菜单\",\"permission\":\"system:menu:create\",\"type\":3,\"sort\":0,\"parentId\":2,\"path\":\"\",\"icon\":\"\",\"component\":\"\",\"status\":1,\"keepAlive\":0}}', '8.8.8.8', 14, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-03-04 10:06:36', NULL, '2023-03-04 10:06:36', b'0');
INSERT INTO `system_api_log` VALUES (1359, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":4,\"name\":\"删除菜单\",\"permission\":\"system:menu:delete\",\"type\":3,\"sort\":0,\"parentId\":2,\"path\":\"\",\"icon\":\"\",\"component\":\"\",\"status\":1,\"keepAlive\":0}}', '8.8.8.8', 15, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-03-04 10:06:40', NULL, '2023-03-04 10:06:40', b'0');
INSERT INTO `system_api_log` VALUES (1360, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":1,\"name\":\"系统管理\",\"permission\":\"\",\"type\":1,\"sort\":0,\"parentId\":0,\"path\":\"system\",\"icon\":\"fa fa-windows\",\"component\":\"\",\"status\":1,\"keepAlive\":0}}', '8.8.8.8', 16, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-03-04 10:08:55', NULL, '2023-03-04 10:08:55', b'0');
INSERT INTO `system_api_log` VALUES (1361, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":2,\"name\":\"菜单管理\",\"permission\":\"system:menu:query\",\"type\":2,\"sort\":0,\"parentId\":1,\"path\":\"menu\",\"icon\":\"el-icon-Menu\",\"component\":\"system/menu/index\",\"status\":1,\"keepAlive\":1}}', '8.8.8.8', 15, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-03-04 10:09:04', NULL, '2023-03-04 10:09:04', b'0');
INSERT INTO `system_api_log` VALUES (1362, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":2,\"name\":\"菜单管理\",\"permission\":\"system:menu:query\",\"type\":2,\"sort\":0,\"parentId\":1,\"path\":\"menu\",\"icon\":\"el-icon-Menu\",\"component\":\"system/menu/index\",\"status\":1,\"keepAlive\":1}}', '8.8.8.8', 15, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-03-04 10:09:10', NULL, '2023-03-04 10:09:10', b'0');
INSERT INTO `system_api_log` VALUES (1363, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":3,\"name\":\"新增菜单\",\"permission\":\"system:menu:create\",\"type\":3,\"sort\":0,\"parentId\":2,\"path\":\"\",\"icon\":\"\",\"component\":\"\",\"status\":1,\"keepAlive\":0}}', '8.8.8.8', 17, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-03-04 10:09:12', NULL, '2023-03-04 10:09:12', b'0');
INSERT INTO `system_api_log` VALUES (1364, 1, 2, '修改系统菜单', 3, 'PUT', '/admin-api/system/permission/menu', '{\"updateVo\":{\"id\":4,\"name\":\"删除菜单\",\"permission\":\"system:menu:delete\",\"type\":3,\"sort\":0,\"parentId\":2,\"path\":\"\",\"icon\":\"\",\"component\":\"\",\"status\":1,\"keepAlive\":0}}', '8.8.8.8', 16, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-03-04 10:09:16', NULL, '2023-03-04 10:09:16', b'0');
INSERT INTO `system_api_log` VALUES (1365, 1, 2, '创建代码生成', 2, 'POST', '/admin-api/system/codegen', '{\"tableNames\":[\"system_user\"]}', '8.8.8.8', 210, 0, '', 0, '2023-03-04 17:22:28', 'org.springframework.jdbc.UncategorizedSQLException', 'SQLException: Incorrect integer value: \'=\' for column \'query_condition\' at row 1', 'org.springframework.jdbc.UncategorizedSQLException: top.sheepyu.module.system.dao.codegen.SystemCodegenColumnMapper.insert (batch index #1) failed. Cause: java.sql.BatchUpdateException: Incorrect integer value: \'=\' for column \'query_condition\' at row 1\n; uncategorized SQLException; SQL state [HY000]; error code [1366]; Incorrect integer value: \'=\' for column \'query_condition\' at row 1; nested exception is java.sql.BatchUpdateException: Incorrect integer value: \'=\' for column \'query_condition\' at row 1\r\n	at org.mybatis.spring.MyBatisExceptionTranslator.translateExceptionIfPossible(MyBatisExceptionTranslator.java:92)\r\n	at com.baomidou.mybatisplus.extension.toolkit.SqlHelper.executeBatch(SqlHelper.java:192)\r\n	at com.baomidou.mybatisplus.extension.toolkit.SqlHelper.executeBatch(SqlHelper.java:217)\r\n	at com.baomidou.mybatisplus.extension.service.impl.ServiceImpl.executeBatch(ServiceImpl.java:240)\r\n	at com.baomidou.mybatisplus.extension.service.impl.ServiceImpl.saveBatch(ServiceImpl.java:136)\r\n	at com.baomidou.mybatisplus.extension.service.IService.saveBatch(IService.java:73)\r\n	at com.baomidou.mybatisplus.extension.service.IService$$FastClassBySpringCGLIB$$f8525d18.invoke(<generated>)\r\n	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)\r\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:793)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)\r\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:763)\r\n	at org.springframework.validation.beanvalidation.MethodValidationInterceptor.invoke(MethodValidationInterceptor.java:123)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\r\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:763)\r\n	at org.springframework.transaction.interceptor.TransactionInterceptor$1.proceedWithInvocation(TransactionInterceptor.java:123)\r\n	at org.springframework.transaction.interceptor.TransactionAspectSupport.invokeWithinTransaction(TransactionAspectSupport.java:388)\r\n	at org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:119)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\r\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:763)\r\n	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:708)\r\n	at top.sheepyu.module.system.service.codegen.SystemCodegenColumnServiceImpl$$EnhancerBySpringCGLIB$$78af0294.saveBatch(<generated>)\r\n	at top.sheepyu.module.system.service.codegen.SystemCodegenBiz.lambda$createCodegen$0(SystemCodegenBiz.java:96)\r\n	at java.util.ArrayList.forEach(ArrayList.java:1259)\r\n	at top.sheepyu.module.system.service.codegen.SystemCodegenBiz.createCodegen(SystemCodegenBiz.java:68)\r\n	at top.sheepyu.module.system.service.codegen.SystemCodegenBiz$$FastClassBySpringCGLIB$$c2fd9d10.invoke(<generated>)\r\n	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)\r\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:793)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)\r\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:763)\r\n	at org.springframework.validation.beanvalidation.MethodValidationInterceptor.invoke(MethodValidationInterceptor.java:123)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\r\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:763)\r\n	at org.springframework.transaction.interceptor.TransactionInterceptor$1.proceedWithInvocation(TransactionInterceptor.java:123)\r\n	at org.springframework.transaction.interceptor.TransactionAspectSupport.invokeWithinTransaction(TransactionAspectSupport.java:388)\r\n	at org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:119)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\r\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:763)\r\n	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:708)\r\n	at top.sheepyu.module.system.service.codegen.SystemCodegenBiz$$EnhancerBySpringCGLIB$$e5f9bad0.createCodegen(<generated>)\r\n	at top.sheepyu.module.system.controller.admin.codegen.SystemCodegenController.createCodegen(SystemCodegenController.java:39)\r\n	at top.sheepyu.module.system.controller.admin.codegen.SystemCodegenController$$FastClassBySpringCGLIB$$687dcfc1.invoke(<generated>)\r\n	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)\r\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:793)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)\r\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:763)\r\n	at org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint.proceed(MethodInvocationProceedingJoinPoint.java:89)\r\n	at top.sheepyu.framework.log.core.aop.RecordLogAspect.processAround(RecordLogAspect.java:76)\r\n	at top.sheepyu.framework.log.core.aop.RecordLogAspect.around(RecordLogAspect.java:55)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethodWithGivenArgs(AbstractAspectJAdvice.java:634)\r\n	at org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethod(AbstractAspectJAdvice.java:624)\r\n	at org.springframework.aop.aspectj.AspectJAroundAdvice.invoke(AspectJAroundAdvice.java:72)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:175)\r\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:763)\r\n	at org.springframework.security.access.intercept.aopalliance.MethodSecurityInterceptor.invoke(MethodSecurityInterceptor.java:61)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\r\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:763)\r\n	at org.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:97)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\r\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:763)\r\n	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:708)\r\n	at top.sheepyu.module.system.controller.admin.codegen.SystemCodegenController$$EnhancerBySpringCGLIB$$f3b6b166.createCodegen(<generated>)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:205)\r\n	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:150)\r\n	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:117)\r\n	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:895)\r\n	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:808)\r\n	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)\r\n	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1070)\r\n	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:963)\r\n	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006)\r\n	at org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:909)\r\n	at javax.servlet.http.HttpServlet.service(HttpServlet.java:681)\r\n	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883)\r\n	at javax.servlet.http.HttpServlet.service(HttpServlet.java:764)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:227)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\r\n	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:111)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:327)\r\n	at org.springframework.security.web.access.intercept.FilterSecurityInterceptor.invoke(FilterSecurityInterceptor.java:115)\r\n	at org.springframework.security.web.access.intercept.FilterSecurityInterceptor.doFilter(FilterSecurityInterceptor.java:81)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.security.web.access.ExceptionTranslationFilter.doFilter(ExceptionTranslationFilter.java:122)\r\n	at org.springframework.security.web.access.ExceptionTranslationFilter.doFilter(ExceptionTranslationFilter.java:116)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.security.web.session.SessionManagementFilter.doFilter(SessionManagementFilter.java:126)\r\n	at org.springframework.security.web.session.SessionManagementFilter.doFilter(SessionManagementFilter.java:81)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.security.web.authentication.AnonymousAuthenticationFilter.doFilter(AnonymousAuthenticationFilter.java:109)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter.doFilter(SecurityContextHolderAwareRequestFilter.java:149)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.security.web.savedrequest.RequestCacheAwareFilter.doFilter(RequestCacheAwareFilter.java:63)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at top.sheepyu.framework.security.core.filter.SecurityTokenFilter.doFilterInternal(SecurityTokenFilter.java:46)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.security.web.authentication.logout.LogoutFilter.doFilter(LogoutFilter.java:103)\r\n	at org.springframework.security.web.authentication.logout.LogoutFilter.doFilter(LogoutFilter.java:89)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.web.filter.CorsFilter.doFilterInternal(CorsFilter.java:91)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.security.web.header.HeaderWriterFilter.doHeadersAfter(HeaderWriterFilter.java:90)\r\n	at org.springframework.security.web.header.HeaderWriterFilter.doFilterInternal(HeaderWriterFilter.java:75)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.security.web.context.SecurityContextPersistenceFilter.doFilter(SecurityContextPersistenceFilter.java:110)\r\n	at org.springframework.security.web.context.SecurityContextPersistenceFilter.doFilter(SecurityContextPersistenceFilter.java:80)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter.doFilterInternal(WebAsyncManagerIntegrationFilter.java:55)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.security.web.FilterChainProxy.doFilterInternal(FilterChainProxy.java:211)\r\n	at org.springframework.security.web.FilterChainProxy.doFilter(FilterChainProxy.java:183)\r\n	at org.springframework.web.filter.DelegatingFilterProxy.invokeDelegate(DelegatingFilterProxy.java:354)\r\n	at org.springframework.web.filter.DelegatingFilterProxy.doFilter(DelegatingFilterProxy.java:267)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\r\n	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\r\n	at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\r\n	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\r\n	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:197)\r\n	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:97)\r\n	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:541)\r\n	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:135)\r\n	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92)\r\n	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:78)\r\n	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:360)\r\n	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:399)\r\n	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65)\r\n	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:890)\r\n	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1789)\r\n	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)\r\n	at org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191)\r\n	at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659)\r\n	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)\r\n	at java.lang.Thread.run(Thread.java:750)\r\nCaused by: java.sql.BatchUpdateException: Incorrect integer value: \'=\' for column \'query_condition\' at row 1\r\n	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)\r\n	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)\r\n	at java.lang.reflect.Constructor.newInstance(Constructor.java:423)\r\n	at com.mysql.cj.util.Util.handleNewInstance(Util.java:192)\r\n	at com.mysql.cj.util.Util.getInstance(Util.java:167)\r\n	at com.mysql.cj.util.Util.getInstance(Util.java:174)\r\n	at com.mysql.cj.jdbc.exceptions.SQLError.createBatchUpdateException(SQLError.java:224)\r\n	at com.mysql.cj.jdbc.ClientPreparedStatement.executeBatchSerially(ClientPreparedStatement.java:816)\r\n	at com.mysql.cj.jdbc.ClientPreparedStatement.executeBatchInternal(ClientPreparedStatement.java:418)\r\n	at com.mysql.cj.jdbc.StatementImpl.executeBatch(StatementImpl.java:795)\r\n	at com.zaxxer.hikari.pool.ProxyStatement.executeBatch(ProxyStatement.java:127)\r\n	at com.zaxxer.hikari.pool.HikariProxyPreparedStatement.executeBatch(HikariProxyPreparedStatement.java)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.apache.ibatis.logging.jdbc.PreparedStatementLogger.invoke(PreparedStatementLogger.java:78)\r\n	at com.sun.proxy.$Proxy116.executeBatch(Unknown Source)\r\n	at org.apache.ibatis.executor.BatchExecutor.doFlushStatements(BatchExecutor.java:123)\r\n	at org.apache.ibatis.executor.BaseExecutor.flushStatements(BaseExecutor.java:129)\r\n	at org.apache.ibatis.executor.BaseExecutor.flushStatements(BaseExecutor.java:122)\r\n	at org.apache.ibatis.executor.CachingExecutor.flushStatements(CachingExecutor.java:114)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.apache.ibatis.plugin.Plugin.invoke(Plugin.java:64)\r\n	at com.sun.proxy.$Proxy113.flushStatements(Unknown Source)\r\n	at org.apache.ibatis.session.defaults.DefaultSqlSession.flushStatements(DefaultSqlSession.java:249)\r\n	at com.baomidou.mybatisplus.extension.toolkit.SqlHelper.lambda$executeBatch$1(SqlHelper.java:223)\r\n	at com.baomidou.mybatisplus.extension.toolkit.SqlHelper.executeBatch(SqlHelper.java:182)\r\n	... 157 more\r\nCaused by: java.sql.SQLException: Incorrect integer value: \'=\' for column \'query_condition\' at row 1\r\n	at com.mysql.cj.jdbc.exceptions.SQLError.createSQLException(SQLError.java:129)\r\n	at com.mysql.cj.jdbc.exceptions.SQLExceptionsMapping.translateException(SQLExceptionsMapping.java:122)\r\n	at com.mysql.cj.jdbc.ClientPreparedStatement.executeInternal(ClientPreparedStatement.java:916)\r\n	at com.mysql.cj.jdbc.ClientPreparedStatement.executeUpdateInternal(ClientPreparedStatement.java:1061)\r\n	at com.mysql.cj.jdbc.ClientPreparedStatement.executeBatchSerially(ClientPreparedStatement.java:795)\r\n	... 180 more\r\n', 'class: top.sheepyu.module.system.service.codegen.SystemCodegenBiz, filename: SystemCodegenBiz.java, method: lambda$createCodegen$0, line: 96\nclass: top.sheepyu.module.system.service.codegen.SystemCodegenBiz, filename: SystemCodegenBiz.java, method: createCodegen, line: 68\nclass: top.sheepyu.module.system.controller.admin.codegen.SystemCodegenController, filename: SystemCodegenController.java, method: createCodegen, line: 39\nclass: top.sheepyu.framework.log.core.aop.RecordLogAspect, filename: RecordLogAspect.java, method: processAround, line: 76\nclass: top.sheepyu.framework.log.core.aop.RecordLogAspect, filename: RecordLogAspect.java, method: around, line: 55\nclass: top.sheepyu.framework.security.core.filter.SecurityTokenFilter, filename: SecurityTokenFilter.java, method: doFilterInternal, line: 46', 0, NULL, NULL, NULL, '2023-03-04 17:22:28', NULL, '2023-03-04 17:22:28', b'0');
INSERT INTO `system_api_log` VALUES (1366, 1, 2, '创建代码生成', 2, 'POST', '/admin-api/system/codegen', '{\"tableNames\":[\"system_user\"]}', '8.8.8.8', 105, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-03-04 17:24:16', NULL, '2023-03-04 17:24:16', b'0');
INSERT INTO `system_api_log` VALUES (1367, 1, 2, '创建代码生成', 2, 'POST', '/admin-api/system/codegen', '{\"tableNames\":[\"system_user\"]}', '8.8.8.8', 73, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-03-04 17:27:33', NULL, '2023-03-04 17:27:33', b'0');
INSERT INTO `system_api_log` VALUES (1368, 1, 2, '删除代码生成', 4, 'DELETE', '/admin-api/system/codegen/109', '{\"id\":109}', '8.8.8.8', 33, 200, '{}', 1, NULL, '', NULL, NULL, NULL, 0, NULL, NULL, NULL, '2023-03-04 17:28:38', NULL, '2023-03-04 17:28:38', b'0');
INSERT INTO `system_api_log` VALUES (1369, 1, 2, '数据库表列表', 1, 'GET', '/admin-api/system/codegen/table-list', '{}', '8.8.8.8', 507, 0, '', 0, '2023-03-04 17:50:05', 'java.lang.NullPointerException', 'NullPointerException: null', 'java.lang.NullPointerException\r\n	at java.lang.String.contains(String.java:2133)\r\n	at top.sheepyu.module.system.controller.admin.codegen.SystemCodegenController.lambda$tableList$0(SystemCodegenController.java:65)\r\n	at java.util.stream.ReferencePipeline$2$1.accept(ReferencePipeline.java:174)\r\n	at java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1384)\r\n	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:482)\r\n	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)\r\n	at java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:708)\r\n	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)\r\n	at java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:499)\r\n	at top.sheepyu.module.system.controller.admin.codegen.SystemCodegenController.tableList(SystemCodegenController.java:67)\r\n	at top.sheepyu.module.system.controller.admin.codegen.SystemCodegenController$$FastClassBySpringCGLIB$$687dcfc1.invoke(<generated>)\r\n	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)\r\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:793)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)\r\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:763)\r\n	at org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint.proceed(MethodInvocationProceedingJoinPoint.java:89)\r\n	at top.sheepyu.framework.log.core.aop.RecordLogAspect.processAround(RecordLogAspect.java:76)\r\n	at top.sheepyu.framework.log.core.aop.RecordLogAspect.around(RecordLogAspect.java:55)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethodWithGivenArgs(AbstractAspectJAdvice.java:634)\r\n	at org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethod(AbstractAspectJAdvice.java:624)\r\n	at org.springframework.aop.aspectj.AspectJAroundAdvice.invoke(AspectJAroundAdvice.java:72)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:175)\r\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:763)\r\n	at org.springframework.security.access.intercept.aopalliance.MethodSecurityInterceptor.invoke(MethodSecurityInterceptor.java:61)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\r\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:763)\r\n	at org.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:97)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\r\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:763)\r\n	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:708)\r\n	at top.sheepyu.module.system.controller.admin.codegen.SystemCodegenController$$EnhancerBySpringCGLIB$$8e51425e.tableList(<generated>)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:205)\r\n	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:150)\r\n	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:117)\r\n	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:895)\r\n	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:808)\r\n	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)\r\n	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1070)\r\n	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:963)\r\n	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006)\r\n	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:898)\r\n	at javax.servlet.http.HttpServlet.service(HttpServlet.java:655)\r\n	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883)\r\n	at javax.servlet.http.HttpServlet.service(HttpServlet.java:764)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:227)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\r\n	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:111)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:327)\r\n	at org.springframework.security.web.access.intercept.FilterSecurityInterceptor.invoke(FilterSecurityInterceptor.java:115)\r\n	at org.springframework.security.web.access.intercept.FilterSecurityInterceptor.doFilter(FilterSecurityInterceptor.java:81)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.security.web.access.ExceptionTranslationFilter.doFilter(ExceptionTranslationFilter.java:122)\r\n	at org.springframework.security.web.access.ExceptionTranslationFilter.doFilter(ExceptionTranslationFilter.java:116)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.security.web.session.SessionManagementFilter.doFilter(SessionManagementFilter.java:126)\r\n	at org.springframework.security.web.session.SessionManagementFilter.doFilter(SessionManagementFilter.java:81)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.security.web.authentication.AnonymousAuthenticationFilter.doFilter(AnonymousAuthenticationFilter.java:109)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter.doFilter(SecurityContextHolderAwareRequestFilter.java:149)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.security.web.savedrequest.RequestCacheAwareFilter.doFilter(RequestCacheAwareFilter.java:63)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at top.sheepyu.framework.security.core.filter.SecurityTokenFilter.doFilterInternal(SecurityTokenFilter.java:46)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.security.web.authentication.logout.LogoutFilter.doFilter(LogoutFilter.java:103)\r\n	at org.springframework.security.web.authentication.logout.LogoutFilter.doFilter(LogoutFilter.java:89)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.web.filter.CorsFilter.doFilterInternal(CorsFilter.java:91)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.security.web.header.HeaderWriterFilter.doHeadersAfter(HeaderWriterFilter.java:90)\r\n	at org.springframework.security.web.header.HeaderWriterFilter.doFilterInternal(HeaderWriterFilter.java:75)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.security.web.context.SecurityContextPersistenceFilter.doFilter(SecurityContextPersistenceFilter.java:110)\r\n	at org.springframework.security.web.context.SecurityContextPersistenceFilter.doFilter(SecurityContextPersistenceFilter.java:80)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter.doFilterInternal(WebAsyncManagerIntegrationFilter.java:55)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\r\n	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:336)\r\n	at org.springframework.security.web.FilterChainProxy.doFilterInternal(FilterChainProxy.java:211)\r\n	at org.springframework.security.web.FilterChainProxy.doFilter(FilterChainProxy.java:183)\r\n	at org.springframework.web.filter.DelegatingFilterProxy.invokeDelegate(DelegatingFilterProxy.java:354)\r\n	at org.springframework.web.filter.DelegatingFilterProxy.doFilter(DelegatingFilterProxy.java:267)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\r\n	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\r\n	at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\r\n	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\r\n	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:197)\r\n	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:97)\r\n	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:541)\r\n	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:135)\r\n	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92)\r\n	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:78)\r\n	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:360)\r\n	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:399)\r\n	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65)\r\n	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:890)\r\n	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1789)\r\n	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)\r\n	at org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191)\r\n	at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659)\r\n	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)\r\n	at java.lang.Thread.run(Thread.java:750)\r\n', 'class: top.sheepyu.module.system.controller.admin.codegen.SystemCodegenController, filename: SystemCodegenController.java, method: lambda$tableList$0, line: 65\nclass: top.sheepyu.module.system.controller.admin.codegen.SystemCodegenController, filename: SystemCodegenController.java, method: tableList, line: 67\nclass: top.sheepyu.framework.log.core.aop.RecordLogAspect, filename: RecordLogAspect.java, method: processAround, line: 76\nclass: top.sheepyu.framework.log.core.aop.RecordLogAspect, filename: RecordLogAspect.java, method: around, line: 55\nclass: top.sheepyu.framework.security.core.filter.SecurityTokenFilter, filename: SecurityTokenFilter.java, method: doFilterInternal, line: 46', 0, NULL, NULL, NULL, '2023-03-04 17:50:05', NULL, '2023-03-04 17:50:05', b'0');

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
-- Records of system_codegen_column
-- ----------------------------
INSERT INTO `system_codegen_column` VALUES (1278, 109, 'id', 'bigint', '用户ID', b'0', b'1', b'1', 'Long', 'id', '', NULL, b'0', b'1', b'0', '=', b'1', 'text', b'1', NULL, 'admin', '2023-03-04 17:24:16', NULL, '2023-03-04 09:28:38', b'1');
INSERT INTO `system_codegen_column` VALUES (1279, 109, 'username', 'varchar(30)', '用户账号', b'0', b'0', b'0', 'String', 'username', '', NULL, b'1', b'1', b'1', '=', b'1', 'text', b'0', NULL, 'admin', '2023-03-04 17:24:16', NULL, '2023-03-04 09:28:38', b'1');
INSERT INTO `system_codegen_column` VALUES (1280, 109, 'password', 'varchar(100)', '密码', b'0', b'0', b'0', 'String', 'password', '', NULL, b'1', b'1', b'1', '=', b'1', 'text', b'0', NULL, 'admin', '2023-03-04 17:24:16', NULL, '2023-03-04 09:28:38', b'1');
INSERT INTO `system_codegen_column` VALUES (1281, 109, 'nickname', 'varchar(30)', '用户昵称', b'0', b'0', b'0', 'String', 'nickname', '', NULL, b'1', b'1', b'1', '=', b'1', 'text', b'0', NULL, 'admin', '2023-03-04 17:24:16', NULL, '2023-03-04 09:28:38', b'1');
INSERT INTO `system_codegen_column` VALUES (1282, 109, 'email', 'varchar(50)', '用户邮箱', b'1', b'0', b'0', 'String', 'email', '', NULL, b'1', b'1', b'1', '=', b'1', 'text', b'0', NULL, 'admin', '2023-03-04 17:24:16', NULL, '2023-03-04 09:28:38', b'1');
INSERT INTO `system_codegen_column` VALUES (1283, 109, 'mobile', 'varchar(11)', '手机号码', b'1', b'0', b'0', 'String', 'mobile', '', NULL, b'1', b'1', b'1', '=', b'1', 'text', b'0', NULL, 'admin', '2023-03-04 17:24:16', NULL, '2023-03-04 09:28:38', b'1');
INSERT INTO `system_codegen_column` VALUES (1284, 109, 'avatar', 'varchar(255)', '头像地址', b'1', b'0', b'0', 'String', 'avatar', '', NULL, b'1', b'1', b'1', '=', b'1', 'text', b'0', NULL, 'admin', '2023-03-04 17:24:16', NULL, '2023-03-04 09:28:38', b'1');
INSERT INTO `system_codegen_column` VALUES (1285, 109, 'dept_id', 'bigint', '部门id', b'1', b'0', b'0', 'Long', 'deptId', '', NULL, b'1', b'1', b'1', '=', b'1', 'text', b'0', NULL, 'admin', '2023-03-04 17:24:16', NULL, '2023-03-04 09:28:38', b'1');
INSERT INTO `system_codegen_column` VALUES (1286, 109, 'post_ids', 'varchar(255)', '职位ids', b'1', b'0', b'0', 'String', 'postIds', '', NULL, b'1', b'1', b'1', '=', b'1', 'text', b'0', NULL, 'admin', '2023-03-04 17:24:16', NULL, '2023-03-04 09:28:38', b'1');
INSERT INTO `system_codegen_column` VALUES (1287, 109, 'status', 'tinyint', '帐号状态（1正常 0停用）', b'0', b'0', b'0', 'Integer', 'status', '', NULL, b'1', b'1', b'1', '=', b'1', 'text', b'0', NULL, 'admin', '2023-03-04 17:24:16', NULL, '2023-03-04 09:28:38', b'1');
INSERT INTO `system_codegen_column` VALUES (1288, 109, 'type', 'int', '1会员,2管理员', b'0', b'0', b'0', 'Integer', 'type', '', NULL, b'1', b'1', b'1', '=', b'1', 'text', b'0', NULL, 'admin', '2023-03-04 17:24:16', NULL, '2023-03-04 09:28:38', b'1');
INSERT INTO `system_codegen_column` VALUES (1289, 109, 'remark', 'varchar(500)', '备注', b'1', b'0', b'0', 'String', 'remark', '', NULL, b'1', b'1', b'1', '=', b'1', 'text', b'0', NULL, 'admin', '2023-03-04 17:24:16', NULL, '2023-03-04 09:28:38', b'1');
INSERT INTO `system_codegen_column` VALUES (1290, 109, 'login_ip', 'varchar(50)', '最后登录IP', b'1', b'0', b'0', 'String', 'loginIp', '', NULL, b'1', b'1', b'1', '=', b'1', 'text', b'0', NULL, 'admin', '2023-03-04 17:24:16', NULL, '2023-03-04 09:28:38', b'1');
INSERT INTO `system_codegen_column` VALUES (1291, 109, 'login_time', 'datetime', '最后登录时间', b'1', b'0', b'0', 'LocalDateTime', 'loginTime', '', NULL, b'1', b'1', b'1', 'between', b'1', 'text', b'0', NULL, 'admin', '2023-03-04 17:24:16', NULL, '2023-03-04 09:28:38', b'1');
INSERT INTO `system_codegen_column` VALUES (1292, 109, 'creator', 'varchar(64)', '创建者', b'1', b'0', b'0', 'String', 'creator', '', NULL, b'0', b'0', b'0', '=', b'0', 'text', b'0', NULL, 'admin', '2023-03-04 17:24:16', NULL, '2023-03-04 09:28:38', b'1');
INSERT INTO `system_codegen_column` VALUES (1293, 109, 'create_time', 'datetime', '创建时间', b'0', b'0', b'0', 'LocalDateTime', 'createTime', '', NULL, b'0', b'0', b'1', 'between', b'1', 'text', b'0', NULL, 'admin', '2023-03-04 17:24:16', NULL, '2023-03-04 09:28:38', b'1');
INSERT INTO `system_codegen_column` VALUES (1294, 109, 'updater', 'varchar(64)', '更新者', b'1', b'0', b'0', 'String', 'updater', '', NULL, b'0', b'0', b'0', '=', b'0', 'text', b'0', NULL, 'admin', '2023-03-04 17:24:16', NULL, '2023-03-04 09:28:38', b'1');
INSERT INTO `system_codegen_column` VALUES (1295, 109, 'update_time', 'datetime', '更新时间', b'0', b'0', b'0', 'LocalDateTime', 'updateTime', '', NULL, b'0', b'0', b'0', 'between', b'0', 'text', b'0', NULL, 'admin', '2023-03-04 17:24:16', NULL, '2023-03-04 09:28:38', b'1');
INSERT INTO `system_codegen_column` VALUES (1296, 109, 'deleted', 'bit(1)', '是否删除', b'0', b'0', b'0', 'Boolean', 'deleted', '', NULL, b'0', b'0', b'0', '=', b'0', 'text', b'0', NULL, 'admin', '2023-03-04 17:24:16', NULL, '2023-03-04 09:28:38', b'1');
INSERT INTO `system_codegen_column` VALUES (1297, 110, 'id', 'bigint', '用户ID', b'0', b'1', b'1', 'Long', 'id', '', NULL, b'0', b'1', b'0', '=', b'1', 'text', b'1', NULL, 'admin', '2023-03-04 17:27:32', NULL, '2023-03-04 17:27:32', b'0');
INSERT INTO `system_codegen_column` VALUES (1298, 110, 'username', 'varchar(30)', '用户账号', b'0', b'0', b'0', 'String', 'username', '', NULL, b'1', b'1', b'1', '=', b'1', 'text', b'0', NULL, 'admin', '2023-03-04 17:27:32', NULL, '2023-03-04 17:27:32', b'0');
INSERT INTO `system_codegen_column` VALUES (1299, 110, 'password', 'varchar(100)', '密码', b'0', b'0', b'0', 'String', 'password', '', NULL, b'1', b'1', b'1', '=', b'1', 'text', b'0', NULL, 'admin', '2023-03-04 17:27:32', NULL, '2023-03-04 17:27:32', b'0');
INSERT INTO `system_codegen_column` VALUES (1300, 110, 'nickname', 'varchar(30)', '用户昵称', b'0', b'0', b'0', 'String', 'nickname', '', NULL, b'1', b'1', b'1', '=', b'1', 'text', b'0', NULL, 'admin', '2023-03-04 17:27:32', NULL, '2023-03-04 17:27:32', b'0');
INSERT INTO `system_codegen_column` VALUES (1301, 110, 'email', 'varchar(50)', '用户邮箱', b'1', b'0', b'0', 'String', 'email', '', NULL, b'1', b'1', b'1', '=', b'1', 'text', b'0', NULL, 'admin', '2023-03-04 17:27:32', NULL, '2023-03-04 17:27:32', b'0');
INSERT INTO `system_codegen_column` VALUES (1302, 110, 'mobile', 'varchar(11)', '手机号码', b'1', b'0', b'0', 'String', 'mobile', '', NULL, b'1', b'1', b'1', '=', b'1', 'text', b'0', NULL, 'admin', '2023-03-04 17:27:32', NULL, '2023-03-04 17:27:32', b'0');
INSERT INTO `system_codegen_column` VALUES (1303, 110, 'avatar', 'varchar(255)', '头像地址', b'1', b'0', b'0', 'String', 'avatar', '', NULL, b'1', b'1', b'1', '=', b'1', 'text', b'0', NULL, 'admin', '2023-03-04 17:27:32', NULL, '2023-03-04 17:27:32', b'0');
INSERT INTO `system_codegen_column` VALUES (1304, 110, 'dept_id', 'bigint', '部门id', b'1', b'0', b'0', 'Long', 'deptId', '', NULL, b'1', b'1', b'1', '=', b'1', 'text', b'0', NULL, 'admin', '2023-03-04 17:27:32', NULL, '2023-03-04 17:27:32', b'0');
INSERT INTO `system_codegen_column` VALUES (1305, 110, 'post_ids', 'varchar(255)', '职位ids', b'1', b'0', b'0', 'String', 'postIds', '', NULL, b'1', b'1', b'1', '=', b'1', 'text', b'0', NULL, 'admin', '2023-03-04 17:27:32', NULL, '2023-03-04 17:27:32', b'0');
INSERT INTO `system_codegen_column` VALUES (1306, 110, 'status', 'tinyint', '帐号状态（1正常 0停用）', b'0', b'0', b'0', 'Integer', 'status', '', NULL, b'1', b'1', b'1', '=', b'1', 'text', b'0', NULL, 'admin', '2023-03-04 17:27:32', NULL, '2023-03-04 17:27:32', b'0');
INSERT INTO `system_codegen_column` VALUES (1307, 110, 'type', 'int', '1会员,2管理员', b'0', b'0', b'0', 'Integer', 'type', '', NULL, b'1', b'1', b'1', '=', b'1', 'text', b'0', NULL, 'admin', '2023-03-04 17:27:32', NULL, '2023-03-04 17:27:32', b'0');
INSERT INTO `system_codegen_column` VALUES (1308, 110, 'remark', 'varchar(500)', '备注', b'1', b'0', b'0', 'String', 'remark', '', NULL, b'1', b'1', b'1', '=', b'1', 'text', b'0', NULL, 'admin', '2023-03-04 17:27:32', NULL, '2023-03-04 17:27:32', b'0');
INSERT INTO `system_codegen_column` VALUES (1309, 110, 'login_ip', 'varchar(50)', '最后登录IP', b'1', b'0', b'0', 'String', 'loginIp', '', NULL, b'1', b'1', b'1', '=', b'1', 'text', b'0', NULL, 'admin', '2023-03-04 17:27:32', NULL, '2023-03-04 17:27:32', b'0');
INSERT INTO `system_codegen_column` VALUES (1310, 110, 'login_time', 'datetime', '最后登录时间', b'1', b'0', b'0', 'LocalDateTime', 'loginTime', '', NULL, b'1', b'1', b'1', 'between', b'1', 'text', b'0', NULL, 'admin', '2023-03-04 17:27:32', NULL, '2023-03-04 17:27:32', b'0');
INSERT INTO `system_codegen_column` VALUES (1311, 110, 'creator', 'varchar(64)', '创建者', b'1', b'0', b'0', 'String', 'creator', '', NULL, b'0', b'0', b'0', '=', b'0', 'text', b'0', NULL, 'admin', '2023-03-04 17:27:32', NULL, '2023-03-04 17:27:32', b'0');
INSERT INTO `system_codegen_column` VALUES (1312, 110, 'create_time', 'datetime', '创建时间', b'0', b'0', b'0', 'LocalDateTime', 'createTime', '', NULL, b'0', b'0', b'1', 'between', b'1', 'text', b'0', NULL, 'admin', '2023-03-04 17:27:32', NULL, '2023-03-04 17:27:32', b'0');
INSERT INTO `system_codegen_column` VALUES (1313, 110, 'updater', 'varchar(64)', '更新者', b'1', b'0', b'0', 'String', 'updater', '', NULL, b'0', b'0', b'0', '=', b'0', 'text', b'0', NULL, 'admin', '2023-03-04 17:27:32', NULL, '2023-03-04 17:27:32', b'0');
INSERT INTO `system_codegen_column` VALUES (1314, 110, 'update_time', 'datetime', '更新时间', b'0', b'0', b'0', 'LocalDateTime', 'updateTime', '', NULL, b'0', b'0', b'0', 'between', b'0', 'text', b'0', NULL, 'admin', '2023-03-04 17:27:32', NULL, '2023-03-04 17:27:32', b'0');
INSERT INTO `system_codegen_column` VALUES (1315, 110, 'deleted', 'bit(1)', '是否删除', b'0', b'0', b'0', 'Boolean', 'deleted', '', NULL, b'0', b'0', b'0', '=', b'0', 'text', b'0', NULL, 'admin', '2023-03-04 17:27:32', NULL, '2023-03-04 17:27:32', b'0');

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
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '代码生成表定义' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_codegen_table
-- ----------------------------
INSERT INTO `system_codegen_table` VALUES (109, 1, 'system_user', '用户信息表', NULL, 'system', 'user', 'User', '用户信息表', 'sheepyu', 'admin', '2023-03-04 17:24:16', 'admin', '2023-03-04 17:28:38', b'1');
INSERT INTO `system_codegen_table` VALUES (110, 1, 'system_user', '用户信息表', NULL, 'system', 'user', 'User', '用户信息表', 'sheepyu', 'admin', '2023-03-04 17:27:32', NULL, '2023-03-04 17:27:32', b'0');

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
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '参数配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_config
-- ----------------------------
INSERT INTO `system_config` VALUES (1, '验证码开关', 'captcha.enable', 'true', '系统验证码的开关', 'admin', '2023-01-21 06:48:12', 'admin', '2023-02-18 14:26:07', b'0');
INSERT INTO `system_config` VALUES (2, '默认密码', 'password.default', '12345678', '重置系统用户时的默认密码', 'admin', '2023-01-23 20:00:48', NULL, '2023-01-23 20:00:48', b'0');
INSERT INTO `system_config` VALUES (3, '文件上传实现', 'file-upload.default', 'LOCAL', '默认的文件上传实现', 'admin', '2023-01-25 09:12:09', 'admin', '2023-01-28 18:06:57', b'0');
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
  `leader_user_id` bigint NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_dept
-- ----------------------------
INSERT INTO `system_dept` VALUES (1, '洋芋', 0, 0, 1, '19507413456', '3029944576@qq.com', 'admin', '2023-01-30 12:10:33', NULL, '2023-01-30 12:10:33', b'0');
INSERT INTO `system_dept` VALUES (2, '长沙分部', 1, 0, 0, '', '', 'admin', '2023-01-30 12:11:28', NULL, '2023-01-30 12:11:28', b'0');
INSERT INTO `system_dept` VALUES (3, '研发部门', 2, 0, 0, '', '', 'admin', '2023-01-30 12:11:47', NULL, '2023-01-30 12:11:47', b'0');
INSERT INTO `system_dept` VALUES (4, '测试部门', 2, 0, 0, '', '', 'admin', '2023-01-30 12:11:52', NULL, '2023-01-30 12:11:52', b'0');

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
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '字典数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_dict_data
-- ----------------------------
INSERT INTO `system_dict_data` VALUES (6, 'common_status', 0, '启用', '1', 1, '', '', 'admin', '2023-01-29 17:41:16', NULL, '2023-01-29 17:41:16', b'0');
INSERT INTO `system_dict_data` VALUES (7, 'common_status', 0, '禁用', '0', 1, '', '', 'admin', '2023-01-29 17:41:27', NULL, '2023-01-29 17:41:27', b'0');
INSERT INTO `system_dict_data` VALUES (8, 'system_menu_type', 0, '目录', '1', 1, 'success', '菜单类型为目录', 'admin', '2023-02-13 14:57:41', NULL, '2023-02-13 14:57:41', b'0');
INSERT INTO `system_dict_data` VALUES (9, 'system_menu_type', 0, '菜单', '2', 1, '', '菜单项', 'admin', '2023-02-13 14:58:17', 'admin', '2023-02-24 19:56:43', b'0');
INSERT INTO `system_dict_data` VALUES (10, 'system_menu_type', 0, '按钮', '3', 1, 'info', '按钮', 'admin', '2023-02-13 14:58:34', NULL, '2023-02-13 14:58:34', b'0');

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
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '字典类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_dict_type
-- ----------------------------
INSERT INTO `system_dict_type` VALUES (6, 'common_status', '通用状态', 1, '启用或禁用', 'admin', '2023-01-29 17:40:28', NULL, '2023-01-29 17:40:28', b'0');
INSERT INTO `system_dict_type` VALUES (7, 'system_menu_type', '菜单资源类型', 1, '菜单资源类型', 'admin', '2023-02-13 14:55:33', NULL, '2023-02-13 07:00:13', b'0');

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
  `complete` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否完成',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_upload_id`(`upload_id` ASC) USING BTREE,
  UNIQUE INDEX `idx_md5`(`md5` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 67 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文件表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_file
-- ----------------------------
INSERT INTO `system_file` VALUES (67, 'bd12356ab871403a8150e19338934932', 22, '功能测试-设计测试用例.mp4', 'fde77f23bbede4f5c3118e28fd67b9b7', 'http://localhost:18080/file/2023/01/29/5b439f49c110470ba0f99bdcbf53f0f2.mp4', 'video/mp4', 226921797, 'http://localhost:18080', '/2023/01/29/5b439f49c110470ba0f99bdcbf53f0f2.mp4', 1, '', 'admin', '2023-01-29 14:59:50');

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
) ENGINE = InnoDB AUTO_INCREMENT = 371 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_file_part
-- ----------------------------

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
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '定时任务表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_job
-- ----------------------------
INSERT INTO `system_job` VALUES (2, '测试定时任务', 1, 'testTask', '测试参数xxx', '0 5 * * * ?', 2, 2, 'admin', '2023-01-17 15:17:28', 'admin', '2023-01-17 15:24:17', b'0');

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
) ENGINE = InnoDB AUTO_INCREMENT = 323 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '定时任务日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_job_log
-- ----------------------------
INSERT INTO `system_job_log` VALUES (91, 1, 'testTask', 'xxx', 1, '2023-01-17 15:14:00', '2023-01-17 15:14:00', 21, 0, '', NULL, '2023-01-17 15:14:00', NULL, '2023-01-17 15:14:00', b'0');
INSERT INTO `system_job_log` VALUES (92, 1, 'testTask', 'xxx', 1, '2023-01-17 15:14:01', '2023-01-17 15:14:01', 20, 0, '', NULL, '2023-01-17 15:14:01', NULL, '2023-01-17 15:14:01', b'0');
INSERT INTO `system_job_log` VALUES (93, 1, 'testTask', 'xxx', 1, '2023-01-17 15:15:00', '2023-01-17 15:15:00', 12, 0, '', NULL, '2023-01-17 15:15:00', NULL, '2023-01-17 15:15:00', b'0');
INSERT INTO `system_job_log` VALUES (94, 2, 'testTask', '测试参数xxx', 1, '2023-01-17 16:05:00', '2023-01-17 16:05:00', 17, 0, '', NULL, '2023-01-17 16:05:00', NULL, '2023-01-17 16:05:00', b'0');
INSERT INTO `system_job_log` VALUES (95, 2, 'testTask', '测试参数xxx', 1, '2023-01-17 17:05:00', '2023-01-17 17:05:00', 133, 0, '', NULL, '2023-01-17 17:05:00', NULL, '2023-01-17 17:05:00', b'0');
INSERT INTO `system_job_log` VALUES (96, 2, 'testTask', '测试参数xxx', 1, '2023-01-17 18:05:00', '2023-01-17 18:05:00', 21, 0, '', NULL, '2023-01-17 18:05:00', NULL, '2023-01-17 18:05:00', b'0');
INSERT INTO `system_job_log` VALUES (97, 2, 'testTask', '测试参数xxx', 1, '2023-01-17 19:05:00', '2023-01-17 19:05:00', 18, 0, '', NULL, '2023-01-17 19:05:00', NULL, '2023-01-17 19:05:00', b'0');
INSERT INTO `system_job_log` VALUES (98, 2, 'testTask', '测试参数xxx', 1, '2023-01-18 10:01:57', '2023-01-18 10:01:57', 61, 0, '', NULL, '2023-01-18 10:01:57', NULL, '2023-01-18 10:01:57', b'0');
INSERT INTO `system_job_log` VALUES (99, 2, 'testTask', '测试参数xxx', 1, '2023-01-18 10:05:00', '2023-01-18 10:05:00', 76, 0, '', NULL, '2023-01-18 10:05:00', NULL, '2023-01-18 10:05:00', b'0');
INSERT INTO `system_job_log` VALUES (100, 2, 'testTask', '测试参数xxx', 1, '2023-01-18 11:05:00', '2023-01-18 11:05:00', 71, 0, '', NULL, '2023-01-18 11:05:00', NULL, '2023-01-18 11:05:00', b'0');
INSERT INTO `system_job_log` VALUES (101, 2, 'testTask', '测试参数xxx', 1, '2023-01-18 11:21:58', '2023-01-18 11:21:58', 96, 0, '', NULL, '2023-01-18 11:21:58', NULL, '2023-01-18 11:21:58', b'0');
INSERT INTO `system_job_log` VALUES (102, 2, 'testTask', '测试参数xxx', 1, '2023-01-18 12:05:00', '2023-01-18 12:05:00', 24, 0, '', NULL, '2023-01-18 12:05:00', NULL, '2023-01-18 12:05:00', b'0');
INSERT INTO `system_job_log` VALUES (103, 2, 'testTask', '测试参数xxx', 1, '2023-01-18 13:05:00', '2023-01-18 13:05:00', 20, 0, '', NULL, '2023-01-18 13:05:00', NULL, '2023-01-18 13:05:00', b'0');
INSERT INTO `system_job_log` VALUES (104, 2, 'testTask', '测试参数xxx', 1, '2023-01-18 14:05:00', '2023-01-18 14:05:00', 22, 0, '', NULL, '2023-01-18 14:05:00', NULL, '2023-01-18 14:05:00', b'0');
INSERT INTO `system_job_log` VALUES (105, 2, 'testTask', '测试参数xxx', 1, '2023-01-18 14:28:37', '2023-01-18 14:28:37', 54, 1, '', NULL, '2023-01-18 14:28:37', NULL, '2023-01-18 14:28:37', b'0');
INSERT INTO `system_job_log` VALUES (106, 2, 'testTask', '测试参数xxx', 1, '2023-01-19 14:42:29', '2023-01-19 14:42:29', 95, 1, '', NULL, '2023-01-19 14:42:29', NULL, '2023-01-19 14:42:29', b'0');
INSERT INTO `system_job_log` VALUES (107, 2, 'testTask', '测试参数xxx', 1, '2023-01-19 15:05:00', '2023-01-19 15:05:00', 51, 1, '', NULL, '2023-01-19 15:05:00', NULL, '2023-01-19 15:05:00', b'0');
INSERT INTO `system_job_log` VALUES (108, 2, 'testTask', '测试参数xxx', 1, '2023-01-19 16:05:00', '2023-01-19 16:05:00', 114, 1, '', NULL, '2023-01-19 16:05:00', NULL, '2023-01-19 16:05:00', b'0');
INSERT INTO `system_job_log` VALUES (109, 2, 'testTask', '测试参数xxx', 1, '2023-01-19 17:05:00', '2023-01-19 17:05:00', 81, 1, '', NULL, '2023-01-19 17:05:00', NULL, '2023-01-19 17:05:00', b'0');
INSERT INTO `system_job_log` VALUES (110, 2, 'testTask', '测试参数xxx', 1, '2023-01-20 10:08:30', '2023-01-20 10:08:30', 141, 1, '', NULL, '2023-01-20 10:08:30', NULL, '2023-01-20 10:08:30', b'0');
INSERT INTO `system_job_log` VALUES (111, 2, 'testTask', '测试参数xxx', 1, '2023-01-20 11:05:00', '2023-01-20 11:05:00', 20, 1, '', NULL, '2023-01-20 11:05:00', NULL, '2023-01-20 11:05:00', b'0');
INSERT INTO `system_job_log` VALUES (112, 2, 'testTask', '测试参数xxx', 1, '2023-01-20 12:05:00', '2023-01-20 12:05:00', 34, 1, '', NULL, '2023-01-20 12:05:00', NULL, '2023-01-20 12:05:00', b'0');
INSERT INTO `system_job_log` VALUES (113, 2, 'testTask', '测试参数xxx', 1, '2023-01-20 13:05:00', '2023-01-20 13:05:00', 13, 1, '', NULL, '2023-01-20 13:05:00', NULL, '2023-01-20 13:05:00', b'0');
INSERT INTO `system_job_log` VALUES (114, 2, 'testTask', '测试参数xxx', 1, '2023-01-20 14:05:00', '2023-01-20 14:05:00', 12, 1, '', NULL, '2023-01-20 14:05:00', NULL, '2023-01-20 14:05:00', b'0');
INSERT INTO `system_job_log` VALUES (115, 2, 'testTask', '测试参数xxx', 1, '2023-01-20 15:05:00', '2023-01-20 15:05:00', 100, 1, '', NULL, '2023-01-20 15:05:00', NULL, '2023-01-20 15:05:00', b'0');
INSERT INTO `system_job_log` VALUES (116, 2, 'testTask', '测试参数xxx', 1, '2023-01-20 16:05:00', '2023-01-20 16:05:00', 47, 1, '', NULL, '2023-01-20 16:05:00', NULL, '2023-01-20 16:05:00', b'0');
INSERT INTO `system_job_log` VALUES (117, 2, 'testTask', '测试参数xxx', 1, '2023-01-20 17:05:00', '2023-01-20 17:05:00', 82, 1, '', NULL, '2023-01-20 17:05:00', NULL, '2023-01-20 17:05:00', b'0');
INSERT INTO `system_job_log` VALUES (118, 2, 'testTask', '测试参数xxx', 1, '2023-01-21 14:15:50', '2023-01-21 14:15:50', 86, 1, '', NULL, '2023-01-21 14:15:50', NULL, '2023-01-21 14:15:50', b'0');
INSERT INTO `system_job_log` VALUES (119, 2, 'testTask', '测试参数xxx', 1, '2023-01-21 15:05:00', '2023-01-21 15:05:00', 17, 1, '', NULL, '2023-01-21 15:05:00', NULL, '2023-01-21 15:05:00', b'0');
INSERT INTO `system_job_log` VALUES (120, 2, 'testTask', '测试参数xxx', 1, '2023-01-21 16:09:56', '2023-01-21 16:09:56', 62, 1, '', NULL, '2023-01-21 16:09:56', NULL, '2023-01-21 16:09:56', b'0');
INSERT INTO `system_job_log` VALUES (121, 2, 'testTask', '测试参数xxx', 1, '2023-01-22 10:56:40', '2023-01-22 10:56:41', 302, 1, '', NULL, '2023-01-22 10:56:40', NULL, '2023-01-22 10:56:41', b'0');
INSERT INTO `system_job_log` VALUES (122, 2, 'testTask', '测试参数xxx', 1, '2023-01-22 11:05:00', '2023-01-22 11:05:00', 15, 1, '', NULL, '2023-01-22 11:05:00', NULL, '2023-01-22 11:05:00', b'0');
INSERT INTO `system_job_log` VALUES (123, 2, 'testTask', '测试参数xxx', 1, '2023-01-22 12:05:00', '2023-01-22 12:05:00', 16, 1, '', NULL, '2023-01-22 12:05:00', NULL, '2023-01-22 12:05:00', b'0');
INSERT INTO `system_job_log` VALUES (124, 2, 'testTask', '测试参数xxx', 1, '2023-01-22 13:05:00', '2023-01-22 13:05:00', 22, 1, '', NULL, '2023-01-22 13:05:00', NULL, '2023-01-22 13:05:00', b'0');
INSERT INTO `system_job_log` VALUES (125, 2, 'testTask', '测试参数xxx', 1, '2023-01-22 14:05:00', '2023-01-22 14:05:00', 14, 1, '', NULL, '2023-01-22 14:05:00', NULL, '2023-01-22 14:05:00', b'0');
INSERT INTO `system_job_log` VALUES (126, 2, 'testTask', '测试参数xxx', 1, '2023-01-22 15:58:01', '2023-01-22 15:58:01', 53, 1, '', NULL, '2023-01-22 15:58:01', NULL, '2023-01-22 15:58:01', b'0');
INSERT INTO `system_job_log` VALUES (127, 2, 'testTask', '测试参数xxx', 1, '2023-01-22 16:45:33', '2023-01-22 16:45:33', 59, 1, '', NULL, '2023-01-22 16:45:33', NULL, '2023-01-22 16:45:33', b'0');
INSERT INTO `system_job_log` VALUES (128, 2, 'testTask', '测试参数xxx', 1, '2023-01-22 17:05:00', '2023-01-22 17:05:00', 12, 1, '', NULL, '2023-01-22 17:05:00', NULL, '2023-01-22 17:05:00', b'0');
INSERT INTO `system_job_log` VALUES (129, 2, 'testTask', '测试参数xxx', 1, '2023-01-23 18:59:57', '2023-01-23 18:59:58', 170, 1, '', NULL, '2023-01-23 18:59:57', NULL, '2023-01-23 18:59:58', b'0');
INSERT INTO `system_job_log` VALUES (130, 2, 'testTask', '测试参数xxx', 1, '2023-01-23 19:05:00', '2023-01-23 19:05:00', 11, 1, '', NULL, '2023-01-23 19:05:00', NULL, '2023-01-23 19:05:00', b'0');
INSERT INTO `system_job_log` VALUES (131, 2, 'testTask', '测试参数xxx', 1, '2023-01-23 20:05:00', '2023-01-23 20:05:00', 36, 1, '', NULL, '2023-01-23 20:05:00', NULL, '2023-01-23 20:05:00', b'0');
INSERT INTO `system_job_log` VALUES (132, 2, 'testTask', '测试参数xxx', 1, '2023-01-23 21:05:00', '2023-01-23 21:05:00', 23, 1, '', NULL, '2023-01-23 21:05:00', NULL, '2023-01-23 21:05:00', b'0');
INSERT INTO `system_job_log` VALUES (133, 2, 'testTask', '测试参数xxx', 1, '2023-01-24 13:35:21', '2023-01-24 13:35:21', 39, 1, '', NULL, '2023-01-24 13:35:21', NULL, '2023-01-24 13:35:21', b'0');
INSERT INTO `system_job_log` VALUES (134, 2, 'testTask', '测试参数xxx', 1, '2023-01-24 14:05:00', '2023-01-24 14:05:00', 12, 1, '', NULL, '2023-01-24 14:05:00', NULL, '2023-01-24 14:05:00', b'0');
INSERT INTO `system_job_log` VALUES (135, 2, 'testTask', '测试参数xxx', 1, '2023-01-24 15:05:00', '2023-01-24 15:05:00', 22, 1, '', NULL, '2023-01-24 15:05:00', NULL, '2023-01-24 15:05:00', b'0');
INSERT INTO `system_job_log` VALUES (136, 2, 'testTask', '测试参数xxx', 1, '2023-01-24 16:05:00', '2023-01-24 16:05:00', 24, 1, '', NULL, '2023-01-24 16:05:00', NULL, '2023-01-24 16:05:00', b'0');
INSERT INTO `system_job_log` VALUES (137, 2, 'testTask', '测试参数xxx', 1, '2023-01-24 17:23:43', '2023-01-24 17:23:43', 48, 1, '', NULL, '2023-01-24 17:23:43', NULL, '2023-01-24 17:23:43', b'0');
INSERT INTO `system_job_log` VALUES (138, 2, 'testTask', '测试参数xxx', 1, '2023-01-24 18:05:00', '2023-01-24 18:05:00', 19, 1, '', NULL, '2023-01-24 18:05:00', NULL, '2023-01-24 18:05:00', b'0');
INSERT INTO `system_job_log` VALUES (139, 2, 'testTask', '测试参数xxx', 1, '2023-01-24 19:05:00', '2023-01-24 19:05:00', 15, 1, '', NULL, '2023-01-24 19:05:00', NULL, '2023-01-24 19:05:00', b'0');
INSERT INTO `system_job_log` VALUES (140, 2, 'testTask', '测试参数xxx', 1, '2023-01-25 17:32:28', '2023-01-25 17:32:28', 62, 1, '', NULL, '2023-01-25 17:32:28', NULL, '2023-01-25 17:32:28', b'0');
INSERT INTO `system_job_log` VALUES (141, 2, 'testTask', '测试参数xxx', 1, '2023-01-25 18:05:00', '2023-01-25 18:05:00', 48, 1, '', NULL, '2023-01-25 18:05:00', NULL, '2023-01-25 18:05:00', b'0');
INSERT INTO `system_job_log` VALUES (142, 2, 'testTask', '测试参数xxx', 1, '2023-01-25 19:05:00', '2023-01-25 19:05:00', 20, 1, '', NULL, '2023-01-25 19:05:00', NULL, '2023-01-25 19:05:00', b'0');
INSERT INTO `system_job_log` VALUES (143, 2, 'testTask', '测试参数xxx', 1, '2023-01-25 20:05:00', '2023-01-25 20:05:00', 52, 1, '', NULL, '2023-01-25 20:05:00', NULL, '2023-01-25 20:05:00', b'0');
INSERT INTO `system_job_log` VALUES (144, 2, 'testTask', '测试参数xxx', 1, '2023-01-26 08:55:35', '2023-01-26 08:55:35', 123, 1, '', NULL, '2023-01-26 08:55:35', NULL, '2023-01-26 08:55:36', b'0');
INSERT INTO `system_job_log` VALUES (145, 2, 'testTask', '测试参数xxx', 1, '2023-01-26 09:05:00', '2023-01-26 09:05:00', 14, 1, '', NULL, '2023-01-26 09:05:00', NULL, '2023-01-26 09:05:00', b'0');
INSERT INTO `system_job_log` VALUES (146, 2, 'testTask', '测试参数xxx', 1, '2023-01-26 10:05:00', '2023-01-26 10:05:00', 58, 1, '', NULL, '2023-01-26 10:05:00', NULL, '2023-01-26 10:05:00', b'0');
INSERT INTO `system_job_log` VALUES (147, 2, 'testTask', '测试参数xxx', 1, '2023-01-26 12:10:52', '2023-01-26 12:10:52', 65, 1, '', NULL, '2023-01-26 12:10:52', NULL, '2023-01-26 12:10:52', b'0');
INSERT INTO `system_job_log` VALUES (148, 2, 'testTask', '测试参数xxx', 1, '2023-01-26 13:40:23', '2023-01-26 13:40:23', 54, 1, '', NULL, '2023-01-26 13:40:23', NULL, '2023-01-26 13:40:23', b'0');
INSERT INTO `system_job_log` VALUES (149, 2, 'testTask', '测试参数xxx', 1, '2023-01-26 14:05:00', '2023-01-26 14:05:00', 11, 1, '', NULL, '2023-01-26 14:05:00', NULL, '2023-01-26 14:05:00', b'0');
INSERT INTO `system_job_log` VALUES (150, 2, 'testTask', '测试参数xxx', 1, '2023-01-26 15:05:00', '2023-01-26 15:05:00', 23, 1, '', NULL, '2023-01-26 15:05:00', NULL, '2023-01-26 15:05:00', b'0');
INSERT INTO `system_job_log` VALUES (151, 2, 'testTask', '测试参数xxx', 1, '2023-01-26 16:05:00', '2023-01-26 16:05:00', 34, 1, '', NULL, '2023-01-26 16:05:00', NULL, '2023-01-26 16:05:00', b'0');
INSERT INTO `system_job_log` VALUES (152, 2, 'testTask', '测试参数xxx', 1, '2023-01-26 17:05:00', '2023-01-26 17:05:00', 35, 1, '', NULL, '2023-01-26 17:05:00', NULL, '2023-01-26 17:05:00', b'0');
INSERT INTO `system_job_log` VALUES (153, 2, 'testTask', '测试参数xxx', 1, '2023-01-26 18:05:00', '2023-01-26 18:05:00', 18, 1, '', NULL, '2023-01-26 18:05:00', NULL, '2023-01-26 18:05:00', b'0');
INSERT INTO `system_job_log` VALUES (154, 2, 'testTask', '测试参数xxx', 1, '2023-01-26 19:05:00', '2023-01-26 19:05:00', 15, 1, '', NULL, '2023-01-26 19:05:00', NULL, '2023-01-26 19:05:00', b'0');
INSERT INTO `system_job_log` VALUES (155, 2, 'testTask', '测试参数xxx', 1, '2023-01-26 20:05:00', '2023-01-26 20:05:00', 15, 1, '', NULL, '2023-01-26 20:05:00', NULL, '2023-01-26 20:05:00', b'0');
INSERT INTO `system_job_log` VALUES (156, 2, 'testTask', '测试参数xxx', 1, '2023-01-26 21:05:00', '2023-01-26 21:05:00', 14, 1, '', NULL, '2023-01-26 21:05:00', NULL, '2023-01-26 21:05:00', b'0');
INSERT INTO `system_job_log` VALUES (157, 2, 'testTask', '测试参数xxx', 1, '2023-01-26 22:05:00', '2023-01-26 22:05:00', 49, 1, '', NULL, '2023-01-26 22:05:00', NULL, '2023-01-26 22:05:00', b'0');
INSERT INTO `system_job_log` VALUES (158, 2, 'testTask', '测试参数xxx', 1, '2023-01-27 09:23:24', '2023-01-27 09:23:24', 43, 1, '', NULL, '2023-01-27 09:23:24', NULL, '2023-01-27 09:23:24', b'0');
INSERT INTO `system_job_log` VALUES (159, 2, 'testTask', '测试参数xxx', 1, '2023-01-27 10:05:00', '2023-01-27 10:05:00', 11, 1, '', NULL, '2023-01-27 10:05:00', NULL, '2023-01-27 10:05:00', b'0');
INSERT INTO `system_job_log` VALUES (160, 2, 'testTask', '测试参数xxx', 1, '2023-01-27 11:05:00', '2023-01-27 11:05:00', 48, 1, '', NULL, '2023-01-27 11:05:00', NULL, '2023-01-27 11:05:00', b'0');
INSERT INTO `system_job_log` VALUES (161, 2, 'testTask', '测试参数xxx', 1, '2023-01-27 12:05:00', '2023-01-27 12:05:00', 31, 1, '', NULL, '2023-01-27 12:05:00', NULL, '2023-01-27 12:05:00', b'0');
INSERT INTO `system_job_log` VALUES (162, 2, 'testTask', '测试参数xxx', 1, '2023-01-27 13:05:00', '2023-01-27 13:05:00', 11, 1, '', NULL, '2023-01-27 13:05:00', NULL, '2023-01-27 13:05:00', b'0');
INSERT INTO `system_job_log` VALUES (163, 2, 'testTask', '测试参数xxx', 1, '2023-01-27 14:05:00', '2023-01-27 14:05:00', 12, 1, '', NULL, '2023-01-27 14:05:00', NULL, '2023-01-27 14:05:00', b'0');
INSERT INTO `system_job_log` VALUES (164, 2, 'testTask', '测试参数xxx', 1, '2023-01-27 15:05:00', '2023-01-27 15:05:00', 16, 1, '', NULL, '2023-01-27 15:05:00', NULL, '2023-01-27 15:05:00', b'0');
INSERT INTO `system_job_log` VALUES (165, 2, 'testTask', '测试参数xxx', 1, '2023-01-27 16:05:00', '2023-01-27 16:05:00', 38, 1, '', NULL, '2023-01-27 16:05:00', NULL, '2023-01-27 16:05:00', b'0');
INSERT INTO `system_job_log` VALUES (166, 2, 'testTask', '测试参数xxx', 1, '2023-01-27 17:05:00', '2023-01-27 17:05:00', 14, 1, '', NULL, '2023-01-27 17:05:00', NULL, '2023-01-27 17:05:00', b'0');
INSERT INTO `system_job_log` VALUES (167, 2, 'testTask', '测试参数xxx', 1, '2023-01-27 18:05:00', '2023-01-27 18:05:00', 31, 1, '', NULL, '2023-01-27 18:05:00', NULL, '2023-01-27 18:05:00', b'0');
INSERT INTO `system_job_log` VALUES (168, 2, 'testTask', '测试参数xxx', 1, '2023-01-28 09:48:44', '2023-01-28 09:48:44', 115, 1, '', NULL, '2023-01-28 09:48:44', NULL, '2023-01-28 09:48:44', b'0');
INSERT INTO `system_job_log` VALUES (169, 2, 'testTask', '测试参数xxx', 1, '2023-01-28 10:08:59', '2023-01-28 10:08:59', 47, 1, '', NULL, '2023-01-28 10:08:59', NULL, '2023-01-28 10:08:59', b'0');
INSERT INTO `system_job_log` VALUES (170, 2, 'testTask', '测试参数xxx', 1, '2023-01-28 11:05:00', '2023-01-28 11:05:00', 33, 1, '', NULL, '2023-01-28 11:05:00', NULL, '2023-01-28 11:05:00', b'0');
INSERT INTO `system_job_log` VALUES (171, 2, 'testTask', '测试参数xxx', 1, '2023-01-28 12:05:00', '2023-01-28 12:05:00', 31, 1, '', NULL, '2023-01-28 12:05:00', NULL, '2023-01-28 12:05:00', b'0');
INSERT INTO `system_job_log` VALUES (172, 2, 'testTask', '测试参数xxx', 1, '2023-01-28 13:05:00', '2023-01-28 13:05:00', 9, 1, '', NULL, '2023-01-28 13:05:00', NULL, '2023-01-28 13:05:00', b'0');
INSERT INTO `system_job_log` VALUES (173, 2, 'testTask', '测试参数xxx', 1, '2023-01-28 14:05:00', '2023-01-28 14:05:00', 15, 1, '', NULL, '2023-01-28 14:05:00', NULL, '2023-01-28 14:05:00', b'0');
INSERT INTO `system_job_log` VALUES (174, 2, 'testTask', '测试参数xxx', 1, '2023-01-28 15:05:00', '2023-01-28 15:05:00', 49, 1, '', NULL, '2023-01-28 15:05:00', NULL, '2023-01-28 15:05:00', b'0');
INSERT INTO `system_job_log` VALUES (175, 2, 'testTask', '测试参数xxx', 1, '2023-01-28 16:05:00', '2023-01-28 16:05:00', 19, 1, '', NULL, '2023-01-28 16:05:00', NULL, '2023-01-28 16:05:00', b'0');
INSERT INTO `system_job_log` VALUES (176, 2, 'testTask', '测试参数xxx', 1, '2023-01-28 17:05:00', '2023-01-28 17:05:00', 21, 1, '', NULL, '2023-01-28 17:05:00', NULL, '2023-01-28 17:05:00', b'0');
INSERT INTO `system_job_log` VALUES (177, 2, 'testTask', '测试参数xxx', 1, '2023-01-28 18:05:00', '2023-01-28 18:05:00', 32, 1, '', NULL, '2023-01-28 18:05:00', NULL, '2023-01-28 18:05:00', b'0');
INSERT INTO `system_job_log` VALUES (178, 2, 'testTask', '测试参数xxx', 1, '2023-01-28 19:05:00', '2023-01-28 19:05:00', 12, 1, '', NULL, '2023-01-28 19:05:00', NULL, '2023-01-28 19:05:00', b'0');
INSERT INTO `system_job_log` VALUES (179, 2, 'testTask', '测试参数xxx', 1, '2023-01-28 20:05:00', '2023-01-28 20:05:00', 10, 1, '', NULL, '2023-01-28 20:05:00', NULL, '2023-01-28 20:05:00', b'0');
INSERT INTO `system_job_log` VALUES (180, 2, 'testTask', '测试参数xxx', 1, '2023-01-29 10:38:45', '2023-01-29 10:38:45', 28, 1, '', NULL, '2023-01-29 10:38:45', NULL, '2023-01-29 10:38:45', b'0');
INSERT INTO `system_job_log` VALUES (181, 2, 'testTask', '测试参数xxx', 1, '2023-01-29 11:05:00', '2023-01-29 11:05:00', 11, 1, '', NULL, '2023-01-29 11:05:00', NULL, '2023-01-29 11:05:00', b'0');
INSERT INTO `system_job_log` VALUES (182, 2, 'testTask', '测试参数xxx', 1, '2023-01-29 12:05:00', '2023-01-29 12:05:00', 34, 1, '', NULL, '2023-01-29 12:05:00', NULL, '2023-01-29 12:05:00', b'0');
INSERT INTO `system_job_log` VALUES (183, 2, 'testTask', '测试参数xxx', 1, '2023-01-29 13:05:00', '2023-01-29 13:05:00', 13, 1, '', NULL, '2023-01-29 13:05:00', NULL, '2023-01-29 13:05:00', b'0');
INSERT INTO `system_job_log` VALUES (184, 2, 'testTask', '测试参数xxx', 1, '2023-01-29 14:05:00', '2023-01-29 14:05:00', 11, 1, '', NULL, '2023-01-29 14:05:00', NULL, '2023-01-29 14:05:00', b'0');
INSERT INTO `system_job_log` VALUES (185, 2, 'testTask', '测试参数xxx', 1, '2023-01-29 15:05:00', '2023-01-29 15:05:00', 13, 1, '', NULL, '2023-01-29 15:05:00', NULL, '2023-01-29 15:05:00', b'0');
INSERT INTO `system_job_log` VALUES (186, 2, 'testTask', '测试参数xxx', 1, '2023-01-29 16:05:00', '2023-01-29 16:05:00', 15, 1, '', NULL, '2023-01-29 16:05:00', NULL, '2023-01-29 16:05:00', b'0');
INSERT INTO `system_job_log` VALUES (187, 2, 'testTask', '测试参数xxx', 1, '2023-01-29 17:05:00', '2023-01-29 17:05:00', 16, 1, '', NULL, '2023-01-29 17:05:00', NULL, '2023-01-29 17:05:00', b'0');
INSERT INTO `system_job_log` VALUES (188, 2, 'testTask', '测试参数xxx', 1, '2023-01-29 18:05:00', '2023-01-29 18:05:00', 47, 1, '', NULL, '2023-01-29 18:05:00', NULL, '2023-01-29 18:05:00', b'0');
INSERT INTO `system_job_log` VALUES (189, 2, 'testTask', '测试参数xxx', 1, '2023-01-29 20:35:44', '2023-01-29 20:35:44', 17, 1, '', NULL, '2023-01-29 20:35:44', NULL, '2023-01-29 20:35:44', b'0');
INSERT INTO `system_job_log` VALUES (190, 2, 'testTask', '测试参数xxx', 1, '2023-01-30 10:56:32', '2023-01-30 10:56:32', 24, 1, '', NULL, '2023-01-30 10:56:32', NULL, '2023-01-30 10:56:32', b'0');
INSERT INTO `system_job_log` VALUES (191, 2, 'testTask', '测试参数xxx', 1, '2023-01-30 11:05:00', '2023-01-30 11:05:00', 16, 1, '', NULL, '2023-01-30 11:05:00', NULL, '2023-01-30 11:05:00', b'0');
INSERT INTO `system_job_log` VALUES (192, 2, 'testTask', '测试参数xxx', 1, '2023-01-30 12:05:00', '2023-01-30 12:05:00', 15, 1, '', NULL, '2023-01-30 12:05:00', NULL, '2023-01-30 12:05:00', b'0');
INSERT INTO `system_job_log` VALUES (193, 2, 'testTask', '测试参数xxx', 1, '2023-01-30 13:05:00', '2023-01-30 13:05:00', 38, 1, '', NULL, '2023-01-30 13:05:00', NULL, '2023-01-30 13:05:00', b'0');
INSERT INTO `system_job_log` VALUES (194, 2, 'testTask', '测试参数xxx', 1, '2023-01-30 14:05:00', '2023-01-30 14:05:00', 13, 1, '', NULL, '2023-01-30 14:05:00', NULL, '2023-01-30 14:05:00', b'0');
INSERT INTO `system_job_log` VALUES (195, 2, 'testTask', '测试参数xxx', 1, '2023-01-30 15:05:00', '2023-01-30 15:05:00', 15, 1, '', NULL, '2023-01-30 15:05:00', NULL, '2023-01-30 15:05:00', b'0');
INSERT INTO `system_job_log` VALUES (196, 2, 'testTask', '测试参数xxx', 1, '2023-01-30 16:05:00', '2023-01-30 16:05:00', 25, 1, '', NULL, '2023-01-30 16:05:00', NULL, '2023-01-30 16:05:00', b'0');
INSERT INTO `system_job_log` VALUES (197, 2, 'testTask', '测试参数xxx', 1, '2023-01-30 17:05:00', '2023-01-30 17:05:00', 34, 1, '', NULL, '2023-01-30 17:05:00', NULL, '2023-01-30 17:05:00', b'0');
INSERT INTO `system_job_log` VALUES (198, 2, 'testTask', '测试参数xxx', 1, '2023-01-30 18:05:00', '2023-01-30 18:05:00', 15, 1, '', NULL, '2023-01-30 18:05:00', NULL, '2023-01-30 18:05:00', b'0');
INSERT INTO `system_job_log` VALUES (199, 2, 'testTask', '测试参数xxx', 1, '2023-01-31 09:06:42', '2023-01-31 09:06:42', 63, 1, '', NULL, '2023-01-31 09:06:42', NULL, '2023-01-31 09:06:43', b'0');
INSERT INTO `system_job_log` VALUES (200, 2, 'testTask', '测试参数xxx', 1, '2023-01-31 10:05:00', '2023-01-31 10:05:00', 42, 1, '', NULL, '2023-01-31 10:05:00', NULL, '2023-01-31 10:05:00', b'0');
INSERT INTO `system_job_log` VALUES (201, 2, 'testTask', '测试参数xxx', 1, '2023-01-31 11:05:00', '2023-01-31 11:05:00', 13, 1, '', NULL, '2023-01-31 11:05:00', NULL, '2023-01-31 11:05:00', b'0');
INSERT INTO `system_job_log` VALUES (202, 2, 'testTask', '测试参数xxx', 1, '2023-01-31 12:05:00', '2023-01-31 12:05:00', 32, 1, '', NULL, '2023-01-31 12:05:00', NULL, '2023-01-31 12:05:00', b'0');
INSERT INTO `system_job_log` VALUES (203, 2, 'testTask', '测试参数xxx', 1, '2023-01-31 14:43:24', '2023-01-31 14:43:24', 49, 1, '', NULL, '2023-01-31 14:43:24', NULL, '2023-01-31 14:43:24', b'0');
INSERT INTO `system_job_log` VALUES (204, 2, 'testTask', '测试参数xxx', 1, '2023-01-31 15:05:00', '2023-01-31 15:05:00', 11, 1, '', NULL, '2023-01-31 15:05:00', NULL, '2023-01-31 15:05:00', b'0');
INSERT INTO `system_job_log` VALUES (205, 2, 'testTask', '测试参数xxx', 1, '2023-01-31 16:05:00', '2023-01-31 16:05:00', 17, 1, '', NULL, '2023-01-31 16:05:00', NULL, '2023-01-31 16:05:00', b'0');
INSERT INTO `system_job_log` VALUES (206, 2, 'testTask', '测试参数xxx', 1, '2023-01-31 17:05:00', '2023-01-31 17:05:00', 31, 1, '', NULL, '2023-01-31 17:05:00', NULL, '2023-01-31 17:05:00', b'0');
INSERT INTO `system_job_log` VALUES (207, 2, 'testTask', '测试参数xxx', 1, '2023-01-31 19:49:17', '2023-01-31 19:49:17', 45, 1, '', NULL, '2023-01-31 19:49:17', NULL, '2023-01-31 19:49:17', b'0');
INSERT INTO `system_job_log` VALUES (208, 2, 'testTask', '测试参数xxx', 1, '2023-02-08 17:01:11', '2023-02-08 17:01:11', 46, 1, '', NULL, '2023-02-08 17:01:11', NULL, '2023-02-08 17:01:11', b'0');
INSERT INTO `system_job_log` VALUES (209, 2, 'testTask', '测试参数xxx', 1, '2023-02-08 17:05:00', '2023-02-08 17:05:00', 45, 1, '', NULL, '2023-02-08 17:05:00', NULL, '2023-02-08 17:05:00', b'0');
INSERT INTO `system_job_log` VALUES (210, 2, 'testTask', '测试参数xxx', 1, '2023-02-08 18:05:00', '2023-02-08 18:05:00', 90, 1, '', NULL, '2023-02-08 18:05:00', NULL, '2023-02-08 18:05:00', b'0');
INSERT INTO `system_job_log` VALUES (211, 2, 'testTask', '测试参数xxx', 1, '2023-02-09 09:04:57', '2023-02-09 09:04:57', 57, 1, '', NULL, '2023-02-09 09:04:57', NULL, '2023-02-09 09:04:57', b'0');
INSERT INTO `system_job_log` VALUES (212, 2, 'testTask', '测试参数xxx', 1, '2023-02-09 09:05:00', '2023-02-09 09:05:00', 36, 1, '', NULL, '2023-02-09 09:05:00', NULL, '2023-02-09 09:05:00', b'0');
INSERT INTO `system_job_log` VALUES (213, 2, 'testTask', '测试参数xxx', 1, '2023-02-09 10:05:00', '2023-02-09 10:05:00', 44, 1, '', NULL, '2023-02-09 10:05:00', NULL, '2023-02-09 10:05:00', b'0');
INSERT INTO `system_job_log` VALUES (214, 2, 'testTask', '测试参数xxx', 1, '2023-02-09 11:05:00', '2023-02-09 11:05:00', 16, 1, '', NULL, '2023-02-09 11:05:00', NULL, '2023-02-09 11:05:00', b'0');
INSERT INTO `system_job_log` VALUES (215, 2, 'testTask', '测试参数xxx', 1, '2023-02-09 13:43:13', '2023-02-09 13:43:13', 112, 1, '', NULL, '2023-02-09 13:43:13', NULL, '2023-02-09 13:43:14', b'0');
INSERT INTO `system_job_log` VALUES (216, 2, 'testTask', '测试参数xxx', 1, '2023-02-09 14:05:00', '2023-02-09 14:05:00', 16, 1, '', NULL, '2023-02-09 14:05:00', NULL, '2023-02-09 14:05:00', b'0');
INSERT INTO `system_job_log` VALUES (217, 2, 'testTask', '测试参数xxx', 1, '2023-02-09 15:05:00', '2023-02-09 15:05:00', 32, 1, '', NULL, '2023-02-09 15:05:00', NULL, '2023-02-09 15:05:00', b'0');
INSERT INTO `system_job_log` VALUES (218, 2, 'testTask', '测试参数xxx', 1, '2023-02-09 16:05:00', '2023-02-09 16:05:00', 41, 1, '', NULL, '2023-02-09 16:05:00', NULL, '2023-02-09 16:05:00', b'0');
INSERT INTO `system_job_log` VALUES (219, 2, 'testTask', '测试参数xxx', 1, '2023-02-09 17:05:00', '2023-02-09 17:05:00', 32, 1, '', NULL, '2023-02-09 17:05:00', NULL, '2023-02-09 17:05:00', b'0');
INSERT INTO `system_job_log` VALUES (220, 2, 'testTask', '测试参数xxx', 1, '2023-02-09 18:05:00', '2023-02-09 18:05:00', 16, 1, '', NULL, '2023-02-09 18:05:00', NULL, '2023-02-09 18:05:00', b'0');
INSERT INTO `system_job_log` VALUES (221, 2, 'testTask', '测试参数xxx', 1, '2023-02-10 10:00:04', '2023-02-10 10:00:04', 83, 1, '', NULL, '2023-02-10 10:00:04', NULL, '2023-02-10 10:00:04', b'0');
INSERT INTO `system_job_log` VALUES (222, 2, 'testTask', '测试参数xxx', 1, '2023-02-10 10:05:00', '2023-02-10 10:05:00', 12, 1, '', NULL, '2023-02-10 10:05:00', NULL, '2023-02-10 10:05:00', b'0');
INSERT INTO `system_job_log` VALUES (223, 2, 'testTask', '测试参数xxx', 1, '2023-02-10 11:05:00', '2023-02-10 11:05:00', 15, 1, '', NULL, '2023-02-10 11:05:00', NULL, '2023-02-10 11:05:00', b'0');
INSERT INTO `system_job_log` VALUES (224, 2, 'testTask', '测试参数xxx', 1, '2023-02-10 12:05:00', '2023-02-10 12:05:00', 19, 1, '', NULL, '2023-02-10 12:05:00', NULL, '2023-02-10 12:05:00', b'0');
INSERT INTO `system_job_log` VALUES (225, 2, 'testTask', '测试参数xxx', 1, '2023-02-10 13:05:00', '2023-02-10 13:05:00', 12, 1, '', NULL, '2023-02-10 13:05:00', NULL, '2023-02-10 13:05:00', b'0');
INSERT INTO `system_job_log` VALUES (226, 2, 'testTask', '测试参数xxx', 1, '2023-02-10 14:05:00', '2023-02-10 14:05:00', 13, 1, '', NULL, '2023-02-10 14:05:00', NULL, '2023-02-10 14:05:00', b'0');
INSERT INTO `system_job_log` VALUES (227, 2, 'testTask', '测试参数xxx', 1, '2023-02-10 15:05:00', '2023-02-10 15:05:00', 13, 1, '', NULL, '2023-02-10 15:05:00', NULL, '2023-02-10 15:05:00', b'0');
INSERT INTO `system_job_log` VALUES (228, 2, 'testTask', '测试参数xxx', 1, '2023-02-10 16:05:00', '2023-02-10 16:05:00', 36, 1, '', NULL, '2023-02-10 16:05:00', NULL, '2023-02-10 16:05:00', b'0');
INSERT INTO `system_job_log` VALUES (229, 2, 'testTask', '测试参数xxx', 1, '2023-02-10 17:05:00', '2023-02-10 17:05:00', 11, 1, '', NULL, '2023-02-10 17:05:00', NULL, '2023-02-10 17:05:00', b'0');
INSERT INTO `system_job_log` VALUES (230, 2, 'testTask', '测试参数xxx', 1, '2023-02-10 18:05:00', '2023-02-10 18:05:00', 15, 1, '', NULL, '2023-02-10 18:05:00', NULL, '2023-02-10 18:05:00', b'0');
INSERT INTO `system_job_log` VALUES (231, 2, 'testTask', '测试参数xxx', 1, '2023-02-10 19:05:00', '2023-02-10 19:05:00', 14, 1, '', NULL, '2023-02-10 19:05:00', NULL, '2023-02-10 19:05:00', b'0');
INSERT INTO `system_job_log` VALUES (232, 2, 'testTask', '测试参数xxx', 1, '2023-02-10 20:05:00', '2023-02-10 20:05:00', 15, 1, '', NULL, '2023-02-10 20:05:00', NULL, '2023-02-10 20:05:00', b'0');
INSERT INTO `system_job_log` VALUES (233, 2, 'testTask', '测试参数xxx', 1, '2023-02-11 08:56:56', '2023-02-11 08:56:56', 148, 1, '', NULL, '2023-02-11 08:56:56', NULL, '2023-02-11 08:56:56', b'0');
INSERT INTO `system_job_log` VALUES (234, 2, 'testTask', '测试参数xxx', 1, '2023-02-11 09:05:00', '2023-02-11 09:05:00', 16, 1, '', NULL, '2023-02-11 09:05:00', NULL, '2023-02-11 09:05:00', b'0');
INSERT INTO `system_job_log` VALUES (235, 2, 'testTask', '测试参数xxx', 1, '2023-02-11 10:05:00', '2023-02-11 10:05:00', 19, 1, '', NULL, '2023-02-11 10:05:00', NULL, '2023-02-11 10:05:00', b'0');
INSERT INTO `system_job_log` VALUES (236, 2, 'testTask', '测试参数xxx', 1, '2023-02-11 11:05:00', '2023-02-11 11:05:00', 13, 1, '', NULL, '2023-02-11 11:05:00', NULL, '2023-02-11 11:05:00', b'0');
INSERT INTO `system_job_log` VALUES (237, 2, 'testTask', '测试参数xxx', 1, '2023-02-11 13:55:17', '2023-02-11 13:55:17', 28, 1, '', NULL, '2023-02-11 13:55:17', NULL, '2023-02-11 13:55:17', b'0');
INSERT INTO `system_job_log` VALUES (238, 2, 'testTask', '测试参数xxx', 1, '2023-02-11 14:05:00', '2023-02-11 14:05:00', 11, 1, '', NULL, '2023-02-11 14:05:00', NULL, '2023-02-11 14:05:00', b'0');
INSERT INTO `system_job_log` VALUES (239, 2, 'testTask', '测试参数xxx', 1, '2023-02-11 15:05:00', '2023-02-11 15:05:00', 14, 1, '', NULL, '2023-02-11 15:05:00', NULL, '2023-02-11 15:05:00', b'0');
INSERT INTO `system_job_log` VALUES (240, 2, 'testTask', '测试参数xxx', 1, '2023-02-12 16:09:07', '2023-02-12 16:09:07', 55, 1, '', NULL, '2023-02-12 16:09:07', NULL, '2023-02-12 16:09:07', b'0');
INSERT INTO `system_job_log` VALUES (241, 2, 'testTask', '测试参数xxx', 1, '2023-02-12 17:05:00', '2023-02-12 17:05:00', 12, 1, '', NULL, '2023-02-12 17:05:00', NULL, '2023-02-12 17:05:00', b'0');
INSERT INTO `system_job_log` VALUES (242, 2, 'testTask', '测试参数xxx', 1, '2023-02-12 18:30:03', '2023-02-12 18:30:03', 52, 1, '', NULL, '2023-02-12 18:30:03', NULL, '2023-02-12 18:30:03', b'0');
INSERT INTO `system_job_log` VALUES (243, 2, 'testTask', '测试参数xxx', 1, '2023-02-12 19:05:00', '2023-02-12 19:05:00', 13, 1, '', NULL, '2023-02-12 19:05:00', NULL, '2023-02-12 19:05:00', b'0');
INSERT INTO `system_job_log` VALUES (244, 2, 'testTask', '测试参数xxx', 1, '2023-02-12 20:05:00', '2023-02-12 20:05:00', 18, 1, '', NULL, '2023-02-12 20:05:00', NULL, '2023-02-12 20:05:00', b'0');
INSERT INTO `system_job_log` VALUES (245, 2, 'testTask', '测试参数xxx', 1, '2023-02-12 21:05:00', '2023-02-12 21:05:00', 14, 1, '', NULL, '2023-02-12 21:05:00', NULL, '2023-02-12 21:05:00', b'0');
INSERT INTO `system_job_log` VALUES (246, 2, 'testTask', '测试参数xxx', 1, '2023-02-13 08:12:55', '2023-02-13 08:12:55', 29, 1, '', NULL, '2023-02-13 08:12:55', NULL, '2023-02-13 08:12:55', b'0');
INSERT INTO `system_job_log` VALUES (247, 2, 'testTask', '测试参数xxx', 1, '2023-02-13 09:05:00', '2023-02-13 09:05:00', 13, 1, '', NULL, '2023-02-13 09:05:00', NULL, '2023-02-13 09:05:00', b'0');
INSERT INTO `system_job_log` VALUES (248, 2, 'testTask', '测试参数xxx', 1, '2023-02-13 10:05:13', '2023-02-13 10:05:13', 48, 1, '', NULL, '2023-02-13 10:05:13', NULL, '2023-02-13 10:05:13', b'0');
INSERT INTO `system_job_log` VALUES (249, 2, 'testTask', '测试参数xxx', 1, '2023-02-13 11:05:00', '2023-02-13 11:05:00', 13, 1, '', NULL, '2023-02-13 11:05:00', NULL, '2023-02-13 11:05:00', b'0');
INSERT INTO `system_job_log` VALUES (250, 2, 'testTask', '测试参数xxx', 1, '2023-02-13 14:19:08', '2023-02-13 14:19:08', 125, 1, '', NULL, '2023-02-13 14:19:08', NULL, '2023-02-13 14:19:08', b'0');
INSERT INTO `system_job_log` VALUES (251, 2, 'testTask', '测试参数xxx', 1, '2023-02-13 15:05:00', '2023-02-13 15:05:00', 11, 1, '', NULL, '2023-02-13 15:05:00', NULL, '2023-02-13 15:05:00', b'0');
INSERT INTO `system_job_log` VALUES (252, 2, 'testTask', '测试参数xxx', 1, '2023-02-13 16:05:00', '2023-02-13 16:05:00', 11, 1, '', NULL, '2023-02-13 16:05:00', NULL, '2023-02-13 16:05:00', b'0');
INSERT INTO `system_job_log` VALUES (253, 2, 'testTask', '测试参数xxx', 1, '2023-02-18 09:40:35', '2023-02-18 09:40:35', 44, 1, '', NULL, '2023-02-18 09:40:35', NULL, '2023-02-18 09:40:35', b'0');
INSERT INTO `system_job_log` VALUES (254, 2, 'testTask', '测试参数xxx', 1, '2023-02-18 10:05:00', '2023-02-18 10:05:00', 22, 1, '', NULL, '2023-02-18 10:05:00', NULL, '2023-02-18 10:05:00', b'0');
INSERT INTO `system_job_log` VALUES (255, 2, 'testTask', '测试参数xxx', 1, '2023-02-18 11:05:00', '2023-02-18 11:05:00', 28, 1, '', NULL, '2023-02-18 11:05:00', NULL, '2023-02-18 11:05:00', b'0');
INSERT INTO `system_job_log` VALUES (256, 2, 'testTask', '测试参数xxx', 1, '2023-02-18 12:05:00', '2023-02-18 12:05:00', 26, 1, '', NULL, '2023-02-18 12:05:00', NULL, '2023-02-18 12:05:00', b'0');
INSERT INTO `system_job_log` VALUES (257, 2, 'testTask', '测试参数xxx', 1, '2023-02-18 13:05:00', '2023-02-18 13:05:00', 21, 1, '', NULL, '2023-02-18 13:05:00', NULL, '2023-02-18 13:05:00', b'0');
INSERT INTO `system_job_log` VALUES (258, 2, 'testTask', '测试参数xxx', 1, '2023-02-18 14:05:00', '2023-02-18 14:05:00', 49, 1, '', NULL, '2023-02-18 14:05:00', NULL, '2023-02-18 14:05:00', b'0');
INSERT INTO `system_job_log` VALUES (259, 2, 'testTask', '测试参数xxx', 1, '2023-02-18 15:05:00', '2023-02-18 15:05:00', 41, 1, '', NULL, '2023-02-18 15:05:00', NULL, '2023-02-18 15:05:00', b'0');
INSERT INTO `system_job_log` VALUES (260, 2, 'testTask', '测试参数xxx', 1, '2023-02-18 16:05:00', '2023-02-18 16:05:00', 18, 1, '', NULL, '2023-02-18 16:05:00', NULL, '2023-02-18 16:05:00', b'0');
INSERT INTO `system_job_log` VALUES (261, 2, 'testTask', '测试参数xxx', 1, '2023-02-18 17:05:00', '2023-02-18 17:05:00', 19, 1, '', NULL, '2023-02-18 17:05:00', NULL, '2023-02-18 17:05:00', b'0');
INSERT INTO `system_job_log` VALUES (262, 2, 'testTask', '测试参数xxx', 1, '2023-02-18 18:05:00', '2023-02-18 18:05:00', 19, 1, '', NULL, '2023-02-18 18:05:00', NULL, '2023-02-18 18:05:00', b'0');
INSERT INTO `system_job_log` VALUES (263, 2, 'testTask', '测试参数xxx', 1, '2023-02-19 17:29:56', '2023-02-19 17:29:56', 276, 1, '', NULL, '2023-02-19 17:29:56', NULL, '2023-02-19 17:29:56', b'0');
INSERT INTO `system_job_log` VALUES (264, 2, 'testTask', '测试参数xxx', 1, '2023-02-22 21:02:07', '2023-02-22 21:02:07', 54, 1, '', NULL, '2023-02-22 21:02:07', NULL, '2023-02-22 21:02:07', b'0');
INSERT INTO `system_job_log` VALUES (265, 2, 'testTask', '测试参数xxx', 1, '2023-02-22 21:05:00', '2023-02-22 21:05:00', 16, 1, '', NULL, '2023-02-22 21:05:00', NULL, '2023-02-22 21:05:00', b'0');
INSERT INTO `system_job_log` VALUES (266, 2, 'testTask', '测试参数xxx', 1, '2023-02-22 22:05:00', '2023-02-22 22:05:00', 19, 1, '', NULL, '2023-02-22 22:05:00', NULL, '2023-02-22 22:05:00', b'0');
INSERT INTO `system_job_log` VALUES (267, 2, 'testTask', '测试参数xxx', 1, '2023-02-23 08:32:01', '2023-02-23 08:32:01', 29, 1, '', NULL, '2023-02-23 08:32:01', NULL, '2023-02-23 08:32:01', b'0');
INSERT INTO `system_job_log` VALUES (268, 2, 'testTask', '测试参数xxx', 1, '2023-02-23 09:05:00', '2023-02-23 09:05:00', 19, 1, '', NULL, '2023-02-23 09:05:00', NULL, '2023-02-23 09:05:00', b'0');
INSERT INTO `system_job_log` VALUES (269, 2, 'testTask', '测试参数xxx', 1, '2023-02-23 10:05:00', '2023-02-23 10:05:00', 19, 1, '', NULL, '2023-02-23 10:05:00', NULL, '2023-02-23 10:05:00', b'0');
INSERT INTO `system_job_log` VALUES (270, 2, 'testTask', '测试参数xxx', 1, '2023-02-23 11:05:00', '2023-02-23 11:05:00', 26, 1, '', NULL, '2023-02-23 11:05:00', NULL, '2023-02-23 11:05:00', b'0');
INSERT INTO `system_job_log` VALUES (271, 2, 'testTask', '测试参数xxx', 1, '2023-02-23 12:05:00', '2023-02-23 12:05:00', 55, 1, '', NULL, '2023-02-23 12:05:00', NULL, '2023-02-23 12:05:00', b'0');
INSERT INTO `system_job_log` VALUES (272, 2, 'testTask', '测试参数xxx', 1, '2023-02-23 13:05:00', '2023-02-23 13:05:00', 16, 1, '', NULL, '2023-02-23 13:05:00', NULL, '2023-02-23 13:05:00', b'0');
INSERT INTO `system_job_log` VALUES (273, 2, 'testTask', '测试参数xxx', 1, '2023-02-23 14:05:00', '2023-02-23 14:05:00', 16, 1, '', NULL, '2023-02-23 14:05:00', NULL, '2023-02-23 14:05:00', b'0');
INSERT INTO `system_job_log` VALUES (274, 2, 'testTask', '测试参数xxx', 1, '2023-02-23 15:05:00', '2023-02-23 15:05:00', 30, 1, '', NULL, '2023-02-23 15:05:00', NULL, '2023-02-23 15:05:00', b'0');
INSERT INTO `system_job_log` VALUES (275, 2, 'testTask', '测试参数xxx', 1, '2023-02-23 16:05:00', '2023-02-23 16:05:00', 14, 1, '', NULL, '2023-02-23 16:05:00', NULL, '2023-02-23 16:05:00', b'0');
INSERT INTO `system_job_log` VALUES (276, 2, 'testTask', '测试参数xxx', 1, '2023-02-23 17:05:00', '2023-02-23 17:05:00', 18, 1, '', NULL, '2023-02-23 17:05:00', NULL, '2023-02-23 17:05:00', b'0');
INSERT INTO `system_job_log` VALUES (277, 2, 'testTask', '测试参数xxx', 1, '2023-02-23 18:05:00', '2023-02-23 18:05:00', 19, 1, '', NULL, '2023-02-23 18:05:00', NULL, '2023-02-23 18:05:00', b'0');
INSERT INTO `system_job_log` VALUES (278, 2, 'testTask', '测试参数xxx', 1, '2023-02-23 19:05:00', '2023-02-23 19:05:00', 12, 1, '', NULL, '2023-02-23 19:05:00', NULL, '2023-02-23 19:05:00', b'0');
INSERT INTO `system_job_log` VALUES (279, 2, 'testTask', '测试参数xxx', 1, '2023-02-24 08:39:25', '2023-02-24 08:39:25', 503, 1, '', NULL, '2023-02-24 08:39:25', NULL, '2023-02-24 08:39:26', b'0');
INSERT INTO `system_job_log` VALUES (280, 2, 'testTask', '测试参数xxx', 1, '2023-02-24 09:05:00', '2023-02-24 09:05:00', 51, 1, '', NULL, '2023-02-24 09:05:00', NULL, '2023-02-24 09:05:00', b'0');
INSERT INTO `system_job_log` VALUES (281, 2, 'testTask', '测试参数xxx', 1, '2023-02-24 10:05:00', '2023-02-24 10:05:00', 38, 1, '', NULL, '2023-02-24 10:05:00', NULL, '2023-02-24 10:05:00', b'0');
INSERT INTO `system_job_log` VALUES (282, 2, 'testTask', '测试参数xxx', 1, '2023-02-24 11:05:00', '2023-02-24 11:05:00', 18, 1, '', NULL, '2023-02-24 11:05:00', NULL, '2023-02-24 11:05:00', b'0');
INSERT INTO `system_job_log` VALUES (283, 2, 'testTask', '测试参数xxx', 1, '2023-02-24 12:05:00', '2023-02-24 12:05:00', 18, 1, '', NULL, '2023-02-24 12:05:00', NULL, '2023-02-24 12:05:00', b'0');
INSERT INTO `system_job_log` VALUES (284, 2, 'testTask', '测试参数xxx', 1, '2023-02-24 13:05:00', '2023-02-24 13:05:00', 20, 1, '', NULL, '2023-02-24 13:05:00', NULL, '2023-02-24 13:05:00', b'0');
INSERT INTO `system_job_log` VALUES (285, 2, 'testTask', '测试参数xxx', 1, '2023-02-24 14:05:00', '2023-02-24 14:05:00', 17, 1, '', NULL, '2023-02-24 14:05:00', NULL, '2023-02-24 14:05:00', b'0');
INSERT INTO `system_job_log` VALUES (286, 2, 'testTask', '测试参数xxx', 1, '2023-02-24 15:05:00', '2023-02-24 15:05:00', 18, 1, '', NULL, '2023-02-24 15:05:00', NULL, '2023-02-24 15:05:00', b'0');
INSERT INTO `system_job_log` VALUES (287, 2, 'testTask', '测试参数xxx', 1, '2023-02-24 16:05:00', '2023-02-24 16:05:00', 17, 1, '', NULL, '2023-02-24 16:05:00', NULL, '2023-02-24 16:05:00', b'0');
INSERT INTO `system_job_log` VALUES (288, 2, 'testTask', '测试参数xxx', 1, '2023-02-24 17:05:00', '2023-02-24 17:05:00', 20, 1, '', NULL, '2023-02-24 17:05:00', NULL, '2023-02-24 17:05:00', b'0');
INSERT INTO `system_job_log` VALUES (289, 2, 'testTask', '测试参数xxx', 1, '2023-02-24 18:05:00', '2023-02-24 18:05:00', 21, 1, '', NULL, '2023-02-24 18:05:00', NULL, '2023-02-24 18:05:00', b'0');
INSERT INTO `system_job_log` VALUES (290, 2, 'testTask', '测试参数xxx', 1, '2023-02-24 19:05:00', '2023-02-24 19:05:00', 35, 1, '', NULL, '2023-02-24 19:05:00', NULL, '2023-02-24 19:05:00', b'0');
INSERT INTO `system_job_log` VALUES (291, 2, 'testTask', '测试参数xxx', 1, '2023-02-24 20:05:00', '2023-02-24 20:05:00', 21, 1, '', NULL, '2023-02-24 20:05:00', NULL, '2023-02-24 20:05:00', b'0');
INSERT INTO `system_job_log` VALUES (292, 2, 'testTask', '测试参数xxx', 1, '2023-02-25 09:30:25', '2023-02-25 09:30:25', 58, 1, '', NULL, '2023-02-25 09:30:25', NULL, '2023-02-25 09:30:25', b'0');
INSERT INTO `system_job_log` VALUES (293, 2, 'testTask', '测试参数xxx', 1, '2023-02-25 10:05:00', '2023-02-25 10:05:00', 19, 1, '', NULL, '2023-02-25 10:05:00', NULL, '2023-02-25 10:05:00', b'0');
INSERT INTO `system_job_log` VALUES (294, 2, 'testTask', '测试参数xxx', 1, '2023-02-25 11:05:00', '2023-02-25 11:05:00', 18, 1, '', NULL, '2023-02-25 11:05:00', NULL, '2023-02-25 11:05:00', b'0');
INSERT INTO `system_job_log` VALUES (295, 2, 'testTask', '测试参数xxx', 1, '2023-02-25 12:05:00', '2023-02-25 12:05:00', 15, 1, '', NULL, '2023-02-25 12:05:00', NULL, '2023-02-25 12:05:00', b'0');
INSERT INTO `system_job_log` VALUES (296, 2, 'testTask', '测试参数xxx', 1, '2023-02-25 13:05:00', '2023-02-25 13:05:00', 15, 1, '', NULL, '2023-02-25 13:05:00', NULL, '2023-02-25 13:05:00', b'0');
INSERT INTO `system_job_log` VALUES (297, 2, 'testTask', '测试参数xxx', 1, '2023-02-25 14:05:00', '2023-02-25 14:05:00', 20, 1, '', NULL, '2023-02-25 14:05:00', NULL, '2023-02-25 14:05:00', b'0');
INSERT INTO `system_job_log` VALUES (298, 2, 'testTask', '测试参数xxx', 1, '2023-02-25 15:05:00', '2023-02-25 15:05:00', 15, 1, '', NULL, '2023-02-25 15:05:00', NULL, '2023-02-25 15:05:00', b'0');
INSERT INTO `system_job_log` VALUES (299, 2, 'testTask', '测试参数xxx', 1, '2023-02-25 16:05:00', '2023-02-25 16:05:00', 14, 1, '', NULL, '2023-02-25 16:05:00', NULL, '2023-02-25 16:05:00', b'0');
INSERT INTO `system_job_log` VALUES (300, 2, 'testTask', '测试参数xxx', 1, '2023-02-25 17:05:00', '2023-02-25 17:05:00', 17, 1, '', NULL, '2023-02-25 17:05:00', NULL, '2023-02-25 17:05:00', b'0');
INSERT INTO `system_job_log` VALUES (301, 2, 'testTask', '测试参数xxx', 1, '2023-02-25 18:05:00', '2023-02-25 18:05:00', 20, 1, '', NULL, '2023-02-25 18:05:00', NULL, '2023-02-25 18:05:00', b'0');
INSERT INTO `system_job_log` VALUES (302, 2, 'testTask', '测试参数xxx', 1, '2023-02-25 19:05:00', '2023-02-25 19:05:00', 13, 1, '', NULL, '2023-02-25 19:05:00', NULL, '2023-02-25 19:05:00', b'0');
INSERT INTO `system_job_log` VALUES (303, 2, 'testTask', '测试参数xxx', 1, '2023-02-25 20:05:00', '2023-02-25 20:05:00', 16, 1, '', NULL, '2023-02-25 20:05:00', NULL, '2023-02-25 20:05:00', b'0');
INSERT INTO `system_job_log` VALUES (304, 2, 'testTask', '测试参数xxx', 1, '2023-02-26 14:20:09', '2023-02-26 14:20:09', 50, 1, '', NULL, '2023-02-26 14:20:09', NULL, '2023-02-26 14:20:09', b'0');
INSERT INTO `system_job_log` VALUES (305, 2, 'testTask', '测试参数xxx', 1, '2023-02-28 16:54:24', '2023-02-28 16:54:24', 61, 1, '', NULL, '2023-02-28 16:54:24', NULL, '2023-02-28 16:54:24', b'0');
INSERT INTO `system_job_log` VALUES (306, 2, 'testTask', '测试参数xxx', 1, '2023-02-28 17:05:00', '2023-02-28 17:05:00', 23, 1, '', NULL, '2023-02-28 17:05:00', NULL, '2023-02-28 17:05:00', b'0');
INSERT INTO `system_job_log` VALUES (307, 2, 'testTask', '测试参数xxx', 1, '2023-02-28 18:05:00', '2023-02-28 18:05:00', 20, 1, '', NULL, '2023-02-28 18:05:00', NULL, '2023-02-28 18:05:00', b'0');
INSERT INTO `system_job_log` VALUES (308, 2, 'testTask', '测试参数xxx', 1, '2023-02-28 19:05:00', '2023-02-28 19:05:00', 55, 1, '', NULL, '2023-02-28 19:05:00', NULL, '2023-02-28 19:05:00', b'0');
INSERT INTO `system_job_log` VALUES (309, 2, 'testTask', '测试参数xxx', 1, '2023-03-01 15:51:03', '2023-03-01 15:51:03', 50, 1, '', NULL, '2023-03-01 15:51:03', NULL, '2023-03-01 15:51:03', b'0');
INSERT INTO `system_job_log` VALUES (310, 2, 'testTask', '测试参数xxx', 1, '2023-03-01 17:16:10', '2023-03-01 17:16:10', 47, 1, '', NULL, '2023-03-01 17:16:10', NULL, '2023-03-01 17:16:10', b'0');
INSERT INTO `system_job_log` VALUES (311, 2, 'testTask', '测试参数xxx', 1, '2023-03-01 20:30:44', '2023-03-01 20:30:44', 98, 1, '', NULL, '2023-03-01 20:30:44', NULL, '2023-03-01 20:30:44', b'0');
INSERT INTO `system_job_log` VALUES (312, 2, 'testTask', '测试参数xxx', 1, '2023-03-01 21:05:00', '2023-03-01 21:05:00', 11, 1, '', NULL, '2023-03-01 21:05:00', NULL, '2023-03-01 21:05:00', b'0');
INSERT INTO `system_job_log` VALUES (313, 2, 'testTask', '测试参数xxx', 1, '2023-03-01 22:05:00', '2023-03-01 22:05:00', 14, 1, '', NULL, '2023-03-01 22:05:00', NULL, '2023-03-01 22:05:00', b'0');
INSERT INTO `system_job_log` VALUES (314, 2, 'testTask', '测试参数xxx', 1, '2023-03-01 23:05:00', '2023-03-01 23:05:00', 14, 1, '', NULL, '2023-03-01 23:05:00', NULL, '2023-03-01 23:05:00', b'0');
INSERT INTO `system_job_log` VALUES (315, 2, 'testTask', '测试参数xxx', 1, '2023-03-03 19:07:48', '2023-03-03 19:07:48', 65, 1, '', NULL, '2023-03-03 19:07:48', NULL, '2023-03-03 19:07:48', b'0');
INSERT INTO `system_job_log` VALUES (316, 2, 'testTask', '测试参数xxx', 1, '2023-03-03 20:05:00', '2023-03-03 20:05:00', 21, 1, '', NULL, '2023-03-03 20:05:00', NULL, '2023-03-03 20:05:00', b'0');
INSERT INTO `system_job_log` VALUES (317, 2, 'testTask', '测试参数xxx', 1, '2023-03-03 21:05:00', '2023-03-03 21:05:00', 10, 1, '', NULL, '2023-03-03 21:05:00', NULL, '2023-03-03 21:05:00', b'0');
INSERT INTO `system_job_log` VALUES (318, 2, 'testTask', '测试参数xxx', 1, '2023-03-03 22:05:00', '2023-03-03 22:05:00', 11, 1, '', NULL, '2023-03-03 22:05:00', NULL, '2023-03-03 22:05:00', b'0');
INSERT INTO `system_job_log` VALUES (319, 2, 'testTask', '测试参数xxx', 1, '2023-03-03 23:05:00', '2023-03-03 23:05:00', 11, 1, '', NULL, '2023-03-03 23:05:00', NULL, '2023-03-03 23:05:00', b'0');
INSERT INTO `system_job_log` VALUES (320, 2, 'testTask', '测试参数xxx', 1, '2023-03-04 00:05:00', '2023-03-04 00:05:00', 11, 1, '', NULL, '2023-03-04 00:05:00', NULL, '2023-03-04 00:05:00', b'0');
INSERT INTO `system_job_log` VALUES (321, 2, 'testTask', '测试参数xxx', 1, '2023-03-04 09:25:35', '2023-03-04 09:25:35', 13, 1, '', NULL, '2023-03-04 09:25:35', NULL, '2023-03-04 09:25:35', b'0');
INSERT INTO `system_job_log` VALUES (322, 2, 'testTask', '测试参数xxx', 1, '2023-03-04 10:05:00', '2023-03-04 10:05:00', 9, 1, '', NULL, '2023-03-04 10:05:00', NULL, '2023-03-04 10:05:00', b'0');
INSERT INTO `system_job_log` VALUES (323, 2, 'testTask', '测试参数xxx', 1, '2023-03-04 11:05:00', '2023-03-04 11:05:00', 12, 1, '', NULL, '2023-03-04 11:05:00', NULL, '2023-03-04 11:05:00', b'0');
INSERT INTO `system_job_log` VALUES (324, 2, 'testTask', '测试参数xxx', 1, '2023-03-04 14:21:50', '2023-03-04 14:21:50', 15, 1, '', NULL, '2023-03-04 14:21:50', NULL, '2023-03-04 14:21:50', b'0');
INSERT INTO `system_job_log` VALUES (325, 2, 'testTask', '测试参数xxx', 1, '2023-03-04 15:05:00', '2023-03-04 15:05:00', 11, 1, '', NULL, '2023-03-04 15:05:00', NULL, '2023-03-04 15:05:00', b'0');
INSERT INTO `system_job_log` VALUES (326, 2, 'testTask', '测试参数xxx', 1, '2023-03-04 16:05:00', '2023-03-04 16:05:00', 11, 1, '', NULL, '2023-03-04 16:05:00', NULL, '2023-03-04 16:05:00', b'0');
INSERT INTO `system_job_log` VALUES (327, 2, 'testTask', '测试参数xxx', 1, '2023-03-04 17:05:17', '2023-03-04 17:05:17', 44, 1, '', NULL, '2023-03-04 17:05:17', NULL, '2023-03-04 17:05:17', b'0');

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
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_menu
-- ----------------------------
INSERT INTO `system_menu` VALUES (1, '系统管理', '', 1, 0, 0, 'system', 'fa fa-windows', '', b'1', b'0', 'admin', '2023-01-30 16:32:59', 'admin', '2023-03-04 10:08:55', b'0');
INSERT INTO `system_menu` VALUES (2, '菜单管理', 'system:menu:query', 2, 0, 1, 'menu', 'el-icon-Menu', 'system/menu/index', b'1', b'1', 'admin', '2023-01-30 16:34:56', 'admin', '2023-03-04 10:09:10', b'0');
INSERT INTO `system_menu` VALUES (3, '新增菜单', 'system:menu:create', 3, 0, 2, '', '', '', b'1', b'0', 'admin', '2023-01-30 16:36:34', 'admin', '2023-03-04 10:09:12', b'0');
INSERT INTO `system_menu` VALUES (4, '删除菜单', 'system:menu:delete', 3, 0, 2, '', '', '', b'1', b'0', 'admin', '2023-01-30 16:37:05', 'admin', '2023-03-04 10:09:16', b'0');
INSERT INTO `system_menu` VALUES (5, '修改菜单', 'system:menu:update', 3, 0, 2, '', '', '', b'1', b'0', 'admin', '2023-01-30 16:37:14', 'admin', '2023-02-24 12:00:32', b'0');
INSERT INTO `system_menu` VALUES (7, '代码生成', 'system:codegen:query', 2, 0, 1, 'codegen', 'fa fa-copy', 'system/codegen/index', b'1', b'1', 'admin', '2023-02-26 09:56:23', 'admin', '2023-02-28 18:28:52', b'0');
INSERT INTO `system_menu` VALUES (8, '菜单查询', 'system:menu:query', 3, 0, 2, '', '#', '', b'1', b'1', 'admin', '2023-02-26 11:00:30', 'admin', '2023-02-26 03:58:56', b'1');
INSERT INTO `system_menu` VALUES (9, '新增代码模板', 'system:codegen:create', 3, 0, 7, '', '#', '', b'1', b'0', 'admin', '2023-02-26 11:01:15', 'admin', '2023-02-26 14:54:09', b'0');
INSERT INTO `system_menu` VALUES (10, '修改代码模板', 'system:codegen:update', 3, 0, 7, '', '#', '', b'1', b'0', 'admin', '2023-02-26 11:02:04', 'admin', '2023-02-26 14:55:08', b'0');
INSERT INTO `system_menu` VALUES (11, '删除代码模板', 'system:codegen:delete', 3, 0, 7, '', '#', '', b'1', b'0', 'admin', '2023-02-26 11:02:31', 'admin', '2023-02-26 14:55:11', b'0');
INSERT INTO `system_menu` VALUES (12, '控制台', 'dashboard', 2, -1, 0, 'dashboard', 'fa fa-dashboard', 'dashboard', b'1', b'1', 'admin', '2023-02-26 11:17:04', 'admin', '2023-02-26 14:21:20', b'0');
INSERT INTO `system_menu` VALUES (14, '用户管理', 'system:user:query', 2, 0, 1, 'user', '', 'system/user/index', b'1', b'1', 'admin', '2023-03-03 21:28:22', NULL, '2023-03-03 21:28:22', b'0');

-- ----------------------------
-- Table structure for system_post
-- ----------------------------
DROP TABLE IF EXISTS `system_post`;
CREATE TABLE `system_post`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '岗位名称',
  `sort` int NOT NULL COMMENT '显示顺序',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '岗位信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_post
-- ----------------------------
INSERT INTO `system_post` VALUES (1, '开发工程师', 0, 'admin', '2023-01-31 09:52:17', NULL, '2023-01-31 09:52:17', b'0');
INSERT INTO `system_post` VALUES (2, '项目经理', 0, 'admin', '2023-01-31 09:52:42', NULL, '2023-01-31 09:52:42', b'0');

-- ----------------------------
-- Table structure for system_role
-- ----------------------------
DROP TABLE IF EXISTS `system_role`;
CREATE TABLE `system_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色权限字符串',
  `sort` int NOT NULL COMMENT '显示顺序',
  `status` tinyint NOT NULL COMMENT '角色状态（0正常 1停用）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_role
-- ----------------------------
INSERT INTO `system_role` VALUES (1, '超级管理员', 'super_admin', 0, 1, '拥有所有权限..', 'admin', '2023-01-31 11:28:08', 'admin', '2023-01-31 16:42:45', b'0');
INSERT INTO `system_role` VALUES (2, '普通角色', 'common', 0, 1, '少许查询权限', '', '2023-01-31 08:32:27', '', '2023-01-31 08:32:37', b'0');

-- ----------------------------
-- Table structure for system_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `system_role_menu`;
CREATE TABLE `system_role_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增编号',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_role_menu
-- ----------------------------

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
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门id',
  `post_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '职位ids',
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
  UNIQUE INDEX `idx_email`(`email` ASC) USING BTREE,
  UNIQUE INDEX `idx_mobile`(`mobile` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_user
-- ----------------------------
INSERT INTO `system_user` VALUES (1, 'admin', '$2a$10$P6AXMyJm0Q3E9Li2f2ts6OBK0uyPUPo/Wlm7U4mUy019TuxoXnjLe', '洋芋_sheepyu', '3029944576@qq.com', '18311112222', 'https://thirdwx.qlogo.cn/mmopen/vi_32/XF9G5zQOXaLVgO739DoicP1ibNtDSl52swglcDOiclNcNtzLicpYcrXjKBsibiaMkxY9MrQ13vdBiaGjM2EjGhVI24m7Q/132', 1, '[1,2]', 1, 2, '我是管理员,嘿嘿', '8.8.8.8', '2023-03-03 19:07:56', 'admin', '2023-01-19 14:59:42', 'admin', '2023-03-03 19:07:56', b'0');
INSERT INTO `system_user` VALUES (2, 'sheepyu_CFq03WGoKm', '$2a$10$P6AXMyJm0Q3E9Li2f2ts6OBK0uyPUPo/Wlm7U4mUy019TuxoXnjLe', '小黑', '1126882717@qq.com', '18311113333', 'https://thirdwx.qlogo.cn/mmopen/vi_32/XF9G5zQOXaLVgO739DoicP1ibNtDSl52swglcDOiclNcNtzLicpYcrXjKBsibiaMkxY9MrQ13vdBiaGjM2EjGhVI24m7Q/132', NULL, NULL, 1, 1, NULL, '127.0.0.1', '2023-01-22 17:34:40', '', '2023-01-22 09:31:17', 'admin', '2023-01-22 17:34:40', b'0');
INSERT INTO `system_user` VALUES (3, 'xiaohei', '$2a$10$95w9FvNCybH/GKZMg5rsv.cZF85h.eVw4uUcadj2rnCVrCednPa5G', '', '', '', '', 0, '[]', 0, 2, '', '', NULL, 'admin', '2023-01-31 16:45:10', NULL, '2023-01-31 16:45:10', b'0');

-- ----------------------------
-- Table structure for system_user_role
-- ----------------------------
DROP TABLE IF EXISTS `system_user_role`;
CREATE TABLE `system_user_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增编号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户和角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_user_role
-- ----------------------------
INSERT INTO `system_user_role` VALUES (1, 1, 1, 'admin', '2023-01-31 04:17:58', 'admin', '2023-01-31 08:44:02', b'0');
INSERT INTO `system_user_role` VALUES (2, 3, 2, 'admin', '2023-01-31 16:46:32', NULL, '2023-01-31 16:46:32', b'0');

SET FOREIGN_KEY_CHECKS = 1;
