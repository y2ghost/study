broker_url = "pyamqp://myuser:mypassword@localhost:5672/myvhost"
result_backend = "redis://localhost:6379/1"
broker_connection_retry_on_startup = True
task_serializer = "json"
result_serializer = "json"
accept_content = ["json"]
timezone = "Asia/Shanghai"
enable_utc = True
task_acks_late = True
worker_concurrency = 1
worker_prefetch_multiplier = 1
worker_send_task_events = True
task_send_sent_event = True

task_routes = {
    "tasks.add": "celery",
}

# celery -A tasks control rate_limit tasks.add 10/m
task_annotations = {
    "tasks.add": {"rate_limit": "10/m"}
}

