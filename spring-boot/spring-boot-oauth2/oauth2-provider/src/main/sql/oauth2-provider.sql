/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50721
Source Host           : localhost:3306
Source Database       : oauth2-provider

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2019-08-20 11:56:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for client
-- ----------------------------
DROP TABLE IF EXISTS `client`;
CREATE TABLE `client` (
  `client_id` int(1) NOT NULL AUTO_INCREMENT COMMENT '客户企业id',
  `client_name` varchar(100) DEFAULT NULL COMMENT '企业名称',
  `account` varchar(100) DEFAULT NULL COMMENT '企业账号',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `call_back_url` varchar(255) DEFAULT NULL COMMENT '认证回调地址',
  `access_token_overdue_seconds` int(1) DEFAULT NULL COMMENT 'access_token过期时间(秒)',
  `refresh_token_overdue_seconds` int(1) DEFAULT NULL COMMENT 'refresh_token过期时间(秒)',
  PRIMARY KEY (`client_id`),
  UNIQUE KEY `index_account` (`account`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of client
-- ----------------------------
INSERT INTO `client` VALUES ('1', '云南成为智能科技有限公司', 'yncw', '123456', 'http://localhost:8002/token/callBack', '20', '60');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` int(1) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `account` varchar(100) DEFAULT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `nick_name` varchar(100) DEFAULT NULL COMMENT '用户昵称',
  `head_icon` varchar(255) DEFAULT NULL COMMENT '用户头像地址',
  `mobile` varchar(100) DEFAULT NULL COMMENT '手机号',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `index_account` (`account`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'xujianjie', '$2a$10$.5xnLBrwhj/qjdj/W8tYde.K8ALuC1wbUg8YYFMpx0jjeB7tM3Ctu', '徐建杰', '/head-icon.png', '18887654321');
