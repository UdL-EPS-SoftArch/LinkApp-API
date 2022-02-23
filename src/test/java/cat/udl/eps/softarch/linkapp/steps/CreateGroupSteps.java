package cat.udl.eps.softarch.linkapp.steps;

import cat.udl.eps.softarch.linkapp.domain.Group;
import cat.udl.eps.softarch.linkapp.repository.GroupRepository;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class CreateGroupSteps {

    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private GroupRepository groupRepository;

    @When("^I Create a public Group called \"([^\"]*)\" ")
    public void iCreateAPublicGroup(String groupName, String description) throws Exception {
        Group group = new Group(groupName, description, true);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(
                                stepDefs.mapper.writeValueAsString(group)
                                ).put("groupName", groupName).toString())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }


}
