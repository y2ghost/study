from provision_item import ProvisionItem


CAP_COST    = 15
JERSEY_COST = 25
PANTS_COST  = 35


class Cap(ProvisionItem):
    def __init__(self):
        super().__init__('cap', CAP_COST)


class Jersey(ProvisionItem):
    def __init__(self):
        super().__init__('jersey', JERSEY_COST)


class Pants(ProvisionItem):
    def __init__(self):
        super().__init__('pants', PANTS_COST)

