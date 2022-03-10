package cat.udl.eps.softarch.linkapp.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Meet extends UriEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Group group;

    @NotNull
    @NotBlank
    private String title;

    @NotNull
    private Boolean status = true;

    @NotNull
    @NotBlank
    private String description;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @NotNull
    private ZonedDateTime creationDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @NotNull
    private ZonedDateTime lastUpdate;

    @NotNull
    private ZonedDateTime initialMeetDate;

    @NotNull
    private ZonedDateTime finalMeetDate;

    //may be null to indicate that this is not set
    private Long maxUsers = null;

    //may be null to indicate that location is not set
    private String location = null;

    @ManyToMany
    @JsonIdentityReference(alwaysAsId = true)
    private Set<UserRole> attending = new HashSet<>();

    @Override
    public Long getId() {
        return id;
    }

}
