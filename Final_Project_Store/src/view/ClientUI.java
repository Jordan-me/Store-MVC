package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.JOptionPane;

import commandPattern.BtnAddUndoClientCommand;
import commandPattern.BtnAddUndoProductBuyCommand;
import commandPattern.BtnShopCommand;
import commandPattern.ICommand;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import listeners.StoreUIeventListener;
import model.Product;

public class ClientUI {
	private Vector<StoreUIeventListener> allListeners = new Vector<StoreUIeventListener>();
	private BorderPane bpWindowContent;
	private TableCatalog tableProducts;
	private ScrollPane scrollPane;
	private ListView<String> listCartShopping;
	private ToolBar toolBarUI;

	private Button btnAddToCart;
	private Button btnUndoFromCart;
	private Button btnPayment;
	private Button btnExit;
	private Button btnAssign;
	private Button btnRemoveMe;

	private TextField tfSearchField;

	private VBox vbMember;
	private Text txtMember;
	private TextField tfNameClient;
	private TextField tfPhoneClient;
	private CheckBox cbClientUpdate;
	private Background myBI;
	private Background myBMem;

	private TableView<Entry<String, Product>> tableUI;

	private BtnShopCommand bShopExitCommand;
	private BtnAddUndoProductBuyCommand bAddProToCartCommand;
	private BtnAddUndoClientCommand bAddClientCommand;

	public ClientUI(BorderPane bpClient, Vector<StoreUIeventListener> allListeners, BtnShopCommand shopCommand) {
		this.bShopExitCommand = shopCommand;
		Insets insets = new Insets(10);
		FileInputStream input = null;
		FileInputStream inputMember = null;
		try {
			input = new FileInputStream("resources/shoppingCart3.png");
			inputMember = new FileInputStream("resources/memberBackground.jpg");

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		myBI = new Background(
				new BackgroundImage(new Image(input), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
						BackgroundPosition.DEFAULT, new BackgroundSize(1.0, 1.0, true, true, false, false)));
		myBMem = new Background(
				new BackgroundImage(new Image(inputMember), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
						BackgroundPosition.DEFAULT, new BackgroundSize(1.0, 1.0, true, true, false, false)));

		this.allListeners = allListeners;
		this.bpWindowContent = bpClient;
		scrollPane = new ScrollPane();
		toolBarUI = new ToolBar();
		vbMember = new VBox(30);
		tableProducts = new TableCatalog(scrollPane, allListeners);

		listCartShopping = new ListView<String>();
		listCartShopping.setBackground(myBI);

		txtMember = new Text("Member details");
		txtMember.setTextAlignment(TextAlignment.CENTER);
		txtMember.setStyle("-fx-font-size: 20px;" + "-fx-font-family:Ariel Black;");

		tfNameClient = new TextField();
		tfNameClient.setPromptText("Enter your name here");

		tfPhoneClient = new TextField();
		tfPhoneClient.setPromptText("Enter your phone here");

		cbClientUpdate = new CheckBox(
				"I agree to receive updates and offers for products\nI know that I can withdraw my consent at any time.");

		btnAddToCart = new Button("Add");// add chosen product to my wishList
		setStyleButton(btnAddToCart);
		btnUndoFromCart = new Button("Remove");// remove last product from my wishList
		setStyleButton(btnUndoFromCart);
		btnPayment = new Button("Payment");// show sumBuy
		setStyleButton(btnPayment);
		btnExit = new Button("Exit");// return to main window
		setStyleButton(btnExit);
		btnAssign = new Button("Assign");// add client to the database
		setStyleButton(btnAssign);
		btnRemoveMe = new Button("Remove me");// remove client from list sending messages the database-undo of assign
		setStyleButton(btnRemoveMe);

		btnExit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendCommand(bShopExitCommand, false, "Undo");
			}
		});
		btnAddToCart.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (!tableUI.getSelectionModel().isEmpty()) {
					bAddProToCartCommand = new BtnAddUndoProductBuyCommand(allListeners,
							tableProducts.getKeySelected(tableUI), listCartShopping);
					sendCommand(bAddProToCartCommand, true, "Add");
				} else {
					JOptionPane.showMessageDialog(null, "please choose product");
				}
			}
		});
		btnUndoFromCart.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendCommand(bAddProToCartCommand, true, "Undo");
			}
		});
		btnPayment.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (listCartShopping.getItems().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Your cart is empty");
				} else {
					String phoneInput = JOptionPane.showInputDialog("Enter your phone-");
					setMessagePayment(phoneInput);
				}
			}
		});
		btnAssign.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				bAddClientCommand = new BtnAddUndoClientCommand(tfNameClient, tfPhoneClient, cbClientUpdate,
						allListeners);
				bAddClientCommand.execute();
			}
		});

		btnRemoveMe.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				bAddClientCommand = new BtnAddUndoClientCommand(JOptionPane.showInputDialog("Enter your phone-"),
						allListeners);
				bAddClientCommand.undo();

			}
		});

		tableUI = tableProducts.getTableForClient();
		tfSearchField = tableProducts.getSearchFieldTable(tableUI);
		tfSearchField.setMinWidth(600);
		toolBarUI.getItems().addAll(new Separator(), btnAddToCart, new Separator(), btnUndoFromCart, new Separator(),
				btnPayment, new Separator(), tfSearchField, new Separator(), btnRemoveMe, new Separator(), btnExit);
		scrollPane.setContent(tableUI);
		scrollPane.fitToWidthProperty().set(true);
		scrollPane.fitToHeightProperty().set(true);

		vbMember.getChildren().addAll(txtMember, tfNameClient, tfPhoneClient, cbClientUpdate, btnAssign);
		vbMember.setBackground(myBMem);
		vbMember.setAlignment(Pos.TOP_CENTER);

		bpWindowContent.setTop(toolBarUI);
		BorderPane.setMargin(toolBarUI, insets);
		bpWindowContent.setCenter(scrollPane);
		BorderPane.setMargin(scrollPane, insets);
		bpWindowContent.setLeft(listCartShopping);
		BorderPane.setMargin(listCartShopping, insets);
		bpWindowContent.setRight(vbMember);
		BorderPane.setMargin(vbMember, insets);

	}

	public void setStyleButton(Button btn) {
		String styles = "-fx-background-color: #4169e1 ;" + "-fx-border-color: #87cefa ;" + "-fx-border-width: 3px;";
		Font font = Font.font("Arial", FontWeight.BOLD, 16);
		btn.setMinWidth(140);
		btn.setStyle(styles);
		btn.setFont(font);
	}

	public void sendCommand(ICommand command, boolean b, String mode) {
		for (StoreUIeventListener l : allListeners) {
			l.setCommandClient(command, b, mode);
		}

	}

	public void setMessagePayment(String phoneInput) {
		for (StoreUIeventListener l : allListeners) {
			if (l.isExsistClient(phoneInput)) {
				l.setClientForProduct(phoneInput, listCartShopping.getItems().listIterator(0));
				updateTableUI();
				listCartShopping.getItems().removeAll(listCartShopping.getItems());
			} else {
				JOptionPane.showMessageDialog(null, "Please save your details in member details");
			}
		}
	}

	public void updateTableUI() {
		tableProducts.updateTableForClient(tableUI.getItems());

	}
}
