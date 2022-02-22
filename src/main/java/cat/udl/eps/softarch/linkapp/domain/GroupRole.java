package cat.udl.eps.softarch.linkapp.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Entity
@Data
public class GroupRole extends UriEntity<String>{

    @Id
    private long id;
    //private Group group;
    //private User user;

    @NotNull
    @NotBlank
    private Boolean admin;

    @Override
    public String getId() {
        return String.valueOf(this.id);
    }
}
