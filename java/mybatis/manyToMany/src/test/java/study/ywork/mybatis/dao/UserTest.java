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

class UserTest {
    private InputStream in;
    private SqlSession sqlSession;
    private IUserDao userDao;

    @BeforeEach
    void init() throws Exception {
        // 1.读取配置文件，生成字节输入流
        in = Resources.getResourceAsStream("SqlMapConfig.xml");
        // 2.获取SqlSessionFactory
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(in);
        // 3.获取SqlSession对象
        sqlSession = factory.openSession(true);
        // 4.获取dao的代理对象
        userDao = sqlSession.getMapper(IUserDao.class);
    }

    @AfterEach
    void destroy() throws Exception {
        // 6.释放资源
        sqlSession.close();
        in.close();
    }

    /**
     * 测试查询所有
     */
    @Test
    void testFindAll() {
        List<User> users = userDao.findAll();
        for (User user : users) {
            System.out.println("-----每个用户的信息------");
            System.out.println(user);
            System.out.println(user.getRoles());
        }
    }
}
