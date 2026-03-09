from intramural import Intramural
from sport import SportType, Sport
from game import Game
from hall import HallName, Hall
from activities_report_visitor import ActivitiesReportVisitor
from scores_report_visitor import ScoresReportVisitor
from winnings_report_visitor import WinningsReportVisitor


def build_tree():
    intramural_node = Intramural()
    
    baseball   = Sport(SportType.BASEBALL)
    football   = Sport(SportType.FOOTBALL)
    volleyball = Sport(SportType.VOLLEYBALL)
    
    intramural_node.add_sport(baseball)
    intramural_node.add_sport(football)
    intramural_node.add_sport(volleyball)
    
    north = Hall(HallName.NORTH)
    south = Hall(HallName.SOUTH)
    east  = Hall(HallName.EAST)
    west  = Hall(HallName.WEST)
    
    intramural_node.add_hall(north)
    intramural_node.add_hall(south)
    intramural_node.add_hall(east)
    intramural_node.add_hall(west)
    
    baseball.add_game(Game(west, 5, east,  3))
    baseball.add_game(Game(east, 3, south, 0))
    
    football.add_game(Game(north, 27, west, 21))
    
    volleyball.add_game(Game(west,  15, south, 10))
    volleyball.add_game(Game(north, 15, south, 13))
    volleyball.add_game(Game(south, 15, west,  14))
    
    return intramural_node

if __name__ == '__main__':
    intramural_node = build_tree()
    
    scores_report_visitor     = ScoresReportVisitor()
    activities_report_visitor = ActivitiesReportVisitor()
    winnings_report_visitor   = WinningsReportVisitor()
    
    intramural_node.accept(scores_report_visitor)
    intramural_node.accept(activities_report_visitor)
    intramural_node.accept(winnings_report_visitor)
    
