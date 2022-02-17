package cat.udl.eps.softarch.linkapp.handler;

import cat.udl.eps.softarch.linkapp.domain.Meet;
import cat.udl.eps.softarch.linkapp.repository.MeetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class MeetEventHandler {

    final Logger logger = LoggerFactory.getLogger(Meet.class);

    final MeetRepository meetRepository;

    public MeetEventHandler(MeetRepository meetRepository) {
        this.meetRepository = meetRepository;
    }

    @HandleBeforeCreate
    public void handleMeetPreCreate(Meet meet) { logger.info("Before creating: {}", meet.toString()); }

    @HandleAfterCreate
    public void handleMeetPostCreate(Meet meet) {
        logger.info("After creating: {}", meet.toString());
        meetRepository.save(meet);
    }



}
