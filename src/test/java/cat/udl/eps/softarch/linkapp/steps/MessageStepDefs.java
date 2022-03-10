package cat.udl.eps.softarch.linkapp.steps;

import cat.udl.eps.softarch.linkapp.domain.*;
import cat.udl.eps.softarch.linkapp.repository.*;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import java.time.ZonedDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


public class MessageStepDefs {

    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private MeetRepository meetRepository;

    @Autowired
    private MeetStepDefs meetStepDefs;

    private static String username;
    private static Group featureGroup;
    private static Meet featureMeet;
    private static Message featureMessage;

    MessageStepDefs(StepDefs stepDefs) {
        this.stepDefs = stepDefs;
    }

    @And("A group exists with a meet")
    public void theMeetWithIdExists() {
        featureGroup = meetStepDefs.theGroupExists();
        Meet meet = new Meet();
        meet.setTitle("title");
        meet.setGroup(featureGroup);
        meet.setDescription("description");
        meet.setStatus(true);
        meet.setLastUpdate(ZonedDateTime.now());
        meet.setCreationDate(ZonedDateTime.now());
        meet.setInitialMeetDate(ZonedDateTime.now());
        meet.setFinalMeetDate(ZonedDateTime.now().plusDays(1));
        featureMeet = meet;
        meetRepository.save(meet);
    }

    @And("The user {string} is assisting to the meet")
    public void userIsAssistingToMeet(String name) {
        username = name;
        User user = userRepository.findById(username).get();
        Meet meet = featureMeet;
        Group group = featureGroup;

        UserRole userRole = userRoleRepository.
                findByRoleKeyUserAndRoleKeyGroup(user, group);

        meet.getAttending().add(userRole);
        meetRepository.save(meet);
    }

    @And("The user {string} is not assisting to the meet")
    public void userIsNotAssistingToMeet(String name) {
        username = name;
        User user = userRepository.findById(username).get();
        Meet meet = featureMeet;
        Group group = featureGroup;

        UserRole userRole = userRoleRepository.
                findByRoleKeyUserAndRoleKeyGroup(user, group);

        assertFalse(meet.getAttending().contains(userRole));
    }

    @When("I send a message to the meet with message {string}")
    public void sendMessageToMeet(String message) throws Throwable {
        Message tmpMessage = new Message();
        tmpMessage.setText(message);
        tmpMessage.setGroup(featureGroup);
        tmpMessage.setMeet(featureMeet);
        stepDefs.result = stepDefs.mockMvc
                .perform(
                        post("/messages/")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(new JSONObject(stepDefs.mapper.writeValueAsString(tmpMessage))
                                        .put("group", "/groups/" + featureGroup.getId())
                                        .put("meet", "/meets/" + featureMeet.getId())
                                        .toString()
                                )
                                .with(AuthenticationStepDefs.authenticate())
                ).andDo(print());
        MockHttpServletResponse response = stepDefs.result.andReturn().getResponse();
        if (response.getStatus() == 201) {
            String content = stepDefs.result.andReturn().getResponse().getContentAsString();
            String uri = JsonPath.read(content, "uri");
            featureMessage = messageRepository.findById(Long.parseLong(uri.substring(uri.length() - 1))).get();
        }
    }

    @Then("It has been created a message with message {string}")
    public void itHasBeenCreatedAMessageWithMessage(String message) throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/messages/{id}", featureMessage.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(jsonPath("$.text", is(message)));
    }

    @When("I edit the message with message {string}")
    public void editMessageToMeet(String editedMessage) throws Throwable {
        featureMessage.setText(editedMessage);
        stepDefs.result = stepDefs.mockMvc
                .perform(
                        put("/messages/" + featureMessage.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate())
                ).andDo(print());
    }

    @When("I delete the message")
    public void iDeleteTheMessage() throws Throwable {
        stepDefs.result = stepDefs.mockMvc
                .perform(
                        delete("/messages/" + featureMessage.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate())
                ).andDo(print());
    }

    @And("The creation time of the message is recent")
    public void theCreationTimeOfTheMessageIsRecent() {
        ZonedDateTime date = featureMessage.getCreationDate();

        assertThat("Date is in the past", date.isBefore(ZonedDateTime.now()));
        ZonedDateTime pre = ZonedDateTime.now().minusSeconds(30);

        assertThat("Date was created in the last 30 seconds", date.isBefore(ZonedDateTime.now()));
    }

    @And("The meet has closed")
    public void theMeetHasClosed() {
        featureMeet.setStatus(Boolean.FALSE);
    }
}

