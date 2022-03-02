package cat.udl.eps.softarch.linkapp.handler;

import cat.udl.eps.softarch.linkapp.domain.Group;
import cat.udl.eps.softarch.linkapp.domain.User;
import cat.udl.eps.softarch.linkapp.repository.GroupRepository;
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
public class GroupEventHandler {
    final Logger logger = LoggerFactory.getLogger(Group.class);

    final GroupRepository groupRepository;

    public GroupEventHandler(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @HandleBeforeLinkSave
    public void handleGroupPreLinkSave(Group group, Object o) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @HandleBeforeSave
    public void handleGroupBeforeSave(Group group) {
        logger.info("Before updating: {}", group.toString());
    }

    @HandleAfterCreate
    public void handleGroupPostCreate(Group group) {
        groupRepository.save(group);
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

