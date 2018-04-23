package by.i4t.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void saveOrUpdate(Object obj) {
        try {
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(obj);
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (RuntimeException re) {
            re.printStackTrace();
        }
    }
}
