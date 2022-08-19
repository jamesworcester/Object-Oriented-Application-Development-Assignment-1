import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.*;
import java.io.*;
import javafx.collections.*;
import javafx.collections.transformation.*;
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
			System.out.println(e);
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
		idColumn.setMinWidth(100);
		idColumn.setMaxWidth(100);
		itemNameColumn.setMinWidth(300);
		itemNameColumn.setMaxWidth(300);
		quantityColumn.setMinWidth(100);
		quantityColumn.setMaxWidth(100);
		sectionColumn.setMinWidth(200);
		sectionColumn.setMaxWidth(200);
		daysAgoColumn.setMinWidth(300);
		daysAgoColumn.setMaxWidth(300);


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
		Label filterLB = new Label("Filter By:");

		String choiceITEM = "ITEM";
		String choiceSECTION = "SECTION";
		String choiceBOUGHT_DAYS_AGO = "BOUGHT_DAYS_AGO";
		ChoiceBox<String> choiceBox = new ChoiceBox<String>();
		choiceBox.getItems().addAll(choiceITEM, choiceSECTION, choiceBOUGHT_DAYS_AGO);

		choiceBox.setValue(choiceITEM);

		 CheckBox checkBox = new CheckBox("Show Expire Only");
		 checkBox.setDisable(true);
	

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
		Button AddBT = new Button("ADD");
		Button UpdateOneBT = new Button("UPDATE ONE");
		Button DeleteBT = new Button("DELETE");

		List<Item> items = warehouseDSC.getAllItems();
		ComboBox<Item> comboBox = new ComboBox<Item>();
		comboBox.getItems().addAll(items);

		ChoiceBox<WarehouseDSC.SECTION> sectionChoiceBox = new ChoiceBox<WarehouseDSC.SECTION>();
		sectionChoiceBox.getItems().setAll(WarehouseDSC.SECTION.values());
		TextField quantityTF = new TextField();

		Button ClearBT = new Button("CLEAR");
		Button SaveBT = new Button("SAVE");
		
		SaveBT.setOnAction(e ->
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
					int productId = warehouseDSC.addProduct(comboBox.getValue().getName(), quantityInt, sectionChoiceBox.getValue());
					Product product = warehouseDSC.searchProduct(productId);
					tableData.add(product);
				}
				catch(Exception exception)
				{
					System.out.println("ERROR: Could not parse Int from quantityTF");
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
		HBox filterHBox = new HBox(filterTF, filterLB, choiceBox, checkBox);

		HBox addUpdateDeleteHBox = new HBox(AddBT, UpdateOneBT, DeleteBT);

		HBox hiddenContainer1 = new HBox(comboBox, sectionChoiceBox, quantityTF);
		hiddenContainer1.setVisible(false);

		HBox hiddenContainer2 = new HBox(ClearBT, SaveBT);
		hiddenContainer2.setVisible(false);

		AddBT.setOnAction(e ->
		{
			hiddenContainer1.setVisible(true);
			hiddenContainer2.setVisible(true);
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
	}	
}
