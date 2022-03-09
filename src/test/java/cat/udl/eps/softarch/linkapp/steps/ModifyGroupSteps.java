package cat.udl.eps.softarch.linkapp.steps;

import cat.udl.eps.softarch.linkapp.domain.*;
import cat.udl.eps.softarch.linkapp.repository.GroupRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRoleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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


    @When("The allowed user {string} modifies the group description to {string}")
    public void userModifiesGroup(String username, String newDescription) throws Exception {
        User user = userRepository.findById(username).get();
        Group tmpGroup = groupRepository.findById((long) 1).get();
        UserRole userRole = userRoleRepository.findByRoleKeyUserAndRoleKeyGroup(user, tmpGroup);

        tmpGroup.setDescription(newDescription);
        JSONObject newJsonDescription = new JSONObject();
        newJsonDescription.put("description", newDescription);
        stepDefs.result = stepDefs.mockMvc.perform(
                patch("/groups/{id}", tmpGroup.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newJsonDescription.toString())
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }
    @When("A NOT allowed user modifies the group description to {string}")
    public void userNotAllowedModifiesGroup(String newDescription) throws Exception {
        User user = userRepository.findById("demo").get();
        Group tmpGroup = groupRepository.findById((long) 1).get();
        UserRole userRole = new UserRole();
        UserRoleKey userRoleKey = new UserRoleKey();
        userRoleKey.setGroup(tmpGroup);
        userRoleKey.setUser(user);
        userRole.setRoleKey(userRoleKey);
        userRole.setRole(UserRoleEnum.SUBSCRIBED);

        try {
            tmpGroup.setDescription(newDescription, userRole);

            JSONObject newJsonDescription = new JSONObject();
            newJsonDescription.put("description", newDescription);
            stepDefs.result = stepDefs.mockMvc.perform(
                            patch("/groups/{id}", tmpGroup.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(newJsonDescription.toString())
                                    .with(AuthenticationStepDefs.authenticate()))
                    .andDo(print());
        } catch (AccessDeniedException e){

        }
    }

    @And("A already created group where with name {string}, id {long} and description {string}")
    public void groupCreated(String name, long id, String description) throws Exception {
        group = new Group(id, name, description, GroupVisibilityEnum.PUBLIC);
        groupRepository.save(group);

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

    @And("The user {string} is an Admin {string} of the group")
    public void userAdminOfThatGroup(String username, String role){
        User user = userRepository.findById(username).get();

        UserRoleKey userRoleKey = new UserRoleKey();
        userRoleKey.setUser(user);
        userRoleKey.setGroup(group);

        UserRole userRole = new UserRole();
        userRole.setRoleKey(userRoleKey);
        userRole.setRole(UserRoleEnum.valueOf(role));

        userRoleRepository.save(userRole);
    }

    @And ("The description of the group is now {string}")
    public void itHasBeenModifiedAGroup(String description) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/groups/{id}", group.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(jsonPath("$.description", is(description)));
    }

    @Then("Nothing happens")
    public void nothingHappens(){
        assert stepDefs.result == null;

    }




}
