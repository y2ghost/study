# 查询数据例子

import sqlite3


def get_cursor():
    conn = sqlite3.connect("books.db")
    return conn.cursor()


def select_all_records_by_author(cursor, author):
    sql = "SELECT * FROM books WHERE author=?"
    cursor.execute(sql, [author])
    print(cursor.fetchall())  # or use fetchone()
    print("\n这是表中的数据记录\n")
    for row in cursor.execute("SELECT rowid, * FROM books ORDER BY author"):
        print(row)


def select_using_like(cursor, text):
    print("\nLIKE查询结果:\n")
    sql = f"""
    SELECT * FROM books
    WHERE title LIKE '{text}%'"""
    cursor.execute(sql)
    print(cursor.fetchall())


if __name__ == '__main__':
    cursor = get_cursor()
    select_all_records_by_author(cursor,
                                 author='Mike Driscoll')
    select_using_like(cursor, text='Python')
