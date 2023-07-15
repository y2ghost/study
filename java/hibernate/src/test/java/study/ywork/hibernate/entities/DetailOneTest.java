package study.ywork.hibernate.entities;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.Test;
import study.ywork.hibernate.util.HibernateUtil;

class DetailOneTest {
    Logger log = LoggerFactory.getLogger(DetailOneTest.class);
    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Test
    void persistEmployee() {
        log.info("... persist employee ...");
        Session session = sessionFactory.openSession();
        // 存储数据
        DetailOne detail = new DetailOne();
        detail.setCity("YueYang");
        EmployeeOne employee = new EmployeeOne();
        employee.setName("yy");
        employee.setEmployeeDetail(detail);
        session.getTransaction().begin();
        session.save(detail);
        session.save(employee);
        session.getTransaction().commit();

        // 获取数据
        employee = session.get(EmployeeOne.class, 1);
        log.info("employee: {}", employee);
    }
}
