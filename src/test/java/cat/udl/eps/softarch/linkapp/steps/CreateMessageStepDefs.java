package cat.udl.eps.softarch.linkapp.steps;

import cat.udl.eps.softarch.linkapp.domain.*;
import cat.udl.eps.softarch.linkapp.repository.*;
import io.cucumber.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;


public class CreateMessageStepDefs {

    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private MeetRepository meetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    private Group group;

    CreateMessageStepDefs(StepDefs stepDefs) {
        this.stepDefs = stepDefs;
    }

    @And("The meet with id {long} belongs to the group with id {long}")
    public void theMeetWithIdExists(Long meetId, Long groupId) {
        if (groupRepository.existsById(groupId)) groupRepository.deleteById(groupId);
        Group group = new Group();
        group.setId(groupId);
        group.setTitle("title");
        group.setDescription("description");
        group.setVisibility(true);
        groupRepository.save(group);
        if (meetRepository.existsById(meetId)) meetRepository.deleteById(meetId);
        Meet meet = new Meet();
        meet.setId(meetId);
        meet.setTitle("title");
        meet.setGroup(group);
        meet.setDescription("description");
        meet.setStatus(true);
        meetRepository.save(meet);
    }

    @And("The user with id {string} belongs to the group with id {long} as {string}")
    public void theUserBelongsToTheGroup(String username, Long groupId, String role) {
        if (groupRepository.existsById(groupId)) groupRepository.deleteById(groupId);
        Group group = new Group();
        group.setId(groupId);
        group.setTitle("title");
        group.setDescription("description");
        group.setVisibility(true);
        groupRepository.save(group);

        if (userRepository.existsById(username)) userRepository.deleteById(username);
        User user = new User();
        user.setUsername("username");
        user.setEmail("user@sample.app");
        user.setPassword("password");
        user.encodePassword();
        user.setPasswordReset(true);
        userRepository.save(user);

        if (userRoleRepository.existsById(groupId)) userRoleRepository.deleteById(groupId);
        UserRoleKey userRoleKey = new UserRoleKey();
        userRoleKey.setUser(user);
        userRoleKey.setGroup(group);

        UserRole userRole = new UserRole();
        userRole.setRoleKey(userRoleKey);
        userRole.setRole(UserRoleEnum.valueOf(role));
        userRoleRepository.save(userRole);
    }
}
