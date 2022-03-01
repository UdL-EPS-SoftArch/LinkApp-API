package cat.udl.eps.softarch.linkapp.steps;

import cat.udl.eps.softarch.linkapp.domain.*;
import cat.udl.eps.softarch.linkapp.repository.GroupRepository;
import cat.udl.eps.softarch.linkapp.repository.MeetRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRoleRepository;
import io.cucumber.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;


public class CreateMessageStepDefs {

    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private MeetRepository meetRepository;

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

    @And("The user \"([^\"]*)\" is assisting to the meet with id {long}")
    public void userIsAssistingToMeet(String username, Long meetId) {
        User user = userRepository.findById(username).get();
        Meet meet = meetRepository.findById(meetId).get();

        UserRole userRole = userRoleRepository.
                findByRoleKeyUserAndRoleKeyGroup(user, group);

        meet.getAttending().add(userRole);
    }
}

