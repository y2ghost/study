from game_report import GameReport
from volleyball_data import VolleyballData

class VolleyballReport(GameReport):
    def __init__(self):
        super().__init__("排球比赛报告")
        self._team1_is_winner = False
        self._losers_score = 0

    def _acquire_data(self):
        data = VolleyballData()
        self._points = data.points

    def _analyze_data(self):
        self._team1_is_winner = self._points[-1] > 0
        for i in range(len(self._points) - 1, 0, -1):
            if ((self._team1_is_winner and (self._points[i] < 0))
                or (not self._team1_is_winner and (self._points[i] > 0))):
                self._losers_score = abs(self._points[i])
                break

    def _print_report(self):
        winner = 1 if self._team1_is_winner else 2
        print(f"获胜队伍 {winner}")
        print(f"最终的比分是 15 比 {self._losers_score}")
        print()
        print("     ....5...10...15")

        for index, score in enumerate(self._points):
            print(f"{index + 1:3d}:", end="")
            winner = 1 if score > 0 else 2
            print(f"{" "*abs(score)}{winner:1d}")

    def _print_footer(self):
        print()
        print("报告结束")

    def generate_report(self):
        self._print_header()
        self._acquire_data()
        self._analyze_data()
        self._print_report()
        self._print_footer()

