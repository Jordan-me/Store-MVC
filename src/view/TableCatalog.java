package view;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.function.Predicate;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import listeners.StoreUIeventListener;
import model.Product;

public class TableCatalog {
	public static final String ColumnMapKey = "SKU";
	private ScrollPane scrollTable;
	private Vector<StoreUIeventListener> allListeners;
	private TableView<Map.Entry<String, Product>> table;
	private Map<String, Product> map;
	private TableColumn<Map.Entry<String, Product>, String> columnSKU;
	private TableColumn<Map.Entry<String, Product>, String> columnName;
	private TableColumn<Map.Entry<String, Product>, String> columnSellPrice;
	private TableColumn<Map.Entry<String, Product>, String> columnCostPrice;
	private TableColumn<Map.Entry<String, Product>, String> columnClientName;
	private TableColumn<Map.Entry<String, Product>, String> columnClientPhone;
	private TableColumn<Map.Entry<String, Product>, String> columnClientUpdate;
	private ObservableList<Map.Entry<String, Product>> items;

	public TableCatalog(ScrollPane scrollTab, Vector<StoreUIeventListener> allListeners) {
		this.scrollTable = scrollTab;
		this.allListeners = allListeners;
		for (StoreUIeventListener l : allListeners) {
			map = l.getMapData();
		}
		columnSKU = new TableColumn<>("SKU");
		columnSKU.setMinWidth(130);
		columnSKU.setSortable(false);
		columnName = new TableColumn<>("Name");
		columnName.setMinWidth(250);
		columnName.setSortable(false);
		columnSellPrice = new TableColumn<>("Sell Price");
		columnSellPrice.setMinWidth(130);
		columnSellPrice.setSortable(false);
		columnCostPrice = new TableColumn<>("Cost Price");
		columnCostPrice.setMinWidth(130);
		columnCostPrice.setSortable(false);

		columnClientName = new TableColumn<>("Buyer name");
		columnClientName.setMinWidth(130);
		columnClientName.setSortable(false);

		columnClientPhone = new TableColumn<>("Buyer phone");
		columnClientPhone.setMinWidth(130);
		columnClientPhone.setSortable(false);

		columnClientUpdate = new TableColumn<>("Subscriber");
		columnClientUpdate.setMinWidth(130);
		columnClientUpdate.setSortable(false);

		columnSKU.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Map.Entry<String, Product>, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(
							TableColumn.CellDataFeatures<Map.Entry<String, Product>, String> p) {
						return new SimpleStringProperty(p.getValue().getKey());
					}
				});

		columnName.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Map.Entry<String, Product>, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(
							TableColumn.CellDataFeatures<Map.Entry<String, Product>, String> p) {

						return new SimpleStringProperty(p.getValue().getValue().getNameP());
					}
				});
		columnSellPrice.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Map.Entry<String, Product>, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Entry<String, Product>, String> p) {

						return new SimpleStringProperty(p.getValue().getValue().getSellPrice().toString());
					}

				});
		columnCostPrice.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Map.Entry<String, Product>, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(
							TableColumn.CellDataFeatures<Map.Entry<String, Product>, String> p) {

						return new SimpleStringProperty(p.getValue().getValue().getCostPrice().toString());
					}
				});
		columnClientName.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Map.Entry<String, Product>, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(
							TableColumn.CellDataFeatures<Map.Entry<String, Product>, String> p) {
						if (p.getValue().getValue().getBuyer() == null) {
							return new SimpleStringProperty("\t-");
						} else {
							return new SimpleStringProperty(p.getValue().getValue().getBuyer().getName());
						}
					}
				});
		columnClientPhone.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Map.Entry<String, Product>, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(
							TableColumn.CellDataFeatures<Map.Entry<String, Product>, String> p) {
						if (p.getValue().getValue().getBuyer() == null) {
							return new SimpleStringProperty("\t-");

						} else {
							return new SimpleStringProperty(p.getValue().getValue().getBuyer().getPhone());
						}
					}
				});
		columnClientUpdate.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Map.Entry<String, Product>, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(
							TableColumn.CellDataFeatures<Map.Entry<String, Product>, String> p) {
						if (p.getValue().getValue().getBuyer() == null) {
							return new SimpleStringProperty("\t-");
						} else {
							if (p.getValue().getValue().getBuyer().isGetUpdate()) {
								return new SimpleStringProperty("\tY");
							} else {

								return new SimpleStringProperty("\tN");
							}
						}
					}
				});
		items = FXCollections.observableArrayList(map.entrySet());
		table = new TableView<>(items);
		table.setEditable(false);
		table.getColumns().setAll(columnSKU, columnName, columnSellPrice, columnCostPrice, columnClientName,
				columnClientPhone, columnClientUpdate);
		table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		table.setVisible(true);
		table.setItems(items);
		scrollTab.setContent(table);
	}

	private void setMap() {
		for (StoreUIeventListener l : allListeners) {
			map = l.getMapData();
		}
	}

	public void addData(String sKU, Product thePro) {

		if (map.containsKey(sKU)) {
			map.replace(sKU, thePro);
		} else {
			map.put(sKU, thePro);
		}
		items.clear();
		items.addAll(map.entrySet());
	}

	public void removeData(String proCode, Product removePro) {
		map.remove(proCode);
		items.clear();
		items.addAll(map.entrySet());

	}

	public void replaceData(String proCode, Product newPro) {
		map.replace(proCode, newPro);
		items.clear();
		items.addAll(map.entrySet());

	}

	public void setTableForClient(TableView<Map.Entry<String, Product>> tableClient) {
		tableClient.getItems().remove(columnCostPrice);
		tableClient.getItems().remove(columnClientName);
		tableClient.getItems().remove(columnClientPhone);
		tableClient.getItems().remove(columnClientUpdate);

	}

	public void setTableForManager() {
		columnCostPrice.setVisible(true);
		columnClientName.setVisible(true);
		columnClientPhone.setVisible(true);
		columnClientUpdate.setVisible(true);
	}

	public void setScrollPane(ScrollPane scroll) {
		scroll.setContent(table);
	}

	public TableView getTableForClient() {
		TableView<Map.Entry<String, Product>> tableClient = new TableView<Map.Entry<String, Product>>();
		for (Entry<String, Product> keyVal : items) {
			if (keyVal.getValue().getBuyer() == null) {
				tableClient.getItems().add(keyVal);
			}
		}
		tableClient.getColumns().setAll(columnSKU, columnName, columnSellPrice);

		return tableClient;
	}

	public TextField getSearchFieldTable(TableView tableInput) {
		TextField searchField = new TextField();
		searchField.setPromptText("Search");

		FilteredList<Map.Entry<String, Product>> filterData = new FilteredList(tableInput.getItems(), e -> true);
		searchField.setOnKeyReleased(e -> {
			searchField.textProperty().addListener((observableValue, oldValue, newValue) -> {
				filterData.setPredicate((Predicate<? super Map.Entry<String, Product>>) p -> {
					String pCode = null;

					if (newValue == null || newValue.isEmpty()) {
						return true;
					}
					String lowerCaseFilter = newValue.toLowerCase();
					if ((p.getKey().toLowerCase()).contains(lowerCaseFilter)) {
						return true;
					} else if ((p.getValue().getNameP().toLowerCase()).contains(lowerCaseFilter)) {
						return true;
					}
					return false;
				});
			});
//			items = new SortedList<>(filterData);
//			tableInput.setItems(items);
			SortedList<Map.Entry<String, Product>> sortedData = new SortedList<>(filterData);
			sortedData.comparatorProperty().bind(tableInput.comparatorProperty());
			tableInput.setItems(sortedData);
		});

		return searchField;
	}

	public TableView getTable() {
		return table;
	}

	public String getKeySelected(TableView tableUI) {
		return columnSKU.getCellData(tableUI.getSelectionModel().getSelectedIndex());

	}

	public void updateTableForClient(ObservableList<Map.Entry<String, Product>> observableList) {
		Iterator<Map.Entry<String, Product>> iTable = observableList.iterator();
		while (iTable.hasNext()) {
			if (iTable.next().getValue().getBuyer() != null) {
				try {
				iTable.remove();
				}catch(UnsupportedOperationException e) {
					System.out.println("Removed");
				}
			}
		}

	}

}
