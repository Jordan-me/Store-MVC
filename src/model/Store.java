package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.Vector;

import client_observer.Sender;
import comparatorSorting.ComaratorBigToSmallSKU;
import comparatorSorting.ComaratorSmallToBigSKU;
import files_Iterator.ProductsFile;
import listeners.StoreEventListener;
import memento.History;
import memento.ProductMemento;

public class Store {

	private ProductsFile theFile;
	private Vector<StoreEventListener> allListeners;
	private Map<String, Product> productMap;
	private Map<String, Client> clienttMap;
	private History careTaker;
	private sortType sortType;
	private int totalNumSells;
	private int totalSellMoney;
	private int totalCostMoney;
	private int totalNumProducts;

	public Store() throws FileNotFoundException, IOException {
		theFile = new ProductsFile();
		allListeners = new Vector<StoreEventListener>();
		clienttMap = new LinkedHashMap<>();
		careTaker = new History();
		totalSellMoney = 0;
		totalNumSells = 0;
		totalCostMoney = 0;
		totalNumProducts = 0;
		theFile.loadDataFromFile(this);
	}

	public Store(sortType sortType) throws FileNotFoundException, IOException {
		this();
		this.sortType = sortType;
		setProductMap();

	}

	public void registerListener(StoreEventListener listen) {
		allListeners.add(listen);
	}

	// add + screenshot on the situation
	public void add(String SKU, Product thePro) {
		if (!validProduct(SKU, thePro)) {
			return;
		}

		if (productMap.containsKey(SKU)) {
			Product oldPro = productMap.get(SKU);
			if ((oldPro.getBuyer() == null) && (thePro.getBuyer() != null)) {
				totalSellMoney += thePro.getSellPrice();
				totalNumSells++;

			} else if ((oldPro.getBuyer() != null) && (thePro.getBuyer() == null)) {
				totalSellMoney -= oldPro.getSellPrice();
				totalNumSells--;
			} else if ((oldPro.getBuyer() != null) && (thePro.getBuyer() != null)) {
				totalSellMoney = totalSellMoney - oldPro.getSellPrice() + thePro.getSellPrice();
			}

			totalCostMoney = totalCostMoney - oldPro.getCostPrice() + thePro.getCostPrice();
			productMap.replace(SKU, thePro);
			careTaker.replace(thePro.createMemento(SKU), oldPro.createMemento(SKU));
			theFile.replaceProductInFile(SKU, thePro);

			fireReplaceProductUI(SKU, thePro);
			return;
		} else {
			if (thePro.getBuyer() != null) {
				totalSellMoney = totalSellMoney + thePro.getSellPrice();
				totalNumSells++;
			}
		}
		totalCostMoney += thePro.getCostPrice();
		productMap.put(SKU, thePro);
		totalNumProducts++;
		careTaker.push(thePro.createMemento(SKU));
		fireAddProductToUI(SKU, thePro);
		Entry<String, Product> prevProduct = getPrevPro(SKU);
		if (sortType.equals(sortType.eINSERT_ORDER)) {
			theFile.writeToFileProduct(SKU, thePro);

			return;
		}
		if (prevProduct != null) {
			theFile.addProductToFile(prevProduct.getKey(), SKU, thePro);

		} else {
			theFile.addProductToFile(null, SKU, thePro);
		}
	}

	public Map.Entry<String, Product> getPrevPro(String sKU) {
		Iterator<Map.Entry<String, Product>> iterator = productMap.entrySet().iterator();
		Entry<String, Product> pPrev = null;
		Entry<String, Product> pNext;

		while (iterator.hasNext()) {
			pNext = iterator.next();
			if (pNext.getKey().equals(sKU)) {
				return pPrev;
			}
			pPrev = pNext;
		}
		return null;
	}

	public void fireAddProductToUI(String SKU, Product thePro) {
		for (StoreEventListener l : allListeners) {
			l.addProductToUI(SKU, thePro);
		}

	}

	public boolean validProduct(String SKU, Product thePro) {
		if (SKU.isEmpty() || thePro.getNameP().isEmpty()
				|| (thePro.getCostPrice() == null) | (thePro.getCostPrice() == null)) {
			fireSetErrorInput("Fill in all the fields you are asked to fill in");
			return false;
		}
		return true;
	}

	private void fireSetErrorInput(String msg) {
		for (StoreEventListener l : allListeners) {
			l.setErrorMassegesToUI(msg);
		}
	}

	// back to the last screenshot
	public void undo() {
		try {
			ProductMemento arr[] = careTaker.pop();
			String proCode = arr[0].getCode();
			Product oldPro = productMap.get(proCode);
			if (arr[1] != null) {
				Product thePro2Replace;
				thePro2Replace = new Product(arr[1].getNameP(), arr[1].getSellPrice(), arr[1].getCostPrice(),
						arr[1].getBuyer());
				totalCostMoney += thePro2Replace.getCostPrice() - oldPro.getCostPrice();
				if (oldPro.getBuyer() != null) {
					totalSellMoney = totalNumSells - oldPro.getSellPrice();
					totalNumSells--;
				}
				if (thePro2Replace.getBuyer() != null) {
					totalSellMoney += thePro2Replace.getSellPrice();
					totalNumSells++;
				}
				productMap.replace(proCode, thePro2Replace);
				theFile.replaceProductInFile(proCode, thePro2Replace);

				fireReplaceProductUI(proCode, thePro2Replace);
				return;
			}
			undo(proCode);
		} catch (Exception e) {
			fireSetErrorInput("Oops... something is wrong with your product\n(negative price is an error)");
		}
	}

	public void undo(String proCode) {
		Product oldPro = productMap.get(proCode);
		if(oldPro== null) {
			fireSetErrorInput("code incorrect");
			return;
		}
		totalCostMoney -= oldPro.getCostPrice();
		careTaker.remove(proCode);
		if (oldPro.getBuyer() != null) {
 
			totalSellMoney = totalSellMoney - oldPro.getSellPrice();
			totalNumSells--;
		}
		theFile.removeProsuctFromFile(proCode);
		totalNumProducts--;
		fireRemoveProductFromUI(proCode, productMap.remove(proCode));
	}

	public void fireRemoveProductFromUI(String proCode, Product oldPro) {
		for (StoreEventListener l : allListeners) {
			l.removeProductFromUI(proCode, oldPro);
		}

	}

	public void fireReplaceProductUI(String proCode, Product thePro2Replace) {
		for (StoreEventListener l : allListeners) {
			l.replaceProductUI(proCode, thePro2Replace);
		}

	}

	// set kind of sorting
	public void setProductMap() {
		NavigableMap<String, Product> orderedMap;
		switch (sortType) {
		case eA_TO_Z:
			orderedMap = new TreeMap<>(new ComaratorSmallToBigSKU());
			productMap = orderedMap;
			break;
		case eZ_TO_A:
			orderedMap = new TreeMap<>(new ComaratorBigToSmallSKU());
			productMap = orderedMap;
			break;
		case eINSERT_ORDER:
			productMap = new LinkedHashMap<>();
		}
	}

	public Map<String, Product> getProductMap() {
		return productMap;
	}

	public void setProductMap(Map<String, Product> productMap) {
		this.productMap = productMap;
	}

	public sortType getSortType() {
		return sortType;

	}

	public void setSortType(sortType sortType) {
		this.sortType = sortType;
		setProductMap();
	}

	public void writeSortType(sortType sortType) {
		setSortType(sortType);
		theFile.writeToFileSortType(sortType);

	}

	public String toString() {
		return productMap.toString();
	}

	public int getTotalSellMoney() {
		return totalSellMoney;
	}

	public int getTotalNumSells() {
		return totalNumSells;
	}

	public int getTotalCostMoney() {
		return totalCostMoney;
	}

	public int getTotalNumProducts() {

		return totalNumProducts;
	}

	public int getTotalIncome() {

		return totalSellMoney - totalCostMoney;
	}

	public String getProductCode(Product p) {
		for (String key : productMap.keySet()) {
			if (productMap.get(key).equals(p)) {
				return key;
			}
		}
		return null;
	}

	public Product getProductCode(String proCode) {
		return productMap.get(proCode);
	}

	public void setProductClient(String phoneInput, Product product, String proCode) {
		Client c = clienttMap.get(phoneInput);
		product.setBuyer(c);

		addSell(proCode, c.getName(), product);

	}

	public void addSell(String proCode, String name, Product product) {
		totalSellMoney += product.getSellPrice();
		totalNumSells++;
		fireAddSellToUI(proCode, name, product);
		theFile.replaceProductInFile(proCode, product);

	}

	public void fireAddSellToUI(String proCode, String name, Product product) {
		for (StoreEventListener l : allListeners) {

			l.addSellToUI(proCode, name, product);
		}

	}

	public boolean isClientExists(String phoneInput) {

		return clienttMap.containsKey(phoneInput);
	}

	public void addClientToDB(String phoneClient, Client c, boolean msg) {
		if (!clienttMap.containsKey(phoneClient)) {
			clienttMap.put(phoneClient, c);
			if (c.isGetUpdate()) {
				Sender.getInstance().wantToGetMsg(c);
			}
			if (msg) {
				fireSetErrorInput("Congratulations!\r\n" + "You have joined to become a club member");
			}
		} else {
			fireSetErrorInput("Already registered in the past");
		}

	}

	public void removeClient(String phoneClient) {
		Client c;
		if (clienttMap.containsKey(phoneClient)) {
			c = clienttMap.get(phoneClient);
			c.setGetUpdate(false);
			Sender.getInstance().dontWantMsg(c);

			fireSetErrorInput("You have been successfully removed");
		} else {
			fireSetErrorInput("You are not on our customer list");
		}

	}

	public void setTotalNumSells(int totalNumSells) {
		this.totalNumSells = totalNumSells;
	}

	public void setTotalSellMoney(int totalSellMoney) {
		this.totalSellMoney = totalSellMoney;
	}

	public void setTotalCostMoney(int totalCostMoney) {
		this.totalCostMoney = totalCostMoney;
	}

	public void setTotalNumProducts(int totalNumProducts) {
		this.totalNumProducts = totalNumProducts;
	}

}
