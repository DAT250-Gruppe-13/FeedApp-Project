package no.hvl.dat250.FeedApp.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.hvl.dat250.FeedApp.DAO.PollDAO;
import no.hvl.dat250.FeedApp.DAO.UserDAO;
import no.hvl.dat250.FeedApp.Models.Poll;
import no.hvl.dat250.FeedApp.Models.User;

@Service
public class PollService {

	@Autowired
	private PollDAO pollDAO;

	@Autowired
	private UserDAO userDAO;

	public List<Poll> readAllPolls() {
		return pollDAO.read();
	}

	public List<Long> readFinishedPoll() {
		List<Poll> polls = readAllPolls();
		List<Long> pollsFinished = new ArrayList<Long>();
		Date date = new Date(System.currentTimeMillis());

		for (Poll p : polls) {

			if (p.getEndDate().before(date)) {
				pollsFinished.add(p.getId());
			}
		}
		return pollsFinished;
	}
	
	

	public Poll readPoll(long id) {
		return pollDAO.read(id);
	}

	public boolean createPoll(Poll poll, String email) {
		User user = readUserByEmail(email);
		if (user == null) {
			return false;
		}
		poll.setUser(user);
		pollDAO.create(poll);
		return true;

	}
	public boolean createPoll(Poll poll) {
		User user = userDAO.read(poll.getUser().getId());;
		if (user == null) {
			return false;
		}
		poll.setUser(user);
		pollDAO.create(poll);
		return true;

	}
	
	public User readUserByEmail(String email) {
		List<User> users = userDAO.read();
		User user = null;
		for (User u : users) {
			if (u.getEmail().equals(email))
				user = u;
		}
		return user;
	}

	public void deletePoll(long id) {
		pollDAO.delete(id);
	}

	public void updatePoll(Poll poll) {
		pollDAO.update(poll);
	}
}
