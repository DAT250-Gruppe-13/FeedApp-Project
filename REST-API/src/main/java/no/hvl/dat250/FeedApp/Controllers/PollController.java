package no.hvl.dat250.FeedApp.Controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	Poll postPoll(@RequestBody Poll poll) {
		boolean success = pollService.createPoll(poll);
		if (!success) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
		}
		return poll;
	}
	
	@GetMapping("/polls/recentlyfinished")
	public ResponseEntity<List<Long>> getEndedPolls() {
		List<Poll> polls = pollService.readAllPolls();
		List<Long> idEndedPolls = new ArrayList<Long>();
		
		for (Poll p : polls)
			idEndedPolls.add(p.getId());
		
		if (!idEndedPolls.isEmpty()) {
			return new ResponseEntity<>(idEndedPolls, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping("/polls/result/{id}")
	public ResponseEntity<Map<String, Object>> getLiveResults(@PathVariable("id") long id) throws JsonProcessingException {
		Poll poll = pollService.readPoll(id);
		if (poll != null) {
			
			int red = 0;
			int green = 0;

			List<Vote> votes = poll.getVotes();
			for (Vote v : votes) {
				int result = v.getResult();
				if (result == 0) {
					red++;
				} else {
					green++;
				}
			}
			
			JSONObject redJson = new JSONObject();
			redJson.put("text", poll.getRed());
			redJson.put("amount", red);
			
			JSONObject greenJson = new JSONObject();
			greenJson.put("text", poll.getGreen());
			greenJson.put("amount", green);
			
			JSONObject json = new JSONObject();
			json.put("id", poll.getId());
			json.put("title", poll.getDescription());
			json.put("description", poll.getDescription());
			json.put("startDate", poll.getStartDate());
			json.put("endDate", poll.getEndDate());
			json.put("red", redJson);
			json.put("green", greenJson);
			
			return new ResponseEntity<>( json.toMap(), HttpStatus.OK);
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