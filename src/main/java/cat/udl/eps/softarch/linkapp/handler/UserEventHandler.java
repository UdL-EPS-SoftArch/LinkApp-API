package cat.udl.eps.softarch.linkapp.handler;

import cat.udl.eps.softarch.linkapp.domain.User;
import cat.udl.eps.softarch.linkapp.exception.ForbiddenException;
import cat.udl.eps.softarch.linkapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterLinkSave;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeLinkSave;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class UserEventHandler {

    final Logger logger = LoggerFactory.getLogger(User.class);

    final UserRepository userRepository;

    public UserEventHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @HandleBeforeCreate
    public void handleUserPreCreate(User player) { logger.info("Before creating: {}", player.toString()); }

    @HandleBeforeSave
    public void handleUserPreSave(User player) {
        logger.info("Before updating: {}", player.toString());
    }

    @HandleBeforeDelete
    public void handleUserPreDelete(User player) throws ForbiddenException {
        logger.info("Before deleting: {}", player.toString());
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean sameId = user.getId().equals(player.getId());
        if (!sameId) {
            throw new ForbiddenException();
        }
    }

    @HandleBeforeLinkSave
    public void handleUserPreLinkSave(User player, Object o) {
        logger.info("Before linking: {} to {}", player.toString(), o.toString());
    }

    @HandleAfterCreate
    public void handleUserPostCreate(User player) {
        logger.info("After creating: {}", player.toString());
        player.encodePassword();
        userRepository.save(player);
    }

    @HandleAfterSave
    public void handleUserPostSave(User player) {
        logger.info("After updating: {}", player.toString());
        if (player.isPasswordReset()) {
            player.encodePassword();
        }
        userRepository.save(player);
    }

    @HandleAfterDelete
    public void handleUserPostDelete(User player) {
        logger.info("After deleting: {}", player.toString());
    }

    @HandleAfterLinkSave
    public void handleUserPostLinkSave(User player, Object o) {
        logger.info("After linking: {} to {}", player.toString(), o.toString());
    }
}
