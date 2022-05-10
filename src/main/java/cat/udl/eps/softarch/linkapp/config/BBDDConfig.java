package cat.udl.eps.softarch.linkapp.config;

import cat.udl.eps.softarch.linkapp.domain.*;
import cat.udl.eps.softarch.linkapp.repository.*;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Profile("test")
public class BBDDConfig {

    @Value("${default-password}")
    String defaultPassword;

    final UserRepository userRepository;
    final GroupRepository groupRepository;
    final MeetAttendingRepository meetAttendingRepository;
    final MeetRepository meetRepository;
    final MessageRepository messageRepository;
    final PostRepository postRepository;
    final UserRoleRepository userRoleRepository;


    public BBDDInitialization(UserRepository userRepository,
                              GroupRepository groupRepository,
                              MeetAttendingRepository meetAttendingRepository,
                              MeetRepository meetRepository,
                              MessageRepository messageRepository,
                              PostRepository postRepository,
                              UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.meetAttendingRepository = meetAttendingRepository;
        this.meetRepository = meetRepository;
        this.messageRepository = messageRepository;
        this.postRepository = postRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @PostConstruct
    public void initializeDatabase() {
        // Sample Admin

        // Sample User
        User user = this.userRepository.findById("user").orElse(new User());
        if (user.getId() == null) {
            user.setEmail("user@sample.app");
            user.setUsername("user");
            user.setPassword(defaultPassword);
            user.encodePassword();
            user = userRepository.save(user);
        }
        //If the database is empty
        if(groupRepository.count() == 0) {
            List<ThemeEnum> themesList1 = new ArrayList<>();
            themesList1.add(ThemeEnum.ADVENTURE);
            Group group1 = new Group();
            group1.setTitle("Title group1");
            group1.setDescription("This is the description of group1. It has no members when created.");
            group1.setVisibility(GroupVisibilityEnum.PUBLIC);
            group1.setThemes(themesList1);
            groupRepository.save(group1);

            List<ThemeEnum> themesList2 = new ArrayList<>();
            themesList2.add(ThemeEnum.ADVENTURE);
            themesList2.add(ThemeEnum.FAMILY);
            themesList2.add(ThemeEnum.ANIMALS);
            Group group2 = new Group();
            group2.setTitle("Title group1");
            group2.setDescription("This is the description of group1. It has no members when created.");
            group2.setVisibility(GroupVisibilityEnum.PUBLIC);
            group2.setThemes(themesList2);
            groupRepository.save(group2);

            UserRoleKey userRoleKey = new UserRoleKey();
            userRoleKey.setGroup(group1);
            userRoleKey.setUser(user);

            UserRole userRole = new UserRole();
            userRole.setRole(UserRoleEnum.ADMIN);
            userRole.setRoleKey(userRoleKey);

        }
    }
}

