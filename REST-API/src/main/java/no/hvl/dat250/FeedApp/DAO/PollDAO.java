package no.hvl.dat250.FeedApp.DAO;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import no.hvl.dat250.FeedApp.Models.Poll;


@Repository
public class PollDAO implements DAO<Poll> {

    private EntityManager em;

    public PollDAO() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("feedback");
        em = factory.createEntityManager();
    }

    @Override
    public List<Poll> read() {
        Query q =em.createQuery("Select p from Poll p");
        return q.getResultList();
    }

    @Override
    public Poll read(long id) {
       return em.find(Poll.class, id);
    }
    
   

    @Override
    public void create(Poll poll) {
        em.getTransaction().begin();
        em.persist(poll);
        em.getTransaction().commit();
    }

    @Override
    public void update(Poll poll) {
        em.getTransaction().begin();
        em.merge(poll);
        em.getTransaction().commit();
    }

    @Override
    public void delete(long id) {
        Poll p = em.find(Poll.class, id);
        em.getTransaction().begin();
        em.remove(p);
        em.getTransaction().commit();
    }
}
