package cat.udl.eps.softarch.linkapp.steps;

import cat.udl.eps.softarch.linkapp.domain.Post;
import cat.udl.eps.softarch.linkapp.domain.User;
import cat.udl.eps.softarch.linkapp.repository.PostRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.OutputStream;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    private String newResourceUri;

    private String newResourceUriComment;

    private Post featurePost;

    private User featureUser;

    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;




    @When("I create a post with text {string}")
    public void iCreateAPostWithText(String description)throws Throwable {
        Post post = new Post();
        post.setText(description);
        stepDefs.result = stepDefs.mockMvc.perform(
                        post("/posts/")
                                .content(stepDefs.mapper.writeValueAsString(post))
                                .with(AuthenticationStepDefs.authenticate())).andDo(print());

        newResourceUri = stepDefs.result.andReturn().getResponse().getHeader("Location");
    }


    @And("It has been created a post with text {string}")
    public void itHasBeenCreatedAPostWithTextAndAuthorUsername(String text) throws Throwable {
        List<Post> posts = postRepository.findByTextContaining(text);
        Post post = posts.get(posts.size()-1);
        stepDefs.result = stepDefs.mockMvc.perform(
                        get(newResourceUri)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(jsonPath("$.text", is(text)));


        User user = userRepository.findByUsername("demo");
        Assert.assertEquals(user.getId(),post.getAuthor().getId());
    }


    @And("Username {string} has not created a post")
    public void usernameHasCreatedPosts(String author) {
        List<Post> posts = postRepository.findByAuthor_UsernameContaining(author);
        Assert.assertTrue(posts.isEmpty());

    }

    @And("Username {string} has created {string} posts")
    public void usernameHasCreatedPosts(String author, String n_posts) {
        List<Post> posts = postRepository.findByAuthor_UsernameContaining(author);
        Assert.assertEquals(String.valueOf(posts.size()),String.valueOf(n_posts));
    }



    @And("I create a comment with text {string}")
    public void iCreateACommentWithIdAndTextAndAuthorUsername(String description, String author) throws Throwable {
        Post post = new Post();
        post.setText(description);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/posts/")
                        .content(stepDefs.mapper.writeValueAsString(post))
                        .with(AuthenticationStepDefs.authenticate())).andDo(print());

        newResourceUriComment = stepDefs.result.andReturn().getResponse().getHeader("Location");
    }

    @When("I create a comment to the previous post with text {string}")
    public void iCreateACommentToPreviousPostWithAuthorUsername(String comment) throws Throwable {
        List<Post> posts = postRepository.findByTextContaining("create post 1");
        Post father = posts.get(posts.size()-1);

        Post post = new Post();
        post.setText(comment);

        post.setFather(father);

        stepDefs.result = stepDefs.mockMvc.perform(
                        post("/posts/")
                                .content(stepDefs.mapper.writeValueAsString(post))
                                .with(AuthenticationStepDefs.authenticate())).andDo(print());


        newResourceUriComment = stepDefs.result.andReturn().getResponse().getHeader("Location");
    }

    @And("The post with text {string} is a comment from post with text {string}")
    public void thePostWithTextIsACommentFromPostWithText(String comment, String post) throws Throwable {

        List<Post> posts = postRepository.findByTextContaining(post);
        Post father = posts.get(posts.size()-1);

        System.out.println(newResourceUriComment);
        stepDefs.result = stepDefs.mockMvc.perform(
                        get(newResourceUriComment)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(jsonPath("$.text", is(comment)));

        stepDefs.result = stepDefs.mockMvc.perform(
                        get(newResourceUriComment+"/father")
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(jsonPath("$.text", is(father.getText())));

    }
}
