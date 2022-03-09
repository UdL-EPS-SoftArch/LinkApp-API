package cat.udl.eps.softarch.linkapp.repository;

import cat.udl.eps.softarch.linkapp.domain.Meet;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.ZonedDateTime;
import java.util.List;

@RepositoryRestResource
public interface MeetRepository extends PagingAndSortingRepository<Meet, Long> {

  /* Interface provides automatically, as defined in https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/PagingAndSortingRepository.html
   * count, delete, deleteAll, deleteById, existsById, findAll, findAllById, findById, save, saveAll
   *
   * Additional methods following the syntax defined in
   * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
   */

  List<Meet> findByTitleContaining(@Param("title") String title);
  List<Meet> findByStatus(@Param("status") Boolean status);
  List<Meet> findByStatusAndFinalMeetDateLessThan(@Param("status") Boolean status, @Param("finalMeetDate") ZonedDateTime meetDate);

}
