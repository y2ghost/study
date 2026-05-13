from observer import Observer


class LogReport(Observer):
    def __init__(self, reporter):
        self._reporter = reporter
        self._count = 0
        reporter.attach(self)

        print('GAME HITS LOG')
        print()

    def update(self, player_name):
        if player_name is not None:
            event = self._reporter.current_event
            hit = event.hit

            if hit == 0:
                what = 'an out'
            elif hit == 1:
                what = 'a single'
            elif hit == 2:
                what = 'a double'
            elif hit == 3:
                what = 'a triple'
            else:
                what = 'a homer'

            self._count += 1
            print(f'{self._count:2d} {player_name:7s}'
                  f' hit {what}')

