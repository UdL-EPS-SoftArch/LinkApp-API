package cat.udl.eps.softarch.linkapp.steps;

import cat.udl.eps.softarch.linkapp.domain.*;
import cat.udl.eps.softarch.linkapp.repository.GroupRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRoleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.jupiter.api.Assertions.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

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


    @When("A user {string} modifies the group description to {string}")
    public void userModifiesGroup(String username, String newDescription) throws Exception {
        //User user = userRepository.findById(username).get();
        Group tmpGroup = groupRepository.findById((long) 1).get();
        /*UserRole userRole = userRoleRepository.findByRoleKeyUserAndRoleKeyGroup(user, tmpGroup);

        tmpGroup.setDescription(newDescription);*/
        JSONObject newJsonDescription = new JSONObject();
        newJsonDescription.put("description", newDescription);
        stepDefs.result = stepDefs.mockMvc.perform(
                patch("/groups/{id}", tmpGroup.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newJsonDescription.toString())
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @When("A user {string} adds the group theme {string}")
    public void userAddsTheme(String username, String newTheme) throws Exception {
        //User user = userRepository.findById(username).get();
        Group tmpGroup = groupRepository.findById((long) 1).get();
        /*UserRole userRole = userRoleRepository.findByRoleKeyUserAndRoleKeyGroup(user, tmpGroup);

        tmpGroup.setDescription(newDescription);*/

        List<ThemeEnum> themes = tmpGroup.getThemes();
        themes.add(ThemeEnum.valueOf(newTheme));
        group = new Group(tmpGroup.getId(), tmpGroup.getTitle(), tmpGroup.getDescription(), GroupVisibilityEnum.PUBLIC, themes);
        groupRepository.save(group);

        /*JSONObject newJsonThemes = new JSONObject();
        List<ThemeEnum> newThemes = tmpGroup.getThemes();
        newThemes.add(ThemeEnum.valueOf(newTheme));
        newJsonThemes.put("themes", newThemes);*/
        stepDefs.result = stepDefs.mockMvc.perform(
                        patch("/groups/{id}", tmpGroup.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new JSONObject(
                                        stepDefs.mapper.writeValueAsString(group)
                                ).toString())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
        /*stepDefs.result = stepDefs.mockMvc.perform(
                        patch("/groups/{id}", tmpGroup.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(newJsonThemes.toString())
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());*/
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

    @And("A already created group where with name {string}, id {long} and description {string} and theme {string}")
    public void groupCreated(String name, long id, String description, String theme) throws Exception {
        List<ThemeEnum> themes = new ArrayList<>();
        themes.add(ThemeEnum.valueOf(theme));
        group = new Group(id, name, description, GroupVisibilityEnum.PUBLIC, themes);
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

    @And("The user {string} is a User {string} of the group")
    @Transactional
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
    @And("The user {string} creates a group with name {string}, id {long} and description {string}")
    public void createGroup(String username, String name, long id, String description) throws Exception {
        group = new Group(id, name, description, GroupVisibilityEnum.PUBLIC);

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

    @And("The number of related themes is {long}")
    public void numberOfThemes(Long themesSize) {
        assertEquals(group.getThemes().size(), themesSize);
    }





}
