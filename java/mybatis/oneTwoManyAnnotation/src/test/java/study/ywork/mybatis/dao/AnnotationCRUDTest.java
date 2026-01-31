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
import java.util.List;

class AnnotationCRUDTest {
    private InputStream in;
    private SqlSession session;
    private IUserDao userDao;

    @BeforeEach
    void init() throws Exception {
        in = Resources.getResourceAsStream("SqlMapConfig.xml");
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(in);
        session = factory.openSession();
        userDao = session.getMapper(IUserDao.class);
    }

    @AfterEach
    void destroy() throws Exception {
        session.commit();
        session.close();
        in.close();
    }

    @Test
    void testFindAll() {
        List<User> users = userDao.findAll();
        for (User user : users) {
            System.out.println("---每个用户的信息----");
            System.out.println(user);
            System.out.println(user.getAccounts());
        }
    }

    @Test
    void testFindOne() {
        User user = userDao.findById(57);
        System.out.println(user);
    }

    @Test
    void testFindByName() {
        List<User> users = userDao.findUserByName("%mybatis%");
        for (User user : users) {
            System.out.println(user);
        }
    }

}
