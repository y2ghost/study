import mariadb
import sys

# 连接初始化
try:
   conn = mariadb.connect(
      host="localhost",
      port=3306,
      user="db_user",
      password="test123456")
   conn.auto_reconnect = True
except mariadb.Error as e:
   print(f"Error connecting to the database: {e}")
   sys.exit(1)

# 处理业务
# 关闭连接
conn.close()

