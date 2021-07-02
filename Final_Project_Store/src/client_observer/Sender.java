package client_observer;

import java.util.ArrayList;

import model.Client;

public class Sender implements Subject {
	private static Sender _instance = new Sender();
	private ArrayList<Observer> observers = new ArrayList<>();
	private ArrayList<String> observersAswered = new ArrayList<>();

	public static Sender getInstance() {
		if (_instance == null)
			_instance = new Sender();
		return _instance;
	}

	public void wantToGetMsg(Client c) {
		observers.add(c);

	}

	@Override
	public void dontWantMsg(Client c) {
		observers.remove(c);

	}

	public void update(String msg) {
		for (Observer o : observers) {
			observersAswered.add(o.observerUpdate(msg));
		}
	}

	public ArrayList<String> getAnswer() {
		return observersAswered;
	}

	public void removeAnswer() {
		observersAswered.removeAll(observersAswered);
	}
}
