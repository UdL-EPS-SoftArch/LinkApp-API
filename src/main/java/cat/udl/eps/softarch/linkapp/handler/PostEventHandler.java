package cat.udl.eps.softarch.linkapp.handler;

import cat.udl.eps.softarch.linkapp.domain.*;
import cat.udl.eps.softarch.linkapp.repository.PostRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.security.access.AccessDeniedException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Component
@RepositoryEventHandler
public class PostEventHandler {

    final Logger logger = LoggerFactory.getLogger(Post.class);

    final PostRepository postRepository;

    final UserRoleRepository userRoleRepository;



    public PostEventHandler(PostRepository postRepository, UserRoleRepository userRoleRepository) {
        this.postRepository = postRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @HandleBeforeCreate
    public void handlePostPreCreate(Post post) {
        User currentUser = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Group group = post.getGroup();

        UserRole userRole = userRoleRepository.findByRoleKeyUserAndRoleKeyGroup(currentUser, group);
        post.setAuthor(userRole);

        if (userRole == null) {
            throw new AccessDeniedException("Not enough permissions");
        }

        post.setCreationDate(ZonedDateTime.now());
        post.setLastUpdate(ZonedDateTime.now());

    }

    @HandleBeforeSave
    public void handlePostBeforeSave(Post post) {
        User currentUser = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        UserRole userRole = post.getAuthor();

        if (!Objects.equals(currentUser.getId(), userRole.getRoleKey().getUser().getId()))
        {
            throw new AccessDeniedException("Not enough permissions");
        }
        post.setLastUpdate(ZonedDateTime.now());
    }

    @HandleAfterCreate
    public void handlePostPostCreate(Post post) throws AccessDeniedException {


    }


    @HandleBeforeDelete
    public void handlePostPreDelete(Post post) throws AccessDeniedException {
        User currentUser = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        UserRole authorUserRole = post.getAuthor();
        UserRole currentUserRole = userRoleRepository.findByRoleKeyUserAndRoleKeyGroup(currentUser, post.getGroup());

        if (!Objects.equals(authorUserRole.getRoleKey().getUser().getId(), currentUserRole.getRoleKey().getUser().getId()) &&
                currentUserRole.getRole() != UserRoleEnum.ADMIN)
        {
            throw new AccessDeniedException("Not enough permissions");
        }

        List<Post> sonsToDelete = postRepository.findByFather(post);

        postRepository.deleteAll(sonsToDelete);


    }

}