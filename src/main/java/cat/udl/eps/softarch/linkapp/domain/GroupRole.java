package cat.udl.eps.softarch.linkapp.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Entity
@Table(name="GroupRole", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "group_id" }) })
@Data
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
