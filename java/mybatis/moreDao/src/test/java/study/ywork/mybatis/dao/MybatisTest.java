package study.ywork.mybatis.dao;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import study.ywork.mybatis.dao.impl.UserDaoImpl;
import study.ywork.mybatis.domain.User;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

class MybatisTest {
    private InputStream in;
    private IUserDao userDao;

    @BeforeEach
    void init() throws Exception {
        // 1.读取配置文件，生成字节输入流
        in = Resources.getResourceAsStream("SqlMapConfig.xml");
        // 2.获取SqlSessionFactory
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(in);
        // 3.使用工厂对象，创建dao对象
        userDao = new UserDaoImpl(factory);
    }

    @AfterEach
    void destroy() throws Exception {
        // 6.释放资源
        in.close();
    }

    /**
     * 测试查询所有
     */
    @Test
    void testFindAll() {
        // 5.执行查询所有方法
        List<User> users = userDao.findAll();
        for (User user : users) {
            System.out.println(user);
        }

    }

    /**
     * 测试保存操作
     */
    @Test
    void testSave() {
        User user = new User();
        user.setUserName("dao impl user");
        user.setAddress("北京市顺义区");
        user.setSex("男");
        user.setBirthday(new Date());
        System.out.println("保存操作之前：" + user);
        // 5.执行保存方法
        userDao.saveUser(user);
        System.out.println("保存操作之后：" + user);
    }

    /**
     * 测试更新操作
     */
    @Test
    void testUpdate() {
        User user = new User();
        user.setId(50);
        user.setUserName("userdaoimpl update user");
        user.setAddress("北京市顺义区");
        user.setSex("女");
        user.setBirthday(new Date());

        // 5.执行保存方法
        userDao.updateUser(user);
    }

    /**
     * 测试删除操作
     */
    @Test
    void testDelete() {
        // 5.执行删除方法
        userDao.deleteUser(54);
    }

    /**
     * 测试删除操作
     */
    @Test
    void testFindOne() {
        // 5.执行查询一个方法
        User user = userDao.findById(50);
        System.out.println(user);
    }

    /**
     * 测试模糊查询操作
     */
    @Test
    void testFindByName() {
        // 5.执行查询一个方法
        List<User> users = userDao.findByName("%王%");
        for (User user : users) {
            System.out.println(user);
        }
    }

    /**
     * 测试查询总记录条数
     */
    @Test
    void testFindTotal() {
        // 5.执行查询一个方法
        int count = userDao.findTotal();
        System.out.println(count);
    }
}
