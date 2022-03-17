package cat.udl.eps.softarch.linkapp.converter;

import cat.udl.eps.softarch.linkapp.domain.MeetAttending;
import cat.udl.eps.softarch.linkapp.domain.MeetAttendingKey;
import cat.udl.eps.softarch.linkapp.domain.UserRole;
import cat.udl.eps.softarch.linkapp.domain.UserRoleKey;
import cat.udl.eps.softarch.linkapp.repository.GroupRepository;
import cat.udl.eps.softarch.linkapp.repository.MeetRepository;
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
    MeetRepository meetRepository;

    @Override
    public Serializable fromRequestId(String id, Class<?> entityType)
    {
        Pattern pattern = Pattern.compile("MeetAttendingKey_user_id_(\\w+)_meet_id_(\\d+)");
        Matcher matcher = pattern.matcher(id);
        String user_id = "";
        long meet_id = 0L;
        while (matcher.find()) {
            user_id = matcher.group(1);
            meet_id = Long.parseLong(matcher.group(2));
        }
        MeetAttendingKey meetAttendingKey = new MeetAttendingKey(
                meetRepository.findById(meet_id).get(),
                userRepository.findById(user_id).get()
        );


        return meetAttendingKey;
    }

    @Override
    public String toRequestId(Serializable source, Class<?> entityType)
    {

        MeetAttendingKey id = (MeetAttendingKey) source;
        return id.toString();
    }

    @Override
    public boolean supports(Class<?> type)
    {
        return MeetAttending.class.equals(type);
    }
}
