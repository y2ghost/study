from sports import Baseball, Football, Volleyball
from venue_strategy import Stadium

def generate_report(sport):
    print(sport.SPORT_TYPE)
    print(f'  players: {sport.recruit_players()}')
    print(f'    venue: {sport.reserve_venue()}')
    print()

if __name__ == '__main__':
    for sport in [Baseball(), Football(), Volleyball()]:
        generate_report(sport)

    # 开闭原则，父类关闭、子类开放
    volleyball = Volleyball()
    volleyball.venue_strategy = Stadium()
    generate_report(volleyball)

