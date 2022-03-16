package cat.udl.eps.softarch.linkapp.converter;

import cat.udl.eps.softarch.linkapp.domain.UserRole;
import cat.udl.eps.softarch.linkapp.domain.UserRoleKey;
import cat.udl.eps.softarch.linkapp.repository.GroupRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.spi.BackendIdConverter;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MeetAttendingKeyConverter implements BackendIdConverter {

    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupRepository groupRepository;

    @Override
    public Serializable fromRequestId(String id, Class<?> entityType)
    {
        Pattern pattern = Pattern.compile("UserRoleKey_user_id_(\\w+)_group_id_(\\d+)");
        Matcher matcher = pattern.matcher(id);
        String user_id = "";
        long group_id = 0L;
        while (matcher.find()) {
            user_id = matcher.group(1);
            group_id = Long.parseLong(matcher.group(2));
        }
        UserRoleKey roleKey = new UserRoleKey();

        roleKey.setUser(userRepository.findById(user_id).get());
        roleKey.setGroup(groupRepository.findById(group_id).get());

        return roleKey;
    }

    @Override
    public String toRequestId(Serializable source, Class<?> entityType)
    {

        UserRoleKey id = (UserRoleKey) source;
        return id.toString();
    }

    @Override
    public boolean supports(Class<?> type)
    {
        return UserRole.class.equals(type);
    }
}
