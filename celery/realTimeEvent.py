from celery import Celery
from hello import app


def my_monitor(app):
    state = app.events.State()

    def show_tasks(event):
        state.event(event)
        task = state.tasks.get(event['uuid'])
        print(f'{task.type}: {task.name}[{task.uuid}] {task.info()}')

    with app.connection() as connection:
        recv = app.events.Receiver(connection, handlers={
            'task-sent': show_tasks,
            'task-received': show_tasks,
            'task-started': show_tasks,
            'task-succeeded': show_tasks,
            'task-failed': show_tasks,
            'task-rejected': show_tasks,
            'task-revoked': show_tasks,
            'task-retried': show_tasks,
            '*': state.event,
        })
        recv.capture(limit=None, timeout=None, wakeup=True)


if __name__ == '__main__':
    my_monitor(app)

