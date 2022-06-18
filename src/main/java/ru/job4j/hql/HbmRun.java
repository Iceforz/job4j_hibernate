package ru.job4j.hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

public class HbmRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();
            session.save(Candidate.of("Stas", "junior", 10000));
            session.save(Candidate.of("Ivan", "middle", 20000));
            session.save(Candidate.of("Nick", "senior", 30000));

            Query query = session.createQuery("from Candidate");
            for (Object candidate : query.getResultList()) {
                System.out.println(candidate);
            }

            System.out.println(session.createQuery(
                    "from Candidate s where s.id = 3").uniqueResult());
            System.out.println(session.createQuery(
                    "from Candidate s where s.name = 'Ivan'").getResultList());

            session.createQuery(
                    "update Candidate  s set s.experience = :experience,"
                            +
                            " s.salary = :salary where s.id = :Id")
                    .setParameter("experience", "middle")
                    .setParameter("salary", 20000)
                    .setParameter("Id", 3)
                    .executeUpdate();

            session.createQuery("delete from Candidate where id = :Id")
                    .setParameter("Id", 1)
                    .executeUpdate();
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}