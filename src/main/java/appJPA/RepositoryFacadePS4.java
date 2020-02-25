package appJPA;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface RepositoryFacadePS4 extends CrudRepository<User2, Long> {
	List<User2> findByUsername(String username);
	
}