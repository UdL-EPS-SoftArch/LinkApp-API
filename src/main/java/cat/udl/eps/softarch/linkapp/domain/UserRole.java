package cat.udl.eps.softarch.linkapp.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;




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
public class UserRole extends UriEntity<UserRoleKey>
{

    @EmbeddedId
    private UserRoleKey roleKey;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    @Override
    public boolean equals(Object obj){
        UserRole key = (UserRole) obj;
        return this.roleKey.equals(key.getRoleKey());
    }

    @Override
    public UserRoleKey getId() {
        return this.roleKey;
    }
}
