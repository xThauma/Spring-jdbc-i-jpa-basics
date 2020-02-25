package appJPA;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
	public void addUser(User2 user) throws IllegalUsernameException, IncorrectEmailException {
		String email = user.getEmail();
		String username = user.getUsername();
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		if (!email.matches(regex))
			throw new IncorrectEmailException();
		if (!username.matches("[a-zA-Z]+"))
			throw new IllegalUsernameException();
		System.out.println(user.toString());
		user.setPassword(getHash(user.getPassword()));
		System.out.println(user.getPassword());
		rfi.save(user);

	}

	@Override
	public User2 getUser(String username) {
		List<User2> userList = (List<User2>) rfi.findAll();
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
		List<User2> userList = (List<User2>) rfi.findAll();
		int listSize = userList.size();
		for (int i = 0; i < listSize; i++) {
			if (userList.get(i).getUsername().equals(username))
				rfi.delete(userList.get(i));
			return;
		}
	}

	@Override
	public boolean verifyUser(String username, char[] password) {
		List<User2> userList =(List<User2>) rfi.findAll();
		for (int i = 0; i < userList.size(); i++) {
			char[] shaedPassword = getHash(password);
			char[] userPassword = userList.get(i).getPassword();
			if (userList.get(i).getUsername().equals(username) && Arrays.equals(shaedPassword, userPassword))
				return true;
		}
		return false;
	}
	

	public char[] getHash(char[] s) {
		String password = "";
		for (int i = 0; i < s.length; i++) {
			password += s[i];
		}
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash;
			hash = digest.digest(password.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			password = hexString.toString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return password.toCharArray();
	}

}
