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

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true)
public class Meet extends UriEntity<String> {

	@Id
	@GeneratedValue
	private Long id;

	@NotBlank
	private String title;

	@NotNull
	@NotBlank
	private Boolean status;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Transient
	private boolean passwordReset;

	@Override
	public String getId() { return id.toString(); }

}
