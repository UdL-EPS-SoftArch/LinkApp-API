package cat.udl.eps.softarch.linkapp.handler;

import cat.udl.eps.softarch.linkapp.domain.Group;
import cat.udl.eps.softarch.linkapp.domain.User;
import cat.udl.eps.softarch.linkapp.domain.UserRole;
import cat.udl.eps.softarch.linkapp.domain.UserRoleEnum;
import cat.udl.eps.softarch.linkapp.repository.UserRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class UserRoleEventHandler {

    final Logger logger = LoggerFactory.getLogger(UserRole.class);

    final UserRoleRepository userRoleRepository;

    public UserRoleEventHandler(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @HandleBeforeCreate
    public void handleUserRolePreCreate(UserRole userRole) {
        logger.info("Before creating: {}", userRole.toString());
    }

    @HandleBeforeSave
    public void handleUserRolePreSave(UserRole userRole) {
        //User who modifies role
        User currentUser = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        Group group = userRole.getRoleKey().getGroup();
        UserRole userRolePrincipal = userRoleRepository
                .findByRoleKeyUserAndRoleKeyGroup(currentUser, group);

        if (currentUser.getId().equals(userRole.getRoleKey().getUser().getId())) {
            throw new AccessDeniedException("Not enough permissions");
        } else if (userRolePrincipal == null || userRolePrincipal.getRole() != UserRoleEnum.ADMIN) {
            throw new AccessDeniedException("Not enough permissions");
        }
    }

    @HandleBeforeDelete
    public void handleUserRolePreDelete(UserRole userRole) {
        logger.info("Before deleting: {}", userRole.toString());
    }

    @HandleBeforeLinkSave
    public void handleUserRolePreLinkSave(UserRole userRole, Object o) {
        logger.info("Before linking: {} to {}", userRole.toString(), o.toString());
    }

    @HandleAfterCreate
    public void handleUserRolePostCreate(UserRole userRole) {
        logger.info("After creating: {}", userRole.toString());
    }

    @HandleAfterSave
    public void handleUserRolePostSave(UserRole userRole) {
        logger.info("After updating: {}", userRole.toString());
    }

    @HandleAfterDelete
    public void handleUserRolePostDelete(UserRole userRole) {
        logger.info("After deleting: {}", userRole.toString());
    }

    @HandleAfterLinkSave
    public void handleUserRolePostLinkSave(UserRole userRole, Object o) {
        logger.info("After linking: {} to {}", userRole.toString(), o.toString());
    }

}
