class Time:
    """示例24小时制"""

    def __init__(self, hour, minute):
        self.hour = hour
        self.minute = minute

    def __repr__(self):
        return "时间(%d, %d)" % (self.hour, self.minute)

    @classmethod
    def from_float(cls, moment):
        """2.5 == 2小时30分钟0秒0微秒"""
        hours = int(moment)
        if hours:
            moment = moment % hours
        minutes = int(moment * 60)
        return cls(hours, minutes)

    def to_float(self):
        """返回自身作为浮点数"""
        return self.hour + self.minute / 60


print(Time(7, 30))
print(Time.from_float(5.75))
t = Time(10, 15)
rc = t.to_float()
print(rc)
