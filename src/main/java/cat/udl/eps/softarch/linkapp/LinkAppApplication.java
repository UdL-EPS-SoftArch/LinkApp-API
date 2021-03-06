package cat.udl.eps.softarch.linkapp;

import cat.udl.eps.softarch.linkapp.domain.Meet;
import cat.udl.eps.softarch.linkapp.repository.MeetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class LinkAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinkAppApplication.class, args);
    }

}
