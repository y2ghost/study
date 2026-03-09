import sys
from baseball_report import BaseballReport
from volleyball_report import VolleyballReport

if __name__ == '__main__':
    if len(sys.argv) < 2:
        print('Usage: report -b | -v')
    elif sys.argv[1] == '-b':
        baseball_report = BaseballReport()
        baseball_report.generate_report()
    else:
        volleyball_report = VolleyballReport()
        volleyball_report.generate_report()

