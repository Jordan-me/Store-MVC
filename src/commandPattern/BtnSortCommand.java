package commandPattern;

import java.util.Vector;

import javax.swing.JOptionPane;

import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import listeners.StoreUIeventListener;

public class BtnSortCommand extends Button implements ICommand {
	private MenuItem btnOpSort;
	private MenuButton btnSort;
	private Vector<StoreUIeventListener> allListeners = new Vector<StoreUIeventListener>();

	public BtnSortCommand(MenuButton menuButtonSort, MenuItem menuItem, Vector<StoreUIeventListener> allListeners) {
		this.btnSort = menuButtonSort;
		this.btnOpSort = menuItem;
		this.allListeners = allListeners;

	}

	@Override
	public void execute() {
		for (StoreUIeventListener l : allListeners) {
			l.setSortTypeEvent(btnOpSort.getText());
		}
		btnSort.setVisible(false);
		JOptionPane.showMessageDialog(null, "The sort selection " + btnOpSort.getText() + " passed successfully");

	}

	@Override
	public void undo() {

	}

}
