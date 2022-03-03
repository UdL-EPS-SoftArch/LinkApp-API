package cat.udl.eps.softarch.linkapp.domain;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


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
    private boolean visibility;

    @Override
    public Long getId() {
        return id;
    }
}
