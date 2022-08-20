/*
 * Name: James WORCESTER
 * Student ID: 20767086
 * Username: 20767086@students.latrobe.edu.au
 * Subject Code: CSE3OAD
 */

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.*;
import java.io.*;
import javafx.collections.*;
import javafx.collections.transformation.*;
import javafx.geometry.Pos;
import javafx.scene.control.cell.*;
import javafx.beans.property.*;

public class WarehouseFX extends Application {

	// used as ChoiceBox value for filter
	public enum FILTER_COLUMNS {
		ITEM,
		SECTION,
		BOUGHT_DAYS_AGO
	};
	
	// the data source controller
	private WarehouseDSC warehouseDSC;
	

	public void init() throws Exception {
		// creating an instance of the data source controller to be used
		// in this application
		warehouseDSC = new WarehouseDSC();

		/* TODO 2-01 - TO COMPLETE ****************************************
		 * call the data source controller database connect method
		 * NOTE: that database connect method throws exception
		 */
		try
		{
			warehouseDSC.connect();
		}
		catch(Exception e)
		{
			System.out.println("ERROR: " + e);
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("ERROR: " + e);
			alert.showAndWait();
		}
		

	}

	public void start(Stage stage) throws Exception {

		/* TODO 2-02 - TO COMPLETE ****************************************
		 * - this method is the start method for your application
		 * - set application title
		 * - show the stage
		 */
		build(stage);
		stage.setTitle("What's in My Warehouse");
		stage.show();


		/* TODO 2-03 - TO COMPLETE ****************************************
		 * currentThread uncaught exception handler
		 */
		Thread.currentThread().setUncaughtExceptionHandler((thread, exception) ->
		{
			System.out.println("ERROR: " + exception);
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("ERROR: " + exception);
			alert.showAndWait();
		});
	}

	public void build(Stage stage) throws Exception {

		// Create table data (an observable list of objects)
		ObservableList<Product> tableData = FXCollections.observableArrayList();

		// Define table columns
		TableColumn<Product, String> idColumn = new TableColumn<Product, String>("Id");
		//TableColumn<Product, Integer> idColumn = new TableColumn<Product, Integer>("Id");
		TableColumn<Product, String> itemNameColumn = new TableColumn<Product, String>("Item");
		TableColumn<Product, Integer> quantityColumn = new TableColumn<Product, Integer>("QTY");
		TableColumn<Product, String> sectionColumn = new TableColumn<Product, String>("Section");
		TableColumn<Product, String> daysAgoColumn = new TableColumn<Product, String>("Bought");
		
		/* TODO 2-04 - TO COMPLETE ****************************************
		 * for each column defined, call their setCellValueFactory method 
		 * using an instance of PropertyValueFactory
		 */
		idColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("Id"));
		//idColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("Id"));
		itemNameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("ItemName"));
		quantityColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("quantity"));
		sectionColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("Section"));
		daysAgoColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("DaysAgo"));

		// Create the table view and add table columns to it
		TableView<Product> tableView = new TableView<Product>();


		/* TODO 2-05 - TO COMPLETE ****************************************
		 * add table columns to the table view create above
		 */
		tableView.getColumns().add(idColumn);
		tableView.getColumns().add(itemNameColumn);
		tableView.getColumns().add(quantityColumn);
		tableView.getColumns().add(sectionColumn);
		tableView.getColumns().add(daysAgoColumn);

		//	Attach table data to the table view
		tableView.setItems(tableData);


		/* TODO 2-06 - TO COMPLETE ****************************************
		 * set minimum and maximum width to the table view and each columns
		 */
		idColumn.setMinWidth(50);
		idColumn.setMaxWidth(50);
		itemNameColumn.setMinWidth(200);
		itemNameColumn.setMaxWidth(200);
		quantityColumn.setMinWidth(50);
		quantityColumn.setMaxWidth(50);
		sectionColumn.setMinWidth(120);
		sectionColumn.setMaxWidth(120);
		daysAgoColumn.setMinWidth(120);
		daysAgoColumn.setMaxWidth(120);


		/* TODO 2-07 - TO COMPLETE ****************************************
		 * call data source controller get all products method to add
		 * all products to table data observable list
		 */
		tableData.addAll(warehouseDSC.getAllProducts());
		

		// =====================================================
		// ADD the remaining UI elements
		// NOTE: the order of the following TODO items can be 
		// 		 changed to satisfy your UI implementation goals
		// =====================================================

		/* TODO 2-08 - TO COMPLETE ****************************************
		 * filter container - part 1
		 * add all filter related UI elements you identified
		 */
		TextField filterTF = new TextField();
		filterTF.setMaxWidth(200);
		filterTF.setMinWidth(200);
		Label filterLB = new Label("Filter By:");
		filterLB.setMaxWidth(80);
		filterLB.setMinWidth(80);

		String choiceITEM = "ITEM";
		String choiceSECTION = "SECTION";
		String choiceBOUGHT_DAYS_AGO = "BOUGHT_DAYS_AGO";
		ChoiceBox<String> choiceBox = new ChoiceBox<String>();
		choiceBox.getItems().addAll(choiceITEM, choiceSECTION, choiceBOUGHT_DAYS_AGO);

		choiceBox.setValue(choiceITEM);
		choiceBox.setMaxWidth(230);
		choiceBox.setMinWidth(230);

		 CheckBox checkBox = new CheckBox("Show Expire Only");
		 checkBox.setDisable(true);
		 checkBox.setMaxWidth(200);
		 checkBox.setMinWidth(200);
	

		/* TODO 2-09 - TO COMPLETE ****************************************
		 * filter container - part 2:
		 * - addListener to the "Filter By" ChoiceBox to clear the filter
		 *   text field vlaue and to enable the "Show Expire Only" CheckBox
		 *   if "BOUGHT_DAYS_AGO" is selected
		 */
				// Add a change listener
				choiceBox.getSelectionModel().selectedItemProperty().addListener(
					// ChangeListener
					(ov, oldValue, newValue) ->
					{
						//System.out.println("\n" + oldValue + " -> " + newValue);
						filterTF.clear();

						if(choiceBox.getValue() == choiceBOUGHT_DAYS_AGO)
						{
							checkBox.setDisable(false);
							filterTF.requestFocus();
						}
						else
						{
							checkBox.setDisable(true);
							filterTF.requestFocus();
						}
					});

		/* TODO 2-10 - TO COMPLETE ****************************************
		 * filter container - part 2:
		 * - addListener to the "Filter By" ChoiceBox to clear and set focus 
		 *   to the filter text field and to enable the "Show Expire Only" 
		 *   CheckBox if "BOUGHT_DAYS_AGO" is selected
		 *
		 * - setOnAction on the "Show Expire Only" Checkbox to clear and 
		 *   set focus to the filter text field
		 */
		
		checkBox.setOnAction((e) ->
		{
			filterTF.clear();
			filterTF.requestFocus();
		});

		/* TODO 2-11 - TO COMPLETE ****************************************
		 * filter container - part 3:
		 * - create a filtered list
		 * - create a sorted list from the filtered list
		 * - bind comparators of sorted list with that of table view
		 * - set items of table view to be sorted list
		 * - set a change listener to text field to set the filter predicate
		 *   of filtered list
		 */

			FilteredList<Product> filteredList = new FilteredList<>(tableData, p -> true);
			SortedList<Product> sortedList = new SortedList<>(filteredList);
			sortedList.comparatorProperty().bind(tableView.comparatorProperty());
			tableView.setItems(sortedList);
			
			filterTF.textProperty().addListener((observable, oldValue, newValue) ->
			{
				filteredList.setPredicate(product ->
				{
					String filterString = newValue.toUpperCase();
					if ((newValue == null || newValue.isEmpty()) && checkBox.isSelected() && product.getItem().canExpire() == true)
					{
						return true;
					}
					else if (newValue == null || newValue.isEmpty())
					{
						 return true;
					}

					
					if(choiceBox.getValue() == choiceITEM && product.getItemName().toUpperCase().contains(filterString))
					{
						return true;
					}
					else if(choiceBox.getValue() == choiceSECTION && product.getSection().toString().contains(filterString))
					{
						return true;
					}
					else if(choiceBox.getValue() == choiceBOUGHT_DAYS_AGO && checkBox.isSelected() == false)
					{
						try
						{
							long testLong = Integer.parseInt(filterString);
							//if(warehouseDSC.product.calcDaysAgo <= testLong)
							if(warehouseDSC.calcDaysAgo(product.getDate()) <= testLong)
							{
								return true;
							}
						}
						catch(Exception e)
						{

						}
					}
					else if(choiceBox.getValue() == choiceBOUGHT_DAYS_AGO && checkBox.isSelected() == true)
					{

						try
						{
							long testLong = Integer.parseInt(filterString);
							//if(warehouseDSC.product.calcDaysAgo <= testLong)
							if(warehouseDSC.calcDaysAgo(product.getDate()) <= testLong && product.getItem().canExpire() == true)
							{
								return true;
							}
						}
						catch(Exception e)
						{

						}
					}

					return false;
				});
			});

		/* TODO 2-12 - TO COMPLETE ****************************************
		 * ACTION buttons: ADD, UPDATE ONE, DELETE
		 * - ADD button sets the add UI elements to visible;
		 *   NOTE: the add input controls and container may have to be
		 * 		   defined before these action controls & container(s)
		 * - UPDATE ONE and DELETE buttons action need to check if a
		 *   table view row has been selected first before doing their
		 *   action; hint: should you also use an Alert confirmation?
		 */	
		Button addBT = new Button("ADD");
		Button updateOneBT = new Button("UPDATE ONE");
		Button deleteBT = new Button("DELETE");

		Label itemLB = new Label("Item");
		Label sectionLB = new Label("Section");
		Label QuantityLB = new Label("Quantity");

		List<Item> items = warehouseDSC.getAllItems();
		ComboBox<Item> comboBox = new ComboBox<Item>();
		comboBox.getItems().addAll(items);
		comboBox.setMaxWidth(400);
		comboBox.setMinWidth(400);

		ChoiceBox<WarehouseDSC.SECTION> sectionChoiceBox = new ChoiceBox<WarehouseDSC.SECTION>();
		sectionChoiceBox.getItems().setAll(WarehouseDSC.SECTION.values());
		sectionChoiceBox.setMaxWidth(150);
		sectionChoiceBox.setMinWidth(150);


		TextField quantityTF = new TextField();
		quantityTF.setMaxWidth(100);
		quantityTF.setMinWidth(100);

		Button clearBT = new Button("CLEAR");
		Button saveBT = new Button("SAVE");

		HBox filterHBox = new HBox(filterTF, filterLB, choiceBox, checkBox);
		filterHBox.setAlignment(Pos.CENTER_LEFT);
		filterHBox.setSpacing(10);

		HBox addUpdateDeleteHBox = new HBox(addBT, updateOneBT, deleteBT);


		VBox hidden1VBox = new VBox(itemLB, comboBox);
		VBox hidden2VBox = new VBox(sectionLB, sectionChoiceBox);
		VBox hidden3VBox = new VBox(QuantityLB, quantityTF);

		HBox hiddenContainer1 = new HBox(hidden1VBox, hidden2VBox, hidden3VBox);
		hiddenContainer1.setSpacing(10);
		hiddenContainer1.setAlignment(Pos.CENTER);
		hiddenContainer1.setVisible(false);
	

		HBox hiddenContainer2 = new HBox(clearBT, saveBT);
		hiddenContainer2.setAlignment(Pos.CENTER);
		hiddenContainer2.setVisible(false);
		
		saveBT.setOnAction(e ->
		{
			if(comboBox.getValue() == null || sectionChoiceBox.getValue() == null || quantityTF.getText() == null)
			{
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setContentText("Please select an item, section and quantity");
				alert.showAndWait();
			}
			else
			{
				try
				{
					int quantityInt = Integer.parseInt(quantityTF.getText());
					//int productId = warehouseDSC.addProduct(comboBox.getValue().getName(), quantityInt, sectionChoiceBox.getValue());
					try
					{
						Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
						alert.setContentText("Are you sure? Add selected product?");
						Optional<ButtonType> result = alert.showAndWait();
						if (result.isPresent() && result.get() == ButtonType.OK) 
						{
							int productId = warehouseDSC.addProduct(comboBox.getValue().getName(), quantityInt, sectionChoiceBox.getValue());
							Product product = warehouseDSC.searchProduct(productId);
							tableData.add(product);
							hiddenContainer1.setVisible(false);
							hiddenContainer2.setVisible(false);
							comboBox.valueProperty().set(null);
							sectionChoiceBox.valueProperty().set(null);
							quantityTF.setText(null);
						}
						
					}
					catch(Exception searchProductException)
					{
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setContentText(searchProductException.getMessage());
						alert.showAndWait();
					}	
				}
				catch(Exception exception)
				{
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setContentText("Please enter an integer into the Quantity field");
					alert.showAndWait();
				}
				
			}


			
		});
		 	

		/* TODO 2-13 - TO COMPLETE ****************************************
		 * add input controls and container(s)
		 * - Item will list item data from the data source controller list
		 *   all items method
		 * - Section will list all sections defined in the data source
		 *   controller SECTION enum
		 * - Quantity: a texf field, self descriptive
		 * - CANCEL button: clears all input controls
		 * - SAVE button: sends the new product information to the data source
		 *   controller add product method; be mindful of exceptions when any
		 *   or all of the input controls are empty upon SAVE button click
		 */	

		// =====================================================================
		// SET UP the Stage
		// =====================================================================
		// Create scene and set stage
		

		addBT.setOnAction(e ->
		{
			hiddenContainer1.setVisible(true);
			hiddenContainer2.setVisible(true);
		});

		updateOneBT.setOnAction(e ->
		{
			Product product = tableView.getSelectionModel().getSelectedItem();
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setContentText("Are you sure? Update the selected product?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) 
			{
				try
				{
					warehouseDSC.useProduct(product.getId());
					product.updateQuantity();
					tableView.getColumns().get(0).setVisible(false);
					tableView.getColumns().get(0).setVisible(true);
	
				}
				catch(Exception exceptionUpdate)
				{
					Alert alert2 = new Alert(Alert.AlertType.ERROR);
					alert2.setContentText(exceptionUpdate.getMessage());
					alert2.showAndWait();
				}
			}


		});

		deleteBT.setOnAction(e ->
		{
			Product product = tableView.getSelectionModel().getSelectedItem();
			try
			{
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setContentText("Are you sure? Delete selected product?");
				Optional<ButtonType> result = alert.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.OK) 
				{
					warehouseDSC.removeProduct(product.getId());
					tableData.remove(product);
					tableView.getColumns().get(0).setVisible(false);
					tableView.getColumns().get(0).setVisible(true);
				}
			}
			catch(Exception exceptionDelete)
			{
				Alert alert2 = new Alert(Alert.AlertType.ERROR);
				alert2.setContentText(exceptionDelete.getMessage());
				alert2.showAndWait();
			}
		});

		clearBT.setOnAction(e ->
		{
			comboBox.valueProperty().set(null);
			sectionChoiceBox.valueProperty().set(null);
			quantityTF.setText(null);
		});

		VBox root = new VBox();
		root.getChildren().add(filterHBox);
		root.getChildren().add(tableView);
		root.getChildren().add(addUpdateDeleteHBox);
		root.getChildren().add(hiddenContainer1);
		root.getChildren().add(hiddenContainer2);

		/* TODO 2-14 - TO COMPLETE ****************************************
		 * - add all your containers, controls to the root
		 */		

		root.setStyle(
			"-fx-font-size: 20;" +
			"-fx-alignment: center;"
		);

		Scene scene = new Scene(root);
		stage.setScene(scene);
	}

	public void stop() throws Exception {

		/* TODO 2-15 - TO COMPLETE ****************************************
		 * call the data source controller database disconnect method
		 * NOTE: that database disconnect method throws exception
		 */
		try
		{
			warehouseDSC.disconnect();
		}
		catch(Exception e)
		{
			System.out.println("ERROR: " + e);
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("ERROR: " + e);
			alert.showAndWait();
		}

	}	
}
