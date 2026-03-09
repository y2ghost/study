from alumni import Alumni
from booster_clubs import BoosterClubs
from students import Students


class FundRaiser:
    def do_fund_raising(self):
        alumni = Alumni()
        clubs = BoosterClubs()
        students = Students()
        
        alumni.send_solicitations()
        clubs.schedule_meetings()
        students.collect_fees()
