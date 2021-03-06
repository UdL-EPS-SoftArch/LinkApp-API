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
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


public class MessageStepDefs
{

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
    private MeetAttendingRepository meetAttendingRepository;

    @Autowired
    private MeetStepDefs meetStepDefs;

    private static String username;
    private static Group featureGroup;
    private static Meet featureMeet;
    private static Message featureMessage;

    MessageStepDefs(StepDefs stepDefs)
    {
        this.stepDefs = stepDefs;
    }

    @And("A group exists with a meet")
    public void theMeetWithIdExists()
    {
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
    public void userIsAssistingToMeet(String name)
    {
        username = name;
        User user = userRepository.findById(username).get();
        Meet meet = featureMeet;
        Group group = featureGroup;

        meetRepository.save(meet);

        MeetAttendingKey meetAttendingKey = new MeetAttendingKey();
        meetAttendingKey.setUser(user);
        meetAttendingKey.setMeet(meet);
        MeetAttending meetAttending = new MeetAttending();
        meetAttending.setMeetAttendingKey(meetAttendingKey);
        meetAttending.setAttends(true);

        meetAttendingRepository.save(meetAttending);
    }

    @And("The user {string} is not assisting to the meet")
    public void userIsNotAssistingToMeet(String name)
    {
        username = name;
        User user = userRepository.findById(username).get();
        Meet meet = featureMeet;
        Group group = featureGroup;

        Optional<MeetAttending> meetAttending = meetAttendingRepository
                .findByMeetAttendingKeyUserAndMeetAttendingKeyMeet(user, meet);
        assertFalse(meetAttending.isPresent());
    }

    @When("I send a message to the meet with message {string}")
    public void sendMessageToMeet(String message) throws Throwable
    {
        Message tmpMessage = new Message();
        tmpMessage.setText(message);
        tmpMessage.setGroup(featureGroup);
        tmpMessage.setMeet(featureMeet);
        stepDefs.result = stepDefs.mockMvc
                .perform(
                        post("/messages/")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(new JSONObject(stepDefs.mapper.writeValueAsString(tmpMessage))
                                        .put("group", "/groups/" + featureGroup.getIdentifier())
                                        .put("meet", "/meets/" + featureMeet.getId())
                                        .toString()
                                )
                                .with(AuthenticationStepDefs.authenticate())
                ).andDo(print());
        MockHttpServletResponse response = stepDefs.result.andReturn().getResponse();
        if (response.getStatus() == 201) {
            String content = stepDefs.result.andReturn().getResponse().getContentAsString();
            Integer id = JsonPath.read(content, "id");
            featureMessage = messageRepository.findById(id.longValue()).get();
        }
    }

    @Then("It has been created a message with message {string}")
    public void itHasBeenCreatedAMessageWithMessage(String message) throws Throwable
    {
        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/messages/{id}", featureMessage.getIdentifier())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(jsonPath("$.text", is(message)));
    }

    @When("I edit the message with message {string}")
    public void editMessageToMeet(String editedMessage) throws Throwable
    {
        featureMessage.setText(editedMessage);
        stepDefs.result = stepDefs.mockMvc
                .perform(
                        put("/messages/" + featureMessage.getIdentifier())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate())
                ).andDo(print());
    }

    @When("I delete the message")
    public void iDeleteTheMessage() throws Throwable
    {
        stepDefs.result = stepDefs.mockMvc
                .perform(
                        delete("/messages/" + featureMessage.getIdentifier())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate())
                ).andDo(print());
    }

    @And("The creation time of the message is recent")
    public void theCreationTimeOfTheMessageIsRecent()
    {
        ZonedDateTime date = featureMessage.getCreationDate();

        assertThat("Date is in the past", date.isBefore(ZonedDateTime.now()));
        ZonedDateTime pre = ZonedDateTime.now().minusSeconds(30);

        assertThat("Date was created in the last 30 seconds", date.isBefore(ZonedDateTime.now()));
    }

    @And("The meet has closed")
    public void theMeetHasClosed()
    {
        assert featureMeet.getId() != null;
        Meet meet = meetRepository.findById(featureMeet.getId()).get();
        meet.setStatus(Boolean.FALSE);
        meetRepository.save(meet);
        featureMeet = meet;
    }

    @And("The author of the message is correct")
    public void theAuthorIsCorrect()
    {
        User author = userRepository.findById(username).get();
        System.out.println(messageRepository.findByAuthor(author).size());
        assertEquals(messageRepository.findByAuthor(author).get(0).getIdentifier(), featureMessage.getIdentifier());
    }
}

