package cat.udl.eps.softarch.linkapp.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Embeddable
@Data
public class UserRoleKey implements Serializable
{

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JsonIdentityReference(alwaysAsId = true)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JsonIdentityReference(alwaysAsId = true)
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public boolean equals(Object obj){
        UserRoleKey key = (UserRoleKey) obj;
        return this.group.getId() == key.group.getId()
                && this.user.getId().equals(key.user.getId());
    }
}
