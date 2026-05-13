package study.ywork.hibernate.entities;

import java.util.HashSet;
import java.util.Set;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.Test;
import study.ywork.hibernate.util.HibernateUtil;

class EmployeeSetTest {
    Logger log = LoggerFactory.getLogger(EmployeeSetTest.class);
    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Test
    void persistEmployee() {
        log.info("... persist employee ...");
        Session session = sessionFactory.openSession();
        // 存储数据
        EmployeeSet employee = new EmployeeSet();
        employee.setName("yytest");
        Set<String> emails = new HashSet<String>();
        emails.add("user1@yytest.com");
        emails.add("user2@yytest.com");
        emails.add("user3@yytest.com");
        employee.setEmails(emails);
        session.getTransaction().begin();
        session.save(employee);
        session.getTransaction().commit();

        // 获取数据
        employee = session.get(EmployeeSet.class, 1);
        log.info("employee: {}", employee);

        // 更新数据
        emails = employee.getEmails();
        emails.add("user3@yytest.com");
        emails.add("yytest@tyforever.cn");
        session.getTransaction().begin();
        session.saveOrUpdate(employee);
        session.getTransaction().commit();

        // 删除数据
        session.getTransaction().begin();
        session.delete(employee);
        session.getTransaction().commit();
    }
}
