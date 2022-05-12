package cat.udl.eps.softarch.linkapp.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.ZonedDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Post extends UriEntity<Long>{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String text;

	@ManyToOne
	@JsonIdentityReference(alwaysAsId = true)
	private UserRole author;

	@ManyToOne
	@JsonIdentityReference(alwaysAsId = true)
	private User user;

	@ManyToOne
	@JsonIdentityReference(alwaysAsId = true)
	private Group group;


	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private ZonedDateTime creationDate;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private ZonedDateTime lastUpdate;


	@ToString.Exclude
	@JsonIdentityReference(alwaysAsId = true)
	@ManyToOne()
	private Post father;

	@Override
	public Long getId() { return id; }

	public ZonedDateTime getCreationDate(){
		return creationDate;
	}
	public ZonedDateTime getLastUpdate() {
		return lastUpdate;
	}
}
