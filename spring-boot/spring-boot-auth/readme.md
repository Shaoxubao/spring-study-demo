1、项目打包
2、编写DockerFile文件
3、构建镜像：docker build -t springboot-auth:1.0 .
4、运行镜像：docker run -d -p 9111:9111 --name springboot-auth01 springboot-auth:1.0