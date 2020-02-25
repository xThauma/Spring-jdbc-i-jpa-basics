package appJDBC;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import appJDBC.UserService.IllegalUsernameException;
import appJDBC.UserService.IncorrectEmailException;

@Controller
public class Application {
	@Autowired
	UserService userService;
	Scanner s;

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
		context.registerShutdownHook();
		Application app = context.getBean("application", Application.class);
		app.run();

	}

	private void run() {
		s = new Scanner(System.in);
		int choiceEntry = -1;

		while (choiceEntry < 5) {
			makeMenu();
			System.out.print("Wybierz opcje: ");
			choiceEntry = Integer.parseInt(s.nextLine());
			switch (choiceEntry) {
			case 1:
				addUser();
				break;
			case 2:
				getUser();
				break;
			case 3:
				removeUser();
				break;
			case 4:
				verifyUser();
				break;
			}

		}
		s.close();
	}

	private void makeMenu() {
		System.out.println("\n-------------------------");
		System.out.println("1. Dodaj użytkownika");
		System.out.println("2. Wyszukaj użytkowniak");
		System.out.println("3. Usuń użytkownika");
		System.out.println("4. Zweryfikuj użytkownika");
		System.out.println("-------------------------");
	}

	private void addUser() {
		System.out.print("Nazwa użytkownika: ");
		String username = s.nextLine();
		System.out.print("hasło użytkownika: ");
		String password = s.nextLine();
		System.out.print("imię użytkownika: ");
		String name = s.nextLine();
		System.out.print("email użytkownika: ");
		String email = s.nextLine();
		try {
			// userService.addUser(new User("kamil123", "password".toCharArray(), "kamil",
			// "kamil@gmail.com"));
			userService.addUser(new User(username, password.toCharArray(), name, email));
		} catch (IllegalUsernameException iue) {
			System.out.println("Zła nazwa użytkownika");
			return;
		} catch (IncorrectEmailException iee) {
			System.out.println("Zły email użytkownika");
			return;
		}
	}

	private void getUser() {
		System.out.print("Nazwa użytkownika: ");
		String username = s.nextLine();
		User user = userService.getUser(username);
		if (user == null)
			System.out.println("Nie ma użytkownika o takiej nazwie");
		else
			System.out.println("Użytkownik to: " + user);
	}

	private void removeUser() {
		System.out.print("Nazwa użytkownika: ");
		String username = s.nextLine();
		try {
			userService.removeUser(username);
		} catch (IllegalUsernameException e) {
			System.out.println("Podany użytkownik nie istnieje");
			return;
		}
		System.out.println("Podany użytkownik został skasowany lub nie istnieje");
	}

	private void verifyUser() {
		System.out.print("Nazwa użytkownika: ");
		String username = s.nextLine();
		System.out.print("hasło użytkownika: ");
		String password = s.nextLine();
		if (userService.verifyUser(username, password.toCharArray())) {
			System.out.println("Użytkownik zweryfikowany");
		} else
			System.out.println("Odmowa autoryzacji");
	}

}
