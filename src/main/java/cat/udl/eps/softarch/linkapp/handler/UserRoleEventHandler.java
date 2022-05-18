package cat.udl.eps.softarch.linkapp.handler;

import cat.udl.eps.softarch.linkapp.domain.Group;
import cat.udl.eps.softarch.linkapp.domain.User;
import cat.udl.eps.softarch.linkapp.domain.UserRole;
import cat.udl.eps.softarch.linkapp.domain.UserRoleEnum;
import cat.udl.eps.softarch.linkapp.repository.UserRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import cat.udl.eps.softarch.linkapp.exception.ForbiddenException;
import cat.udl.eps.softarch.linkapp.repository.UserRepository;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterLinkSave;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeLinkSave;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import java.util.List;


@Component
@RepositoryEventHandler
public class UserRoleEventHandler {

    final Logger logger = LoggerFactory.getLogger(UserRole.class);
    final UserRepository userRepository;
    final UserRoleRepository userRoleRepository;

    public UserRoleEventHandler(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @HandleBeforeCreate
    public void handleUserRolePreCreate(UserRole userRole) {
        //User who modifies role
        User currentUser = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        Group group = userRole.getRoleKey().getGroup();
        UserRole userRolePrincipal = userRoleRepository.findByRoleKeyUserAndRoleKeyGroup(currentUser, group);

        if (userRolePrincipal != null) {
            throw new AccessDeniedException("Already subscribed to this group");
        }
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
    public void handleUserRolePreDelete(UserRole userRole) throws ForbiddenException {
        logger.info("Before deleting: {}", userRole.toString());
        if (userRole.getRole().equals(UserRoleEnum.ADMIN)) {
            List<UserRole> userRoleList = userRoleRepository.findByRoleKeyGroupAndRole(userRole.getRoleKey().getGroup(), UserRoleEnum.ADMIN);
            if (userRoleList.size() == 1) {
                throw new ForbiddenException();
            }
        }
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
