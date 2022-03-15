package cat.udl.eps.softarch.linkapp.steps;

import cat.udl.eps.softarch.linkapp.domain.Group;
import cat.udl.eps.softarch.linkapp.domain.GroupVisibilityEnum;
import cat.udl.eps.softarch.linkapp.domain.User;
import cat.udl.eps.softarch.linkapp.repository.GroupRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.core.gherkin.Step;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreateGroupSteps {

    @Autowired
    private StepDefs stepDefs;
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    private final Pattern idPattern = Pattern.compile("\\d+$");

    private static Group group;

    public static Group getCreatedGroup() { return group; }

    @When("^I Create a public Group called \"([^\"]*)\" with description \"([^\"]*)\"")
    public void iCreateAPublicGroup(String groupName, String description) throws Exception {
        Group tmpGroup = new Group();
        tmpGroup.setId((long) 1);
        tmpGroup.setTitle(groupName);
        tmpGroup.setDescription(description);
        tmpGroup.setVisibility(GroupVisibilityEnum.PUBLIC);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/groups/")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(
                                    stepDefs.mapper.writeValueAsString(tmpGroup)
                                ).toString())
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());

        MockHttpServletResponse response = stepDefs.result.andReturn().getResponse();
        if (response.getStatus() == 201)
        {
            String content = response.getContentAsString();
            String uri = JsonPath.read(content, "uri");
            Matcher m = idPattern.matcher(uri);
            if (!m.find())
                throw new RuntimeException("Unexpected uri");

            group = groupRepository.findById(Long.parseLong(m.group())).get();
        }
    }

    @And("It has been created a Group with title {string} and description {string}")
    public void itHasBeenCreatedAGroup(String title, String description) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/groups/{id}", group.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(jsonPath("$.title", is(title)))
                .andExpect(jsonPath("$.description", is(description)));
    }


}
