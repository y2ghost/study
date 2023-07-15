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
import java.util.Date;
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
    void testSave() {
        User user = new User();
        user.setUsername("mybatis annotation");
        user.setAddress("北京市昌平区");
        userDao.saveUser(user);
    }

    @Test
    void testUpdate() {
        User user = new User();
        user.setId(57);
        user.setUsername("mybatis annotation update");
        user.setAddress("北京市海淀区");
        user.setSex("男");
        user.setBirthday(new Date());
        userDao.updateUser(user);
    }

    @Test
    void testDelete() {
        userDao.deleteUser(51);
    }

    @Test
    void testFindOne() {
        User user = userDao.findById(57);
        System.out.println(user);
    }

    @Test
    void testFindByName() {
        List<User> users = userDao.findUserByName("mybatis");
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    void testFindTotal() {
        int total = userDao.findTotalUser();
        System.out.println(total);
    }
}
