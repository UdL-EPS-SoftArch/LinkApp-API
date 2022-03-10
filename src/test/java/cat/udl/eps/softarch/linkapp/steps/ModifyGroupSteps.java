package cat.udl.eps.softarch.linkapp.steps;

import cat.udl.eps.softarch.linkapp.domain.*;
import cat.udl.eps.softarch.linkapp.repository.GroupRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRoleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class ModifyGroupSteps {

    @Autowired
    private StepDefs stepDefs;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    @When("The user {string} modifies the group description to {string}")
    public void userModifiesGroup(String username, String newDescription) throws Exception {

        JSONObject newJsonDescription = new JSONObject();
        newJsonDescription.put("description", newDescription);
        stepDefs.result = stepDefs.mockMvc.perform(
                patch("/groups/{id}", CreateGroupSteps.getCreatedGroup().getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newJsonDescription.toString())
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @And ("The description of the group is now {string}")
    public void itHasBeenModifiedAGroup(String description) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/groups/{id}", CreateGroupSteps.getCreatedGroup().getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(jsonPath("$.description", is(description)));
    }




}
