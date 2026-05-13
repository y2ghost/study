from abc import ABC, abstractmethod

class GameReport(ABC):
    def __init__(self, title):
        self._title = title

    def _print_header(self):
        print(self._title)
        print()

    def _print_footer(self):
        print()
        print("报告结束")

    @abstractmethod
    def _acquire_data(self): pass

    @abstractmethod
    def _analyze_data(self): pass

    @abstractmethod
    def _print_report(self): pass

    def generate_report(self):
        self._print_header()
        self._acquire_data()
        self._analyze_data()
        self._print_report()
        self._print_footer()

