package cat.udl.eps.softarch.linkapp.steps;

import cat.udl.eps.softarch.linkapp.domain.*;
import cat.udl.eps.softarch.linkapp.repository.GroupRepository;
import cat.udl.eps.softarch.linkapp.repository.MeetRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRoleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import java.time.ZonedDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class MeetStepDefs
{

    private static Group featureGroup;

    private static Meet featureMeet;

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

    private final Pattern idPattern = Pattern.compile("\\d+$");

    @And("A group exists")
    public Group theGroupExists()
    {
        Group group = new Group();
        group.setTitle("title");
        group.setDescription("description");
        group.setVisibility(GroupVisibilityEnum.PUBLIC);
        featureGroup = group;
        groupRepository.save(group);
        return group;
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
        if (response.getStatus() == 201)
        {
            String content = response.getContentAsString();
            String uri = JsonPath.read(content, "uri");
            Matcher m = idPattern.matcher(uri);
            if (!m.find())
                throw new RuntimeException("Unexpected uri");

            featureMeet = meetRepository.findById(Long.parseLong(m.group())).get();
        }
    }

    @Then("It has been created a meet with title {string}, description {string}, maxUsers {long}, location {string}, status {string}")
    public void itHasBeenCreatedAMeetWithIdTitleDescriptionMaxUsersLocation(String title, String description, Long maxUsers, String location, String meetStatus) throws Throwable
    {
        Boolean status = meetStatus.equals("true");
        stepDefs.result = stepDefs.mockMvc.perform(
                        get(featureMeet.getUri())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(jsonPath("$.title", is(title)))
                .andExpect(jsonPath("$.description", is(description)))
                .andExpect(jsonPath("$.maxUsers", is(maxUsers.intValue())))
                .andExpect(jsonPath("$.location", is(location)))
                .andExpect(jsonPath("$.status", is(status)));
    }

    @When("I delete the meet")
    public void iDeleteTheMeet() throws Throwable
    {
        stepDefs.result = stepDefs.mockMvc
                .perform(
                        delete(featureMeet.getUri())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate())
                ).andDo(print());
    }

    @And("I update the user {string} role of the group to {string}")
    public void updateTheUserRoleTo(String username, String role)
    {
        User user = userRepository.findById(username).get();
        UserRole userRole = userRoleRepository.findByRoleKeyUserAndRoleKeyGroup(user, featureGroup);
        userRole.setRole(UserRoleEnum.valueOf(role));
        userRoleRepository.save(userRole);
    }

    @And("The creation time of the meet is recent")
    public void theCreationTimeOfTheMeetIsRecent()
    {
        ZonedDateTime date = featureMeet.getCreationDate();

        assertThat("Date is in the past", date.isBefore(ZonedDateTime.now()));
        ZonedDateTime pre = ZonedDateTime.now().minusMinutes(5);

        assertThat("Date was created in the last 5 min", date.isBefore(ZonedDateTime.now()));
    }

    @When("I edit the meet with title {string}, description {string}, maxUsers {long}, location {string}")
    public void iEditTheMeetWithTitleDescriptionMaxUsersLocation(String title, String description, Long maxUsers, String location) throws Throwable
    {
        Meet tmpMeet = new Meet();
        tmpMeet.setTitle(title);
        tmpMeet.setDescription(description);
        tmpMeet.setMaxUsers(maxUsers);
        tmpMeet.setLocation(location);
        tmpMeet.setMeetDate(ZonedDateTime.now());
        stepDefs.result = stepDefs.mockMvc
                .perform(
                        put(featureMeet.getUri())
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        new JSONObject(
                                            stepDefs.mapper.writeValueAsString(tmpMeet)
                                        )
                                        .put("group", "/groups/" + featureGroup.getId())
                                        .toString()
                                )
                                .with(AuthenticationStepDefs.authenticate())
                ).andDo(print());
        MockHttpServletResponse response = stepDefs.result.andReturn().getResponse();
        if (response.getStatus() == 200)
        {
            String content = response.getContentAsString();
            String uri = JsonPath.read(content, "uri");
            featureMeet = meetRepository.findById(Long.parseLong(uri.substring(uri.length() - 1))).get();
        }
    }

    @When("I patch the meet with title {string}, description {string}, maxUsers {long}, location {string}")
    public void iPatchTheMeetWithTitleDescriptionMaxUsersLocation(String title, String description, Long maxUsers, String location) throws Throwable
    {
        Meet tmpMeet = new Meet();
        tmpMeet.setId(featureMeet.getId());
        tmpMeet.setTitle(title);
        tmpMeet.setDescription(description);
        tmpMeet.setMaxUsers(maxUsers);
        tmpMeet.setLocation(location);
        tmpMeet.setMeetDate(ZonedDateTime.now());
        stepDefs.result = stepDefs.mockMvc
                .perform(
                        patch("/meets/" + featureMeet.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .content(new JSONObject(
                                            stepDefs.mapper.writeValueAsString(tmpMeet)
                                        )
                                        .put("group", "/groups/" + featureGroup.getId())
                                        .toString()
                                )
                                .with(AuthenticationStepDefs.authenticate())
                ).andDo(print());
        MockHttpServletResponse response = stepDefs.result.andReturn().getResponse();
        if (response.getStatus() == 200)
        {
            String content = response.getContentAsString();
            String uri = JsonPath.read(content, "uri");
            Matcher m = idPattern.matcher(uri);
            if (!m.find())
                throw new RuntimeException("Unexpected uri");

            featureMeet = meetRepository.findById(Long.parseLong(m.group())).get();
        }
    }

    @And("The edition time of the meet is recent")
    public void theEditionTimeOfTheMeetIsRecent()
    {
        ZonedDateTime date = featureMeet.getLastUpdate();

        assertThat("Date is in the past", date.isBefore(ZonedDateTime.now()));
        ZonedDateTime pre = ZonedDateTime.now().minusMinutes(5);

        assertThat("Date was edited in the last 5 min", date.isBefore(ZonedDateTime.now()));
    }
}
