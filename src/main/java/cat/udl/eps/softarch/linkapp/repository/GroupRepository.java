package cat.udl.eps.softarch.linkapp.repository;

import cat.udl.eps.softarch.linkapp.domain.Group;
import cat.udl.eps.softarch.linkapp.domain.Meet;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface GroupRepository extends PagingAndSortingRepository<Group, Long> {

  /* Interface provides automatically, as defined in https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/PagingAndSortingRepository.html
   * count, delete, deleteAll, deleteById, existsById, findAll, findAllById, findById, save, saveAll
   *
   * Additional methods following the syntax defined in
   * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
   */
}
