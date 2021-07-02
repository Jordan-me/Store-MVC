package commandPattern;

import java.util.Vector;

import javax.swing.JOptionPane;

import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.stage.Stage;
import listeners.StoreUIeventListener;
import view.LogInDisplay;

public class BtnConnectCommand extends Button implements ICommand {
	Button btLogIn;
	MenuButton btnSort;
	private Vector<StoreUIeventListener> allListeners = new Vector<StoreUIeventListener>();
	private Button btnRemoveSKU;
	private Button btnSendMsg;

	public BtnConnectCommand(Button btLogIn, Vector<StoreUIeventListener> allListeners, MenuButton menuButtonSort,
			Button btnRemoveSKU, Button btnSendMsg) {
		this(allListeners);
		this.btLogIn = btLogIn;
		this.btnSort = menuButtonSort;
		this.btnRemoveSKU = btnRemoveSKU;
		this.btnSendMsg = btnSendMsg;

	}

	public BtnConnectCommand(Vector<StoreUIeventListener> allListeners) {
		this.allListeners = allListeners;

	}

	@Override
	public void undo() {
		JOptionPane.showMessageDialog(null, "Bye- have a nice day");
		btLogIn.setText("Log In");
		btnRemoveSKU.setVisible(false);
		btnSendMsg.setVisible(false);

		for (StoreUIeventListener l : allListeners) {
			l.show(1);
		}

	}

	@Override
	public void execute() {
		if (!btnSort.isVisible()) {
			LogInDisplay loginDis = new LogInDisplay(new Stage(), btLogIn, allListeners);
		} else {
			JOptionPane.showMessageDialog(null, "It's the system is empty- please choose sort type for your products");
		}

	}

}
