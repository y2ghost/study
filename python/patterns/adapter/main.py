from baseball_data import BaseballData
from football_info import FootballInfo
from football_data import FootballData
from volleyball_stats import VolleyballStats
from volleyball_data import VolleyballData
from attendance_report import AttendanceReport


if __name__ == '__main__':
    baseball_data = BaseballData()
    football_info = FootballInfo()
    volleyball_stats = VolleyballStats()
    
    football_data = FootballData(football_info)
    volleyball_data = VolleyballData(volleyball_stats)
    
    report = AttendanceReport()
    
    report.legacy_print(baseball_data,  'Baseball')
    report.legacy_print(football_data,  'Football')
    report.legacy_print(volleyball_data,'Volleyball')

