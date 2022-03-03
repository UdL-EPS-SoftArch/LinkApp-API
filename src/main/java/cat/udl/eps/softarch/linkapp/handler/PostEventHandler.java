package cat.udl.eps.softarch.linkapp.handler;

import cat.udl.eps.softarch.linkapp.domain.Post;
import cat.udl.eps.softarch.linkapp.domain.User;
import cat.udl.eps.softarch.linkapp.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.security.access.AccessDeniedException;
import java.time.ZonedDateTime;
import java.util.Objects;

@Component
@RepositoryEventHandler
public class PostEventHandler {

    final Logger logger = LoggerFactory.getLogger(Post.class);

    final PostRepository PostRepository;

    public PostEventHandler(PostRepository PostRepository) {
        this.PostRepository = PostRepository;
    }

    @HandleBeforeCreate
    public void handlePostPreCreate(Post Post) {
        Post.setCreationDate(ZonedDateTime.now());
        Post.setLastUpdate(ZonedDateTime.now());
    }

    @HandleBeforeSave
    public void handlePostBeforeSave(Post Post) {
        Post.setLastUpdate(ZonedDateTime.now());
    }

    @HandleAfterCreate
    public void handlePostPostCreate(Post Post) {
        PostRepository.save(Post);
    }

    @HandleBeforeDelete
    public void handlePostPreDelete(Post post) throws AccessDeniedException {
        User currentUser = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        User user = post.getAuthor();

        if (!Objects.equals(currentUser.getId(), user.getId()))
        {
            throw new AccessDeniedException("Not enough permissions");
        }
    }

}