
package cat.udl.eps.softarch.linkapp.steps;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cat.udl.eps.softarch.linkapp.domain.User;
import cat.udl.eps.softarch.linkapp.repository.UserRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

public class DeleteStepDefsPost {

    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private UserRepository userRepository;

    @Given("^There is a registered user with username \"([^\"]*)\" and password \"([^\"]*)\" and email \"([^\"]*)\"$")
    public void thereIsARegisteredUserWithUsernameAndPasswordAndEmail(String username, String password, String email) {
        if (!userRepository.existsById(username)) {
            User user = new User();
            user.setEmail(email);
            user.setUsername(username);
            user.setPassword(password);
            user.encodePassword();
            userRepository.save(user);
        }
    }

    @And("^I can login with username \"([^\"]*)\" and password \"([^\"]*)\"$")
    public void iCanLoginWithUsernameAndPassword(String username, String password) throws Throwable {
        AuthenticationStepDefs.currentUsername = username;
        AuthenticationStepDefs.currentPassword = password;

        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/identity", username)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @And("It exists a post with id \"([^\"]*)\" created by username \"([^\"]*)\"")
    public void itExistsAPostWithIdCreatedByUsername(String id, String username) {
        
    }

    @When("^I delete a post with id \"([^\"]*)\"")
    public void iDeleteAPostWithId(String id) throws Throwable{
        stepDefs.result = stepDefs.mockMvc.perform(
                delete("/posts/{id}", id).with(AuthenticationStepDefs.authenticate()));
    }

    @And("^It has been deleted a post with id \"([^\"]*)\"$")
    public void itHasBeenDeletedAPostWithId(String id) throws Throwable {

    }
}
