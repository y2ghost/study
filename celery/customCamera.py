from pprint import pformat
from celery.events.snapshot import Polaroid


class DumpCam(Polaroid):
    # clear after flush (incl, state.event_count).
    clear_after = True

    def on_shutter(self, state):
        if not state.event_count:
            # 没有事件
            return
        print('Workers: {0}'.format(pformat(state.workers, indent=4)))
        print('Tasks: {0}'.format(pformat(state.tasks, indent=4)))
        print('Total: {0.event_count} events, {0.task_count} tasks'.format(state))


