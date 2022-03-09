package cat.udl.eps.softarch.linkapp.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;


@Embeddable
@Data
public class UserRoleKey implements Serializable {

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
        if (!(obj instanceof UserRoleKey)) {
            return false;
        }

        UserRoleKey key = (UserRoleKey) obj;
        return Objects.equals(this.group.getId(), key.group.getId())
                && Objects.equals(this.user.getId(), key.user.getId());
    }
}
