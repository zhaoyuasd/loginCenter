## 单点登录

- 1 启动loginServcice 服务 登录中心
- 2 启动WebService-1  服务  web服务
- 3 访问请求 http://127.0.0.1:8081/login?username=zy&password=asd
    username和password随便指定
   
- 4 访问 http://127.0.0.1:8081/showInfo

>到4的时候 服务已经登录成功 这里我们再启动一个服务

- 5 修改WebSerivce-1 的端口 再次启动
- 6 访问 http://127.0.0.1:修改后的端口/showInfo

>这里6 要在同一个浏览器里面使用 实际上如果要在其他浏览器上
也可以,但是要额外传值 可以标明用户正在使用的地电脑 这个可以在后续添加