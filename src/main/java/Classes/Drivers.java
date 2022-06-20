package Classes;

import javax.persistence.*;

@Entity
@Table(name = "new_schema.drivers")
public class Drivers {
	@Id
	@Column(name="driver_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int driver_id;

	@Column(name="driver_team")
	private String team;

	@Column(name="driver_name")
	private String name;

	@Column(name="driver_lastname")
	private String last_name;

	@Column(name="driver_score")
	private String score;

	public int getId() {
		return driver_id;
	}
	public void setTeam(String new_team) {
		this.team = new_team;
	}
	public String getTeam() {
		return team;
	}
	public void setName(String new_name) {
		this.name = new_name;
	}
	public String getName() {
		return name;
	}
	public void setLast_name(String new_last_name) {
		this.last_name = new_last_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setScore(String new_score) {
		this.score = new_score;
	}
	public String getScore() {
		return score;
	}
	

}