echo服务测试
- ```bash
- # AsyncEcho测试
- gradle run -PmainClass=study.ywork.vertx.echo.AsyncEcho
- nc/netcat localhost 3000
- # Echo测试
- gradle run
- curl http://localhost:8080
- # EventLoop测试
- gradle run -PmainClass=study.ywork.vertx.echo.EventLoop
- nc/netcat localhost 3000
- # syncEcho测试
- gradle run -PmainClass=study.ywork.vertx.echo.SyncEcho
- nc/netcat localhost 3000
- ```

verticle组件测试
- ```bash
- gradle run -PmainClass=study.ywork.vertx.verticle.HelloVerticle
- gradle run -PmainClass=study.ywork.vertx.verticle.BlockEventVerticle
- gradle run -PmainClass=study.ywork.vertx.verticle.NoticeVerticle
- gradle run -PmainClass=study.ywork.vertx.verticle.DeployerVerticle
- gradle run -PmainClass=study.ywork.vertx.verticle.ConfigVerticle
- gradle run -PmainClass=study.ywork.vertx.verticle.WorkerVerticle
- gradle run -PmainClass=study.ywork.vertx.verticle.OffloadVerticle
- gradle run -PmainClass=study.ywork.vertx.verticle.MixedThreadVerticle
- gradle run -PmainClass=study.ywork.vertx.verticle.ContextExamples
- ```

eventbus组件测试
- ```bash
- gradle run -PmainClass=study.ywork.vertx.eventbus.Standalone
- gradle run -PmainClass=study.ywork.vertx.eventbus.ClusterOneDemo
- gradle run -PmainClass=study.ywork.vertx.eventbus.ClusterTwoDemo
- ```

stream组件测试
- ```bash
- gradle run -PmainClass=study.ywork.vertx.stream.VertxStream
- gradle run -PmainClass=study.ywork.vertx.stream.Player
- ```

callback组件测试
- ```bash
- gradle run -PmainClass=study.ywork.vertx.callback.EdgeService
- ```

