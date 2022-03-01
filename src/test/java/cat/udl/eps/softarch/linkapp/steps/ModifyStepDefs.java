package cat.udl.eps.softarch.linkapp.steps;

import cat.udl.eps.softarch.linkapp.domain.User;
import cat.udl.eps.softarch.linkapp.repository.UserRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ResponseStatusException;

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
                                .content(new JSONObject().put("email", modifiedEmail).toString())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @When("^I modify the password of the user \"([^\"]*)\" by \"([^\"]*)\"$")
    public void iModifyPasswordOfUserByModifiedPasword(String username, String modifiedPassword) throws Throwable {
        String encodedPassword = User.passwordEncoder.encode(modifiedPassword);
        // Patch updates one field whereas put overwrites all fields
        stepDefs.result = stepDefs.mockMvc.perform(
                        patch("/users/{username}", username)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new JSONObject().put("password", encodedPassword).toString())
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
