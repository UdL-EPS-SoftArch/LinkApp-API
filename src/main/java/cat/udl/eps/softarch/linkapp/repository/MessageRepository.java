package cat.udl.eps.softarch.linkapp.repository;

import cat.udl.eps.softarch.linkapp.domain.Message;
import cat.udl.eps.softarch.linkapp.domain.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface MessageRepository extends PagingAndSortingRepository<Message, Integer> {
  List<Message> findByUser(User user);
  public Message findByIdContaining(@Param("number") Integer number);
}