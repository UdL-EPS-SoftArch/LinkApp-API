package cat.udl.eps.softarch.linkapp.handler;

import cat.udl.eps.softarch.linkapp.domain.Message;
import cat.udl.eps.softarch.linkapp.domain.User;
import cat.udl.eps.softarch.linkapp.repository.MessageRepository;
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

    public MessageEventHandler(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @HandleBeforeCreate
    public void handleMessagePreCreate(Message message) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        message.setAuthor(user);
        message.setCreationDate(ZonedDateTime.now());
    }

    @HandleAfterCreate
    public void handleMeetPostCreate(Message message) {
        messageRepository.save(message);
    }
}


