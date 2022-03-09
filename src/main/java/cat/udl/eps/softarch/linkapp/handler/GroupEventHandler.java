package cat.udl.eps.softarch.linkapp.handler;

import cat.udl.eps.softarch.linkapp.domain.*;
import cat.udl.eps.softarch.linkapp.repository.GroupRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RepositoryEventHandler
public class
GroupEventHandler {
    final Logger logger = LoggerFactory.getLogger(Group.class);

    final GroupRepository groupRepository;
    final UserRoleRepository userRoleRepository;

    public GroupEventHandler(GroupRepository groupRepository, UserRoleRepository userRoleRepository) {
        this.groupRepository = groupRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @HandleBeforeLinkSave
    public void handleGroupPreLinkSave(Group group, Object o) {

    }

    @HandleBeforeSave
    public void handleGroupBeforeSave(Group group) {
        logger.info("Before updating: {}", group.toString());
    }

    @HandleBeforeDelete
    public void handleGroupBeforeDelete(Group group) {
        List<UserRole> roles = userRoleRepository.findByRoleKeyGroup(group);
        for (UserRole role: roles) {
            assert role.getId() != null;
            userRoleRepository.deleteById(role.getId());
        }
    }

    @HandleBeforeCreate
    public void handleGroupPreCreate(Group group) {

    }


    @HandleAfterCreate
    public void handleGroupPostCreate(Group group) {
        groupRepository.save(group);
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        UserRoleKey userRoleKey = new UserRoleKey();
        userRoleKey.setUser(user);
        userRoleKey.setGroup(group);

        UserRole userRole = new UserRole();
        userRole.setRoleKey(userRoleKey);
        userRole.setRole(UserRoleEnum.ADMIN);

        userRoleRepository.save(userRole);
    }

    @HandleAfterDelete
    public void handleGroupPostDelete(Group group) {
        logger.info("After deleting: {}", group.toString());
    }

    @HandleAfterLinkSave
    public void handleUserPostLinkSave(User player, Object o) {
        logger.info("After linking: {} to {}", player.toString(), o.toString());
    }
}