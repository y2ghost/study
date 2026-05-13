import matplotlib.pyplot as plt
from observer import Observer


class GraphReport(Observer):
    def __init__(self, reporter):
        self._reporter = reporter
        self._singles = 0
        self._doubles = 0
        self._triples = 0
        self._homers  = 0
        reporter.attach(self)

    def _display_graph(self):
        hits = [self._homers,  self._triples,
                self._doubles, self._singles]
        what = ['Homers',  'Triples',
                'Doubles', 'Singles']

        _, ax = plt.subplots(figsize=(6, 2))
        ax.barh(what, hits, height=0.75)

        plt.title('GAME HITS GRAPH')
        plt.xticks([5, 10, 15, 20, 25, 30], 
                   ['5', '10', '15', '20', '25', '30'])

        for i in range(len(what)):
            ax.text(hits[i] + 1, i, str(hits[i]),
                    fontsize=10, ha='left', va='center')

        plt.show()

    def update(self, player_name):
        if player_name is not None:
            event = self._reporter.current_event
            hit = event.hit

            if hit == 1:
                self._singles += 1
            elif hit == 2:
                self._doubles += 1
            elif hit == 3:
                self._triples += 1
            elif hit == 4:
                self._homers += 1
        else:
            self._display_graph()

