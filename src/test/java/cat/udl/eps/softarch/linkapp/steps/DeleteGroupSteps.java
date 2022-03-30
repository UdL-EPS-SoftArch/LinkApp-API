package cat.udl.eps.softarch.linkapp.steps;

import cat.udl.eps.softarch.linkapp.domain.*;
import cat.udl.eps.softarch.linkapp.repository.GroupRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRoleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeleteGroupSteps {
    @Autowired
    private StepDefs stepDefs;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    private static Group group;

    @When("The user {string} deletes the group {long}")
    public void userDeletesGroup(String username, long id) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                delete("/groups/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate())
                )
                .andDo(print());
    }

    @When("The user deletes the group")
    public void userDeletesGroup2() throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
            delete("/groups/{id}", CreateGroupSteps.getCreatedGroup().getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate())
                )
                .andDo(print());

    }

    @And("A post is created in that group")
    public void postCreates() throws Exception {
        Group group = groupRepository.findById((long) 1).get();
        Post post = new Post();
        post.setId((long) 1);
        stepDefs.result = stepDefs.mockMvc.perform(
                post("/posts/")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(stepDefs.mapper.writeValueAsString(post))
                                .put("group", "/groups/" + group.getId())
                                .toString()
                        )
                        .with(AuthenticationStepDefs.authenticate())
                        .with(AuthenticationStepDefs.authenticate())).andDo(print());
    }

    @Then("I check if the post has been deleted")
    public void getPost() throws Exception{

        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/posts/{id}",(long) 1)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }
}
