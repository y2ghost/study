from observer import Observer


class TableReport(Observer):
    def __init__(self, reporter):
        self._reporter = reporter
        self._hit_map = {}
        reporter.attach(self)

    def _print_table(self):
        print()
        print('GAME HITS TABLE')
        print()
        print('Player     Outs   Singles Doubles Triples Homers')
        print('------------------------------------------------')

        for player_name, hits in self._hit_map.items():
            print(f'{player_name:6s}', end='')

            for hit in hits:
                if hit > 0:
                    print(f'{hit:8d}', end='')
                else:
                    print('        ', end='')
            print()

    def update(self, player_name):
        event = self._reporter.current_event

        if player_name is not None:
            hit = event.hit

            if not player_name in self._hit_map:
                self._hit_map[player_name] = 5*[0]
                
            self._hit_map[player_name][hit] += 1
        else:
            self._print_table()
