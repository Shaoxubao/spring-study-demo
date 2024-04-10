/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50721
Source Host           : localhost:3306
Source Database       : oauth2-client

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2019-08-20 11:56:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` int(1) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `account` varchar(100) DEFAULT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `nick_name` varchar(100) DEFAULT NULL COMMENT '用户昵称',
  `mobile` varchar(100) DEFAULT NULL COMMENT '手机号',
  `third_account` varchar(100) DEFAULT NULL COMMENT '第三方用户名',
  `access_token` varchar(255) DEFAULT NULL COMMENT '第三方服务器token',
  `refresh_token` varchar(255) DEFAULT NULL COMMENT '第三方服务器刷新access_token的token',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `index_account` (`account`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'admin', '123456', '超级管理员', '18888888888', null, null, null);
INSERT INTO `user` VALUES ('4', 'user_1566011157468', '123456', '徐建杰', '18887654321', 'xujianjie', '091d473f-64de-46da-9942-cc61bd094892', '889dc870-bfec-407c-b3bc-4bb8d6e83fe2');
