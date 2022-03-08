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

    private Post featurePost;

    private User featureUser;

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
        featureUser = user;
        featurePost = post;
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

    @When("I create a comment {string} and author username {string}")
    public void iCreateACommentAndAuthorUsername(String comment, String author) {
        List<Post> posts = postRepository.findByAuthor_UsernameContaining(author);
        String id = String.valueOf(posts.get(posts.size()-1).getId());
    }

    @And("I create a comment with text {string} and author username {string}")
    public void iCreateACommentWithIdAndTextAndAuthorUsername(String description, String author) throws Throwable {
        Post post = new Post();
        User user = userRepository.findById(author).get();
        post.setText(description);
        post.setAuthor(user);
        featureUser = user;
        featurePost = post;
        stepDefs.result = stepDefs.mockMvc.perform(
                post("/posts/")
                        .content(stepDefs.mapper.writeValueAsString(post))
                        .with(AuthenticationStepDefs.authenticate())).andDo(print());


    }

    @When("I create a comment {string} to previous post with author username {string}")
    public void iCreateACommentToPreviousPostWithAuthorUsername(String comment, String author) throws Throwable {


    }

    @And("It has been created a comment {string} with author username {string}")
    public void itHasBeenCreatedACommentWithAuthorUsername(String comment, String author) throws Throwable{
        List<Post> posts = postRepository.findByAuthor_UsernameContaining(author);
        String id = String.valueOf(posts.get(posts.size()-1).getId());
        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/posts/{id}",id)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(jsonPath("$.comments", is(comment)));

    }

    @And("The post with text {string} is a comment from post with text {string} with author {string}")
    public void thePostWithTextIsACommentFromPostWithText(String comment, String post, String author) throws Throwable {

        List<Post> posts = postRepository.findByAuthor_UsernameContaining(author);
        Post comm = posts.get(posts.size()-1);
        Post father = posts.get(posts.size()-2);

        String id_post = String.valueOf(father.getId());
        String id_comm = String.valueOf(comm.getId());

        comm.setFather(father);

        System.out.println(comm);
        System.out.println(comm.getFather());

        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/posts/{id}",id_comm)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(jsonPath("$.text", is(comment)))
                .andExpect(jsonPath("$.father", is(post)));


        System.out.println(comm);
        System.out.println(comm.getFather());

    }
}
