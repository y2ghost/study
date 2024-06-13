import mariadb

def create_connection_pool():
    pool = mariadb.ConnectionPool(
      host="localhost",
      port=3306,
      user="db_user",
      password="test123456",
      pool_name="web-app",
      pool_size=20)
    return pool
try:
    pool=create_connection_pool()
    pconn = pool.get_connection()
    cur = pconn.cursor()
except mariadb.PoolError as e:
   print(f"Error opening connection from pool: {e}")
   pconn = mariadb.connection(
      host="localhost", port=3306,
      user="db_user", password="test123456")

pconn.close()
pool.close()

# 手工添加连接到连接池的方式
pool = mariadb.ConnectionPool(
    pool_name = "pool_manual",
    pool_size = 3,
    pool_reset_connection = False,
)

# 配置连接参数
pool.set_config(
    host="localhost",
    port=3306,
    user="db_user",
    password="test123456")
# pool.set_config(database = "testdb")
# 必须手工添加连接
pool.add_connection()
pool.add_connection()
pool.add_connection()
conn = pool.get_connection()
conn.close()
pool.close()

