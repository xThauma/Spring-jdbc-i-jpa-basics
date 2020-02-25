package appJDBC;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private RepositoryFacadePS4 rfi;

	public UserServiceImpl() {
		super();
	}

	@Override
	public void addUser(User user) throws IllegalUsernameException, IncorrectEmailException {
		String email = user.getEmail();
		String username = user.getUsername();
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		if (!email.matches(regex))
			throw new IncorrectEmailException();
		if (!username.matches("[a-zA-Z]+"))
			throw new IllegalUsernameException();
		System.out.println(user.toString());
		user.setPassword(rfi.getHash(user.getPassword()));
		System.out.println(user.getPassword());
		rfi.save(user);

	}

	@Override
	public User getUser(String username) {
		List<User> userList = rfi.findAll();
		for (int i = 0; i < userList.size(); i++) {
			if (userList.get(i).getUsername().equals(username))
				return userList.get(i);
		}
		return null;
	}

	@Override
	public void removeUser(String username) throws IllegalUsernameException {
		if (!username.matches("[a-zA-Z]+"))
			throw new IllegalUsernameException();
		List<User> userList = rfi.findAll();
		int listSize = userList.size();
		for (int i = 0; i < listSize; i++) {
			if (userList.get(i).getUsername().equals(username))
				rfi.delete(userList.get(i).getId());
			return;
		}
	}

	@Override
	public boolean verifyUser(String username, char[] password) {
		List<User> userList = rfi.findAll();
		for (int i = 0; i < userList.size(); i++) {
			char[] shaedPassword = rfi.getHash(password);
			char[] userPassword = userList.get(i).getPassword();
			if (userList.get(i).getUsername().equals(username) && Arrays.equals(shaedPassword, userPassword))
				return true;
		}
		return false;
	}

}
