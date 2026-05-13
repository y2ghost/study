from baseball_reporter import BaseballReporter
from log_report import LogReport
from table_report import TableReport
from graph_report import GraphReport
from fan_club_report import FanClubReport


if __name__ == '__main__':
    reporter = BaseballReporter()
    
    log   = LogReport(reporter)
    table = TableReport(reporter)
    graph = GraphReport(reporter)
    club  = FanClubReport(reporter, 'George')
    
    reporter.report_hits()
    club.print_bulletin()

