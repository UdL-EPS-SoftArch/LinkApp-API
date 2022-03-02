package cat.udl.eps.softarch.linkapp.steps;

import cat.udl.eps.softarch.linkapp.domain.*;
import cat.udl.eps.softarch.linkapp.repository.GroupRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRoleRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

public class ModifyGroupSteps {

    @Autowired
    private StepDefs stepDefs;
    private UserRepository userRepository;
    private GroupRepository groupRepository;
    private UserRoleRepository userRoleRepository;

    private static Group group;

    @And("A already created group where with name {string}, id {long} and description {string}")
    public void groupCreated(String name, long id, String description){
        group = new Group(id, name, description, GroupVisibilityEnum.PUBLIC);
        groupRepository.save(group);
    }

    @And("The user {string} is an Admin {string} of the group")
    public void userAdminOfThatGroup(String username, String role){
        User user = userRepository.findById(username).get();

        UserRoleKey userRoleKey = new UserRoleKey();
        userRoleKey.setUser(user);
        userRoleKey.setGroup(group);

        UserRole userRole = new UserRole();
        userRole.setRoleKey(userRoleKey);
        userRole.setRole(UserRoleEnum.valueOf(role));
        userRoleRepository.save(userRole);
    }
    @When("The user {string} modifies the group {long} description to {string}")
    public void userModifiesGroup(String user, long id, String newDescription){

    }




}
