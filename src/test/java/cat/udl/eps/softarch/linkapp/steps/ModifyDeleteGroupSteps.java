package cat.udl.eps.softarch.linkapp.steps;

import cat.udl.eps.softarch.linkapp.domain.*;
import cat.udl.eps.softarch.linkapp.repository.GroupRepository;
import cat.udl.eps.softarch.linkapp.repository.MeetRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRoleRepository;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class ModifyDeleteGroupSteps
{

    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private MeetRepository meetRepository;


    private static Meet featureMeet;
    private static Group group;


    @When("The user modifies the group description to {string}")
    public void userModifiesGroup(String newDescription) throws Exception
    {
        Group tmpGroup = group;

        tmpGroup.setDescription(newDescription);
        JSONObject newJsonDescription = new JSONObject();
        newJsonDescription.put("description", newDescription);
        stepDefs.result = stepDefs.mockMvc.perform(
                        patch("/groups/{id}", tmpGroup.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(newJsonDescription.toString())
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
        updateGroupReference(tmpGroup.getId());
    }

    @When("A user {string} deletes the theme {string}")
    public void userDeletesTheme(String username, String deletedTheme) throws Exception
    {
        Group tmpGroup = group;

        int length = tmpGroup.getThemes().size();
        JSONArray newThemes = new JSONArray();
        JSONObject newThemesObject = new JSONObject();

        for (int i = 0; i < length; i++) {
            if (!tmpGroup.getThemes().get(i).name().equals(deletedTheme)) {
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
    public void userDeletesThemes(String username, String deletedTheme1, String deletedTheme2) throws Exception
    {
        Group tmpGroup = group;

        int length = tmpGroup.getThemes().size();
        JSONArray newThemes = new JSONArray();
        JSONObject newThemesObject = new JSONObject();

        for (int i = 0; i < length; i++) {
            if (!tmpGroup.getThemes().get(i).name().equals(deletedTheme1) && !tmpGroup.getThemes().get(i).name().equals(deletedTheme2)) {
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


    @When("A user adds the theme {string}")
    public void userAddsTheme(String newTheme) throws Throwable
    {
        Group tmpGroup = group;

        int length = tmpGroup.getThemes().size();
        JSONArray newThemes = new JSONArray();
        JSONObject newThemesObject = new JSONObject();

        for (int i = 0; i < length; i++) {
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

    @When("A user adds the themes {string}, {string}, {string}")
    public void userAddsThemes(String newTheme1, String newTheme2, String newTheme3) throws Throwable
    {
        Group tmpGroup = group;

        int length = tmpGroup.getThemes().size();
        JSONArray newThemes = new JSONArray();
        JSONObject newThemesObject = new JSONObject();

        for (int i = 0; i < length; i++) {
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


    @And("A already created group where with name {string} and description {string}")
    public void groupCreated(String name, String description) throws Exception
    {
        Group tmpGroup = new Group(0L, name, description, GroupVisibilityEnum.PUBLIC);

        stepDefs.result = stepDefs.mockMvc.perform(
                        post("/groups/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new JSONObject(
                                        stepDefs.mapper.writeValueAsString(tmpGroup)
                                ).toString())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());

        updateGroupReference();
    }

    private void updateGroupReference() throws Exception
    {
        int status = stepDefs.result.andReturn().getResponse().getStatus();
        if (status == 200 || status == 201) {
            String content = stepDefs.result.andReturn().getResponse().getContentAsString();
            String uri = JsonPath.read(content, "uri");
            updateGroupReference(Long.parseLong(uri.substring(uri.length() - 1)));
        }
    }

    private void updateGroupReference(Long id) throws Exception
    {
        group = groupRepository.findById(id).get();
    }

    @And("A already created group where with name {string} and description {string} and theme {string}")
    public void groupCreated(String name, String description, String theme) throws Exception
    {
        List<ThemeEnum> themes = new ArrayList<ThemeEnum>();
        themes.add(ThemeEnum.valueOf(theme));
        Group tmpGroup = new Group(0L, name, description, GroupVisibilityEnum.PUBLIC, themes);

        stepDefs.result = stepDefs.mockMvc.perform(
                        post("/groups/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new JSONObject(
                                        stepDefs.mapper.writeValueAsString(tmpGroup)
                                ).toString())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
        updateGroupReference();
    }

    @And("A already created group where with name {string} and description {string} and theme {string} and {string}")
    public void groupCreatedMultipleGroups(String name, String description, String theme1, String theme2) throws Exception
    {
        List<ThemeEnum> themes = new ArrayList<>();
        themes.add(ThemeEnum.valueOf(theme1));
        themes.add(ThemeEnum.valueOf(theme2));
        Group tmpGroup = new Group(0L, name, description, GroupVisibilityEnum.PUBLIC, themes);

        stepDefs.result = stepDefs.mockMvc.perform(
                        post("/groups/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new JSONObject(
                                        stepDefs.mapper.writeValueAsString(tmpGroup)
                                ).toString())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
        updateGroupReference();
    }

    @And("The user {string} is a User {string} of the group")
    @Transactional
    public void userAdminOfThatGroup(String username, String role)
    {
        User user = userRepository.findById(username).get();

        UserRoleKey userRoleKey = new UserRoleKey();
        userRoleKey.setUser(user);
        userRoleKey.setGroup(group);


        UserRole userRole = userRoleRepository.findByRoleKeyUserAndRoleKeyGroup(user, group);
        if (userRole == null) {
            userRole = new UserRole();
        }

        userRole.setRoleKey(userRoleKey);
        userRole.setRole(UserRoleEnum.valueOf(role));

        userRoleRepository.save(userRole);
    }

    @And("The description of the group is now {string}")
    public void itHasBeenModifiedAGroup(String description) throws Exception
    {
        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/groups/{id}", group.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(jsonPath("$.description", is(description)));
    }

    @And("The user {string} creates a group with name {string} and description {string}")
    public void createGroup(String username, String name, String description) throws Exception
    {
        group = new Group(0L, name, description, GroupVisibilityEnum.PUBLIC);

        stepDefs.result = stepDefs.mockMvc.perform(
                        post("/groups/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new JSONObject(
                                        stepDefs.mapper.writeValueAsString(group)
                                ).toString())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
        updateGroupReference();
    }

    @And("The number of related themes is {long}")
    public void numberOfThemes(Long themesSize)
    {
        assertEquals(themesSize, (group.getThemes() == null) ? 0 : group.getThemes().size());
    }


    @And("I create a meet in that group")
    public void iCreateAMeetInThatGroup() throws Exception
    {
        Meet tmpMeet = new Meet();
        tmpMeet.setTitle("title");
        tmpMeet.setDescription("description");
        tmpMeet.setMaxUsers(3L);
        tmpMeet.setLocation("location");
        tmpMeet.setInitialMeetDate(ZonedDateTime.now());
        tmpMeet.setFinalMeetDate(ZonedDateTime.now().plusHours(1));
        stepDefs.result = stepDefs.mockMvc
                .perform(
                        post("/meets/")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(new JSONObject(stepDefs.mapper.writeValueAsString(tmpMeet))
                                        .put("group", "/groups/" + group.getId())
                                        .toString()
                                )
                                .with(AuthenticationStepDefs.authenticate())
                ).andDo(print());

        MockHttpServletResponse response = stepDefs.result.andReturn().getResponse();
        if (response.getStatus() == 201) {
            String content = response.getContentAsString();
            String uri = JsonPath.read(content, "uri");
            Matcher m = Pattern.compile("\\d+$").matcher(uri);
            if (!m.find())
                throw new RuntimeException("Unexpected uri");

            featureMeet = meetRepository.findById(Long.parseLong(m.group())).get();
        }
    }

    @Then("I check if the meet has been deleted")
    public void iCheckIfTheMeetHasBeenDeleted() throws Exception
    {
        stepDefs.result = stepDefs.mockMvc.perform(
                        get(featureMeet.getUri())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @When("The user deletes the group")
    public void userDeletesGroup() throws Exception
    {
        stepDefs.result = stepDefs.mockMvc.perform(
                        delete(group.getUri())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate())
                )
                .andDo(print());
    }

    @And("A post is created in that group")
    public void postCreates() throws Exception
    {
        Group group = groupRepository.findById((long) 1).get();
        Post post = new Post();
        post.setId((long) 1);
        stepDefs.result = stepDefs.mockMvc.perform(
                post("/posts/")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(stepDefs.mapper.writeValueAsString(post))
                                .put("group", "/groups/" + group.getId())
                                .toString()
                        )
                        .with(AuthenticationStepDefs.authenticate())
                        .with(AuthenticationStepDefs.authenticate())).andDo(print());
    }

    @Then("I check if the post has been deleted")
    public void getPost() throws Exception
    {

        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/posts/{id}", (long) 1)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }
}
