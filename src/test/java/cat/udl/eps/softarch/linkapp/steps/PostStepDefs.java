package cat.udl.eps.softarch.linkapp.steps;

import cat.udl.eps.softarch.linkapp.domain.Group;
import cat.udl.eps.softarch.linkapp.domain.Post;
import cat.udl.eps.softarch.linkapp.repository.GroupRepository;
import cat.udl.eps.softarch.linkapp.repository.PostRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class PostStepDefs {
    private String newResourceUri;

    private String newResourceUriComment;

    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private GroupRepository groupRepository;

    //////////GENERAL STEP DEFS//////////

    @And("There is no post created")
    public void thereIsNoPostCreatedWithId()  throws Throwable{
        Assert.assertEquals(0,postRepository.count());
    }

    //////////DELETE STEP DEFS//////////

    @When("I delete the post")
    public void iDeleteAPost() throws Throwable{
        stepDefs.result = stepDefs.mockMvc.perform(
                delete(newResourceUri).with(AuthenticationStepDefs.authenticate()));
    }

    @When("I delete the comment")
    public void iDeleteAComment() throws Throwable{
        stepDefs.result = stepDefs.mockMvc.perform(
                delete(newResourceUriComment).with(AuthenticationStepDefs.authenticate()));
    }

    @When("I delete the post with id {string}")
    public void iDeleteAPostWithId(String id) throws Throwable{
        stepDefs.result = stepDefs.mockMvc.perform(
                delete("/posts/{id}",id).with(AuthenticationStepDefs.authenticate()));
    }

    @And("It has been deleted the post")
    public void itHasBeenDeletedAPost() throws Throwable{
        stepDefs.result = stepDefs.mockMvc.perform(
                        get(newResourceUri)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andExpect(status().isNotFound());
    }

    @And("It has been deleted the comment")
    public void itHasBeenDeletedAComment() throws Throwable{
        stepDefs.result = stepDefs.mockMvc.perform(
                        get(newResourceUriComment)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andExpect(status().isNotFound());
    }

    @And("The post has not been deleted")
    public void thePostHasNotBeenDeleted() throws Throwable{
        stepDefs.result = stepDefs.mockMvc.perform(
                        get(newResourceUri)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    //////////MODIFY POST STEP DEFS//////////

    @When("I modify the post just created with new text {string}")
    public void iModifyThePostJustCreated(String text) throws Throwable{
        stepDefs.result = stepDefs.mockMvc.perform(
                        patch(newResourceUri)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new JSONObject().put("text", text).toString())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @When("I modify the comment just created with new text {string}")
    public void iModifyTheCommentJustCreated(String text) throws Throwable{
        stepDefs.result = stepDefs.mockMvc.perform(
                        patch(newResourceUriComment)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new JSONObject().put("text", text).toString())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }


    @When("I modify the post with id {string} with new text {string}")
    public void iModifyAPostWithId(String id, String text) throws Throwable{
        stepDefs.result = stepDefs.mockMvc.perform(
                        patch("/posts/{id}",id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new JSONObject().put("text", text).toString())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @And("It has been modified the post just created with the text {string}")
    public void itHasBeenModifiedAPostWithIdWithTheText(String text) throws Throwable{
        stepDefs.result = stepDefs.mockMvc.perform(
                        get(newResourceUri)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(jsonPath("$.text", is(text)));
    }

    @And("It has been modified the comment just created with the text {string}")
    public void itHasBeenModifiedThenCommentWithIdWithTheText(String text) throws Throwable{
        stepDefs.result = stepDefs.mockMvc.perform(
                        get(newResourceUriComment)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(jsonPath("$.text", is(text)));
    }
    @And("The post has not been modified")
    public void thePostHasNotBeenModified() throws Throwable{
        stepDefs.result = stepDefs.mockMvc.perform(
                        get(newResourceUri)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(jsonPath("$.text", is("hola")));
    }
    @And("The comment has not been modified")
    public void theCommentHasNotBeenModified() throws Throwable{
        stepDefs.result = stepDefs.mockMvc.perform(
                        get(newResourceUriComment)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(jsonPath("$.text", is("create comment 1")));
    }

    //CREATE STEP DEFS
    @When("I create a post with text {string}")
    public void iCreateAPostWithText(String description)throws Throwable {

        List<Group> groups = groupRepository.findByTitleContaining("title");
        Group group = groups.get(groups.size()-1);
        Post post = new Post();
        post.setText(description);
        stepDefs.result = stepDefs.mockMvc.perform(
                post("/posts/")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(stepDefs.mapper.writeValueAsString(post))
                                .put("group", "/groups/" + group.getId())
                                .toString()
                        )
                        .with(AuthenticationStepDefs.authenticate())
                        .with(AuthenticationStepDefs.authenticate())).andDo(print());

        newResourceUri = stepDefs.result.andReturn().getResponse().getHeader("Location");
    }


    @And("There is a post created with text {string}")
    public void itHasBeenCreatedAPostWithTextAndAuthorUsername(String text) throws Throwable {
        List<Post> posts = postRepository.findByTextContaining(text);
        Post post = posts.get(posts.size()-1);
        stepDefs.result = stepDefs.mockMvc.perform(
                        get(newResourceUri)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(jsonPath("$.text", is(text)));


    }

    @When("I create a comment to the previous post with text {string}")
    public void iCreateACommentToPreviousPostWithAuthorUsername(String comment) throws Throwable {

        List<Group> groups = groupRepository.findByTitleContaining("title");
        Group group = groups.get(groups.size()-1);

        List<Post> posts = postRepository.findByTextContaining("create post 1");
        Post father = posts.get(posts.size()-1);

        Post post = new Post();
        post.setText(comment);
        post.setFather(father);


        stepDefs.result = stepDefs.mockMvc.perform(
                post("/posts/")
                        .content(new JSONObject(stepDefs.mapper.writeValueAsString(post))
                        .put("group", "/groups/" + group.getId())
                        .toString()
                )
                .with(AuthenticationStepDefs.authenticate())
                .with(AuthenticationStepDefs.authenticate())).andDo(print());


        newResourceUriComment = stepDefs.result.andReturn().getResponse().getHeader("Location");
    }

    @And("The post with text {string} is a comment from post with text {string}")
    public void thePostWithTextIsACommentFromPostWithText(String comment, String post) throws Throwable {

        List<Post> posts = postRepository.findByTextContaining(post);
        Post father = posts.get(posts.size()-1);

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

    @And("There are {string} posts created")
    public void usernameHasCreatedPosts(String n_posts) {
        Long posts = postRepository.count();
        Assert.assertEquals(posts,Long.valueOf(n_posts));
    }
}
