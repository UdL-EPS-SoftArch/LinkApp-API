package cat.udl.eps.softarch.linkapp.steps;

import cat.udl.eps.softarch.linkapp.domain.*;
import cat.udl.eps.softarch.linkapp.repository.PostRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class CreateStepDefsPost
{

    private static Post featurePost;

    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;




    @When("I create a post with text {string} and author username {string}")
    public void iCreateAPostWithIdAndTextAndAuthorUsername(String description, String author)throws Throwable {
        Post post = new Post();
        User user = userRepository.findById(author).get();
        post.setText(description);
        post.setAuthor(user);
        stepDefs.result = stepDefs.mockMvc.perform(
                        post("/posts/")
                                .content(stepDefs.mapper.writeValueAsString(post))
                                .with(AuthenticationStepDefs.authenticate())).andDo(print());


    }


    @And("It has been created a post with text {string} and author username {string}")
    public void itHasBeenCreatedAPostWithTextAndAuthorUsername(String text, String author) throws Throwable {
        List<Post> posts = postRepository.findByAuthor_UsernameContaining(author);
        String id = String.valueOf(posts.get(posts.size()-1).getId());
        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/posts/{id}",id)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())

                .andExpect(jsonPath("$.text", is(text)));
    }
}
