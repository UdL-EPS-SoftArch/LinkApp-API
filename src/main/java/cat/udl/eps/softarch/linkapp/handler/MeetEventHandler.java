package cat.udl.eps.softarch.linkapp.handler;

import cat.udl.eps.softarch.linkapp.domain.Meet;
import cat.udl.eps.softarch.linkapp.domain.User;
import cat.udl.eps.softarch.linkapp.repository.MeetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@RepositoryEventHandler
public class MeetEventHandler {

    final Logger logger = LoggerFactory.getLogger(Meet.class);

    final MeetRepository meetRepository;

    public MeetEventHandler(MeetRepository meetRepository) {
        this.meetRepository = meetRepository;
    }

    @HandleBeforeCreate
    public void handleMeetPreCreate(Meet meet) {
        meet.setCreationDate(ZonedDateTime.now());
        meet.setLastUpdate(ZonedDateTime.now());
        meet.setStatus(true);
    }

    @HandleBeforeLinkSave
    public void handleMeetPreLinkSave(Meet meet, Object o) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @HandleBeforeSave
    public void handleMeetBeforeSave(Meet meet) {
        meet.setLastUpdate(ZonedDateTime.now());
    }

    @HandleAfterCreate
    public void handleMeetPostCreate(Meet meet) {
        meetRepository.save(meet);
    }

}
