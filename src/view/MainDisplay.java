package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Vector;

import javax.swing.JOptionPane;

import commandPattern.BtnAddUndoProductCommand;
import commandPattern.BtnConnectCommand;
import commandPattern.BtnShopCommand;
import commandPattern.BtnSortCommand;
import commandPattern.ICommand;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import listeners.StoreUIeventListener;
import model.Product;

public class MainDisplay implements AbstractStoreView {
	private final int ID = 1;
	private final int UI_MANAGER = 2;
	private final int UI_CLIENT = 3;
	private Vector<StoreUIeventListener> allListeners = new Vector<StoreUIeventListener>();

	private Stage stage;
	private Scene scene;

	// for first window
	private BorderPane bpRoot;

	private MenuButton menuButtonSort;
	private MenuItem menuItemInsert;
	private MenuItem menuItemDown;
	private MenuItem menuItemUp;

	private ToolBar toolBar;
	private Button btnLogIn;
	private Button btnRemoveSKU;
	private Button btnSendMsg;

	private Label textWelcome;
	private Button btnShopping;
	private HBox hbCatalog;
	private Label lbManager;
	private BtnConnectCommand connectCommand;
	private BtnShopCommand shopCommand;

	public MainDisplay(Stage theStage, boolean flag) throws FileNotFoundException {
		String styleWelcomeText = "-fx-font-size: 50px;" + "-fx-font-family:Algerian;"
				+ "-fx-effect: innershadow( three-pass-box , rgba(0,0,0,0.7) , 6, 0.0 , 0 , 2 );";
		this.stage = theStage;
		menuItemInsert = new MenuItem("Insert Order");
		menuItemDown = new MenuItem("Descending Order");
		menuItemUp = new MenuItem("Ascending Order");
		menuButtonSort = new MenuButton("Options", null, menuItemInsert, menuItemDown, menuItemUp);

		menuButtonSort.setVisible(flag);

		FileInputStream input = new FileInputStream("resources/menuIcon.png");
		Image image = new Image(input);
		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(30);
		imageView.setFitWidth(30);
		menuButtonSort.setGraphic(imageView);

		btnLogIn = new Button("Log In");
		btnLogIn.setMinHeight(40);

		btnLogIn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (btnLogIn.getText().equals("Log In")) {
					connectCommand = new BtnConnectCommand(btnLogIn, allListeners, menuButtonSort, btnRemoveSKU,
							btnSendMsg);
					sendCommand(connectCommand, "Log");
				} else {
					sendCommand(connectCommand, "Undo");
				}

			}
		});

		menuItemInsert.setOnAction(e -> {

			sendCommand(new BtnSortCommand(menuButtonSort, menuItemInsert, allListeners), "Sort");
		});

		menuItemDown.setOnAction(e -> {

			sendCommand(new BtnSortCommand(menuButtonSort, menuItemDown, allListeners), "Sort");
		});

		menuItemUp.setOnAction(e -> {

			sendCommand(new BtnSortCommand(menuButtonSort, menuItemUp, allListeners), "Sort");
		});
		btnRemoveSKU = new Button("Remove");
		btnRemoveSKU.setVisible(false);
		btnSendMsg = new Button("Send MSG");
		btnSendMsg.setVisible(false);

		lbManager = new Label("Hello, Boss");
		toolBar = new ToolBar();
		toolBar.getItems().addAll(new Separator(), btnLogIn, new Separator(), lbManager, new Separator(),
				menuButtonSort, new Separator(), btnRemoveSKU, new Separator(), btnSendMsg, new Separator());

		btnShopping = new Button("Shopping");
		btnShopping.setMinWidth(250);
		btnShopping.setMinHeight(50);
		btnShopping.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		btnShopping.setOnAction(e -> {
			shopCommand = new BtnShopCommand(allListeners);
			sendCommandClient(shopCommand, false, "Log");
		});

		textWelcome = new Label("Welcome To our ProgStroe");
		textWelcome.setUnderline(true);
		textWelcome.setStyle(styleWelcomeText);
		textWelcome.setTextFill(Color.ANTIQUEWHITE);
		textWelcome.setBackground(new Background(new BackgroundFill(Color.BURLYWOOD, null, null)));

		hbCatalog = new HBox();
		hbCatalog.setAlignment(Pos.BOTTOM_CENTER);
		hbCatalog.setPadding(new Insets(20));
		hbCatalog.getChildren().addAll(btnShopping);

		bpRoot = new BorderPane();
		bpRoot.setTop(toolBar);
		bpRoot.setCenter(textWelcome);
		textWelcome.setTextAlignment(TextAlignment.CENTER);
		bpRoot.setBottom(hbCatalog);

		scene = new Scene(bpRoot, 900, 600);
		input = new FileInputStream("resources/opening2.jpg");
		BackgroundImage myBI = new BackgroundImage(new Image(input), BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
				new BackgroundSize(1.0, 1.0, true, true, false, false));

		bpRoot.setBackground(new Background(myBI));
		stage.setScene(scene);
		stage.setTitle("ProgStore");
		stage.setResizable(true);

		stage.show();
	}

	private void sendCommandClient(ICommand c, boolean b, String m) {
		for (StoreUIeventListener l : allListeners) {
			l.setCommandClient(c, b, m);
		}

	}

	@Override
	public void registerListener(StoreUIeventListener listener) {
		allListeners.add(listener);
	}

	@Override
	public void sendCommand(ICommand command, String mode) {

		for (StoreUIeventListener l : allListeners) {
			l.setCommand(command, mode);
		}

	}

	@Override
	public void show(int i) {
		if (i == ID) {
			stage.setScene(scene);
			bpRoot.setTop(toolBar);

		} else if (i == UI_MANAGER) {

			buildManagerUI();

		} else if (i == UI_CLIENT) {
			buildClientUI();
		}

	}

	private BorderPane bpClient;
	private Scene sceneUI;

	public void buildClientUI() {

		bpClient = new BorderPane();
		if (menuButtonSort.isVisible()) {
			JOptionPane.showMessageDialog(null, "Please select the sort type of the catalog");
			return;
		}
		ClientUI clientWindow = new ClientUI(bpClient, allListeners, shopCommand);
		sceneUI = new Scene(bpClient, 1500, 700);
		stage.setScene(sceneUI);
		JOptionPane.showMessageDialog(null, "Wellcome!");

	}

	// for UI_MANAGER
	private TabPane tabPane;
	private Tab tabEconomicBalance;
	private Tab tabCatalogProducts;

	private VBox vbWindow;
	private Scene uiManScene;

	public void buildManagerUI() {
		vbWindow = new VBox();

		tabPane = new TabPane();
		tabPane.setTabMinWidth(250);
		tabPane.setTabMinHeight(40);

		tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		tabEconomicBalance = new Tab("Balance");

		tabCatalogProducts = new Tab("Catalog");
		creatBalancTab();
		createCatalogTab();
		btnRemoveSKU.setVisible(true);
		btnRemoveSKU.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				String sku = JOptionPane.showInputDialog("Enter the product SKU");
				for (StoreUIeventListener l : allListeners) {
					l.removeProductEvent(sku);
				}
			}
		});
		btnSendMsg.setVisible(true);
		btnSendMsg.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				String msg = JOptionPane.showInputDialog("Insert a message");
				for (StoreUIeventListener l : allListeners) {
					l.sendMessage(msg);
				}
				Thread t = new Thread(new TextAreaClients(msg, new TextArea()));
				t.start();
				t.interrupt();

			}
		});
		tabPane.getTabs().addAll(tabEconomicBalance, tabCatalogProducts);

		vbWindow = new VBox(5);
		vbWindow.setPadding(new Insets(10, 0, 0, 10));
		vbWindow.getChildren().addAll(toolBar, tabPane);
		vbWindow.setMinHeight(1000);
		vbWindow.setMinWidth(1000);

		VBox.setVgrow(tabPane, Priority.ALWAYS);
		VBox.setVgrow(toolBar, Priority.ALWAYS);
		uiManScene = new Scene(vbWindow, 1300, 700);
		stage.setScene(uiManScene);

	}

	private ScrollPane scrollTab;
	private GridPane gTable;
	private VBox addElenents;
	private HBox hbSellPrice;
	private HBox hbCostPrice;
	private TableCatalog tablebCatalog;
	private TextField tfSearchField;
	private Text txtAddPro;
	private TextField tfSKU;
	private TextField tfname;

	private Spinner<Integer> spinnerSellPrice;
	private Spinner<Integer> spinnerCostPrice;
	private Label lbSellPrice;

	private Label lbCostPrice;

	private Button btnSave;
	private Button btnUndo;
	private BtnAddUndoProductCommand bAddUndoCommand;
	private IntegerSpinnerValueFactory vfSell;
	private IntegerSpinnerValueFactory vfCost;

	public void createCatalogTab() {
		spinnerSellPrice = new Spinner<Integer>();
		vfSell = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000000);
		spinnerSellPrice.setValueFactory(vfSell);
		spinnerSellPrice.setEditable(true);

		spinnerSellPrice.getEditor().setPromptText("0");
		setMaxLengthField(spinnerSellPrice.getEditor());
		setNumericTextField(spinnerSellPrice.getEditor());

		vfCost = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000000);
		spinnerCostPrice = new Spinner<Integer>();
		spinnerCostPrice.setValueFactory(vfCost);
		vfCost.valueProperty().setValue(0);
		spinnerCostPrice.setEditable(true);
		spinnerCostPrice.getEditor().setPromptText("0");
		setMaxLengthField(spinnerCostPrice.getEditor());
		setNumericTextField(spinnerCostPrice.getEditor());

		String styles = "-fx-background-color: #4169e1 ;" + "-fx-border-color: #87cefa ;" + "-fx-border-width: 3px;";
		Font font = Font.font("Arial", FontWeight.BOLD, 16);

		FileInputStream input = null;
		try {
			input = new FileInputStream("resources/addProBack.jpg");
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		Background myBI = new Background(
				new BackgroundImage(new Image(input), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
						BackgroundPosition.DEFAULT, new BackgroundSize(1.0, 1.0, true, true, false, false)));

		scrollTab = new ScrollPane();
		addElenents = new VBox(30);
		addElenents.setBackground(myBI);

		gTable = new GridPane();
		hbCostPrice = new HBox(5);
		hbSellPrice = new HBox(5);

		txtAddPro = new Text("\tCreate New Product");
		txtAddPro.setStyle("-fx-font-size: 20px;" + "-fx-font-family:Ariel;"
				+ "-fx-effect: innershadow( three-pass-box , rgba(0,0,0,0.7) , 6, 0.0 , 0 , 2 );");
		tfSKU = new TextField();
		tfSKU.setMinWidth(275);
		tfSKU.setPromptText("Type Product's SKU here");

		tfname = new TextField();
		tfname.setMinWidth(275);
		tfname.setPromptText("Type Product's Name here");

		lbSellPrice = new Label("Selling price");
		lbSellPrice.setMinWidth(100);
		lbCostPrice = new Label("Cost Price");
		lbCostPrice.setMinWidth(100);

		btnSave = new Button("Save");
		btnSave.setMinWidth(159);
		btnSave.setStyle(styles);
		btnSave.setFont(font);

		btnSave.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				bAddUndoCommand = new BtnAddUndoProductCommand(allListeners, btnSave, btnUndo, tfSKU, tfname,
						spinnerCostPrice.getEditor(), spinnerSellPrice.getEditor());
				sendCommand(bAddUndoCommand, "Add");
			}
		});
		btnUndo = new Button("Undo");
		btnUndo.setMinWidth(159);
		btnUndo.setStyle(styles);
		btnUndo.setFont(font);
		btnUndo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendCommand(bAddUndoCommand, "Undo");
			}
		});

		tablebCatalog = new TableCatalog(scrollTab, allListeners);
		tablebCatalog.setTableForManager();
		tfSearchField = tablebCatalog.getSearchFieldTable(tablebCatalog.getTable());

		tfSearchField.setMinWidth(1000);
		tfSearchField.setMinHeight(38);
		hbCostPrice.getChildren().addAll(lbCostPrice, spinnerCostPrice);
		hbSellPrice.getChildren().addAll(lbSellPrice, spinnerSellPrice);
		addElenents.getChildren().addAll(txtAddPro, tfSKU, tfname, hbCostPrice, hbSellPrice);

		gTable.add(tfSearchField, 0, 0, 1, 10);
		gTable.add(btnSave, 10, 0, 1, 5);
		gTable.add(btnUndo, 15, 0, 1, 5);
		gTable.add(scrollTab, 0, 5, 10, 10);
		gTable.add(addElenents, 10, 5, 10, 10);

		tabCatalogProducts.setContent(gTable);

	}

	public void setNumericTextField(TextField editor) {
		editor.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.isEmpty()) {

				editor.setText("0");

			}
			if (!newValue.matches("\\d*")) {

				editor.setText(oldValue);
			}
		});

	}

	public void setMaxLengthField(TextField editor) {
		final int MAX_LENGTH = 1000000;
		editor.lengthProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					// Check if the new character is greater than LIMIT
					if (editor.getText().length() >= MAX_LENGTH) {

						editor.setText(editor.getText().substring(0, MAX_LENGTH));
					}
				}
			}
		});
	}

	private GridPane grid;
	private PieChartEconomic myChart;

	public void creatBalancTab() {
		grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(0, 10, 0, 10));
		int loses = 0, profit = 0, sumIncome = 0;
		int numOfProducts = 0, sellsProducts = 0;
		for (StoreUIeventListener l : allListeners) {
			loses = l.getEpenses();
			profit = l.getIncome();
			numOfProducts = l.getNumOfProducts();
			sellsProducts = l.getNumOfSellsProducts();
			sumIncome = l.getSumIncome();

		}
		myChart = new PieChartEconomic(grid, loses, profit, numOfProducts, sellsProducts, sumIncome, allListeners);

		tabEconomicBalance.setContent(grid);
	}

	@Override
	public void addProduct(String sKU, Product thePro) {
		tablebCatalog.addData(sKU, thePro);
		JOptionPane.showMessageDialog(null, "Add successfully");
		tfSKU.clear();
		tfname.clear();
		updatePieChart();

	}

	public void updatePieChart() {
		for (StoreUIeventListener l : allListeners) {
			myChart.setDataExpenses(l.getEpenses());
			myChart.setDataIncom(l.getIncome());
			myChart.setTextSellNum(l.getNumOfSellsProducts(), l.getNumOfProducts());
			myChart.setSumIncome(l.getSumIncome());
		}

	}

	@Override
	public void replaceProduct(String proCode, Product thePro2Replace) {
		tablebCatalog.replaceData(proCode, thePro2Replace);
		JOptionPane.showMessageDialog(null, "Successfully replaced:");
		updatePieChart();

	}

	@Override
	public void removeProduct(String proCode, Product oldPro) {
		tablebCatalog.removeData(proCode, oldPro);
		JOptionPane.showMessageDialog(null, "Successfully removed");
		updatePieChart();
	}

	@Override
	public void addSell(String proCode, String name, Product product) {
		JOptionPane.showMessageDialog(null,
				name + "  bought " + product.getNameP() + " successfully\n\tThanks for your purchase ");

	}

}
