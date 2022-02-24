package cat.udl.eps.softarch.linkapp.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;




@Entity
@Table(name="GroupRole", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "group_id" }) })
@Data
@EqualsAndHashCode(callSuper = true)
public class GroupRole extends UriEntity<GroupRoleKey>{

    @EmbeddedId
    private GroupRoleKey roleKey;

    @Enumerated(EnumType.STRING)
    private GroupRolesEnum role;

    @Override
    public GroupRoleKey getId() {
        return this.roleKey;
    }
}
