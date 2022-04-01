package model;

import java.io.Serializable;

import memento.ProductMemento;

public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nameP;
	private int costPrice;
	private int sellPrice;
	private Client buyer;

	public Product(String nameP, int costPrice, int sellPrice, Client theClient) throws Exception {
		this.nameP = nameP;
		setCostPrice(costPrice);
		setSellPrice(sellPrice);
		this.buyer = theClient;
	}

	public String getNameP() {
		return nameP;
	}

	public void setNameP(String nameP) {
		this.nameP = nameP;
	}

	public Integer getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(int costPrice) throws Exception {
		if (costPrice < 0) {
			throw new Exception("illegal price");
		}
		this.costPrice = costPrice;
	}

	public Integer getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(int sellPrice) throws Exception {
		if (sellPrice < 0) {
			throw new Exception("illegal price");
		}
		this.sellPrice = sellPrice;
	}

	public Client getBuyer() {
		return buyer;
	}

	public void setBuyer(Client buyer) {
		this.buyer = buyer;
	}

	public ProductMemento createMemento(String code) {
		return new ProductMemento(code, costPrice, sellPrice, nameP, buyer);
	}

	public void restore(ProductMemento memento) {

		if (memento != null) {

			this.costPrice = memento.getCostPrice();
			this.sellPrice = memento.getSellPrice();
			this.nameP = memento.getNameP();
			this.buyer = memento.getBuyer();

		} else {

			System.err.println("Can't restore without memento object!");
		}
	}

	@Override
	public String toString() {
		return "Product [nameP=" + nameP + ", costPrice=" + costPrice + ", sellPrice=" + sellPrice + ", buyer=" + buyer
				+ "]";
	}

}
