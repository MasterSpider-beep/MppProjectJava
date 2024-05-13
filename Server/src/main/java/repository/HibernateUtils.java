package repository;

import domain.Ticket;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

public class HibernateUtils {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory(){
        if ((sessionFactory==null)||(sessionFactory.isClosed()))
            sessionFactory=createNewSessionFactory();
        return sessionFactory;
    }

    private static  SessionFactory createNewSessionFactory(){
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("jakarta.persistence.jdbc.url", "jdbc:sqlite:D:/Sem4/Mpp/DB/TourismAgency.db");
        sessionFactory = new Configuration()
                .addAnnotatedClass(Ticket.class)
                .addProperties(hibernateProperties)
                .buildSessionFactory();
        return sessionFactory;
    }

    public static  void closeSessionFactory(){
        if (sessionFactory!=null)
            sessionFactory.close();
    }
}
