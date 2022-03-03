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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class ModifyGroupSteps {

    @Autowired
    private StepDefs stepDefs;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    private static Group group;

    @And("A already created group where with name {string}, id {long} and description {string}")
    public void groupCreated(String name, long id, String description){
        group = new Group(id, name, description, GroupVisibilityEnum.PUBLIC);
        groupRepository.save(group);
    }

    @And("The user {string} is an Admin {string} of the group")
    public void userAdminOfThatGroup(String username, String role){
        User user = userRepository.findById(username).get();

        UserRoleKey userRoleKey = new UserRoleKey();
        userRoleKey.setUser(user);
        userRoleKey.setGroup(group);

        UserRole userRole = new UserRole();
        userRole.setRoleKey(userRoleKey);
        userRole.setRole(UserRoleEnum.valueOf(role));

        group.addMember(userRole);
        userRoleRepository.save(userRole);
    }
    @When("The user {string} modifies the group {long} description to {string}")
    public void userModifiesGroup(String username, long id, String newDescription) throws Exception {
        User user = userRepository.findById(username).get();
        UserRole member = userRoleRepository.findByRoleKeyUserAndRoleKeyGroup(user, group);
        group.setDescription(member, newDescription);

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




}
