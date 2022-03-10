package cat.udl.eps.softarch.linkapp.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Objects;


@Entity
@Table(name = "UserRole",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {
                "user_id",
                "group_id"
            }
        )
    }
)
@Data
@EqualsAndHashCode(callSuper = true)
public class UserRole extends UriEntity<UserRoleKey> {

    @EmbeddedId
    private UserRoleKey roleKey;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof UserRole)) {
            return false;
        }

        UserRole key = (UserRole) obj;
        return Objects.equals(this.roleKey, key.getRoleKey());
    }

    @Override
    public UserRoleKey getId() {
        return this.roleKey;
    }
}
