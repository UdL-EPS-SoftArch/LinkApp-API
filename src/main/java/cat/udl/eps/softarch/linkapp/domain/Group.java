package cat.udl.eps.softarch.linkapp.domain;


import cat.udl.eps.softarch.linkapp.repository.UserRoleRepository;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;


@Entity
@Data
@Table(name = "LinkGroup")
@EqualsAndHashCode(callSuper = true)
public class Group extends UriEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @NotNull
    private String title;

    @NotBlank
    @NotNull
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private GroupVisibilityEnum visibility;

    //@NotNull
    @ElementCollection(fetch=FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<ThemeEnum> themes;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;


    public Group() {}

    public Group(long id, String name, String description, GroupVisibilityEnum visibility) {
        this.id = id;
        this.title = name;
        this.description = description;
        this.visibility = visibility;
    }

    public Group(long id, String name, String description, GroupVisibilityEnum visibility, List<ThemeEnum> themes) {
        this.id = id;
        this.title = name;
        this.description = description;
        this.visibility = visibility;
        this.themes = themes;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object object){
        Group group = (Group) object;
        return group.getId().equals(this.getId());
    }

/*    public void setDescription(String description, UserRole userRole) {
        if (userRole.getRole() == UserRoleEnum.ADMIN && userRole.getRoleKey().getGroup().equals(this)){
            this.description = description;
        } else {
            throw new AccessDeniedException("User doesn't have permissions");
        }


    }
    */
}
