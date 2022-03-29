package cat.udl.eps.softarch.linkapp.handler;

import cat.udl.eps.softarch.linkapp.domain.*;
import cat.udl.eps.softarch.linkapp.exception.ValidationError;
import cat.udl.eps.softarch.linkapp.repository.MeetAttendingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RepositoryEventHandler
public class
MeetAttendingEventHandler {
    final Logger logger = LoggerFactory.getLogger(MeetAttending.class);

    final MeetAttendingRepository meetAttendingRepository;

    public MeetAttendingEventHandler(MeetAttendingRepository meetAttendingRepository) {
        this.meetAttendingRepository = meetAttendingRepository;
    }

    @HandleBeforeLinkSave
    public void handleMeetAttendingPreLinkSave(MeetAttending meetAttending, Object o) {

    }

    @HandleBeforeSave
    public void handleMeetAttendingBeforeSave(MeetAttending meetAttending) {
        logger.info("Before updating: {}", meetAttending.toString());
    }

    @HandleBeforeDelete
    public void handleMeetAttendingBeforeDelete(MeetAttending meetAttending)
    {
    }

    @HandleBeforeCreate
    public void handleMeetAttendingPreCreate(MeetAttending meetAttending)
    {
        assert meetAttending.getId() != null;
        Meet meet = meetAttending.getMeetAttendingKey().getMeet();
        Long maxUsers = meet.getMaxUsers();
        List <MeetAttending> meetAttendings = meetAttendingRepository.findByMeetAttendingKeyMeetAndAttends(meet, true);
        if (maxUsers != null && meetAttendings.size() >= maxUsers) {
            throw new ValidationError("Too many attending");
        }
    }


    @HandleAfterCreate
    public void handleMeetAttendingPostCreate(MeetAttending meetAttending) {
        meetAttendingRepository.save(meetAttending);
    }

    @HandleAfterDelete
    public void handleMeetAttendingPostDelete(MeetAttending meetAttending) {
        logger.info("After deleting: {}", meetAttending.toString());
    }

    @HandleAfterLinkSave
    public void handleUserPostLinkSave(User player, Object o) {
        logger.info("After linking: {} to {}", player.toString(), o.toString());
    }
}

