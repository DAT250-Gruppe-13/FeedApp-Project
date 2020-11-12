package no.hvl.dat250.FeedApp.Controllers;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;

import no.hvl.dat250.FeedApp.Models.Poll;
import no.hvl.dat250.FeedApp.Models.Vote;
import no.hvl.dat250.FeedApp.Service.PollService;

@RestController
public class PollController {

	@Autowired
	private PollService pollService;

	@GetMapping("/polls")
	List<Poll> getPolls() {
		return pollService.readAllPolls();
	}

	@GetMapping("/polls/{id}")
	Poll getPoll(@PathVariable(name = "id") int id) {
		return pollService.readPoll(id);
	}

	@DeleteMapping("/polls/{id}")
	void deletePoll(@PathVariable(name = "id") int id) {
		pollService.deletePoll(id);
	}
	
	@PostMapping("/polls")
	Poll postPoll(@RequestHeader (name="userEmail") String email,@RequestBody Poll poll) {
		System.out.println(poll.toString());
		System.out.println(email);
		boolean success = pollService.createPoll(poll, email);
		if (!success) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
		}
		return poll;
	}

	@GetMapping("/polls/finished")
	public ResponseEntity<List<Long>> getFinishedPolls() {
		
		List<Long> pollsFinished = pollService.readFinishedPoll();
		
		if (!pollsFinished.isEmpty()) {
			return new ResponseEntity<>(pollsFinished, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}

	@GetMapping("/polls/result/{id}")
	public ResponseEntity<Map<String, Object>> getResults(@PathVariable("id") long id) throws JsonProcessingException {
		Poll poll = pollService.readPoll(id);
		if (poll != null) {

			int red = 0;
			int green = 0;

			List<Vote> votes = poll.getVotes();
			for (Vote v : votes) {
				int r = v.getValue();
				if (r == 0) {
					red++;
				} else {
					green++;
				}
			}

			JSONObject jsonRed = new JSONObject();
			jsonRed.put("text", poll.getRed());
			jsonRed.put("value", red);

			JSONObject jsonGreen = new JSONObject();
			jsonGreen.put("text", poll.getGreen());
			jsonGreen.put("value", green);

			JSONObject json = new JSONObject();
			json.put("id", poll.getId());
			json.put("title", poll.getTitle());
			json.put("description", poll.getDescription());
			json.put("startDate", poll.getStartDate());
			json.put("endDate", poll.getEndDate());
			json.put("red", jsonRed);
			json.put("green", jsonGreen);

			return new ResponseEntity<>(json.toMap(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/polls")
	Poll putPoll(@RequestBody Poll poll) {
		pollService.updatePoll(poll);
		return poll;
	}

}