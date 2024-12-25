基本依赖
- pip install pika

基本测试
- python send.py
- python receive.py
- python new_task.py "A very hard task which takes two seconds.."
- python worker.py
- python receive_logs.py
- python emit_log.py "info: This is the log message"
- python receive_logs_direct.py info
- python emit_log_direct.py info "The message"
- python receive_logs_topic.py "*.rabbit"
- python emit_log_topic.py red.rabbit Hello
- python rpc_server.py
- python rpc_client.py

