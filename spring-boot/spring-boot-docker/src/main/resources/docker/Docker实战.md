一、安装docker及可视化工具portainer
	docker run -d -p 9000:9000 -v /var/run/docker.sock:/var/run/docker.sock -v portainer_data:/data portainer/portainer
	portainer无法访问界面：重启docker(systemctl restart docker)
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
