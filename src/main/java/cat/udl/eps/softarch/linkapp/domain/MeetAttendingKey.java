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
public class MeetAttendingKey implements Serializable {

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JsonIdentityReference(alwaysAsId = true)
    @JoinColumn(name = "meet_id")
    private Meet meet;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JsonIdentityReference(alwaysAsId = true)
    @JoinColumn(name = "user_id")
    private User user;

    public MeetAttendingKey(Meet meet, User user)
    {
        this.meet = meet;
        this.user = user;
    }

    public MeetAttendingKey()
    {

    }

    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof MeetAttendingKey)) {
            return false;
        }

        MeetAttendingKey key = (MeetAttendingKey) obj;
        return Objects.equals(this.meet.getId(), key.meet.getId())
                && Objects.equals(this.user.getId(), key.user.getId());
    }

    @Override
    public String toString()
    {
        return "MeetAttendingKey_user_id_" + user.getId() + "_meet_id_" + meet.getId();
    }
}
