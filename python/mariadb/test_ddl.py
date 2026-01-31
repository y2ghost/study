import mariadb
import sys

def truncate_contacts(cur):
   try:
      cur.execute("TRUNCATE contacts")
   except mariadb.Error as e:
      print(f"Error altering contacts: {e}")
      conn.close()
      sys.exit(1)

def alter_contacts(cur):
   try:
      cur.execute("ALTER TABLE contacts ADD COLUMN IF NOT EXISTS (contact_since INT)")
   except mariadb.Error as e:
      print(f"Error altering contacts: {e}")
      conn.close()
      sys.exit(1)

try:
   conn = mariadb.connect(
      host="localhost",
      port=3306,
      user="db_user",
      password="test123456",
      db="testdb")
except mariadb.Error as e:
   print(f"Error connecting to the database: {e}")
   sys.exit(1)

cur = conn.cursor()
truncate_contacts(cur)
alter_contacts(cur)
conn.close()

