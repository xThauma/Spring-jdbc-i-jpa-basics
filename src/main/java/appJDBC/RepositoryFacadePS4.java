package appJDBC;

import java.util.List;

public interface RepositoryFacadePS4 {
	public void save(User c);

	public void delete(Long id);

	public void update(User c);

	public int count();

	public List<User> findAll();

	public char[] getHash(char[] password);
}