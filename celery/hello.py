from celery import Celery
from celery import Task
from celery import Task
from celery import shared_task
from celery.utils.log import get_task_logger
import time


log = get_task_logger("hello logger")
app = Celery()
app.config_from_object("celeryconfig")


@app.task(queue='hello', bind=True)
def add(self, x, y):
    req = self.request
    log.info(f"show task info: {req.id} {req.hostname}")
    log.info(f"show task info: {req.args} {req.kwargs}")
    log.info(f"show task info: {type(req.args)} {type(req.kwargs)}")
    return x + y

class DebugTask(Task):
    def __call__(self, *args, **kwargs):
        print('TASK STARTING: {0.name}[{0.request.id}]'.format(self))
        return self.run(*args, **kwargs)


@shared_task(queue='hello', base=DebugTask)
def hello(to):
    time.sleep(10)
    return 'hello {0}'.format(to)


@app.task(bind=True)
def dump_context(self, x, y):
    print('Executing task id {0.id}, args: {0.args!r} kwargs: {0.kwargs!r}'.format(
        self.request))


@app.task(bind=True)
def update_state(self, state):
    self.update_state(state=state.upper(), meta={})
    time.sleep(30)


@app.task(bind=True)
def onHello(self, a, b):
    time.sleep(1)
    self.update_state(state="PROGRESS", meta={'progress': 50})
    time.sleep(1)
    self.update_state(state="PROGRESS", meta={'progress': 90})
    time.sleep(1)
    return 'hello world: %i' % (a+b)


if "__main__" == __name__:
    def on_raw_message(body):
        print(body)

    a, b = 1, 1
    r = onHello.apply_async(args=(a, b))
    print(r.get(on_message=on_raw_message, propagate=False))

