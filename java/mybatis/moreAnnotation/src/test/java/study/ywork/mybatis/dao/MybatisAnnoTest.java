package study.ywork.mybatis.dao;

import study.ywork.mybatis.domain.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;
import java.io.InputStream;
import java.util.List;

class MybatisAnnoTest {
    @Test
    void testUserDao() throws Exception {
        // 1.获取字节输入流
        InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
        // 2.根据字节输入流构建SqlSessionFactory
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(in);
        // 3.根据SqlSessionFactory生产一个SqlSession
        SqlSession session = factory.openSession();
        // 4.使用SqlSession获取Dao的代理对象
        IUserDao userDao = session.getMapper(IUserDao.class);

        // 5.执行DAO的方法
        List<User> users = userDao.findAll();
        for (User user : users) {
            System.out.println(user);
        }

        // 6.释放资源
        session.close();
        in.close();
    }
}
