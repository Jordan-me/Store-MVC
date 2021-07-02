package view;

import java.util.Vector;

import commandPattern.ICommand;
import commandPattern.btLogInCommand;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import listeners.StoreUIeventListener;
import model.Product;

public class LogInDisplay implements AbstractStoreView {
	private final int ID = 0;
	private Vector<StoreUIeventListener> allListeners = new Vector<StoreUIeventListener>();
	private Stage theStage;
	private Label lUser;
	private TextField tfUser;
	private Label lPassword;
	private PasswordField password;
	private GridPane gpRoot;
	private Button btLogin;
	private Scene scene;
	private Button signIn;

	public LogInDisplay(Stage theStage, Button signIn, Vector<StoreUIeventListener> allListeners) {
		this.allListeners = allListeners;
		this.signIn = signIn;
		gpRoot = new GridPane();
		this.theStage = theStage;
		lUser = new Label("User");
		tfUser = new TextField();
		btLogin = new Button("Login");
		btLogin.setOnAction(
				e -> sendCommand(new btLogInCommand(signIn, btLogin, tfUser, password, allListeners), "Log"));
		tfUser.setMinSize(100, 20);
		lPassword = new Label("Password");
		password = new PasswordField();
		password.setMinSize(100, 20);

		lUser.setPadding(new Insets(10));
		lPassword.setPadding(new Insets(10));
		gpRoot.setRowIndex(lUser, 1);
		gpRoot.setColumnIndex(lUser, 1);
		gpRoot.setRowIndex(tfUser, 1);
		gpRoot.setColumnIndex(tfUser, 3);
		gpRoot.setRowIndex(lPassword, 2);
		gpRoot.setColumnIndex(lPassword, 1);
		gpRoot.setRowIndex(password, 2);
		gpRoot.setColumnIndex(password, 3);
		gpRoot.setRowIndex(btLogin, 3);
		gpRoot.setColumnIndex(btLogin, 3);

		gpRoot.getChildren().addAll(lUser, tfUser, lPassword, password, btLogin);

		scene = new Scene(gpRoot, 350, 120);
		theStage.setScene(scene);
		theStage.setResizable(false);
		theStage.show();

	}

	@Override
	public void registerListener(StoreUIeventListener listen) {
		allListeners.add(listen);

	}

	@Override
	public void sendCommand(ICommand command, String mode) {
		for (StoreUIeventListener l : allListeners) {
			l.setCommand(command, mode);
		}

	}

	@Override
	public void show(int i) {
		if (i == ID) {
			theStage.show();
		} else {
			theStage.hide();
		}

	}

	@Override
	public void addProduct(String sKU, Product thePro) {

	}

	@Override
	public void replaceProduct(String proCode, Product thePro2Replace) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeProduct(String proCode, Product oldPro) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addSell(String proCode, String name, Product product) {
		// TODO Auto-generated method stub

	}

}
