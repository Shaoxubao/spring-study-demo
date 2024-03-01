一、安装docker及可视化工具portainer
	docker run -d -p 9000:9000 -v /var/run/docker.sock:/var/run/docker.sock -v portainer_data:/data portainer/portainer
	portainer无法访问界面：重启docker(systemctl restart docker),永久解决方案：需要重启Linux
	vim /etc/selinux/config
	修改SELINUX=permissive
二、Docker构建springboot应用
	Dockerfile编写：
	FROM openjdk:8-jdk-alpine
	VOLUME /tmp
	ARG JAR_FILE=spring-boot-auth.jar
	COPY ${JAR_FILE} app.jar
	ENTRYPOINT ["java","-jar","/app.jar"]
	构建镜像：
	docker build -t my-springboot .
	查看镜像：
	docker images
	运行镜像生产一个容器：
	docker run -itd -p 9111:9111 my-springboot
三、安装Docker Compose
	https://cloud.tencent.com/developer/article/1351621?areaSource=102001.9&traceId=JB3C4BxRKneqv2fFtWeJM
	https://blog.csdn.net/weixin_44359151/article/details/131793294

	Docker Compose项目实战：
	部署mysql(使用docker-compose):或者https://blog.csdn.net/seashouwang/article/details/119109118
	version: '3'
	services:
	  mysql:
		image: mysql:5.7
		container_name: mysql
		command: mysqld --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci
		restart: always
		environment:
		  MYSQL_ROOT_PASSWORD: 123456 #设置root帐号密码
		ports:
		  - 3306:3306
		volumes:
		  - /mydata/mysql-master/data/db:/var/lib/mysql #数据文件挂载
		  - /mydata/mysql-master/data/conf:/etc/mysql/conf.d #配置文件挂载
		  - /mydata/mysql-master/log:/var/log/mysql #日志文件挂载
	进入容器bash：
	docker exec -it mysql /bin/bash
	mysql -uroot -p 登录数据库
	部署redis：
	docker run  -p 6379:6379 --name redis --privileged=true -v /app/redis2/redis.conf:/etc/redis/redis.conf -v /app/redis2/data:/data -d redis:6.0.8 redis-server /etc/redis/redis.conf
	
	mvn打包项目
	使用DockerFile部署
	编写DockerFile文件：名为：docker_demo
	构建springboot镜像：docker build -f docker_demo -t xubao_springboot_docker:1.0 .
	docker images
	# 运行容器，对外暴露端口
	docker run -d -p 6001:6001 镜像ID(上一步构建的镜像) --name springboot_docker1.0
	docker ps
	使用Compose部署(一次性部署mysql、redis、项目应用)
	编写docker-compose.yml文件
	修改本地代码配置(mysql、redis端口)重新构建springboot镜像：docker build -f docker_demo1.1 -t xubao_springboot_docker:1.1 .
	docker images
	docker-compose up -d

四、docker compose安装nacos
	配置：
	在mysql服务器手动创建nacos数据库
	导入nacos-mysql.sql文件（在nacos-server-1.4.3\nacos\conf目录下）
	编写docker-compose-nacos.yml
	# 启动
	docker-compose -f docker-compose-nacos.yml up -d
五、apisix nacos实战
	基于上面安装的nacos
	本地搭建一个服务，以nacos作为注册中心
	apisix新增配置:
	discovery:
	  nacos:
		host:
		  - "http://192.168.43.91:49155"
		prefix: "/nacos/v1/"
		fetch_interval: 30    # default 30 sec
		weight: 100           # default 100
		timeout:
		  connect: 2000       # default 2000 ms
		  send: 2000          # default 2000 ms
		  read: 5000          # default 5000 ms
	打包服务，上传服务器
	编写服务的DockerFile
	构建镜像：  docker build -f docker_demo_order -t xubao_springboot_order .
				docker build -f docker_demo_user -t xubao_springboot_user .
	运行镜像：  docker run -d -p 8021:8021 --name xubao_springboot_order1.0 镜像ID(上一步构建的镜像)
				docker run -d -p 8022:8022 --name xubao_springboot_user1.0 镜像ID(上一步构建的镜像)
	给apisix新增一个路由也可通过dashborad来添加：
	curl http://127.0.0.1:9180/apisix/admin/routes/1 -H 'X-API-KEY: edd1c9f034335f136f87ad84b625c8f1' -X PUT -i -d '
	{
		"uri": "/user/*",
		"upstream": {
			"service_name": "user-service",
			"type": "roundrobin",
			"discovery_type": "nacos"
		}
	}'
	通过apisix访问user服务：http://192.168.43.91:9080/user/getOrder