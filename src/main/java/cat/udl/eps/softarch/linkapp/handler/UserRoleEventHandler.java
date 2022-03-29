package cat.udl.eps.softarch.linkapp.handler;
import cat.udl.eps.softarch.linkapp.domain.User;
import cat.udl.eps.softarch.linkapp.domain.UserRole;
import cat.udl.eps.softarch.linkapp.domain.UserRoleEnum;
import cat.udl.eps.softarch.linkapp.exception.ForbiddenException;
import cat.udl.eps.softarch.linkapp.repository.GroupRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRepository;
import cat.udl.eps.softarch.linkapp.repository.MeetRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRoleRepository;
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
import java.util.List;


@Component
@RepositoryEventHandler
public class UserRoleEventHandler {

    final Logger logger = LoggerFactory.getLogger(User.class);
    final UserRepository userRepository;
    final UserRoleRepository userRoleRepository;

    public UserRoleEventHandler(UserRepository userRepository, UserRoleRepository userRoleRepository, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
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
}
