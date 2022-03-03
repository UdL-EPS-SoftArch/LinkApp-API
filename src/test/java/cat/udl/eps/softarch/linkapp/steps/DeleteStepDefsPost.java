
package cat.udl.eps.softarch.linkapp.steps;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cat.udl.eps.softarch.linkapp.domain.Post;
import cat.udl.eps.softarch.linkapp.domain.User;
import cat.udl.eps.softarch.linkapp.repository.UserRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import cat.udl.eps.softarch.linkapp.repository.PostRepository;

public class DeleteStepDefsPost {

    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @And("There is a post with id {string} created by a user with username {string}")
    public Post thereIsAPostWithIdCreatedByAUserWithUsername(String id, String user) {
        Post post = new Post();
        post.setId(Long.valueOf(id));
        post.setText("hola");
        User author = userRepository.findById(user).get();
        post.setAuthor(author);
        postRepository.save(post);
        return post;
    }

    @When("I delete a post with id {string}")
    public void iDeleteAPostWithId(String id) throws Throwable{
        stepDefs.result = stepDefs.mockMvc.perform(
                delete("/posts/{id}", id).with(AuthenticationStepDefs.authenticate()));
    }

    @And("It has been deleted a post with id {string}")
    public void itHasBeenDeletedAPostWithId(String id) throws Throwable{
        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/posts/{id}", id)
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
}
