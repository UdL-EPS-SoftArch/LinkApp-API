package cat.udl.eps.softarch.linkapp.steps;

import cat.udl.eps.softarch.linkapp.domain.Post;
import cat.udl.eps.softarch.linkapp.repository.PostRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class ModifyStepDefsPost {

    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @When("I modify a post with id {string} with new text {string}")
    public void iModifyAPostWithId(String id, String text) throws Throwable{
        stepDefs.result = stepDefs.mockMvc.perform(
                        patch("/posts/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new JSONObject().put("text", text).toString())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @And("It has been modified a post with id {string} with the text {string}")
    public void itHasBeenModifiedAPostWithIdWithTheText(String id, String text) throws Throwable{
        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/posts/{id}",id)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(jsonPath("$.text", is(text)));
    }
}
