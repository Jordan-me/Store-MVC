package main;

import controller.StoreController;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Store;
import view.AbstractStoreView;
import view.MainDisplay;

public class Main extends Application {
	public static void main(String[] args) {

		Application.launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		System.out.println("Here we go...");

		Store theStore = new Store();
		AbstractStoreView theMainView;
		if (theStore.getProductMap() == null) {
			theMainView = new MainDisplay(new Stage(), true);
		} else {
			theMainView = new MainDisplay(new Stage(), false);
		}

		StoreController controller = new StoreController(theMainView, theStore);

	}
}
