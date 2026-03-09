class TeamReport:
    def print(self, team):
        print()
        print(team.name)
        it = team.iterator
        
        while it.has_next():
            p = it.next()
            print(f'{p.player_id} {p.last_name}, {p.first_name}')

