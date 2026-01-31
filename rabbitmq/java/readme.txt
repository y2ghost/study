创建账户
sudo rabbitmqctl add_user test test123456
sudo rabbitmqctl add_vhost myvhost
sudo rabbitmqctl set_user_tags test mytag
sudo rabbitmqctl set_permissions -p myvhost test ".*" ".*" ".*"
sudo rabbitmqctl status

检查队列
- sudo rabbitmqctl list_queues --vhost myvhost
- sudo rabbitmqctl list_queues --vhost myvhost name messages_ready messages_unacknowledged

测试发送接收
- gradle run -PmainClass=study.ywork.rabbitmq.client.Send
- gradle run -PmainClass=study.ywork.rabbitmq.client.Recv

测试Worker
- gradle run -PmainClass=study.ywork.rabbitmq.client.NewTask --args "first message"
- gradle run -PmainClass=study.ywork.rabbitmq.client.Worker

测试Logs
- gradle run -PmainClass=study.ywork.rabbitmq.client.EmitLog --args "log test"
- gradle run -PmainClass=study.ywork.rabbitmq.client.ReceiveLogs

测试LogsDirect
- gradle run -PmainClass=study.ywork.rabbitmq.client.EmitLogDirect --args "info test"
- gradle run -PmainClass=study.ywork.rabbitmq.client.ReceiveLogsDirect --args "info warning error"

测试LogsTopic
- gradle run -PmainClass=study.ywork.rabbitmq.client.EmitLogTopic --args '"kern.critical" "A critical kernel error"'
- gradle run -PmainClass=study.ywork.rabbitmq.client.ReceiveLogsTopic --args "kern.*"

测试RPC
- gradle run -PmainClass=study.ywork.rabbitmq.client.RPCServer
- gradle run -PmainClass=study.ywork.rabbitmq.client.RPCClient

测试Header
- gradle run -PmainClass=study.ywork.rabbitmq.client.EmitLogHeader --args "header_queue agent haha"
- gradle run -PmainClass=study.ywork.rabbitmq.client.ReceiveLogHeader --args "header_queue"

确认发布消息已被处理
- gradle run -PmainClass=study.ywork.rabbitmq.client.PublisherConfirms

stream基本示例
- gradle run -PmainClass=study.ywork.rabbitmq.client.stream.Send
- gradle run -PmainClass=study.ywork.rabbitmq.client.stream.Receive
- gradle run -PmainClass=study.ywork.rabbitmq.client.stream.OffsetTrackingSend
- gradle run -PmainClass=study.ywork.rabbitmq.client.stream.OffsetTrackingReceive

