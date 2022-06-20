package Classes;

import javax.persistence.*;

@Entity
@Table(name = "new_schema.winners")
public class Winners {
	@Id
	@Column(name="winner_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int winner_id;

	@Column(name="driver_id")
	private int driver_id;

	@Column(name="track_id")
	private int track_id;

	@Column(name="place")
	private int place;

	public int getId() {
		return winner_id;
	}

	public int getDriver_id() {
		return driver_id;
	}
	public void setDriver_id(int new_driver_id) {
		this.driver_id = new_driver_id;
	}
	public int getTrack_id() { return track_id;	}
	public void setTrack_id(int new_track_id) {
		this.track_id = new_track_id;
	}
	public int getPlace() { return place; }
	public void setPlace(int new_place) {
		this.place = new_place;
	}


}