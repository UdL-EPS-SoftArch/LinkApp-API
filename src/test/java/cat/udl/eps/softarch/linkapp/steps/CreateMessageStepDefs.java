package cat.udl.eps.softarch.linkapp.steps;

import cat.udl.eps.softarch.linkapp.domain.*;
import cat.udl.eps.softarch.linkapp.repository.*;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import java.time.ZonedDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class CreateMessageStepDefs {

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

    CreateMessageStepDefs(StepDefs stepDefs) {
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
        meet.setMeetDate(ZonedDateTime.now().plusDays(1));
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

    @When("I delete the message")
    public void iDeleteTheMessage() throws Throwable {
        stepDefs.result = stepDefs.mockMvc
                .perform(
                        delete("/messages/" + featureMessage.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate())
                ).andDo(print());
    }
}

