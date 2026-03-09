from provision_item import ProvisionItem


BALL_COST  =  5
BAT_COST   = 25
GLOVE_COST = 35


class Ball(ProvisionItem):
    def __init__(self):
        super().__init__('ball', BALL_COST)


class Bat(ProvisionItem):
    def __init__(self):
        super().__init__('bat', BAT_COST)


class Glove(ProvisionItem):
    def __init__(self):
        super().__init__('glove', GLOVE_COST)

