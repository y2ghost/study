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
        // 4.获取DAO的代理对象
        userDao = sqlSession.getMapper(IUserDao.class);
    }

    @AfterEach
    void destroy() throws Exception {
        sqlSession.close();
        in.close();
    }

    /**
     * 测试一级缓存
     */
    @Test
    void testFirstLevelCache() {
        User user1 = userDao.findById(41);
        System.out.println(user1);
        // 清空缓存
        sqlSession.clearCache();
        userDao = sqlSession.getMapper(IUserDao.class);
        User user2 = userDao.findById(41);
        System.out.println(user2);
        System.out.println(user1 == user2);
    }

    /**
     * 测试缓存的同步
     */
    @Test
    void testClearlCache() {
        // 1.根据id查询用户
        User user1 = userDao.findById(41);
        System.out.println(user1);

        // 2.更新用户信息
        user1.setUsername("update user clear cache");
        user1.setAddress("北京市海淀区");
        userDao.updateUser(user1);

        // 3.再次查询id为41的用户
        User user2 = userDao.findById(41);
        System.out.println(user2);
        System.out.println(user1 == user2);
    }
}
