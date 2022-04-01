package client_observer;

import model.Client;

public interface Subject {
	public void wantToGetMsg(Client c);

	public void dontWantMsg(Client c);

}
