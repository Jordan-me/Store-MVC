package view;

import commandPattern.ICommand;
import listeners.StoreUIeventListener;
import model.Product;

public interface AbstractStoreView {
	void registerListener(StoreUIeventListener listener);

	void sendCommand(ICommand command, String mode);

	void show(int i);

	void addProduct(String sKU, Product thePro);

	void replaceProduct(String proCode, Product thePro2Replace);

	void removeProduct(String proCode, Product oldPro);

	void addSell(String proCode, String name, Product product);

}
