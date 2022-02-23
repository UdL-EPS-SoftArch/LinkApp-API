package cat.udl.eps.softarch.linkapp.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

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
	private User author;



	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@NotNull
	private ZonedDateTime createDate;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@NotNull
	private ZonedDateTime lastUpdate;

	@ToString.Exclude
	@ManyToMany(cascade = CascadeType.ALL)
	private Set<Post> comments = new HashSet<>();

	@Override
	public Long getId() { return id; }

	public ZonedDateTime getCreateDate(){
		return createDate;
	}
	public ZonedDateTime getLastUpdate() {
		return lastUpdate;
	}
}
