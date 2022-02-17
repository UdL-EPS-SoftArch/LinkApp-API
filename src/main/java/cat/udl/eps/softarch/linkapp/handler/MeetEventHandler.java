package cat.udl.eps.softarch.linkapp.handler;

import cat.udl.eps.softarch.linkapp.domain.Meet;
import cat.udl.eps.softarch.linkapp.domain.User;
import cat.udl.eps.softarch.linkapp.repository.MeetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Date;

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
        meet.setCreationDate(new Date());
        logger.info("Before creating: {}", meet.toString());
    }

    @HandleBeforeSave
    public void handleMeetPreSave(Meet meet) {
        meet.setLastUpdate(new Date());
        logger.info("Before updating: {}", meet.toString());
    }

    @HandleAfterCreate
    public void handleMeetPostCreate(Meet meet) {
        logger.info("After creating: {}", meet.toString());
        meetRepository.save(meet);
    }



}
