package cat.udl.eps.softarch.linkapp.steps;

import cat.udl.eps.softarch.linkapp.domain.Group;
import cat.udl.eps.softarch.linkapp.domain.User;
import cat.udl.eps.softarch.linkapp.repository.GroupRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.cucumber.core.gherkin.Step;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultMatcher;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class CreateGroupSteps {

    @Autowired
    private StepDefs stepDefs;
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @When("^I Create a public Group called \"([^\"]*)\" with description \"([^\"]*)\"")
    public void iCreateAPublicGroup(String groupName, String description) throws Exception {
        Group group = new Group(groupName, description, true);
        groupRepository.save(group);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(
                                stepDefs.mapper.writeValueAsString(group)
                                ).toString())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @Then("It has been created a Group with title \"([^\"]*)\" and description \"([^\"]*)\"")
    public void itHasBeenCreatedAGroup(String title, String description) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/group/{title}", title)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect((ResultMatcher) jsonPath("$.description", is(description)));
    }


}
