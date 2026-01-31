package study.ywork.mybatis.dao;

import study.ywork.mybatis.domain.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.InputStream;

class SecondLevelCatchTest {
    private InputStream in;
    private SqlSessionFactory factory;

    @BeforeEach
    void init() throws Exception {
        in = Resources.getResourceAsStream("SqlMapConfig.xml");
        factory = new SqlSessionFactoryBuilder().build(in);

    }

    @AfterEach
    void destroy() throws Exception {
        in.close();
    }

    @Test
    void testFindOne() {
        SqlSession session = factory.openSession();
        IUserDao userDao = session.getMapper(IUserDao.class);
        User user = userDao.findById(57);
        System.out.println(user);
        session.close();// 释放一级缓存
        SqlSession session1 = factory.openSession();// 再次打开session
        IUserDao userDao1 = session1.getMapper(IUserDao.class);
        User user1 = userDao1.findById(57);
        System.out.println(user1);
        session1.close();
    }
}
