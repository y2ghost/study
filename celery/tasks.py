from celery import Celery

# 基本初始化示例
# app = Celery("tasks",
#     broker="pyamqp://myuser:mypassword@localhost:5672/myvhost",
#     backend="redis://localhost:6379/1")
# app.conf.broker_connection_retry_on_startup = True

# 配置对象初始化
app = Celery("tasks")
app.config_from_object("celeryconfig")

@app.task
def add(x, y):
    return x + y

