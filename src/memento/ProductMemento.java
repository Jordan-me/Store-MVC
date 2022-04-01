package memento;

import model.Client;

//memento
public class ProductMemento {
	protected String code;
	protected int costPrice;
	protected int sellPrice;
	protected String nameP;
	protected Client buyer;

	public ProductMemento(String code, int costPrice2, int sellPrice2, String nameP, Client buyer) {
		super();
		this.code = code;
		this.costPrice = costPrice2;
		this.sellPrice = sellPrice2;
		this.nameP = nameP;
		this.buyer = buyer;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getNameP() {
		return nameP;
	}

	public void setNameP(String nameP) {
		this.nameP = nameP;
	}

	public int getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(int costPrice) {
		this.costPrice = costPrice;
	}

	public int getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(int sellPrice) {
		this.sellPrice = sellPrice;
	}

	public Client getBuyer() {
		return buyer;
	}

	public void setBuyer(Client buyer) {
		this.buyer = buyer;
	}

	@Override
	public String toString() {
		return "ProductMemento [costPrice=" + costPrice + ", sellPrice=" + sellPrice + ", nameP=" + nameP + ", buyer="
				+ buyer + "]";
	}

}
