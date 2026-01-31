package study.ywork.hibernate.entities;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.Test;
import study.ywork.hibernate.util.HibernateUtil;

class PersonOneTest {
    Logger log = LoggerFactory.getLogger(PersonOneTest.class);
    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Test
    void testPerson() {
        log.info("... test person ...");
        Session session = sessionFactory.openSession();
        // 存储数据
        PassportDetailOne detail = new PassportDetailOne();
        detail.setPassportno("YY123456");
        PersonOne person = new PersonOne();
        person.setName("yy");
        person.setPassportDetail(detail);
        session.getTransaction().begin();
        session.save(person);
        session.getTransaction().commit();

        // 获取数据
        person = session.get(PersonOne.class, 1);
        log.info("person: {}", person);
    }
}
