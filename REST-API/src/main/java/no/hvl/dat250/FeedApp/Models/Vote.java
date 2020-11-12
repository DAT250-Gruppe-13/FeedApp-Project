package no.hvl.dat250.FeedApp.Models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Entity
@Data
@Table(name = "vote")
public class Vote {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private long id;

	private Integer value;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonIgnoreProperties(value = "votes")
	private User voter;

	@ManyToOne
	@JoinColumn(name = "poll_id")
	@JsonIgnoreProperties(value = "votes")
	private Poll poll;

}
