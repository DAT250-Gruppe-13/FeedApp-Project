package no.hvl.dat250.FeedApp.Models;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")

@Entity
@Data
@Table(name = "polls")
public class Poll {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private long id;

	@Column(name = "description")
	private String description;
	@Column(name = "title")
	private String title;
	@Column(name = "startDate")
	private Date startDate;
	@Column(name = "endDate")
	private Date endDate;
	private String red;
	private String green;
	private int noVotes;
	private int yesVotes;
	
	

	@Column(name = "code")
	private String code;
	@Column(name = "privat")
	private boolean privat;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonIgnoreProperties(value = "poll")
	private User user;

	@JsonIgnore
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy = "poll", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private List<Vote> votes;


}
