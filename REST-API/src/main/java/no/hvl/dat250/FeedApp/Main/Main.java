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
        Vote vote = createVote(user, 0, poll);
        Vote vote2 = createVote(user, 1, poll);
        Vote vote3 = createVote(user, 1, poll );
        
        vote.setPoll(poll);
        List<Vote> votes = new ArrayList<Vote>();
        votes.add(vote);
        votes.add(vote2);
        votes.add(vote3);
        poll.setVotes(votes);
        user.setVotes(votes);
        List<Poll> polls = new ArrayList<Poll>();
        polls.add(poll);
        user.setPoll(polls);
       

        em.persist(user);
        em.persist(vote);
        em.persist(vote2);
        em.persist(vote3);
        em.persist(poll);
        

        em.getTransaction().commit();
        
        Query q = em.createQuery("select t from User t");
        List<User> userList = q.getResultList();
        for (User u : userList) {
            System.out.println(u.toString());
        }
        Query q1 = em.createQuery("select t from Poll t");
        List<Poll> pollList = q1.getResultList();
        for (Poll p : pollList) {
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
        user.setEmail("kjetil@gmail.com");
        user.setName("Kjetil");
        return user;
    }
	
	public static Poll createPoll(User user) {
		Poll poll = new Poll();
		poll.setDescription("JA/NEI");
		poll.setCode("123");
		poll.setPrivat(true);
		poll.setStartDate(new Date(System.currentTimeMillis()));
		poll.setEndDate(new Date(System.currentTimeMillis()));
		poll.setGreen("green");
		poll.setRed("red");
		poll.setNoVotes(4);
		poll.setYesVotes(0);
		poll.setTitle("Poll");
		poll.setUser(user);
		
		return poll;
		
		
	}
	
	public static Vote createVote(User user, int value, Poll poll) {
		Vote vote = new Vote();
		vote.setValue(value);
		vote.setVoter(user);
		vote.setPoll(poll);
		return vote;
		
	}

}
