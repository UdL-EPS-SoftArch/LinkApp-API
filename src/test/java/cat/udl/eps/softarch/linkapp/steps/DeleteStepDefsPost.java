
package cat.udl.eps.softarch.linkapp.steps;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



import cat.udl.eps.softarch.linkapp.domain.Post;
import cat.udl.eps.softarch.linkapp.domain.User;
import cat.udl.eps.softarch.linkapp.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import cat.udl.eps.softarch.linkapp.repository.PostRepository;

import java.util.List;

public class DeleteStepDefsPost {

    private String newResourceUri;

    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @And("There is a post created by a user with username {string}")
    public void thereIsAPostCreatedByAUserWithUsername(String user) throws Throwable {
        Post post = new Post();
        post.setText("hola");
        User author = userRepository.findById(user).get();
        post.setAuthor(author);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/posts/")
                        .content(stepDefs.mapper.writeValueAsString(post))
                        .with(AuthenticationStepDefs.authenticate())).andDo(print());

        newResourceUri = stepDefs.result.andReturn().getResponse().getHeader("Location");
    }

    @When("I delete the post")
    public void iDeleteAPost() throws Throwable{
        stepDefs.result = stepDefs.mockMvc.perform(
                delete(newResourceUri).with(AuthenticationStepDefs.authenticate()));
    }

    @When("I delete the post with id {string}")
    public void iDeleteAPostWithId(String id) throws Throwable{
        stepDefs.result = stepDefs.mockMvc.perform(
                delete("/posts/{id}",id).with(AuthenticationStepDefs.authenticate()));
    }

    @And("It has been deleted the post")
    public void itHasBeenDeletedAPost() throws Throwable{
        stepDefs.result = stepDefs.mockMvc.perform(
                        get(newResourceUri)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andExpect(jsonPath("$.id").doesNotExist());
    }


    @And("There is no post created with id {string}")
    public void thereIsNoPostCreatedWithId(String id)  throws Throwable{
        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/posts/{id}", id)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andExpect(jsonPath("$.id").doesNotExist());
    }


    @And("There is a comment created by a user with username {string} from the post just created by user with username {string}")
    public void thereIsACommentCreatedByAUserWithUsernameFromThePostJustCreated(String user, String father) throws Throwable{
        Post post_child = new Post();
        post_child.setText("hola");
        User author = userRepository.findById(user).get();
        post_child.setAuthor(author);
        List<Post> posts = postRepository.findByAuthor_UsernameContaining(father);
        String id = String.valueOf(posts.get(posts.size()-1).getId());
        post_child.setFather(postRepository.findById(Long.valueOf(id)).get());

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/posts/")
                        .content(stepDefs.mapper.writeValueAsString(post_child))
                        .with(AuthenticationStepDefs.authenticate())).andDo(print());

        newResourceUri = stepDefs.result.andReturn().getResponse().getHeader("Location");
    }
}
