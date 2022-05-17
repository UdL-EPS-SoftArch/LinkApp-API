package cat.udl.eps.softarch.linkapp.config;

import cat.udl.eps.softarch.linkapp.domain.*;
import cat.udl.eps.softarch.linkapp.repository.*;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class BBDDConfig
{

    @Value("${default-password}")
    String defaultPassword;

    final UserRepository userRepository;
    final GroupRepository groupRepository;
    final MeetAttendingRepository meetAttendingRepository;
    final MeetRepository meetRepository;
    final MessageRepository messageRepository;
    final PostRepository postRepository;
    final UserRoleRepository userRoleRepository;


    public BBDDConfig(UserRepository userRepository,
                      GroupRepository groupRepository,
                      MeetAttendingRepository meetAttendingRepository,
                      MeetRepository meetRepository,
                      MessageRepository messageRepository,
                      PostRepository postRepository,
                      UserRoleRepository userRoleRepository)
    {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.meetAttendingRepository = meetAttendingRepository;
        this.meetRepository = meetRepository;
        this.messageRepository = messageRepository;
        this.postRepository = postRepository;
        this.userRoleRepository = userRoleRepository;
    }

    private User createUser(String name){
        User user = this.userRepository.findById(name).orElse(new User());
        if (user.getId() == null) {
            user.setEmail(name + "@sample.app");
            user.setUsername(name);
            user.setName(name.toUpperCase());
            user.setPassword(defaultPassword);
            user.encodePassword();
            return userRepository.save(user);
        }
        return user;
    }

    private Meet createMeet(Group group) {
        Meet meet = new Meet();

        meet.setTitle("Title 1 Meet");
        meet.setDescription(
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry." +
                        "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s," +
                        "when an unknown printer took a galley of type" +
                        "and scrambled it to make a type specimen book."
        );

        meet.setLocation("EPS Lleida");

        meet.setFinalMeetDate(
                ZonedDateTime.of(
                        2022,
                        7,
                        3,
                        19,
                        0,
                        0,
                        0,
                        ZoneId.systemDefault()
                )
        );

        meet.setInitialMeetDate(
                ZonedDateTime.of(
                        2022,
                        7,
                        3,
                        15,
                        0,
                        0,
                        0,
                        ZoneId.systemDefault()
                )
        );

        meet.setCreationDate(
                ZonedDateTime.of(
                        2022,
                        6,
                        1,
                        12,
                        0,
                        0,
                        0,
                        ZoneId.systemDefault()
                )
        );

        meet.setLastUpdate(
                ZonedDateTime.now()
        );

        meet.setGroup(group);

        meet = meetRepository.save(meet);
        return meet;
    }

    @PostConstruct
    @Transactional
    public void initializeDatabase()
    {
        User user = createUser("default1");
        User user2 = createUser("default2");
        User user3 = createUser("default3");



        //If the database is empty
        if (groupRepository.count() == 0) {
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
            group2.setTitle("Title group2");
            group2.setDescription("This is the description of group2. It has no members when created.");
            group2.setVisibility(GroupVisibilityEnum.PUBLIC);
            group2.setThemes(themesList2);
            groupRepository.save(group2);

            List<ThemeEnum> themesList3 = new ArrayList<>();
            themesList3.add(ThemeEnum.FASHION);
            themesList3.add(ThemeEnum.CRAFTS);
            Group group3 = new Group();
            group3.setTitle("Title group3");
            group3.setDescription("This is the description of group3. It has no members when created.");
            group3.setVisibility(GroupVisibilityEnum.PUBLIC);
            group3.setThemes(themesList3);
            groupRepository.save(group3);

            UserRoleKey userRoleKey1 = new UserRoleKey();
            userRoleKey1.setGroup(group1);
            userRoleKey1.setUser(user);

            UserRoleKey userRoleKey2 = new UserRoleKey();
            userRoleKey2.setGroup(group2);
            userRoleKey2.setUser(user);

            UserRoleKey userRoleKey3 = new UserRoleKey();
            userRoleKey3.setGroup(group3);
            userRoleKey3.setUser(user);

            UserRoleKey userRoleKey4 = new UserRoleKey();
            userRoleKey4.setGroup(group1);
            userRoleKey4.setUser(user2);

            UserRoleKey userRoleKey5 = new UserRoleKey();
            userRoleKey5.setGroup(group2);
            userRoleKey5.setUser(user2);

            UserRole userRole1 = new UserRole();
            userRole1.setRole(UserRoleEnum.ADMIN);
            userRole1.setRoleKey(userRoleKey1);
            userRoleRepository.save(userRole1);

            UserRole userRole2 = new UserRole();
            userRole2.setRole(UserRoleEnum.ADMIN);
            userRole2.setRoleKey(userRoleKey2);
            userRoleRepository.save(userRole2);

            UserRole userRole3 = new UserRole();
            userRole3.setRole(UserRoleEnum.ADMIN);
            userRole3.setRoleKey(userRoleKey3);
            userRoleRepository.save(userRole3);

            UserRole userRole4 = new UserRole();
            userRole4.setRole(UserRoleEnum.SUBSCRIBED);
            userRole4.setRoleKey(userRoleKey4);
            userRoleRepository.save(userRole4);

            UserRole userRole5 = new UserRole();
            userRole5.setRole(UserRoleEnum.AUTHORIZED);
            userRole5.setRoleKey(userRoleKey5);
            userRoleRepository.save(userRole5);

            Meet meet = createMeet(group1);

            MeetAttendingKey attendingKey = new MeetAttendingKey();
            attendingKey.setUser(user);
            attendingKey.setMeet(meet);

            MeetAttending meetAttending = new MeetAttending();
            meetAttending.setMeetAttendingKey(attendingKey);
            meetAttending.setAttends(true);
            meetAttending = meetAttendingRepository.save(meetAttending);

            MeetAttendingKey attendingKey2 = new MeetAttendingKey();
            attendingKey2.setUser(user2);
            attendingKey2.setMeet(meet);

            MeetAttending meetAttending2 = new MeetAttending();
            meetAttending2.setMeetAttendingKey(attendingKey2);
            meetAttending2.setAttends(false);
            meetAttending2 = meetAttendingRepository.save(meetAttending2);

            Message message = new Message();
            message.setMeet(meet);
            message.setGroup(group1);
            message.setText("Hello world! :)");
            message.setAuthor(user);
            message.setCreationDate(
                    ZonedDateTime.now()
            );
            messageRepository.save(message);

            Message message2 = new Message();
            message2.setMeet(meet);
            message2.setGroup(group1);
            message2.setText("This is a second text!!!");
            message2.setAuthor(user);
            message2.setCreationDate(
                    ZonedDateTime.now()
            );
            messageRepository.save(message2);

            Post post = new Post();
            post.setAuthor(userRole1);
            post.setText("This is a long post... blah blah blabh kjaskldjaskldjaskldj");
            post.setCreationDate(
                    ZonedDateTime.now()
            );
            post.setLastUpdate(
                    ZonedDateTime.now()
            );

            postRepository.save(post);

            Post child = new Post();
            child.setAuthor(userRole4);
            child.setText("This is a post Reply!!!");
            child.setCreationDate(
                    ZonedDateTime.now()
            );
            child.setLastUpdate(
                    ZonedDateTime.now()
            );
            child.setFather(post);

            postRepository.save(child);
        }
    }
}

