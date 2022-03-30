package cat.udl.eps.softarch.linkapp.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;


@Entity
@Table(name = "MeetAttending",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {
                "user_id",
                "meet_id"
            }
        )
    }
)
@Data
@EqualsAndHashCode(callSuper = true)
public class MeetAttending extends UriEntity<MeetAttendingKey> {

    @EmbeddedId
    private MeetAttendingKey meetAttendingKey;

    @NotNull
    private Boolean attends;

    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof MeetAttending)) {
            return false;
        }

        MeetAttending key = (MeetAttending) obj;
        return Objects.equals(this.meetAttendingKey, key.getMeetAttendingKey());
    }

    @Override
    public MeetAttendingKey getId() {
        return this.meetAttendingKey;
    }
}
