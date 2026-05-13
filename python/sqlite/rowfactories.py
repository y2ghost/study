import sqlite3


con = sqlite3.connect(":memory:")
con.row_factory = sqlite3.Row
res = con.execute("SELECT 'Earth' AS name, 6378 AS radius")
row = res.fetchone()
row.keys()

print(type(row))
print(row[0])           # 下标访问
print(row["name"])      # 字典访问
print(row["RADIUS"])    # 列名忽略大小写
con.close()


# 自定义返回row类型
def dict_factory(cursor, row):
    fields = [column[0] for column in cursor.description]
    return {key: value for key, value in zip(fields, row)}


con = sqlite3.connect(":memory:")
con.row_factory = dict_factory
for row in con.execute("SELECT 1 AS a, 2 AS b"):
    print(row)

con.close()

