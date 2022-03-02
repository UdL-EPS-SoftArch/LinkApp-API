package cat.udl.eps.softarch.linkapp.steps;

import cat.udl.eps.softarch.linkapp.domain.*;
import cat.udl.eps.softarch.linkapp.repository.GroupRepository;
import cat.udl.eps.softarch.linkapp.repository.MeetRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRoleRepository;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class CreateMeetStepDefs
{

    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private MeetRepository meetRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    private static Group featureGroup;
    private static Meet featureMeet;


    @And("A group exists")
    public void theGroupWithIdExists() {
        Group group = new Group();
        group.setTitle("title");
        group.setDescription("description");
        group.setVisibility(true);
        featureGroup = group;
        groupRepository.save(group);
    }

    @And("The user {string} belongs to that group as {string}")
    public void userBelongsToGroup(String username, String role)
    {
        User user = userRepository.findById(username).get();

        UserRoleKey userRoleKey = new UserRoleKey();
        userRoleKey.setUser(user);
        userRoleKey.setGroup(featureGroup);

        UserRole userRole = new UserRole();
        userRole.setRoleKey(userRoleKey);
        userRole.setRole(UserRoleEnum.valueOf(role));
        userRoleRepository.save(userRole);
    }

    @When("I create a meet in that group with title {string}, description {string}, maxUsers {long}, location {string}")
    public void iCreateAMeetWithTitleDescriptionMaxUsersLocation(String title, String description, Long maxUsers, String location) throws Throwable
    {
        Meet tmpMeet = new Meet();
        tmpMeet.setTitle(title);
        tmpMeet.setDescription(description);
        tmpMeet.setMaxUsers(maxUsers);
        tmpMeet.setLocation(location);
        tmpMeet.setMeetDate(ZonedDateTime.now());
        stepDefs.result = stepDefs.mockMvc
                .perform(
                        post("/meets/")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(new JSONObject(stepDefs.mapper.writeValueAsString(tmpMeet))
                                        .put("group", "/groups/" + featureGroup.getId())
                                        .toString()
                                )
                                .with(AuthenticationStepDefs.authenticate())
                ).andDo(print());
        MockHttpServletResponse response = stepDefs.result.andReturn().getResponse();
        if (response.getStatus() == 201){
            String content = stepDefs.result.andReturn().getResponse().getContentAsString();
            String uri = JsonPath.read(content, "uri");
            featureMeet = meetRepository.findById(Long.parseLong(uri.substring(uri.length() - 1))).get();
        }
    }

    @Then("It has been created a meet with title {string}, description {string}, maxUsers {long}, location {string}, status {string}")
    public void itHasBeenCreatedAMeetWithIdTitleDescriptionMaxUsersLocation(String title, String description, Long maxUsers, String location, String meetStatus) throws Throwable {
        Boolean status = meetStatus.equals("true");
        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/meets/{id}", featureMeet.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(jsonPath("$.title", is(title)))
                .andExpect(jsonPath("$.description", is(description)))
                .andExpect(jsonPath("$.maxUsers", is(maxUsers.intValue())))
                .andExpect(jsonPath("$.location", is(location)))
                .andExpect(jsonPath("$.status", is(status)));
    }

    @And("I delete the meet")
    public void iDeleteTheMeet() throws Throwable
    {
        stepDefs.result = stepDefs.mockMvc
                .perform(
                        delete("/meets/" + featureMeet.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate())
                ).andDo(print());
    }
}
