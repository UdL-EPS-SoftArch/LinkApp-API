package cat.udl.eps.softarch.linkapp.steps;

import cat.udl.eps.softarch.linkapp.domain.User;
import cat.udl.eps.softarch.linkapp.repository.UserRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ModifyStepDefs {

    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private UserRepository userRepository;

    @When("^I modify the email of the user \"([^\"]*)\" by \"([^\"]*)\"$")
    public void iModifyEmailOfUserByModifiedEmail(String username, String modifiedEmail) throws Throwable {
        // Patch updates one field whereas put overwrites all fields
        stepDefs.result = stepDefs.mockMvc.perform(
                        patch("/users/{username}", username)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content((new JSONObject().put("email", modifiedEmail)).toString())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @And("^The email of the user \"([^\"]*)\" has been modified by \"([^\"]*)\"$")
    public void itHasBeenModifiedEmailOfUserByModifiedEmail(String username, String modifiedEmail) throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/users/{username}", username)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(jsonPath("$.email", is(modifiedEmail)));
    }
}
