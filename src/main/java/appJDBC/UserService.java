package appJDBC;

public interface UserService {
	class IllegalUsernameException extends RuntimeException {
	}

	class IncorrectEmailException extends RuntimeException {
	}

	void addUser(User user) throws IllegalUsernameException, IncorrectEmailException;

	User getUser(String username);

	void removeUser(String username) throws IllegalUsernameException;

	boolean verifyUser(String username, char[] password);
	
	
}