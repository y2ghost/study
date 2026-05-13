from visitor import Visitor


class WinningsReportVisitor(Visitor):
    def visit_intramural(self, intramural):
        print()
        print('WINNINGS REPORT')
        print()
        
    def visit_sport(self, sport):
        return
        
    def visit_game(self, game):
        return
    
    def visit_hall(self, hall):
        print(f'{hall.name:>12s} won {hall.win_count} game(s)')

