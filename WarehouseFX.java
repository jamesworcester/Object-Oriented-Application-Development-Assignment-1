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
		warehouseDSC.connect();

	}

	public void start(Stage stage) throws Exception {

		/* TODO 2-02 - TO COMPLETE ****************************************
		 * - this method is the start method for your application
		 * - set application title
		 * - show the stage
		 */
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
		TableColumn<Product, Integer> idColumn = new TableColumn<Product, Integer>("Id");
		//TableColumn<Product, String> idColumn = new TableColumn<Product, String>("Id");
		TableColumn<Product, String> itemNameColumn = new TableColumn<Product, String>("Item");
		TableColumn<Product, Integer> quantityColumn = new TableColumn<Product, Integer>("QTY");
		TableColumn<Product, String> sectionColumn = new TableColumn<Product, String>("Section");
		TableColumn<Product, String> daysAgoColumn = new TableColumn<Product, String>("Bought");
		
		/* TODO 2-04 - TO COMPLETE ****************************************
		 * for each column defined, call their setCellValueFactory method 
		 * using an instance of PropertyValueFactory
		 */
		idColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("Id"));
		itemNameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("Item"));
		quantityColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("QTY"));
		sectionColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("Section"));
		daysAgoColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("Bought"));

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
		itemNameColumn.setMinWidth(150);
		itemNameColumn.setMaxWidth(150);
		quantityColumn.setMinWidth(50);
		quantityColumn.setMaxWidth(50);
		sectionColumn.setMinWidth(100);
		sectionColumn.setMaxWidth(100);
		daysAgoColumn.setMinWidth(100);
		daysAgoColumn.setMaxWidth(100);


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
		choiceBox.setDisable(true);

		CheckBox checkBox = new CheckBox("Show Expire Only");
	

		/* TODO 2-09 - TO COMPLETE ****************************************
		 * filter container - part 2:
		 * - addListener to the "Filter By" ChoiceBox to clear the filter
		 *   text field vlaue and to enable the "Show Expire Only" CheckBox
		 *   if "BOUGHT_DAYS_AGO" is selected
		 */
		choiceBox.getSelectionModel().selectedIndexProperty().addListener(
			(ov, oldValue, newValue) ->
			{
				if(choiceBox.getValue() == choiceBOUGHT_DAYS_AGO)
				{
					filterTF.clear();
					filterTF.requestFocus();
					checkBox.setDisable(false);
				}
			}
		);

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
				if(newValue == null || newValue.isEmpty())
				{
					return true;
				}

				String filterString = newValue.toUpperCase();

				if(choiceBox.getValue() == choiceITEM && product.getItemName().toUpperCase().contains(filterString))
				{
					return true;
				}
				
				
				if(choiceBox.getValue() == choiceSECTION && product.getSection().toString().toUpperCase().contains(filterString))
				{
					return true;
				}

				if(choiceBox.getValue() == choiceBOUGHT_DAYS_AGO  && product.getDateStr().toUpperCase().contains(filterString))
				{
					return true;
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
		VBox root = new VBox();

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
