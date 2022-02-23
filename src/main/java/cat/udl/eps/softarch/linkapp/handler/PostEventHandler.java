package cat.udl.eps.softarch.linkapp.handler;

import cat.udl.eps.softarch.linkapp.domain.Post;
import cat.udl.eps.softarch.linkapp.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

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
        Post.setCreateDate(ZonedDateTime.now());
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

}