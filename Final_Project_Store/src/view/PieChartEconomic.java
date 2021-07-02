package view;

import java.text.DecimalFormat;
import java.util.Vector;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import listeners.StoreUIeventListener;

public class PieChartEconomic {
	private GridPane grid;
	private StackPane spPie;
	private double sumIncome;
	private PieChart myPie;
	private Text txtSellsNum;
	private Text sumIncomText;
	private PieChart.Data dataIncom;
	private PieChart.Data dataExpenses;
	private Label caption;
	private Group root;
	private Vector<StoreUIeventListener> allListeners;

	public PieChartEconomic(GridPane pane, int loses, int profit, int numOfProducts, int sellsProducts, int sumIncome,
			Vector<StoreUIeventListener> allListeners) {
		this.allListeners = allListeners;
		this.grid = pane;

		dataExpenses = new PieChart.Data("Expenses", loses);
		dataIncom = new PieChart.Data("Income", profit);
		ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(dataExpenses, dataIncom);
		myPie = new PieChart(pieData);
		myPie.setTitle("Income / expenses");

		txtSellsNum = new Text();

		sumIncomText = new Text();
		txtSellsNum = new Text();
		setSumIncome(sumIncome);
		setTextSumIncome();
		setTextSellNum(sellsProducts, numOfProducts);
		sumIncomText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		txtSellsNum.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		myPie.setClockwise(true);
		myPie.setStartAngle(180);

		caption = new Label("");
		caption.setTextFill(Color.BLACK);
		caption.setStyle("-fx-font: 20 arial;");

		for (final PieChart.Data data : myPie.getData()) {
		 
			data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					caption.setVisible(true);
					Node node = data.getNode();
					caption.setTranslateX(node.getLayoutX());
					caption.setTranslateY(node.getLayoutX());
					caption.setLabelFor(node);
					for (StoreUIeventListener l : allListeners) {
						caption.setText(String.valueOf(new DecimalFormat("#.##")
								.format(data.getPieValue() / (l.getEpenses() + l.getIncome()) * 100)) + "%");
					}

				}
			});
			data.getNode().addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent arg0) {
					caption.setVisible(false);
				}

			});
		}
		spPie = new StackPane();
		root = new Group();
		root.getChildren().addAll(myPie, caption);
		spPie.getChildren().add(root);

		grid.add(txtSellsNum, 1, 7);
		grid.add(sumIncomText, 1, 8);

		grid.add(spPie, 10, 5, 4, 4);
	 
	}

	public void setDataIncom(int profit) {
		dataIncom.setPieValue(profit);
	}

	public void setSumIncome(int sumIn) {
		this.sumIncome = sumIn;
		setTextSumIncome();
	}

	public void setDataExpenses(int loses) {
		dataExpenses.setPieValue(loses);
	}

	public void setTextSellNum(int productsSold, int numOfProducts) {
		txtSellsNum.setText("You sold " + productsSold + " products out of " + numOfProducts);

	}

	public void setTextSumIncome() {

		if (sumIncome >= 0) {
			sumIncomText.setText("your account in credit of " + sumIncome + " dollars");
		} else {
			sumIncomText.setText("Your account is $" + sumIncome + " in debt.");
		}
	}

}
