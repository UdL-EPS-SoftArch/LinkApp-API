package cat.udl.eps.softarch.linkapp.repository;

import cat.udl.eps.softarch.linkapp.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MeetAttendingRepository extends PagingAndSortingRepository<MeetAttending, MeetAttendingKey>
{
    /* Interface provides automatically, as defined in https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/PagingAndSortingRepository.html
     * count, delete, deleteAll, deleteById, existsById, findAll, findAllById, findById, save, saveAll
     *
     * Additional methods following the syntax defined in
     * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
     */
    Optional<MeetAttending> findByMeetAttendingKeyUserAndMeetAttendingKeyMeet(@Param("user") User user, @Param("meet") Meet meet);
    List<MeetAttending> findByMeetAttendingKeyMeet(@Param("meet") Meet meet);
}
