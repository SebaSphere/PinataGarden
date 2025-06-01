package dev.sebastianb.hibernateorm.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public interface ITable {

    SessionFactory getSessionFactory();


    default void writePersistentData(ITable table) {
        Session session = this.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(table);
        transaction.commit();
        session.close();
    }

    default void mergePersistentData(ITable table) {
        Session session = this.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        session.merge(table);

        transaction.commit();
        session.close();
    }


    default void deleteData(ITable table, String objectId) {
        Session session = this.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.remove(session.find(table.getClass(), objectId));
        transaction.commit();

    }

}
