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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
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

    @When("A user {string} deletes the theme {string}")
    public void userDeletesTheme(String username, String deletedTheme) throws Exception {
        //User user = userRepository.findById(username).get();
        Group tmpGroup = groupRepository.findById((long) 1).get();
        /*UserRole userRole = userRoleRepository.findByRoleKeyUserAndRoleKeyGroup(user, tmpGroup);*/

        int length = tmpGroup.getThemes().size();
        JSONArray newThemes = new JSONArray();
        JSONObject newThemesObject = new JSONObject();

        for(int i=0;i<length;i++) {
            if(!tmpGroup.getThemes().get(i).name().equals(deletedTheme)){
                newThemes.put(tmpGroup.getThemes().get(i));
            }
        }

        newThemesObject.put("themes", newThemes);
        stepDefs.result = stepDefs.mockMvc.perform(
                        patch("/groups/{id}", tmpGroup.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(newThemesObject.toString())
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
        group = groupRepository.findById(group.getId()).get();
    }

    @When("A user {string} deletes the themes {string}, {string}")
    public void userDeletesThemes(String username, String deletedTheme1, String deletedTheme2) throws Exception {
        //User user = userRepository.findById(username).get();
        Group tmpGroup = groupRepository.findById((long) 1).get();
        /*UserRole userRole = userRoleRepository.findByRoleKeyUserAndRoleKeyGroup(user, tmpGroup);*/

        int length = tmpGroup.getThemes().size();
        JSONArray newThemes = new JSONArray();
        JSONObject newThemesObject = new JSONObject();

        for(int i=0;i<length;i++) {
            if(!tmpGroup.getThemes().get(i).name().equals(deletedTheme1) && !tmpGroup.getThemes().get(i).name().equals(deletedTheme2)){
                newThemes.put(tmpGroup.getThemes().get(i));
            }
        }

        newThemesObject.put("themes", newThemes);
        stepDefs.result = stepDefs.mockMvc.perform(
                        patch("/groups/{id}", tmpGroup.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(newThemesObject.toString())
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
        group = groupRepository.findById(group.getId()).get();
    }


    @When("A user {string} adds the theme {string}")
    public void userAddsTheme(String username, String newTheme) throws Throwable {
        //User user = userRepository.findById(username).get();
        Group tmpGroup = groupRepository.findById((long) 1).get();
        /*UserRole userRole = userRoleRepository.findByRoleKeyUserAndRoleKeyGroup(user, tmpGroup);*/

        int length = tmpGroup.getThemes().size();
        JSONArray newThemes = new JSONArray();
        JSONObject newThemesObject = new JSONObject();

        for(int i=0;i<length;i++) {
            newThemes.put(tmpGroup.getThemes().get(i));
        }
        newThemes.put(newTheme);

        newThemesObject.put("themes", newThemes);
        stepDefs.result = stepDefs.mockMvc.perform(
                        patch("/groups/{id}", tmpGroup.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(newThemesObject.toString())
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
        group = groupRepository.findById(group.getId()).get();
    }

    @When("A user {string} adds the themes {string}, {string}, {string}")
    public void userAddsThemes(String username, String newTheme1, String newTheme2, String newTheme3) throws Throwable {
        //User user = userRepository.findById(username).get();
        Group tmpGroup = groupRepository.findById((long) 1).get();
        /*UserRole userRole = userRoleRepository.findByRoleKeyUserAndRoleKeyGroup(user, tmpGroup);*/

        int length = tmpGroup.getThemes().size();
        JSONArray newThemes = new JSONArray();
        JSONObject newThemesObject = new JSONObject();

        for(int i=0;i<length;i++) {
            newThemes.put(tmpGroup.getThemes().get(i));
        }
        newThemes.put(newTheme1);
        newThemes.put(newTheme2);
        newThemes.put(newTheme3);

        newThemesObject.put("themes", newThemes);
        stepDefs.result = stepDefs.mockMvc.perform(
                        patch("/groups/{id}", tmpGroup.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(newThemesObject.toString())
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
        group = groupRepository.findById(group.getId()).get();
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
        List<ThemeEnum> themes = new ArrayList<ThemeEnum>();
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

    @And("A already created group where with name {string}, id {long} and description {string} and theme {string} and {string}")
    public void groupCreatedMultipleGroups(String name, long id, String description, String theme1, String theme2) throws Exception {
        List<ThemeEnum> themes = new ArrayList<ThemeEnum>();
        themes.add(ThemeEnum.valueOf(theme1));
        themes.add(ThemeEnum.valueOf(theme2));
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
        assertEquals(themesSize, (group.getThemes() == null) ? 0 : group.getThemes().size());
    }


}
