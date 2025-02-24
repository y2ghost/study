package study.ywork.hibernate.entities;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.Test;
import study.ywork.hibernate.util.HibernateUtil;

class MovieTest {
    Logger log = LoggerFactory.getLogger(MovieTest.class);
    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Test
    void testPerson() {
        log.info("... test move ...");
        Session session = sessionFactory.openSession();
        // 存储数据
        Movie movie = new Movie();
        movie.setName("大头儿子小头爸爸");
        Actor actor1 = new Actor();
        actor1.setName("魏星");
        actor1.setMovie(movie);

        Actor actor2 = new Actor();
        actor2.setName("崔世昱");
        actor2.setMovie(movie);
        session.getTransaction().begin();
        session.save(movie);
        session.save(actor1);
        session.save(actor2);
        session.getTransaction().commit();

        // 获取数据
        movie = session.get(Movie.class, 1);
        log.info("move: {}", movie);
    }
}
