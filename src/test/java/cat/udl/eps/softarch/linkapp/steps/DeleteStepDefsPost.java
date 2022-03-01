
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

public class DeleteStepDefsPost {

    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private UserRepository userRepository;

    @And("There is a post created by a user with username \"([^\"]*)\"$")
    public void thereIsAPostCreatedByAUserWithUsername(String user) {
        Post post = new Post();
        post.setAuthor(new User("Xavier"));
        group.setVisibility(true);
        postRepository.save(post);
    }

    @When("I delete a post with id \"([^\"]*)\"$")
    public void iDeleteAPostWithId(String id) throws Throwable{
        stepDefs.result = stepDefs.mockMvc.perform(
                delete("/posts/{id}", id).with(AuthenticationStepDefs.authenticate()));
    }

    @And("^It has been deleted a post with id \"([^\"]*)\"$")
    public void itHasBeenDeletedAPostWithId(String id) throws Throwable{
        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/posts/{id}", id)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andExpect(jsonPath("$.id").doesNotExist());
    }


}
