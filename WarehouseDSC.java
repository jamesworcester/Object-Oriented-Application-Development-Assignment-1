import java.sql.*;
import java.util.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

public class WarehouseDSC {

	// the date format we will be using across the application
	public static final String DATE_FORMAT = "dd/MM/yyyy";

	/*
		FREEZER, // freezing cold
		MEAT, // MEAT cold
		COOLING, // general Warehousearea
		CRISPER // veg and fruits section

		note: Enums are implicitly public static final
	*/
	public enum SECTION {
		FREEZER,
		MEAT,
		COOLING,
		CRISPER
	};

	private static Connection connection;
	private static Statement statement;
	private static PreparedStatement preparedStatement;

	public static void connect() throws SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");


			/* TODO 1-01 - TO COMPLETE ****************************************
			 * change the value of the string for the following 3 lines:
			 * - url
			 * - user
			 * - password
			 */			
			String url = "jdbc:mysql://localhost:3306/warehousedb";
			String user = "root";
			String password = "password";

			connection = DriverManager.getConnection(url, user, password);
			statement = connection.createStatement();
  		} catch(Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}		
	}

	public static void disconnect() throws SQLException {
		if(preparedStatement != null) preparedStatement.close();
		if(statement != null) statement.close();
		if(connection != null) connection.close();
	}



	public Item searchItem(String name) throws Exception {
		String queryString = "SELECT * FROM item WHERE name = ?";


		/* TODO 1-02 - TO COMPLETE ****************************************
		 * - preparedStatement to add argument name to the queryString
		 * - resultSet to execute the preparedStatement query
		 * - iterate through the resultSet result
		 */	
		preparedStatement = connection.prepareStatement(queryString);
		preparedStatement.setString(1, name);
		ResultSet rs = preparedStatement.executeQuery();

		Item item = null;

		if (rs.next()) { // i.e. the item exists

			/* TODO 1-03 - TO COMPLETE ****************************************
			 * - if resultSet has result, get data and create an Item instance
			 */
			boolean expires = rs.getBoolean(2);
			item = new Item(name, expires);
		}	

		return item;
	}

	public Product searchProduct(int id) throws Exception {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
		String queryString = "SELECT * FROM product WHERE id = ?";

		/* TODO 1-04 - TO COMPLETE ****************************************
		 * - preparedStatement to add argument name to the queryString
		 * - resultSet to execute the preparedStatement query
		 * - iterate through the resultSet result
		 */	
		preparedStatement = connection.prepareStatement(queryString);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery();

		Product product = null;

		if (rs.next()) { // i.e. the product exists

			/* TODO 1-05 - TO COMPLETE ****************************************
			 * - if resultSet has result, get data and create a product instance
			 * - making sure that the item name from product exists in 
			 *   item table (use searchItem method)
			 * - pay attention about parsing the date string to LocalDate
			 */
			Item item = searchItem(rs.getString(2));
			if(item == null) {
				// may need to add more here to handle null values
				throw new Exception("Exception: Product with id '"+id+"' does not have an associated Item name");
			}
			LocalDate date = LocalDate.parse(rs.getString(3), dtf);
			int quantity = rs.getInt(4);
			SECTION section = SECTION.valueOf(rs.getString(5));
			
			product = new Product(id, item, date, quantity, section);
		}

		return product;
	}

	public List<Item> getAllItems() throws Exception {
		String queryString = "SELECT * FROM item";

		/* TODO 1-06 - TO COMPLETE ****************************************
		 * - resultSet to execute the statement query
		 */	
		ResultSet rs = statement.executeQuery(queryString);

		List<Item> items = new ArrayList<Item>();

		/* TODO 1-07 - TO COMPLETE ****************************************
		 * - iterate through the resultSet result, create intance of Item
		 *   and add to list items
		 */	
		while (rs.next())
		{
			String name = rs.getString(1);
			Boolean expires = rs.getBoolean(2);

			items.add(new Item(name, expires));
		}

		return items;
	}

	public List<Product> getAllProducts() throws Exception {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
		String queryString = "SELECT * FROM product";

		/* TODO 1-08 - TO COMPLETE ****************************************
		 * - resultSet to execute the statement query
		 */	
		ResultSet rs = statement.executeQuery(queryString);

		List<Product> products = new ArrayList<Product>();

		/* TODO 1-09 - TO COMPLETE ****************************************
		 * - iterate through the resultSet result, create intance of Item
		 *   and add to list items
		 * - making sure that the item name from each product exists in 
		 *   item table (use searchItem method)
		 * - pay attention about parsing the date string to LocalDate
		 */	
		while (rs.next())
		{
			int id = rs.getInt(1);
			Item item = searchItem(rs.getString(2));
			if(item == null) {
				//may need to add more here to handle null values
				throw new Exception("Exception: Product with id '"+id+"' does not have an associated Item name");
			}
			LocalDate date = LocalDate.parse(rs.getString(3), dtf);
			int quantity = rs.getInt(4);
			SECTION section = SECTION.valueOf(rs.getString(5));

			products.add(new Product(id, item, date, quantity, section));
		}

		return products;
	}


	public int addProduct(String name, int quantity, SECTION section) throws Exception {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
		LocalDate date = LocalDate.now();
		String dateStr = date.format(dtf);
		
		// NOTE: should we check if itemName (argument name) exists in item table?
		//		--> adding a product with a non-existing item name should through an exception
		Item item = searchItem(name);
		if(item == null)
		{
			throw new Exception("Exception: No Item with name '"+name+"' exists in the item table");
		}


		//String command = "INSERT INTO Product VALUES(?, ?, ?, ?, ?)";
		String command = "INSERT INTO Product VALUES(default, ?, ?, ?, ?)";

		/* TODO 1-10 - TO COMPLETE ****************************************
		 * - preparedStatement to add arguments to the queryString
		 * - resultSet to executeUpdate the preparedStatement query
		 */
		preparedStatement = connection.prepareStatement(command);
		preparedStatement.setString(2, name);
		preparedStatement.setString(3, dateStr);
		preparedStatement.setInt(4, quantity);
		preparedStatement.setString(5, section.toString());
		preparedStatement.executeUpdate();

		// retrieving & returning last inserted record id
		ResultSet rs = statement.executeQuery("SELECT LAST_INSERT_ID()");
		rs.next();
		int newId = rs.getInt(1);

		return newId;		
	}

	public Product useProduct(int id) throws Exception {

		/* TODO 1-11 - TO COMPLETE ****************************************
		 * - search product by id
		 * - check if has quantity is greater one; if not throw exception
		 *   with adequate error message
		 */		
		Product product = searchProduct(id);
		if(product.getQuantity() <= 1)
		{
			throw new Exception("Exception: The product with '"+id+"' has a quantity less than or equal to 1");
		}

		String queryString = 
			"UPDATE product " +
			"SET quantity = quantity - 1 " +
			"WHERE quantity > 1 " + 
			"AND id = " + id + ";";


		/* TODO 1-12 - TO COMPLETE ****************************************
		 * - statement execute update on queryString
		 * - should the update affect a row search product by id and
		 *   return it; else throw exception with adequate error message
		 *
		 * NOTE: method should return instance of product
		 */	
		preparedStatement = connection.prepareStatement(queryString);
		preparedStatement.setInt(1, id);
		try {
			preparedStatement.executeUpdate();
		}
		catch (Exception e){
			new Exception("Exception: The update did not effect affect a row");
		}
		Product product_updated = searchProduct(id);
		return product_updated;

	}

	public int removeProduct(int id) throws Exception {
		String queryString = "DELETE FROM product WHERE id = " + id + ";";

		/* TODO 1-13 - TO COMPLETE ****************************************
		 * - search product by id
		 * - if product exists, statement execute update on queryString
		 *   return the value value of that statement execute update
		 * - if product does not exist, throw exception with adequate 
		 *   error message
		 *
		 * NOTE: method should return int: the return value of a
		 *		 stetement.executeUpdate(...) on a DELETE query
		 */	
		Product product = searchProduct(id);
		if(product == null)
		{
			throw new Exception("Exception: No product with ID:'"+id+"' exists in the Product table");
		}
		else
		{
			preparedStatement = connection.prepareStatement(queryString);
			preparedStatement.setInt(1, id);
			return preparedStatement.executeUpdate();
		}

	}

	// STATIC HELPERS -------------------------------------------------------

	public static long calcDaysAgo(LocalDate date) {
    	return Math.abs(Duration.between(LocalDate.now().atStartOfDay(), date.atStartOfDay()).toDays());
	}

	public static String calcDaysAgoStr(LocalDate date) {
    	String formattedDaysAgo;
    	long diff = calcDaysAgo(date);

    	if (diff == 0)
    		formattedDaysAgo = "today";
    	else if (diff == 1)
    		formattedDaysAgo = "yesterday";
    	else formattedDaysAgo = diff + " days ago";	

    	return formattedDaysAgo;			
	}

	// To perform some quick tests	
	public static void main(String[] args) throws Exception {
		WarehouseDSC myWarehouseDSC = new WarehouseDSC();

		myWarehouseDSC.connect();

		System.out.println("\nSYSTEM:\n");

		System.out.println("\n\nshowing all of each:");
		System.out.println(myWarehouseDSC.getAllItems());
		System.out.println(myWarehouseDSC.getAllProducts());

		int addedId = myWarehouseDSC.addProduct("Milk", 40, SECTION.COOLING);
		System.out.println("added: " + addedId);
		System.out.println("deleting " + (addedId - 1) + ": " + (myWarehouseDSC.removeProduct(addedId - 1) > 0 ? "DONE" : "FAILED"));
		System.out.println("using " + (addedId) + ": " + myWarehouseDSC.useProduct(addedId));
		System.out.println(myWarehouseDSC.searchProduct(addedId));

		myWarehouseDSC.disconnect();
	}
}