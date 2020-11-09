package no.hvl.dat250.FeedApp.Controllers;

import no.hvl.dat250.FeedApp.Models.Vote;
import no.hvl.dat250.FeedApp.Service.VotingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class VoteController {


    @Autowired
    private VotingService votingService;

    @GetMapping("/votes")
    List<Vote> getVotes() {
        return votingService.readAllVotes();
    }

    @GetMapping("/votes/{id}")
    Vote getVote(@PathVariable(name = "id") int id) {
        return votingService.readVote(id);
    }
    
    @PostMapping("/votes")
    Vote postVote(@RequestBody Vote vote) {
        boolean success = votingService.addVoteToPoll(vote);
        if (!success) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User and/or poll not found");
        }
        return vote;
    }
    

}
