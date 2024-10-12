    1、WebSocket简介
        WebSocket协议是基于TCP的一种新的网络协议。它实现了浏览器与服务器全双工(full-duplex)通信——允许服务器主动发送信息给客户端。

    2、为什么需要WebSocket
    HTTP 是基于请求响应式的，即通信只能由客户端发起，服务端做出响应，无状态，无连接。

    无状态：每次连接只处理一个请求，请求结束后断开连接。
    无连接：对于事务处理没有记忆能力，服务器不知道客户端是什么状态。
    通过HTTP实现即时通讯，只能是页面轮询向服务器发出请求，服务器返回查询结果。轮询的效率低，非常浪费资源，因为必须不停连接，或者 HTTP 连接始终打开。

    WebSocket的最大特点就是，服务器可以主动向客户端推送信息，客户端也可以主动向服务器发送信息，是真正的双向平等对话。

    WebSocket特点：
    （1）建立在 TCP 协议之上，服务器端的实现比较容易。
    （2）与 HTTP 协议有着良好的兼容性。默认端口也是80和443，并且握手阶段采用 HTTP 协议，因此握手时不容易屏蔽，能通过各种 HTTP 代理服务器。
    （3）数据格式比较轻量，性能开销小，通信高效。
    （4）可以发送文本，也可以发送二进制数据。
    （5）没有同源限制，客户端可以与任意服务器通信。
    （6）协议标识符是ws（如果加密，则为wss），服务器网址就是 URL。
    3、WebSocketServer类分析
    因为WebSocket是类似客户端服务端的形式(采用ws协议)，那么这里的WebSocketServer其实就相当于一个ws协议的Controller
    直接@ServerEndpoint("/imserver/{userId}") 、@Component启用即可，然后在里面实现@OnOpen开启连接，@onClose关闭连接，@onMessage接收消息等方法。
    新建一个ConcurrentHashMap用于接收当前userId的WebSocket或者Session信息，方便IM之间对userId进行推送消息。单机版实现到这里就可以。
    集群版（多个ws节点）还需要借助 MySQL或者 Redis等进行订阅广播方式处理，改造对应的 sendMessage方法即可。
    4、controller
    controller中只有一个简单的界面跳转操作，其他的不需要。
    5、websocketDemo.html
    新建一个文件，放到 templates目录下面。页面简单使用js代码调用WebSocket
    6、测试运行效果
    （1）访问页面，建立连接
    启动项目，访问 http://localhost:8081/demo/toWebSocketDemo/{cid} 跳转到页面，然后就可以和WebSocket交互了。
    这里开启三个浏览器的窗口：
        http://localhost:8081/demo/toWebSocketDemo/user-1
        http://localhost:8081/demo/toWebSocketDemo/user-2
        http://localhost:8081/demo/toWebSocketDemo/user-3
    然后打开浏览器的控制台。此时idea控制台中的输出信息如下所示。说明连接建立成功。
    （2）指定sid发送消息
    user-2给 user-1发送数据，也可以自己给自己发送数据。
    指定的窗口能够收到数据，其他窗口收不到数据。
    （3）群发送消息
    user-3群发送数据。在代码中定义群发的条件为：当不指定 toUserid时，则为群发。
    
    注意：html代码中需要引入jquery：<script src="http://libs.baidu.com/jquery/2.1.4/jquery.min.js"></script>