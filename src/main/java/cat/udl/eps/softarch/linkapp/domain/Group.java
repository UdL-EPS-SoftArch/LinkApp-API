package cat.udl.eps.softarch.linkapp.domain;


import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

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


    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIdentityReference(alwaysAsId = true)
    private List<UserRole> members = new ArrayList<>();


    public Group() {}

    @Override
    public Long getId() {
        return id;
    }

    public Group(long id, String name, String description, GroupVisibilityEnum visibility){
        this.id = id;
        this.title = name;
        this.description = description;
        this.visibility = visibility;
    }

    public Group(String name, String description, GroupVisibilityEnum visibility){
        this.title = name;
        this.description = description;
        this.visibility = visibility;
    }

    public void setDescription(UserRole role, String description) {
        if (members.contains(role) && role.getRole() == UserRoleEnum.ADMIN){
            this.description = description;
            //members = new ArrayList<>();
        }else {
            throw new SecurityException("User not authorized!");
        }
    }

    public void addMember(UserRole member){
        members.add(member);
    }
}
