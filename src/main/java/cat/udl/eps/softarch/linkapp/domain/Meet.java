package cat.udl.eps.softarch.linkapp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true)
public class Meet extends UriEntity<Long> {

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
    public Long getId() {
        return id;
    }

}
