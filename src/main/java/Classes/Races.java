package Classes;

import javax.persistence.*;

@Entity
@Table(name = "new_schema.races")
public class Races {
	@Id
	@Column(name="race_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int race_id;

	@Column(name="race_track")
	private String track;

	@Column(name="race_distance")
	private String distance;

	@Column(name="race_data")
	private String data;

	public int getId() {
		return race_id;
	}
	public void setTrack(String new_track) {
		this.track = new_track;
	}
	public String getTrack() {
		return track;
	}
	public void setDistance(String new_distance) {
		this.distance = new_distance;
	}
	public String getDistance() {
		return distance;
	}
	public void setData(String new_data) {
		this.data = new_data;
	}
	public String getData() {
		return data;
	}


}