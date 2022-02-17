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

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeleteStepDefs {

  @Autowired
  private StepDefs stepDefs;

  @Autowired
  private UserRepository userRepository;

  @When("^I delete the user with username \"([^\"]*)\", email \"([^\"]*)\" and password \"([^\"]*)\"$")
  public void iDeleteUserWithUsernameEmailAndPassword(String username, String email, String password) throws Throwable {

    stepDefs.result = stepDefs.mockMvc.perform(
                    delete("/users/{username}", username).with(AuthenticationStepDefs.authenticate()));
  }

  @And("^It has been deleted a user with username \"([^\"]*)\" and email \"([^\"]*)\"")
  public void itHasBeenCreatedAUserWithUsername(String username, String email) throws Throwable {
    stepDefs.result = stepDefs.mockMvc.perform(
                    get("/users/{username}", username)
                            .accept(MediaType.APPLICATION_JSON)
                            .with(AuthenticationStepDefs.authenticate()))
            .andExpect(jsonPath("$.email").doesNotExist())
            .andExpect(jsonPath("$.password").doesNotExist());
  }

}
