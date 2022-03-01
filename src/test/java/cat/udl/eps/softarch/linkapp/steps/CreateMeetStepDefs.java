package cat.udl.eps.softarch.linkapp.steps;

import cat.udl.eps.softarch.linkapp.domain.*;
import cat.udl.eps.softarch.linkapp.repository.GroupRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRoleRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.ZonedDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreateMeetStepDefs {

    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

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
        CreateMeetStepDefs.username = username;
        CreateMeetStepDefs.password = "password";
    }

    @And("The group with id {long} exists")
    public void theGroupWithIdExists(Long groupId) {
        if (groupRepository.existsById(groupId)) groupRepository.deleteById(groupId);
        Group group = new Group();
        group.setTitle("title");
        group.setDescription("description");
        group.setVisibility(true);
        group.setId(groupId);
        groupRepository.save(group);
    }

    @And("The user {string} belongs to the group {long} as {string}")
    public void userBelongsToGroup(String username, Long groupId, String role) {
        User user = userRepository.findById(username).get();
        Group group = groupRepository.findById(groupId).get();

        UserRoleKey userRoleKey = new UserRoleKey();
        userRoleKey.setUser(user);
        userRoleKey.setGroup(group);

        UserRole userRole = new UserRole();
        userRole.setRoleKey(userRoleKey);
        userRole.setRole(UserRoleEnum.valueOf(role));
        userRoleRepository.save(userRole);

    }

    @When("I create a meet in the group with id {long} with title {string}, description {string}, maxUsers {int}, location {string}")
    public void iCreateAMeetWithTitleDescriptionMaxUsersLocation(Long groupId, String title, String description, int maxUsers, String location) throws Throwable {
        stepDefs.result = stepDefs.mockMvc
                .perform(
                        post("/meets/")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        new JSONObject()
                                                .put("group", "/groups/"+groupId)
                                                .put("title", title)
                                                .put("description", description)
                                                .put("maxUsers", maxUsers)
                                                .put("location", location)
                                                .put("meetDate", ZonedDateTime.now().toString())
                                                .toString()
                                )
                                .with(httpBasic(username, password))
                ).andDo(print());
    }
}
