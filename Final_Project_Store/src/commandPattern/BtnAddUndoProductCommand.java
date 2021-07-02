package commandPattern;

import java.util.Vector;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import listeners.StoreUIeventListener;

public class BtnAddUndoProductCommand extends Button implements ICommand {
	private Vector<StoreUIeventListener> allListeners = new Vector<StoreUIeventListener>();
	private Button btnSave;
	private TextField tfSKU;
	private TextField tfname;
	private TextField tfCostPrice;
	private TextField tfSellPrice;
	private Button btnUndo;

	public BtnAddUndoProductCommand(Vector<StoreUIeventListener> allListeners, Button btnSave, Button btnUndo,
			TextField tfSKU, TextField tfname, TextField tfCostPrice, TextField tfSellPrice) {
		this.allListeners = allListeners;
		this.btnSave = btnSave;
		this.btnUndo = btnUndo;
		this.tfSKU = tfSKU;
		this.tfname = tfname;
		this.tfCostPrice = tfCostPrice;
		this.tfSellPrice = tfSellPrice;
	}

	@Override
	public void execute() {
		for (StoreUIeventListener l : allListeners) {
			l.addProductEvent(tfSKU.getText(), tfname.getText(), tfCostPrice.getText(), tfSellPrice.getText());
		}
	}

	@Override
	public void undo() {
		for (StoreUIeventListener l : allListeners) {
			l.removeProductEvent(null);
		}

	}

}
