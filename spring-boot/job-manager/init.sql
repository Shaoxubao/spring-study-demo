CREATE TABLE `acct_schedule_job` (
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `name` varchar(100) DEFAULT NULL COMMENT '任务名称',
 `status` tinyint(2) DEFAULT NULL COMMENT '任务状态 1有效 2无效',
 `corn` varchar(50) DEFAULT NULL COMMENT '定时任务corn 表达式',
 `description` varchar(200) DEFAULT NULL COMMENT '任务描述',
 `bean_class` varchar(200) DEFAULT NULL COMMENT '任务执行时调用哪个类的方法 包名+类名，全路径',
 `method_name` varchar(200) DEFAULT NULL COMMENT '任务执行方法名称',
 `create_time` date DEFAULT NULL COMMENT '创建时间',
 `create_name` varchar(50) DEFAULT NULL COMMENT '创建人',
 `update_time` date DEFAULT NULL COMMENT '更新时间',
 `update_name` varchar(50) DEFAULT NULL COMMENT '更新人',
 `group_code` varchar(50) DEFAULT NULL COMMENT '分组code',
 `group_query_code` varchar(20) DEFAULT NULL COMMENT '分组查询code',
 `sql_id` int(11) DEFAULT NULL COMMENT 'sqlID',
 `task_sort` int(11) DEFAULT NULL COMMENT '定时任务号',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `acct_schedule_job_log` (
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `name` varchar(255) DEFAULT NULL COMMENT '任务名称',
 `bean_class` varchar(255) DEFAULT NULL COMMENT '任务执行时调用哪个类的方法 包名+类名，全路径',
 `job_start` datetime DEFAULT NULL COMMENT '任务执行开始时间',
 `job_end` datetime DEFAULT NULL COMMENT '任务执行结束时间',
 `exception_info` text COMMENT '异常信息',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

INSERT INTO `babytun`.`acct_schedule_job` (`id`, `name`, `status`, `corn`, `description`, `bean_class`, `method_name`, `create_time`, `create_name`, `update_time`, `update_name`, `group_code`, `group_query_code`, `sql_id`, `task_sort`) VALUES (1, '监测定时任务', 1, '0 0/1 * * * ?', '监测定时任务', 'com.baoge.job.ManageTaskJob', 'execute', '2024-10-21', '', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `babytun`.`acct_schedule_job` (`id`, `name`, `status`, `corn`, `description`, `bean_class`, `method_name`, `create_time`, `create_name`, `update_time`, `update_name`, `group_code`, `group_query_code`, `sql_id`, `task_sort`) VALUES (2, '测试定时任务', 1, '0 0/1 * * * ?', '测试定时任务', 'com.baoge.job.TestJob', 'execute', '2024-10-21', NULL, NULL, NULL, NULL, NULL, NULL, 1);
