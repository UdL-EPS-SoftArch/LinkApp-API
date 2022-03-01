package cat.udl.eps.softarch.linkapp.steps;

import cat.udl.eps.softarch.linkapp.domain.Group;
import cat.udl.eps.softarch.linkapp.domain.GroupVisibilityEnum;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
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

    private static String username;
    private static String password;

    @Given("^I am authenticated as \"([^\"]*)\"")
    public void authenticatedAs(String username) {
        if (userRepository.existsById(username)) userRepository.deleteById(username);
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setEmail("user@linkup.com");
        user.encodePassword();
        userRepository.save(user);
        CreateGroupSteps.username = username;
        CreateGroupSteps.password = "password";
    }

    @When("^I Create a public Group called \"([^\"]*)\" with id \"([^\"]*)\" with description \"([^\"]*)\"")
    public void iCreateAPublicGroup(String groupName, long id, String description) throws Exception {
        Group group = new Group(id, groupName, description, GroupVisibilityEnum.PUBLIC);
        //groupRepository.save(group);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/groups/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(
                                stepDefs.mapper.writeValueAsString(group)
                                ).toString())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @Then("It has been created a Group with title \"([^\"]*)\" with id {long} and description \"([^\"]*)\"")
    public void itHasBeenCreatedAGroup(String title, long id, String description) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/groups/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect((ResultMatcher) jsonPath("$.title", is(title)))
                .andExpect((ResultMatcher) jsonPath("$.description", is(description)));
    }


}
