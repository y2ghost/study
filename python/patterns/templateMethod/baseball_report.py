import matplotlib.pyplot as plt
from game_report import GameReport
from baseball_data import BaseballData

class BaseballReport(GameReport):
    def __init__(self):
        super().__init__("棒球比赛报道")
        self._singles = 0
        self._doubles = 0
        self._triples = 0
        self._homers  = 0

    def _acquire_data(self):
        data = BaseballData()
        self._hits = data.hits

    def _analyze_data(self):
        for hit in self._hits:
            if hit == 1: self._singles += 1    
            elif hit == 2: self._doubles += 1
            elif hit == 3: self._triples += 1
            elif hit == 4: self._homers += 1

    def _print_report(self):
        print("   打击类型")
        print(f"{self._singles:2d} 单打")
        print(f"{self._doubles:2d} 双打")
        print(f"{self._triples:2d} 上垒")
        print(f"{self._homers:2d} 本垒打")

        plt.bar(["单打", "双打", "三打", "本垒打"],
            [self._singles, self._doubles, 
            self._triples, self._homers])
        plt.title("击中次数")
        plt.xlabel("打击类型")
        plt.ylabel("次数")
        plt.show()

