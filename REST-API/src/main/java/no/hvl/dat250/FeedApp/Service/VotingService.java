package no.hvl.dat250.FeedApp.Service;


import no.hvl.dat250.FeedApp.DAO.PollDAO;
import no.hvl.dat250.FeedApp.DAO.UserDAO;
import no.hvl.dat250.FeedApp.DAO.VoteDAO;
import no.hvl.dat250.FeedApp.Models.Poll;
import no.hvl.dat250.FeedApp.Models.User;
import no.hvl.dat250.FeedApp.Models.Vote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VotingService {

    @Autowired
    private PollDAO pollDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private VoteDAO voteDAO;

    public List<Vote> readAllVotes() {
        return voteDAO.read();
    }

    public Vote readVote(long id) {
        return voteDAO.read(id);
    }
    
    public boolean addVoteToPoll(Vote vote) {
        User user = userDAO.read(vote.getVoter().getId());
        Poll poll = pollDAO.read(vote.getPoll().getId());
        if (user == null || poll == null) {
            return false;
        }
        vote.setVoter(user);
        vote.setPoll(poll);
        voteDAO.create(vote);
        return true;
    }
    
}
