package cat.udl.eps.softarch.linkapp.handler;

import cat.udl.eps.softarch.linkapp.domain.*;
import cat.udl.eps.softarch.linkapp.exception.ValidationError;
import cat.udl.eps.softarch.linkapp.repository.MeetRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException.UnprocessableEntity;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;

@Component
@RepositoryEventHandler
public class MeetEventHandler
{

    final Logger logger = LoggerFactory.getLogger(Meet.class);

    final MeetRepository meetRepository;
    final UserRoleRepository userRoleRepository;

    public MeetEventHandler(MeetRepository meetRepository, UserRoleRepository userRoleRepository)
    {
        this.meetRepository = meetRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @HandleBeforeDelete
    public void handleMeetPreDelete(Meet meet)
    {
        User currentUser = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        Group group = meet.getGroup();
        UserRole userRole = userRoleRepository
                .findByRoleKeyUserAndRoleKeyGroup(currentUser, group);

        if (userRole == null || userRole.getRole() == UserRoleEnum.SUBSCRIBED)
        {
            throw new AccessDeniedException("Not enough permissions");
        }
    }

    @HandleBeforeCreate
    @Transactional
    public void handleMeetPreCreate(Meet meet) {
        ZonedDateTime now = ZonedDateTime.now().minusMinutes(1);
        if (meet.getInitialMeetDate().isBefore(now))
            throw new ValidationError("Initial meet date can't be in the past");

        if (meet.getInitialMeetDate().isAfter(meet.getFinalMeetDate()))
            throw new ValidationError("Final date can't be set before initial date");

        meet.setCreationDate(ZonedDateTime.now());
        meet.setLastUpdate(ZonedDateTime.now());
        meet.setStatus(true);

        User currentUser = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        Group group = meet.getGroup();

        UserRole userRole = userRoleRepository
                .findByRoleKeyUserAndRoleKeyGroup(currentUser, group);
        meet.getAttending().add(userRole);
        if (userRole == null || userRole.getRole() == UserRoleEnum.SUBSCRIBED)
        {
            throw new AccessDeniedException("Not enough permissions");
        }

    }

    @HandleBeforeLinkSave
    public void handleMeetPostCreate(Meet meet)
    {

    }

    @HandleBeforeSave
    public void handleMeetBeforeSave(Meet meet)
    {
        meet.setLastUpdate(ZonedDateTime.now());
    }


    @HandleAfterSave
    public void handleMeetPostSave(Meet meet)
    {
        meetRepository.save(meet);
    }

}
