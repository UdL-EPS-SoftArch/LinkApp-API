package cat.udl.eps.softarch.linkapp.repository;

import cat.udl.eps.softarch.linkapp.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRoleRepository extends PagingAndSortingRepository<UserRole, Long>{
    /* Interface provides automatically, as defined in https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/PagingAndSortingRepository.html
     * count, delete, deleteAll, deleteById, existsById, findAll, findAllById, findById, save, saveAll
     *
     * Additional methods following the syntax defined in
     * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
     */
    List<UserRole> findByRoleKeyUser(@Param("user") User user);
    List<UserRole> findByRoleKeyGroup(@Param("group") Group group);
    UserRole findByRoleKeyUserAndRoleKeyGroup(@Param("user") User user, @Param("group") Group group);
    List<UserRole> findByRoleKeyGroupAndRole(@Param("group") Group group, @Param("role") UserRoleEnum role);
}
