package cat.udl.eps.softarch.linkapp.handler;

import cat.udl.eps.softarch.linkapp.domain.MeetAttending;
import cat.udl.eps.softarch.linkapp.domain.Message;
import cat.udl.eps.softarch.linkapp.domain.User;
import cat.udl.eps.softarch.linkapp.domain.UserRole;
import cat.udl.eps.softarch.linkapp.exception.ValidationError;
import cat.udl.eps.softarch.linkapp.repository.MeetAttendingRepository;
import cat.udl.eps.softarch.linkapp.repository.MessageRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRoleRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Optional;

@Component
@RepositoryEventHandler
public class MessageEventHandler {

    final Logger logger = LoggerFactory.getLogger(Message.class);

    final MessageRepository messageRepository;
    final UserRoleRepository userRoleRepository;
    final MeetAttendingRepository meetAttendingRepository;

    public MessageEventHandler(MessageRepository messageRepository, UserRoleRepository userRoleRepository, MeetAttendingRepository meetAttendingRepository) {
        this.messageRepository = messageRepository;
        this.userRoleRepository = userRoleRepository;
        this.meetAttendingRepository = meetAttendingRepository;
    }

    @HandleBeforeCreate
    public void handleMessagePreCreate(Message message) throws Exception {
        User currentUser = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        message.setCreationDate(ZonedDateTime.now());

        Optional<MeetAttending> meetAttending = meetAttendingRepository.findByMeetAttendingKeyUserAndMeetAttendingKeyMeet(currentUser, message.getMeet());
        
        if (meetAttending.isPresent()) {
            message.setAuthor(currentUser);
        }else{
            throw new AccessDeniedException("You cannot send a message to a meet you are not assisting");
        }

        if (message.equals("")) {
            throw new ValidationError("Must not be blank");
        }

        if (!message.getMeet().getStatus()) {
            throw new AccessDeniedException("you cannot send a message to a closed meet");
        }
    }

    @HandleBeforeDelete
    public void handleMessagePreDelete(Message message) {
        throw new AccessDeniedException("You cannot delete a message");
    }

    @HandleAfterCreate
    public void handleMeetPostCreate(Message message) {
        messageRepository.save(message);
    }

}


