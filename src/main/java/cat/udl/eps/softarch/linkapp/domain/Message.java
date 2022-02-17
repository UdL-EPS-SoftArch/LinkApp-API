package cat.udl.eps.softarch.linkapp.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Message extends UriEntity<String>  {

    @Id
    @GeneratedValue
    Long id;

    @Temporal(TemporalType.DATE)
    Date publicationDate;

    @Temporal(TemporalType.TIME)
    Date publicationTime;

    @Temporal(TemporalType.TIMESTAMP)
    Date creationDateTime;

    @NotBlank
    @Column(unique = true)
    private String text;

    @NotBlank
    @Column(unique = true)
    private String author;

    @NotBlank
    @Column(unique = true)
    private String meet;

}
