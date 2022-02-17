package cat.udl.eps.softarch.linkapp.domain;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
@Data

public class Group extends UriEntity<String> {

    @Id
    private long id;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @Override
    public String getId() {
        return String.valueOf(this.id);
    }
}
