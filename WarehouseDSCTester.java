import java.util.List;

public class WarehouseDSCTester {
    public static void main(String[] args) throws Exception {
        WarehouseDSC myWarehouseDSC = new WarehouseDSC();
        myWarehouseDSC.connect();

        // System.out.println("WarehouseDSC Test:");
        // System.out.println("TESTING searchItem");
        // System.out.println("Query: SELECT * FROM item WHERE name = ?");
        // Item item1 = myWarehouseDSC.searchItem("Frozen Yogurt");
        // System.out.println("item1 result: " + item1);
        // Item item2 = myWarehouseDSC.searchItem("Fish");
        // System.out.println("item2 result: " + item2);
        // Item item3 = myWarehouseDSC.searchItem("Gorgonzola");
        // System.out.println("item3 result: " + item3);

        // System.out.println("\n\nTESTING searchProduct");
        // System.out.println("Query: SELECT * FROM product WHERE id = ?");
        // Product product1 = myWarehouseDSC.searchProduct(19);
        // System.out.println("product1 result: " + product1);
        // Product product2 = myWarehouseDSC.searchProduct(53);
        // System.out.println("product2 result: " + product2);
        // Product product3 = myWarehouseDSC.searchProduct(57);
        // System.out.println("product3 result: " + product3);

        // System.out.println("\n\nTESTING getAllItems");
        // System.out.println("Query: SELECT * FROM item");
        // List<Item> items = myWarehouseDSC.getAllItems();
        // System.out.println(items);

        // System.out.println("\n\nTESTING getAllProducts");
        // System.out.println("Query: SELECT * FROM product");
        // List<Product> products = myWarehouseDSC.getAllProducts();
        // System.out.println(products);

        // System.out.println("\n\nTESTING addProduct");
        // System.out.println("\n\nQuery: INSERT INTO product (itemName, date, quantity, section) VALUES (?, ?, ?, ?)");
        // int newId1 = myWarehouseDSC.addProduct("Beef", 1, WarehouseDSC.SECTION.MEAT);
        // Product product4 = myWarehouseDSC.searchProduct(newId1);
        // System.out.println("product4 result: " + product4);
        // int newId2 = myWarehouseDSC.addProduct("Broccoli", 2, WarehouseDSC.SECTION.CRISPER);
        // Product product5 = myWarehouseDSC.searchProduct(newId2);
        // System.out.println("product5 result: " + product5);
        // int newId3 = myWarehouseDSC.addProduct("Cabbage", 3, WarehouseDSC.SECTION.COOLING);
        // Product product6 = myWarehouseDSC.searchProduct(newId3);
        // System.out.println("product6 result: " + product6);
        // int newId4 = myWarehouseDSC.addProduct("Pecorino", 4, WarehouseDSC.SECTION.FREEZER);
        // Product product7 = myWarehouseDSC.searchProduct(newId4);
        // System.out.println("product7 result: " + product7);
        // int newId5 = myWarehouseDSC.addProduct("Tangerines", 5, WarehouseDSC.SECTION.FREEZER);
        // Product product8 = myWarehouseDSC.searchProduct(newId5);
        // System.out.println("product8 result: " + product8);
        // int newId6 = myWarehouseDSC.addProduct("Tofu", 6, WarehouseDSC.SECTION.FREEZER);
        // Product product9 = myWarehouseDSC.searchProduct(newId6);
        // System.out.println("product9 result: " + product9);

        // System.out.println("\n\nTESTING useProduct");
        // System.out.println("Query UPDATE product SET quantity = quantity - 1 WHERE quantity > 1 AND id = 'id';");
        // myWarehouseDSC.useProduct(262);
        // Product product10 = myWarehouseDSC.searchProduct(262);
        // System.out.println("product10 result: " + product10);
        // myWarehouseDSC.useProduct(263);
        // Product product11 = myWarehouseDSC.searchProduct(263);
        // System.out.println("product11 result: " + product11);
        // myWarehouseDSC.useProduct(264);
        // Product product12 = myWarehouseDSC.searchProduct(264);
        // System.out.println("product12 result: " + product12);

        // System.out.println("\n\nTESTING removeProduct");
        // System.out.println("Query: DELETE FROM product WHERE id = 'id'");
        // myWarehouseDSC.removeProduct(262);
        // myWarehouseDSC.removeProduct(263);
        // myWarehouseDSC.removeProduct(264);

        myWarehouseDSC.disconnect();
    }
}
