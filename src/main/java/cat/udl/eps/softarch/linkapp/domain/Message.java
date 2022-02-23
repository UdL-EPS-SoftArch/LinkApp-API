package cat.udl.eps.softarch.linkapp.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Date;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Message extends UriEntity<Long>  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull
    private ZonedDateTime creationDate;

    @NotBlank
    private String text;

    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    private User author;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToOne
    @NotNull
    private Meet meet;

    @Override
    public Long getId() { return id; }

}
