package listeners;

import model.Product;

public interface StoreEventListener {

	void setErrorMassegesToUI(String msg);

	void addProductToUI(String sKU, Product thePro);

	void removeProductFromUI(String proCode, Product oldPro);

	void replaceProductUI(String proCode, Product thePro2Replace);

	void addSellToUI(String proCode, String name, Product product);

}
