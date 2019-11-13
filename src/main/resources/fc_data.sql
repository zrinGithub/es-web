/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : localhost:3306
 Source Schema         : fc_data

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 13/11/2019 20:16:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for player
-- ----------------------------
DROP TABLE IF EXISTS `player`;
CREATE TABLE `player`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `CN_NAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '中文名称',
  `EN_NAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '英文名称',
  `NATION` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '国籍',
  `BIRTH` timestamp(0) NULL DEFAULT NULL COMMENT '生日',
  `AGE` int(11) NULL DEFAULT NULL COMMENT '年龄',
  `WEIGHT` decimal(10, 2) NULL DEFAULT NULL COMMENT '重量',
  `HEIGHT` decimal(10, 2) NULL DEFAULT NULL COMMENT '身高',
  `TEAM_NAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '队名',
  `POSITION` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '位置',
  `NUMBER` int(2) NULL DEFAULT NULL COMMENT '球衣号码',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '球员信息表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
