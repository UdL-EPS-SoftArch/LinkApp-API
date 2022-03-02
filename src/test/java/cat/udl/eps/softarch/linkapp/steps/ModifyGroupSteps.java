package cat.udl.eps.softarch.linkapp.steps;

import cat.udl.eps.softarch.linkapp.domain.Group;
import cat.udl.eps.softarch.linkapp.domain.GroupVisibilityEnum;
import cat.udl.eps.softarch.linkapp.domain.User;
import cat.udl.eps.softarch.linkapp.repository.GroupRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRepository;
import io.cucumber.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;

public class ModifyGroupSteps {

    @Autowired
    private StepDefs stepDefs;
    private UserRepository userRepository;
    private GroupRepository groupRepository;

    private static Group group;

    @And("A already created group where with name {string}, id {long} and description {string}")
    public void groupCreated(String name, long id, String description){
        group = new Group(id, name, description, GroupVisibilityEnum.PUBLIC);
    }

    @And("The user {string} belongs to that group as {string}")
    public void userAdminOfThatGroup(String username, String role){
        User user = userRepository.findById(username).get();




    }
}
