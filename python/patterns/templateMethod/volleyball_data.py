import random
from time import time_ns

class VolleyballData:
    def __init__(self):
        random.seed(time_ns())
        self._points = []
        score1 = score2 = 0

        while (score1 < 15) and (score2 < 15):
            if random.randint(1, 2) == 1:
                score1 += 1
                self._points.append(score1)
            else:
                score2 += 1
                self._points.append(-score2)

    @property
    def points(self): return self._points

