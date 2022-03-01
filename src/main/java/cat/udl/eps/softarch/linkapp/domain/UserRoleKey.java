package cat.udl.eps.softarch.linkapp.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


@Embeddable
@Data
public class UserRoleKey implements Serializable {

    @ManyToOne(optional = false)
    @JsonIdentityReference(alwaysAsId = true)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(optional = false)
    @JsonIdentityReference(alwaysAsId = true)
    @JoinColumn(name = "user_id")
    private User user;
}
