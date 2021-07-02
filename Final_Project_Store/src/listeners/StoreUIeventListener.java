package listeners;

import java.util.ListIterator;
import java.util.Map;

import commandPattern.ICommand;
import model.Product;

public interface StoreUIeventListener {
	void setCommand(ICommand c, String mode);

	int getEpenses();

	int getIncome();

	int getNumOfProducts();

	int getNumOfSellsProducts();

	void show(int i);

	Map<String, Product> getMapData();

	void setSortTypeEvent(String sort);

	void addProductEvent(String sku, String name, String costPrice, String sellPrice);

	void removeProductEvent(String sku);

	int getSumIncome();

	String getCode(Product p);

	void setCommandClient(ICommand c, boolean b, String mode);

	Product getProduct(String proCode);

	void setClientForProduct(String phoneInput, ListIterator<String> listIterator);

	boolean isExsistClient(String phoneInput);

	void addClient(String name, String phoneClient, boolean valUpdate);

	void removeClientFromUpdate(String phoneClient);

	void sendMessage(String msg);

}
