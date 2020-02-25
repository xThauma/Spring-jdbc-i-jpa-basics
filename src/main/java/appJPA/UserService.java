package appJPA;

public interface UserService {
	class IllegalUsernameException extends RuntimeException {
	}

	class IncorrectEmailException extends RuntimeException {
	}

	void addUser(User2 user) throws IllegalUsernameException, IncorrectEmailException;

	User2 getUser(String username);

	void removeUser(String username) throws IllegalUsernameException;

	boolean verifyUser(String username, char[] password);
	
	
}