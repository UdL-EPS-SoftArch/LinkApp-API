package cat.udl.eps.softarch.linkapp.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Post extends UriEntity<String>{

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
	private Date uploadDate;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@NotNull
	private Date lastUpdate;

	@ToString.Exclude
	@ManyToMany(cascade = CascadeType.ALL)
	private Set<Post> comments = new HashSet<>();

	@Override
	public String getId() { return id.toString(); }
}
