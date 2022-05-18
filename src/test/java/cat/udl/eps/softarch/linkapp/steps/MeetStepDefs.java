package cat.udl.eps.softarch.linkapp.steps;

import cat.udl.eps.softarch.linkapp.service.ScheduledCronJobsService;
import cat.udl.eps.softarch.linkapp.domain.*;
import cat.udl.eps.softarch.linkapp.repository.GroupRepository;
import cat.udl.eps.softarch.linkapp.repository.MeetRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRoleRepository;
import io.cucumber.java.en.Given;
import org.junit.Assert;
import org.springframework.dao.EmptyResultDataAccessException;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class MeetStepDefs
{

    private static Group featureGroup;

    private static Meet featureMeet;

    private static Meet cronMeet;

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
        group.setIdentifier(1L);
        group.setTitle("title");
        group.setDescription("description");
        group.setVisibility(GroupVisibilityEnum.PUBLIC);
        featureGroup = group;
        groupRepository.save(group);
        return group;
    }

    @And("The user {string} belongs to that group as {string}")
    public void userBelongsToGroup(String username, String role) throws Throwable {
        User user = userRepository.findById(username).get();

        UserRoleKey userRoleKey = new UserRoleKey();
        userRoleKey.setUser(user);
        userRoleKey.setGroup(featureGroup);

        UserRole userRole = new UserRole();
        userRole.setRoleKey(userRoleKey);
        userRole.setRole(UserRoleEnum.valueOf(role));
        userRoleRepository.save(userRole);

        stepDefs.result = stepDefs.mockMvc.perform(
                        get(userRole.getUri())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @When("The user {string} leaves the group")
    public void leaveGroup(String username) throws Throwable {
        User user = userRepository.findById(username).get();
        UserRole userRole = userRoleRepository.findByRoleKeyUserAndRoleKeyGroup(user, featureGroup);

        stepDefs.result = stepDefs.mockMvc
                .perform(
                        delete(userRole.getUri())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate())
                ).andDo(print());
    }

    @When("I create a meet in that group with title {string}, description {string}, maxUsers {long}, location {string}")
    public void iCreateAMeetWithTitleDescriptionMaxUsersLocation(String title, String description, Long maxUsers, String location) throws Throwable
    {
        Meet tmpMeet = new Meet();
        tmpMeet.setTitle(title);
        tmpMeet.setDescription(description);
        tmpMeet.setMaxUsers(maxUsers);
        tmpMeet.setLocation(location);
        tmpMeet.setInitialMeetDate(ZonedDateTime.now());
        tmpMeet.setFinalMeetDate(ZonedDateTime.now().plusHours(1));
        stepDefs.result = stepDefs.mockMvc
                .perform(
                        post("/meets/")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(new JSONObject(stepDefs.mapper.writeValueAsString(tmpMeet))
                                        .put("group", "/groups/" + featureGroup.getIdentifier())
                                        .toString()
                                )
                                .with(AuthenticationStepDefs.authenticate())
                ).andDo(print());
        MockHttpServletResponse response = stepDefs.result.andReturn().getResponse();
        if (response.getStatus() == 201) {
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
    public void updateTheUserRoleTo(String username, String role) throws Throwable
    {
        User user = userRepository.findById(username).get();
        UserRole userRole = userRoleRepository.findByRoleKeyUserAndRoleKeyGroup(user, featureGroup);
        stepDefs.result = stepDefs.mockMvc.perform(
                        patch(userRole.getUri())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new JSONObject().put("role", role).toString())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @And("The role of the user {string} has been changed to {string}")
    public void userRoleUpdatedTo(String username, String role) throws Throwable
    {
        User user = userRepository.findById(username).get();
        UserRole userRole = userRoleRepository.findByRoleKeyUserAndRoleKeyGroup(user, featureGroup);

        stepDefs.result = stepDefs.mockMvc.perform(
                        get(userRole.getUri())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(jsonPath("$.role", is(role)));
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
        tmpMeet.setInitialMeetDate(ZonedDateTime.now());
        tmpMeet.setFinalMeetDate(ZonedDateTime.now().plusHours(1));
        stepDefs.result = stepDefs.mockMvc
                .perform(
                        put(featureMeet.getUri())
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        new JSONObject(
                                                stepDefs.mapper.writeValueAsString(tmpMeet)
                                        )
                                                .put("group", featureGroup.getUri())
                                                .toString()
                                )
                                .with(AuthenticationStepDefs.authenticate())
                ).andDo(print());
        MockHttpServletResponse response = stepDefs.result.andReturn().getResponse();
        if (response.getStatus() == 200) {
            String content = response.getContentAsString();
            Integer id = JsonPath.read(content, "id");
            featureMeet = meetRepository.findById(id.longValue()).get();
        }
    }

    @When("I patch the meet with title {string}, description {string}, maxUsers {long}, location {string}")
    public void iPatchTheMeetWithTitleDescriptionMaxUsersLocation(String title, String description, Long maxUsers, String location) throws Throwable
    {
        Meet tmpMeet = new Meet();
        tmpMeet.setIdentifier(featureMeet.getId());
        tmpMeet.setTitle(title);
        tmpMeet.setDescription(description);
        tmpMeet.setMaxUsers(maxUsers);
        tmpMeet.setLocation(location);
        tmpMeet.setInitialMeetDate(ZonedDateTime.now());
        tmpMeet.setFinalMeetDate(ZonedDateTime.now().plusHours(1));
        stepDefs.result = stepDefs.mockMvc
                .perform(
                        patch("/meets/" + featureMeet.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .content(new JSONObject(
                                                stepDefs.mapper.writeValueAsString(tmpMeet)
                                        )
                                                .put("group", featureGroup.getUri())
                                                .toString()
                                )
                                .with(AuthenticationStepDefs.authenticate())
                ).andDo(print());
        MockHttpServletResponse response = stepDefs.result.andReturn().getResponse();
        if (response.getStatus() == 200) {
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

    @And("The user {string} does not belong to the group")
    public void theUserDoesNotBelongToTheGroup(String username)
    {
        User user = userRepository.findById(username).get();

        UserRoleKey userRoleKey = new UserRoleKey();
        userRoleKey.setUser(user);
        userRoleKey.setGroup(featureGroup);

        try {
            userRoleRepository.deleteById(userRoleKey);
        } catch (EmptyResultDataAccessException e) {
            // do nothing
        }
    }

    @Given("I create a meet that ends in the past in that group")
    public void iCreateAMeetThatEndsInThePast()
    {
        cronMeet = new Meet();
        cronMeet.setGroup(featureGroup);
        cronMeet.setTitle("title");
        cronMeet.setDescription("description");
        cronMeet.setMaxUsers(10L);
        cronMeet.setLocation("location");
        cronMeet.setCreationDate(ZonedDateTime.now());
        cronMeet.setLastUpdate(ZonedDateTime.now());
        cronMeet.setInitialMeetDate(ZonedDateTime.now().minusDays(1));
        cronMeet.setFinalMeetDate(ZonedDateTime.now().minusDays(1).plusHours(1));
        meetRepository.save(cronMeet);
    }

    @When("The cron status job is executed")
    public void theCronStatusJobIsExecuted()
    {
        ScheduledCronJobsService.updateMeetStatusJob(meetRepository);
    }

    @Then("Then the meet status is false")
    public void thenTheMeetStatusIsFalse()
    {
        assert cronMeet.getId() != null;
        Meet meet = meetRepository.findById(cronMeet.getId()).get();
        Assert.assertFalse(meet.getStatus());
    }

    @When("I create a meet in that group with initial date in the past")
    public void iCreateAMeetInThatGroupWithInitialDateInThePast() throws Throwable
    {
        Meet tmpMeet = new Meet();
        tmpMeet.setGroup(featureGroup);
        tmpMeet.setTitle("title");
        tmpMeet.setDescription("description");
        tmpMeet.setMaxUsers(10L);
        tmpMeet.setLocation("location");
        tmpMeet.setCreationDate(ZonedDateTime.now());
        tmpMeet.setLastUpdate(ZonedDateTime.now());
        tmpMeet.setInitialMeetDate(ZonedDateTime.now().minusDays(1));
        tmpMeet.setFinalMeetDate(ZonedDateTime.now().minusDays(1).plusHours(1));
        stepDefs.result = stepDefs.mockMvc
                .perform(
                        post("/meets/")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(new JSONObject(
                                                stepDefs.mapper.writeValueAsString(tmpMeet)
                                        )
                                                .put("group", featureGroup.getUri())
                                                .toString()
                                )
                                .with(AuthenticationStepDefs.authenticate())
                ).andDo(print());
    }

    @When("I create a meet in that group with final date before initial date")
    public void iCreateAMeetInThatGroupWithFinalDateBeforeInitialDate() throws Throwable
    {
        Meet tmpMeet = new Meet();
        tmpMeet.setGroup(featureGroup);
        tmpMeet.setTitle("title");
        tmpMeet.setDescription("description");
        tmpMeet.setMaxUsers(10L);
        tmpMeet.setLocation("location");
        tmpMeet.setCreationDate(ZonedDateTime.now());
        tmpMeet.setLastUpdate(ZonedDateTime.now());
        tmpMeet.setInitialMeetDate(ZonedDateTime.now().plusDays(1));
        tmpMeet.setFinalMeetDate(ZonedDateTime.now().plusDays(1).minusHours(1));
        stepDefs.result = stepDefs.mockMvc
                .perform(
                        post("/meets/")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(new JSONObject(
                                                stepDefs.mapper.writeValueAsString(tmpMeet)
                                        )
                                                .put("group", "/groups/" + featureGroup.getIdentifier())
                                                .toString()
                                )
                                .with(AuthenticationStepDefs.authenticate())
                ).andDo(print());
    }

    @When("The user {string} tries to attend the meeting")
    public void theUserTriesToAttendTheMeeting(String username) throws Throwable
    {
        User user = userRepository.findById(username).get();
        MeetAttendingKey meetAttendingKey = new MeetAttendingKey(featureMeet, user);
        MeetAttending meetAttending = new MeetAttending();
        meetAttending.setMeetAttendingKey(meetAttendingKey);
        meetAttending.setAttends(true);


        JSONObject object = new JSONObject(
                stepDefs.mapper.writeValueAsString(meetAttending)
        );

        stepDefs.result = stepDefs.mockMvc.perform(
                        post("/meetAttendings/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(object.toString())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }


}
