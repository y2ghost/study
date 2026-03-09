from observer import Observer


class FanClubReport(Observer):
    def __init__(self, reporter, idol):
        self._reporter = reporter
        self._idol = idol
        self._what = None
        reporter.attach(self)

    def print_bulletin(self):
        print()
        print('FAN CLUB BULLETIN')
        print(f'The first at-bat by {self._idol}'
              f' resulted in {self._what}.')

    def update(self, player_name):
        if player_name == self._idol:
            event = self._reporter.current_event            
            hit = event.hit

            if hit == 0:
                self._what = 'an out'
            elif hit == 1:
                self._what = 'a single'
            elif hit == 2:
                self._what = 'a double'
            elif hit == 3:
                self._what = 'a triple'
            else:
                self._what = 'a homer'

            self._reporter.detach(self)

