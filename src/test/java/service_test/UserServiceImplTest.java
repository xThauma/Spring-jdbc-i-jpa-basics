package service_test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import appJDBC.User;
import appJDBC.UserService;
import appJPA.Application;

public class UserServiceImplTest {
	@Autowired
	UserService userService;

	@BeforeEach
	public void setup() {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"beans2_test.xml"});    
        context.registerShutdownHook();
        Application app = context.getBean(Application.class); 

	}

	@Test
	public void test1() {
		assertThrows(Exception.class, () -> {
			userService.addUser(new User(1L, "Kamiluser", "password".toCharArray(), "kamil", "kamilmail.com"));
		});
	}

	@Test
	public void test2() {
		assertThrows(Exception.class, () -> {
			userService.addUser(new User(1L, "Kamiluser12", "password".toCharArray(), "kamil", "kamil@gmail.com"));
		});
	}

	@Test
	public void test3() {
		userService.addUser(new User(1L, "Kamiluser", "password".toCharArray(), "kamil", "kamil@gmail.com"));
		assertEquals(userService.verifyUser("Kamiluser", "password".toCharArray()), true);
	}

	@Test
	public void test4() {
		userService.addUser(new User(1L, "Kamiluser", "password".toCharArray(), "kamil", "kamil@gmail.com"));
		assertEquals(userService.verifyUser("Kamiluse", "password".toCharArray()), false);
	}

	@Test
	public void test5() {
		assertThrows(Exception.class, () -> {
			userService.removeUser("Kamil123");
		});
	}

	@Test
	public void test6() {
		userService.addUser(new User(1L, "Kamiluser", "password".toCharArray(), "kamil", "kamil@gmail.com"));
		assertEquals(userService.getUser("Kamiluserr"), null);
	}

	@Test
	public void test7() {
		userService.addUser(new User(1L, "Kamiluser", "password".toCharArray(), "kamil", "kamil@gmail.com"));
		assertNotEquals(userService.getUser("Kamiluser"), null);
	}
}
