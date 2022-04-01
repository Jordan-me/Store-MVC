package commandPattern;

import java.util.Vector;

import javax.swing.JOptionPane;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import listeners.StoreUIeventListener;

public class BtnAddUndoClientCommand implements ICommand {
	private TextField tfNameClient;

	private CheckBox cbClientUpdate;
	private String phoneClient;
	private Vector<StoreUIeventListener> allListeners;

	public BtnAddUndoClientCommand(TextField tfNameClient, TextField tfPhoneClient, CheckBox cbClientUpdate,
			Vector<StoreUIeventListener> allListeners) {
		this(tfPhoneClient.getText(), allListeners);
		this.cbClientUpdate = cbClientUpdate;
		this.tfNameClient = tfNameClient;

	}

	public BtnAddUndoClientCommand(String phoneClient, Vector<StoreUIeventListener> allListeners) {
		this.allListeners = allListeners;
		this.phoneClient = phoneClient;
	}

	@Override
	public void execute() {
		String name = tfNameClient.getText();

		boolean valUpdate = cbClientUpdate.isSelected();
		if (name.isEmpty() || phoneClient.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Please fill in your details in the fields - name and phone number");
		} else {
			for (StoreUIeventListener l : allListeners) {
				l.addClient(name, phoneClient, valUpdate);

			}
		}

	}

	@Override
	public void undo() {
		for (StoreUIeventListener l : allListeners) {
			l.removeClientFromUpdate(phoneClient);

		}

	}

}
