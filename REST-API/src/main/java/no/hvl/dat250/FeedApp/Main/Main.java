package no.hvl.dat250.FeedApp.Main;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import no.hvl.dat250.FeedApp.Models.Poll;
import no.hvl.dat250.FeedApp.Models.User;
import no.hvl.dat250.FeedApp.Models.Vote;


public class Main {
	
	private static final String PERSISTENCE_UNIT_NAME = "feedback";
    private static EntityManagerFactory factory;

	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();
        em.clear();

        em.getTransaction().begin();
        
        User user = createUser();
        Poll poll = createPoll(user);
        Vote vote = createVote(user);
        
        vote.setPoll(poll);
        ArrayList<Vote> votes = new ArrayList<Vote>();
        votes.add(vote);
        poll.setVotes(votes);
       

        em.persist(user);
        em.persist(poll);
        em.persist(vote);

        em.getTransaction().commit();
        
        Query q = em.createQuery("select t from User t");
        List<User> pollList = q.getResultList();
        for (User p : pollList) {
            System.out.println(p.toString());
        }
        Query q2 = em.createQuery("select v from Vote v");
        List<Vote> voteList = q2.getResultList();
        for (Vote v : voteList) {
            System.out.println(v.toString());
        }
        
        em.close();

	}
	
	public static User createUser() {
        User user = new User();
        user.setAdmin(true);
        user.setMail("kjetil@gmail.com");
        user.setName("Kjetil");
        return user;
    }
	
	public static Poll createPoll(User user) {
		Poll poll = new Poll();
		poll.setDescription("Stem");
		poll.setCode("123");
		poll.setPrivat(true);
		poll.setStartDate(new Date(System.currentTimeMillis()));
		poll.setEndDate(new Date(System.currentTimeMillis()));
		poll.setGreen("green");
		poll.setRed("red");
		poll.setTitle("Test1");
		poll.setUser(user);
		return poll;
		
		
	}
	
	public static Vote createVote(User user) {
		Vote vote = new Vote();
		vote.setResult(3);
		vote.setVoter(user);
		return vote;
		
	}

}
