from customCamera import DumpCam

def main(app, freq=1.0):
    state = app.events.State()
    with app.connection() as connection:
        recv = app.events.Receiver(connection, handlers = {'*': state.event })
        with DumpCam(state, freq=freq):
            recv.capture(limit=None, timeout=None)


if '__main__' == __name__:
    from hello import app
    main(app)

