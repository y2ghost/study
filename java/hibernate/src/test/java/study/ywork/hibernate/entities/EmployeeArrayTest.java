package study.ywork.hibernate.entities;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.Test;
import study.ywork.hibernate.util.HibernateUtil;

class EmployeeArrayTest {
    Logger log = LoggerFactory.getLogger(EmployeeArrayTest.class);
    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Test
    void persistEmployee() {
        log.info("... persist employee ...");
        Session session = sessionFactory.openSession();
        // 存储数据
        EmployeeArray employee = new EmployeeArray();
        employee.setName("yytest");
        String[] emails = { "user1@yytest.com", "user2@yytest.com", "user3@yytest.com" };
        employee.setEmails(emails);
        session.getTransaction().begin();
        session.save(employee);
        session.getTransaction().commit();

        // 获取数据
        employee = session.get(EmployeeArray.class, 1);
        log.info("employee: {}", employee);

        // 更新数据
        emails = new String[] { "yytest@tyforever.cn" };
        session.getTransaction().begin();
        session.saveOrUpdate(employee);
        session.getTransaction().commit();

        // 删除数据
        session.getTransaction().begin();
        session.delete(employee);
        session.getTransaction().commit();
    }
}
