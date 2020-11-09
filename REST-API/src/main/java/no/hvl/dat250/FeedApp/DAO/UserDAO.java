package no.hvl.dat250.FeedApp.DAO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import no.hvl.dat250.FeedApp.Models.User;

import java.util.List;
import java.util.Optional;


@Repository
public class UserDAO implements DAO<User> {

    private EntityManager em;

    public UserDAO() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("feedback");
        em = factory.createEntityManager();
    }

    @Override
    public List<User> read() {
        Query q = em.createQuery("Select u from User u");
        return q.getResultList();
    }

    @Override
    public User read(long id) {
        return em.find(User.class, id);
    }

    @Override
    public void create(User user) {
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
    }

    @Override
    public void update(User user) {
        em.getTransaction().begin();
        em.merge(user);
        em.getTransaction().commit();
    }

    @Override
    public void delete(long id) {
        User u = em.find(User.class, id);
        em.getTransaction().begin();
        em.remove(u);
        em.getTransaction().commit();
    }
}
