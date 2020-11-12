package no.hvl.dat250.FeedApp.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.hvl.dat250.FeedApp.DAO.UserDAO;
import no.hvl.dat250.FeedApp.Models.User;

@Service
public class UserService {

	@Autowired
	private UserDAO userDAO;

	public List<User> readAllUsers() {
		return userDAO.read();
	}

	public User readUserById(long id) {
		return userDAO.read(id);
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

	public void createUser(User user) {
		userDAO.create(user);
	}

	public void deleteUser(long id) {
		userDAO.delete(id);
	}

	public void updateUser(User user) {
		userDAO.update(user);
	}
}
