package commandPattern;

import java.util.Vector;

import javax.swing.JOptionPane;

import javafx.scene.control.ListView;
import listeners.StoreUIeventListener;
import model.Product;

public class BtnAddUndoProductBuyCommand implements ICommand {
	private String proCode;
	private Vector<StoreUIeventListener> allListeners;
	private ListView<String> listCartShopping;
	private String lastItemSaved;

	public BtnAddUndoProductBuyCommand(Vector<StoreUIeventListener> allListeners, String keyProduct,
			ListView<String> listCartShopping) {
		this.proCode = keyProduct;
		this.allListeners = allListeners;
		this.listCartShopping = listCartShopping;
	}

	@Override
	public void execute() {
		Product p = null;
		for (StoreUIeventListener l : allListeners) {
			p = l.getProduct(proCode);
		}
		lastItemSaved = proCode + " " + p.getNameP() + "   " + p.getSellPrice() + "$";
		if (!listCartShopping.getItems().contains(lastItemSaved)) {
			listCartShopping.getItems().add(lastItemSaved);
		} else {

			JOptionPane.showMessageDialog(null, "please choose another product");

		}

	}

	@Override
	public void undo() {
		listCartShopping.getItems().remove(lastItemSaved);

	}

}
