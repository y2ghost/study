import mariadb
import sys

def add_account(cur, first_name, last_name, email, amount):
    cur.execute("INSERT INTO testdb.accounts(first_name, last_name, email, amount) VALUES (?, ?, ?, ?)",
      (first_name, last_name, email, amount))

def update_account_amount(cur, email, change):
   cur.execute("UPDATE testdb.accounts SET amount=(amount-?) WHERE email=?",
         (change, email))

try:
   conn = mariadb.connect(
      host="localhost",
      port=3306,
      user="db_user",
      password="test123456",
      db="testdb")
   # 启动自动commit
   # conn.autocommit = True
   cur = conn.cursor()
   new_account_fname = "f1"
   new_account_lname = "l1"
   new_account_email = "f1@l2.dev"
   new_account_amount = 418000000000.00
   add_account(cur,
      new_account_fname,
      new_account_lname,
      new_account_email,
      new_account_amount)

   # 更新数据
   new_account_change = 1000000.00
   update_account_amount(cur,
      new_account_email,
      new_account_change)
   conn.commit()
   conn.close()
except Exception as e:
   print(f"Error committing transaction: {e}")
   conn.rollback()

