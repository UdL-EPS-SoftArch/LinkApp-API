package cat.udl.eps.softarch.linkapp.domain;


import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Data

public class Group extends UriEntity<String> {

    @Id
    @GeneratedValue
    private long id;

    @NotBlank
    @NotNull
    private String title;

    @NotBlank
    @NotNull
    private String description;

    @NotBlank
    @NotNull
    private boolean visibility;

    @Override
    public String getId() {
        return String.valueOf(this.id);
    }
}
