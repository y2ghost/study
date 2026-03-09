from visitor import Visitor


class ActivitiesReportVisitor(Visitor):
    def visit_intramural(self, intramural):
        print()
        print('ACTIVITIES REPORT')
        print()
        
    def visit_sport(self, sport):
        print(f'{sport.type:>12s}: '
              f'{sport.games_count} game(s)')
        
    def visit_game(self, game):
        return
    
    def visit_hall(self, hall):
        return
