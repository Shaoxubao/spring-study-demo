    参考https://blog.51cto.com/u_13312531/7085170
    1.生成服务器私钥（.key 文件）
    输入以下命令生成服务器私钥
    openssl genrsa -des3 -out server.pass.key 2048
    随后输入一个4位以上的密码，然后再输入一次密码。
    命令解释如下：
    genra：生成RSA私钥
    -des3：使用des3算法
    -out ：指定生成文件名
    2048：私钥长度设置为2048
    2.去除私钥中的密码
    输入以下命令去除私钥中的密码
    openssl rsa -in server.pass.key -out server.key
    3.生成证书签名请求（.csr文件）
    输入以下命令生成证书签名请求：
    openssl req -new -key server.key -out server.csr -subj "/C=CN/ST=Zhejiang/L=Hangzhou/O=OrganizeName/OU=UnitName/CN=192.168.1.58"
    参数介绍:
    req：生成证书签名请求
    -new：新生成
    -key：私钥文件
    -out：指定生成的CSR文件名
    -subj：生成CSR证书的参数
    subj参数说明如下：
    字段 全称 举例
    /C= Country 国家 CN
    /ST= State or Province 州/省 Zhejiang
    /L= Location or City 城市 Hangzhou
    /O= Organization 组织/企业 OrganizeName
    /OU= Organization Unit 部门 UnitName
    /CN= Common Name 域名or IP www.yourdomain.com 或者 192.168.x.x
    4.生成自签名SSL证书（.crt文件）
    输入以下命令生成自签名SSL证书
    openssl x509 -req -days 3650 -in server.csr -signkey server.key -out server.crt
    参数介绍:
    -days：证书有效期
    -req：需要证书
    -in：输入csr文件
    -signkey：指定 .key 文件
    -out：输出crt文件
    文件夹总共有4个文件，server.pass.key 、server.key、server.csr、server.crt。
    其中配置nginx的话需要server.key和server.crt两个文件。
    至此，证书生成完成。
    5、双击安装server.crt证书；
    6、配置nginx
        服务端配置443端口并使用SSL证书,我们需要在nginx目录下的nginx.conf进行https的配置
        server {
            #监听443端口
            listen       443 ssl;
            server_name  127.0.0.1;
            #ssl证书的crt文件路径
            ssl_certificate     D:\\soft\\nginx-1.24.0\\ssl\\server.crt;
            #ssl证书的key文件路径
            ssl_certificate_key D:\\soft\\nginx-1.24.0\\ssl\\server.key;
            #反向代理
            location / {
                root   html;
                index  index.html index.htm;
                proxy_pass  http://127.0.0.1:80;
                }
    	}
    nginx -s reload重新启动nginx，浏览器https://localhost访问。
    
    注意：为HTTPS准备的证书需要注意，创建的签名请求的CN必须与域名完全一致，否则无法通过浏览器验证。