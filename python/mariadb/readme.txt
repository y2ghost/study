Connection类
- 连接数据库的类
- connect函数: 建立连接
- cursor函数: 获取游标
- change_user函数: 切换用户
- reconnect函数: 重新连接
- close函数: 关闭连接

ConnectionPool类
- 维护连接池子
- get_connection函数: 获取连接对象
- add_connection函数: 添加连接对象
- close函数: 关闭连接池

