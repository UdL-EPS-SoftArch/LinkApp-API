package cat.udl.eps.softarch.linkapp.handler;

import cat.udl.eps.softarch.linkapp.domain.Message;
import cat.udl.eps.softarch.linkapp.domain.User;
import cat.udl.eps.softarch.linkapp.domain.UserRole;
import cat.udl.eps.softarch.linkapp.repository.MessageRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRoleRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@RepositoryEventHandler
public class MessageEventHandler {

    final Logger logger = LoggerFactory.getLogger(Message.class);

    final MessageRepository messageRepository;
    final UserRoleRepository userRoleRepository;

    public MessageEventHandler(MessageRepository messageRepository, UserRoleRepository userRoleRepository) {
        this.messageRepository = messageRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @HandleBeforeCreate
    public void handleMessagePreCreate(Message message) throws Exception {
        User currentUser = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        message.setCreationDate(ZonedDateTime.now());
        UserRole userRole = userRoleRepository
                .findByRoleKeyUserAndRoleKeyGroup(currentUser, message.getGroup());

        if (message.getMeet().getAttending().contains(userRole)) {
            message.setAuthor(currentUser);
        }else{
            throw new AccessDeniedException("You cannot send a message to a meet you are not assisting");
        }

        if (message.equals("")) {
            throw new Exception("Must not be blank");
        }
    }

    @HandleBeforeDelete
    public void handleMessagePreDelete(Message message) {
        throw new AccessDeniedException("You cannot delete a message");
    }

    @HandleBeforeSave
    public void handleMessagePreSave(Message message) {
        if(messageRepository.existsById(message.getId())){
            throw new AccessDeniedException("You cannot edit a message");
        }
    }

    @HandleAfterCreate
    public void handleMeetPostCreate(Message message) {
        messageRepository.save(message);
    }

}


