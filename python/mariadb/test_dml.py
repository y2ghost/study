import mariadb
import sys

def add_contact(cur, first_name, last_name, email):
    cur.execute("INSERT INTO contacts(first_name, last_name, email) VALUES (?, ?, ?)",
          (first_name, last_name, email))

def add_multiple_contacts(cur, data):
    cur.executemany("INSERT INTO contacts(first_name, last_name, email) VALUES (?, ?, ?)", data)

try:
   conn = mariadb.connect(
      host="localhost",
      port=3306,
      user="db_user",
      password="test123456",
      db="testdb",
      autocommit=True)
except mariadb.Error as e:
   print(f"Error connecting to the database: {e}")
   sys.exit(1)

cur = conn.cursor()
new_contact_fname = "f2"
new_contact_lname = "l2"
new_contact_email = "f2@l2.dev"

add_contact(cur,
    new_contact_fname,
    new_contact_lname,
    new_contact_email)

new_contacts = [
    ("multi1", "last1", "multi1@last1.dev"),
    ("multi2", "last2", "multi2@last2.dev"),
    ("multi3", "last3", "multi3@last3.dev")
  ]
add_multiple_contacts(cur, new_contacts)


def print_contacts(cur):
     """打印contacts表的信息"""
     contacts = []
     cur.execute("SELECT first_name, last_name, email FROM contacts")

     for (first_name, last_name, email) in cur:
        contacts.append(f"{first_name} {last_name} <{email}>")

     print("\n".join(contacts))

print_contacts(cur)
conn.close()
