/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80021
 Source Host           : localhost:3306
 Source Schema         : shiro_jwt

 Target Server Type    : MySQL
 Target Server Version : 80021
 File Encoding         : 65001

 Date: 24/10/2022 20:29:08
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`
(
    `id`           bigint NOT NULL COMMENT 'id',
    `name`         varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '姓名',
    `age`          int                                                           DEFAULT NULL COMMENT '年龄',
    `sex`          tinyint                                                       DEFAULT '0' COMMENT '性别：0-女 1-男',
    `username`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '账号',
    `password`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码',
    `created_date` datetime                                                      DEFAULT NULL COMMENT '创建时间',
    `updated_date` datetime                                                      DEFAULT NULL COMMENT '修改时间',
    `is_deleted`   int                                                           DEFAULT '0' COMMENT '删除标识',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of t_user
-- ----------------------------
BEGIN;
INSERT INTO `t_user`
VALUES (1508421137384648705, '豹哥', 12, 0, 'baoge',
        '$2a$10$x9Yxwdhpk7Q.1yMU2e/h4eQ1yN3OFHuiIrBxst44FYLY6waJzRt22', '2022-03-28 07:29:59', NULL, 0);
COMMIT;

SET
FOREIGN_KEY_CHECKS = 1;
