from team import Team_1, Team_2, Team_3
from report import TeamReport


if __name__ == '__main__':
    report = TeamReport()
    
    team_1 = Team_1()
    report.print(team_1)
        
    team_2 = Team_2()
    report.print(team_2)
    
    team_3 = Team_3()
    report.print(team_3)
