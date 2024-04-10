### Spring-Boot OAuth 2.0实战

#### OAuth 2.0简介

    OAuth 2.0为OAuth的协议的延续版本，是一个关于授权的开放网络标准，在全世界得到广泛应用。
    基于OAuth 2.0授权有一下几个特点：1、安全：平台商无需使用用户的用户名与密码就可以申请获得该用户资源的授权；
    2、开放：任何消费方都可以使用 OAuth 认证服务，任何服务提供方 ( Service Provider) 都可以实现自身的 OAuth 认证服务。
    3、简单：不管是消费方还是服务提供方，都很容易于理解与使用。OAuth 2.0认证流程如下：

![](F:\workspace\spring-study-demo\spring-boot\spring-boot-oauth2\img.png)


    provider为服务提供方，即第三方服务系统，主要用作管理用户资源和为其他系统提供认证服务。
    例如：我们使用的QQ账号是在腾讯的服务器上，腾讯提供统一对外接口给各个平台（如：今日头条），用户下载今日头条app后，
    就可以选择使用QQ登录今日头条app。用户的个人信息是存放于腾讯服务器上的，用户可以授权今日头条获取自己在腾讯服务器上的个人信息，
    今日头条获取到用户信息后存于自己的系统用，以继续为用户服务。一般用户信息的存储和用户登录认证都是在provider方完成。
    
    client为平台系统，如：各个科技公司开发的系统，平台系统为用户提供更为具体的服务，比如：看新闻、看视频、玩游戏等等。client想要使用provider提供的第三方登录服务，必须要先向provider注册自己，也就是需要让provider识别自己是不是可信的平台服务商，所以在注册时provider会下发给各个client一组账号密码来唯一标识各个平台，从而可配置provider开放给各个client的访问域。
    
    user即普通用户，普通用户使用平台商提供的服务。
    
    传统的平台系统都各自有一套自己的用户认证体系，这就导致用户每使用一个平台都需要注册一组账号密码，时间久了就记不清了，从而导致用户的流失。使用OAuth 2.0提供的统一认证服务会很好的解决这个问题，用户只要记住一组账号密码，就可以在很多平台登录，提升了用户体验。

####  1、provider整合注意事项
    在oauth2-provider模块中，存放着用户的详细信息，一般包括用户名、密码、昵称、手机号、地址等等信息。需要在MyUserDetailsService的UserService实现自定义的用户认证方法，即：自定义实现用户表user，查询用户数据需要自己实现，同时实现MyUserDetails配置用户的账号密码、权限、是否过期、是否禁用等等，以便OAuth 2.0框架识别用户的一些基本信息以达到登录认证的作用。MyClientDetailsService的ClientService实现平台商的认证方法，对应到数据库的client表，存放平台商的一些基本账户信息和授权码回调地址，以及token过期时间。MyClientDetails配置了平台商的访问域、认证方式（主要有：authorization_code、password、client_credentials、implicit、refresh_ token五种，本文只介绍authorization_code方式的认证）、token过期等，系统根据你的配置就会限制平台商的一些访问操作及token过期等。
    
    SecurityConfig中主要是开启spring-security和配置一些拦截过滤以及启用自定义用户认证，用户使用第三方登录时输入账号密码将在此处配置认证。ResourceServerConfig中主要配置对哪些资源路径进行拦截认证，例如：对获取用户的手机号拦截，对获取用户的昵称不进行拦截。AuthorizationServerConfig主要启用平台商认证配置。
    
    在UserController中实现用户登录并授权成功后，client端可以通过下发的token访问到用户信息。
####  2、client整合注意事项
    client端主要负责以下几个工作：
    
    为用户重定向到资源服务器进行登录认证。
    认证成功后获取资源服务器授权码的回调。
    根据授权码获取access_token和refresh_token。
    使用access_token从资源服务器获取用户私有信息，并存入自身系统，让用户成为自己系统的用户。
    维护access_token的生命周期，当发现access_token过期时，需要让用户在无感知情况下使用refresh_token来刷新access_token。
    当refresh_token过期后，若用户还想访问自己的私有信息，则需要提示用户重新登录。
    client端需要对用户暴露第三方登录入口，如：QQ登录、微信登录、支付宝登录等第三方授权平台。当用户选择第三方登录时，需要根据选择的入口提供不同的认证服务器登录页面的重定向。
    
    当将用户重定向到第三方服务器进行登录并授权后，第三方服务器会根据平台商注册的回调地址将授权码回调给平台商。平台商根据授权码和平台商账号密码向第三方资源服务器获取access_token和refresh_token，使用access_token来获取用户信息，使用refresh_token来刷新access_token。
    
    当平台商获取到用户信息后，会将用户信息保存到自身系统的用户表中，同时提示用户登录成功。第三方返回给平台商的用户信息可能会缺少必要信息，比如：手机号、住址等，第三方一般不会提供如此敏感的信息，这就需要平台商自己提示用户：绑定手机号享XXX服务等来引导用户继续完善自己的用户信息以获得更为精准的服务。
    
    当用户使用第三方登录成功后，可能后续还会获取用户的其他资源，如：用户想在平台商提供的相册功能访问自己在第三方的相册，那么平台商就会继续使用access_token访问第三方服务器来获取用户的相册。此时可能access_token已经过期，那么client就需要在用户无感知的情况下使用refresh_token来换取新的access_token继续访问用户资源。众所周知，token都有过期时间，当refresh_token也过期的话，就需要告诉用户，登录已经过期，想要继续访问私有资源必要要重新登录，那么再进行登录流程获取用户信息和新的token。
#### 3、运行：

打开浏览器，输入地址：http://localhost:8002/api/user/thirdLogin  会自动重定向到8001服务器进行用户认证：

![](F:\workspace\spring-study-demo\spring-boot\spring-boot-oauth2\2.png)

输入用户名密码认证通过后，client端会自动为用户进行token获取与刷新、用户信息获取与存储等操作，所展现给用户的操作就是：选择第三方登录、进行第三方登录、第三方登录成功。client端会处理与provider端的复杂交互。

![](F:\workspace\spring-study-demo\spring-boot\spring-boot-oauth2\3.png)

获取用户信息：

![](F:\workspace\spring-study-demo\spring-boot\spring-boot-oauth2\4.png)

access_token过期后client端会自动刷新token，若是refresh_token也过期的话，会提示用户重新登录，此时需要再次访问http://localhost:8002/api/user/thirdLogin 登录后就可以让client端获取新的token继续访问用户信息。
![](F:\workspace\spring-study-demo\spring-boot\spring-boot-oauth2\5.png)

