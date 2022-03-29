package cat.udl.eps.softarch.linkapp.handler;

import cat.udl.eps.softarch.linkapp.domain.*;
import cat.udl.eps.softarch.linkapp.repository.GroupRepository;
import cat.udl.eps.softarch.linkapp.repository.MeetRepository;
import cat.udl.eps.softarch.linkapp.repository.PostRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.security.access.AccessDeniedException;
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
    final PostRepository postRepository;
    final MeetRepository meetRepository;

    public GroupEventHandler(GroupRepository groupRepository, UserRoleRepository userRoleRepository,
                             PostRepository postRepository, MeetRepository meetRepository) {
        this.groupRepository = groupRepository;
        this.userRoleRepository = userRoleRepository;
        this.postRepository = postRepository;
        this.meetRepository = meetRepository;
    }

    @HandleBeforeLinkSave
    public void handleGroupPreLinkSave(Group group, Object o) {

    }


    @HandleBeforeSave
    public void handleGroupBeforeSave(Group group) {
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        UserRole userRole = userRoleRepository.findByRoleKeyUserAndRoleKeyGroup(user, group);

        if (userRole == null || userRole.getRole() != UserRoleEnum.ADMIN) {
            throw new AccessDeniedException("Only ADMINS can modify Groups!");
        }




    }

    @HandleBeforeDelete
    public void handleGroupBeforeDelete(Group group) throws AccessDeniedException {

        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        UserRole userRole = userRoleRepository.findByRoleKeyUserAndRoleKeyGroup(user, group);

        if (userRole == null || userRole.getRole() != UserRoleEnum.ADMIN) {
            throw new AccessDeniedException("Only ADMINS can delete Groups!");
        }

        List<UserRole> roles = userRoleRepository.findByRoleKeyGroup(group);
        for (UserRole role: roles) {
            assert role.getId() != null;
            userRoleRepository.deleteById(role.getId());
        }

        List<Post> posts = postRepository.findByGroup(group);
        for (Post post: posts) {
            assert post.getId() != null;
            postRepository.deleteById(post.getId());
        }

        List<Meet> meets = meetRepository.findByGroup(group);
        for (Meet meet: meets){
            assert meet.getId() != null;
            postRepository.deleteById(meet.getId());

        }

    }

    @HandleBeforeCreate
    public void handleGroupPreCreate(Group group) {

    }


    @HandleAfterCreate
    public void handleGroupPostCreate(Group group) {
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
        //groupRepository.save(group);
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