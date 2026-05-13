from sport import Sport, Category, SportType


class VarsityBaseball(Sport):
    _CATEGORY = Category.VARSITY
    _SPORT_TYPE = SportType.BASEBALL


class VarsityFootball(Sport):
    _CATEGORY = Category.VARSITY
    _SPORT_TYPE = SportType.FOOTBALL


class IntramuralBaseball(Sport):
    _CATEGORY = Category.INTRAMURAL
    _SPORT_TYPE = SportType.BASEBALL


class IntramuralFootball(Sport):
    _CATEGORY = Category.INTRAMURAL
    _SPORT_TYPE = SportType.FOOTBALL


class IntramuralVolleyball(Sport):
    _CATEGORY = Category.INTRAMURAL
    _SPORT_TYPE = SportType.VOLLEYBALL
