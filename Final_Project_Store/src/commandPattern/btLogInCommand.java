package commandPattern;

import java.util.Vector;

import javax.swing.JOptionPane;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import listeners.StoreUIeventListener;

public class btLogInCommand extends Button implements ICommand {

	private final String USER = "Arnon";
	private final String PASSWORD = "Arnon";
	private PasswordField password;
	private Button logIn;
	private TextField user;
	private Button signIn;
	private Stage theStage;
	private Vector<StoreUIeventListener> allListeners = new Vector<StoreUIeventListener>();

	public btLogInCommand(Button signIn, Button btLogin, TextField tfUser, PasswordField password,
			Vector<StoreUIeventListener> allListeners) {
		this(allListeners);
		this.logIn = btLogin;
		this.password = password;
		this.user = tfUser;
		this.signIn = signIn;
		this.theStage = (Stage) btLogin.getScene().getWindow();
	}

	public btLogInCommand(Vector<StoreUIeventListener> allListeners) {
		this.allListeners = allListeners;
	}

	@Override
	public void execute() {

		if (!(user.getText().equals(USER) && password.getText().equals(PASSWORD))) {
			JOptionPane.showMessageDialog(null, "incorrect details");
			return;
		} else {

			signIn.setText("Log out");
			theStage.hide();

		}
		for (StoreUIeventListener l : allListeners) {
			l.show(2);
		}
	}

	@Override
	public void undo() {
		signIn.setText("Log In");

		for (StoreUIeventListener l : allListeners) {
			l.show(1);
		}
	}

}
