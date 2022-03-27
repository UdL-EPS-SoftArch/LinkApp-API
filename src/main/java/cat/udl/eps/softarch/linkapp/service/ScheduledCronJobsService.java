package cat.udl.eps.softarch.linkapp.service;

import cat.udl.eps.softarch.linkapp.domain.Meet;
import cat.udl.eps.softarch.linkapp.repository.MeetRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class ScheduledCronJobsService {

    private final MeetRepository meetRepository;

    public ScheduledCronJobsService(MeetRepository meetRepository) {
        this.meetRepository = meetRepository;
    }

    @Scheduled(cron = "${meet-status-cron}")
    @Transactional
    protected void updateMeetStatusCronJob() {
        updateMeetStatusJob(meetRepository);
    }

    public static void updateMeetStatusJob(MeetRepository meetRepository) {
        List<Meet> meets = meetRepository.findByStatusAndFinalMeetDateLessThan(true, ZonedDateTime.now());
        for (Meet meet : meets) {
            meet.setStatus(false);
            meetRepository.save(meet);
        }
    }
}
