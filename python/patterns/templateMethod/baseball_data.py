import random
from time import time_ns

class BaseballData:
    def __init__(self):
        random.seed(time_ns())
        self._hits = []
        outs = 0

        while outs < 27:
            hit = int(abs(random.gauss(0.0, 1.75)))
            if hit > 4: hit = 4
            if hit != 0: self._hits.append(hit)
            else: outs += 1

    @property
    def hits(self): return self._hits

