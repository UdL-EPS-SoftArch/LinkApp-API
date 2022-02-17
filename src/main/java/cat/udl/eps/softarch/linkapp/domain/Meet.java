package cat.udl.eps.softarch.linkapp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true)
public class Meet extends UriEntity<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@NotBlank
	private String title;

	@NotNull
	private Boolean status;

	@NotNull
	@NotBlank
	private String description;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@NotNull
	private Date creationDate;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@NotNull
	private Date lastUpdate;

	@NotNull
	private Date meetDate;

	//may be null to indicate that this is not set
	private Long maxUsers;

	//may be null to inidcate that location is not set
	private String location;

	@Override
	public String getId() { return id.toString(); }

}
