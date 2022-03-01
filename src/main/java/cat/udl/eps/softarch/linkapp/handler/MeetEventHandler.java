package cat.udl.eps.softarch.linkapp.handler;

import cat.udl.eps.softarch.linkapp.domain.*;
import cat.udl.eps.softarch.linkapp.repository.MeetRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

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
    public void handleMeetPreCreate(Meet meet)
    {
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
    }

    @HandleBeforeLinkSave
    public void handleMeetPreLinkSave(Meet meet, Object o)
    {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @HandleBeforeSave
    public void handleMeetBeforeSave(Meet meet)
    {
        meet.setLastUpdate(ZonedDateTime.now());
    }

    @HandleAfterCreate
    public void handleMeetPostCreate(Meet meet)
    {
        meetRepository.save(meet);
    }

}
