package controller;

import java.util.ListIterator;
import java.util.Map;

import javax.swing.JOptionPane;

import client_observer.Sender;
import commandPattern.BtnConnectCommand;
import commandPattern.BtnShopCommand;
import commandPattern.CommandManager;
import commandPattern.ICommand;
import commandPattern.btLogInCommand;
import listeners.StoreEventListener;
import listeners.StoreUIeventListener;
import model.Client;
import model.Product;
import model.Store;
import model.sortType;
import view.AbstractStoreView;

public class StoreController implements StoreEventListener, StoreUIeventListener {
	private Store storeModel;
	private AbstractStoreView storeView;
	private CommandManager cmd;

	public StoreController(AbstractStoreView view, Store model) {
		storeView = view;
		storeModel = model;
		setListenerView(storeView);
		storeModel.registerListener(this);
		cmd = new CommandManager();
	}

	@Override
	public void setCommand(ICommand c, String mode) {
		if (c instanceof btLogInCommand) {
			btLogInCommand com = (btLogInCommand) c;
			com.execute();
			return;
		}
		if (mode.equals("Log")) {
			cmd.executeCommand(c, false);
			return;
		} else if ((c instanceof BtnConnectCommand) && mode.equals("Undo")) {
			cmd.undo(true);
			return;
		}
		if (mode.equals("Undo")) {
			if (cmd.isUndoAvailable()) {
				cmd.undo(false);
			} else {
				setErrorMassegesToUI("You have nowhere to go back");
			}
		} else {
			cmd.executeCommand(c, true);
		}

	}

	@Override
	public int getEpenses() {
		return storeModel.getTotalCostMoney();
	}

	@Override
	public int getIncome() {
		return storeModel.getTotalSellMoney();
	}

	@Override
	public int getNumOfProducts() {

		return storeModel.getTotalNumProducts();
	}

	@Override
	public int getNumOfSellsProducts() {
		return storeModel.getTotalNumSells();
	}

	public void setListenerView(AbstractStoreView view) {
		view.registerListener(this);
	}

	@Override
	public void show(int i) {
		storeView.show(i);

	}

	@Override
	public Map<String, Product> getMapData() {
		return storeModel.getProductMap();
	}

	@Override
	public void setSortTypeEvent(String sortName) {
		storeModel.writeSortType(sortType.getSortType(sortName));

	}

	@Override
	public void addProductEvent(String sku, String name, String costPrice, String sellPrice) {
		int sellFee = Integer.parseInt(sellPrice);
		int costFee = Integer.parseInt(costPrice);
		Product p;

		try {
			p = new Product(name, costFee, sellFee, null);
			storeModel.add(sku, p);
		} catch (Exception e) {
			setErrorMassegesToUI(e.getMessage());
		}
	}

	@Override
	public void setErrorMassegesToUI(String msg) {
		JOptionPane.showMessageDialog(null, msg);

	}

	@Override
	public void addProductToUI(String sKU, Product thePro) {
		storeView.addProduct(sKU, thePro);
	}

	@Override
	public void removeProductEvent(String sku) {
		if (sku == null) {
			storeModel.undo();

		} else if (sku.isEmpty()) {
			setErrorMassegesToUI("Enter code!!!");

		} else {

			storeModel.undo(sku);
		}

	}

	@Override
	public void removeProductFromUI(String proCode, Product oldPro) {

		storeView.removeProduct(proCode, oldPro);

	}

	@Override
	public void replaceProductUI(String proCode, Product thePro2Replace) {
		storeView.replaceProduct(proCode, thePro2Replace);

	}

	@Override
	public int getSumIncome() {
		return storeModel.getTotalIncome();
	}

	@Override
	public String getCode(Product p) {
		return storeModel.getProductCode(p);

	}

	@Override
	public void setCommandClient(ICommand c, boolean b, String mode) {
		if (mode.equals("Undo")) {
			if (!b) {
				cmd.undoClient(b);
				return;
			}
			if (cmd.isUndoClientAvailable()) {
				cmd.undoClient(b);
			} else {
				setErrorMassegesToUI("You have nowhere to go back");
			}
		} else {
			cmd.executeCommandClient(c, b);
		}

	}

	@Override
	public Product getProduct(String proCode) {
		return storeModel.getProductCode(proCode);

	}

	@Override
	public void setClientForProduct(String phoneInput, ListIterator<String> listIterator) {
		String[] str;
		while (listIterator.hasNext()) {
			str = listIterator.next().split(" ");
			storeModel.setProductClient(phoneInput, getProduct(str[0]), str[0]);
		}

	}

	@Override
	public boolean isExsistClient(String phoneInput) {
		return storeModel.isClientExists(phoneInput);
	}

	@Override
	public void addClient(String name, String phoneClient, boolean valUpdate) {
		Client c = new Client(name, phoneClient, valUpdate);
		storeModel.addClientToDB(phoneClient, c, true);

	}

	@Override
	public void removeClientFromUpdate(String phoneClient) {
		storeModel.removeClient(phoneClient);

	}

	@Override
	public void addSellToUI(String proCode, String name, Product product) {
		storeView.addSell(proCode, name, product);

	}

	@Override
	public void sendMessage(String msg) {
		Sender.getInstance().update(msg);

	}

}
