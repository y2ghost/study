class AttendanceReport:    
    def legacy_print(self, game_data, title):
        print(title)
        print(f'  {game_data.report_venue()}: ', end='')
        print(f'{game_data.report_attendance()}')
        print()
