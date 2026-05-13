import sqlite3


con = sqlite3.connect(":memory:")
con.execute("CREATE TABLE lang(id INTEGER PRIMARY KEY, name VARCHAR UNIQUE)")

# 成功执行后自动调用con.commit()
with con:
    con.execute("INSERT INTO lang(name) VALUES(?)", ("Python",))

# 异常必须捕获
# 异常代码块处理完后自动调用con.rollback()    
try:
    with con:
        con.execute("INSERT INTO lang(name) VALUES(?)", ("Python",))
except sqlite3.IntegrityError:
    print("couldn't add Python twice")

# 连接仍旧需要自动关闭
con.close()

