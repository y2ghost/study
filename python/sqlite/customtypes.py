import sqlite3


class Point:
    def __init__(self, x, y):
        self.x, self.y = x, y

    def __conform__(self, protocol):
        if protocol is sqlite3.PrepareProtocol:
            return f"{self.x};{self.y}"

con = sqlite3.connect(":memory:")
cur = con.cursor()

cur.execute("SELECT ?", (Point(4.0, -3.2),))
print(cur.fetchone()[0])
con.close()

class Point2:
    def __init__(self, x, y):
        self.x, self.y = x, y

def adapt_point2(point):
    return f"{point.x};{point.y}"

sqlite3.register_adapter(Point2, adapt_point2)

con = sqlite3.connect(":memory:")
cur = con.cursor()

cur.execute("SELECT ?", (Point2(1.0, 2.5),))
print(cur.fetchone()[0])
con.close()

