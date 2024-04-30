import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {

    // Instance variables for list of product
    private List<Product> products;
    private boolean firstOfPurchase;

    // Instance variables for list of product
    private Map<String, Integer> categoryOfCountMap;
    private Map<Product, Integer> productQuantityMap;


    // Constructor but Not Parameterized
    public ShoppingCart() {
        this.products = new ArrayList<>();
        this.categoryOfCountMap = new HashMap<>();
        this.productQuantityMap = new HashMap<>();
        this.firstOfPurchase = false; // Set to false initially

    }


    //  add new products to the cart
    public void addProduct(Product product) {
        //update the quantity
        int quantityOfProduct = productQuantityMap.getOrDefault(product, 0);
        productQuantityMap.put(product, quantityOfProduct + 1);

        // Update the category
        String category = getCategoryForProduct(product);
        updateCategoryCount(category, 1);
    }

    // update the count of category
    private void updateCategoryCount(String category, int countChange) {
        int currentCount = categoryOfCountMap.getOrDefault(category, 0);
        categoryOfCountMap.put(category, currentCount + countChange);
    }


    // Method to calculate the total cost of products in the cart
    public double calculateTotalCost() {
        double totalCost = 0;
        // Iterate through the entries of the productQuantityMap
        for (Map.Entry<Product, Integer> entry : productQuantityMap.entrySet()) {
            totalCost += entry.getKey().getPrice() * entry.getValue();
        }
        return totalCost;
    }


    // Method to check if any category has at least three items in the cart
    public boolean hasThreeItemsInSameCategory() {
        //
        //
        for (int count : categoryOfCountMap.values()) {
            if (count >= 3) {
                return true;
            }
        }
        return false;
    }

    //  category of a products  to system
    private String getCategoryForProduct(Product product) {
        // Implement logic to determine the category for a given product
        if (product instanceof Electronics) {
            return "Electronics";
        } else if (product instanceof Clothing) {
            return "Clothing";
        } else {
            return "None";
        }
        //implement the users
    }


    // Method to get the list of products in the cart
    //
    public List<Product> getCartItems() {
        List<Product> cartItems = new ArrayList<>();
        //Iterate through the entries of the productQuantityMap
        for (Map.Entry<Product, Integer> entry : productQuantityMap.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();

            for (int i = 0; i < quantity; i++) {
                cartItems.add(product);
            }
        }
        return cartItems;
    }

    // Method to show the quantity of a specific product in the cart
    public int getProductQuantity(Product product) {
        int quantity = productQuantityMap.getOrDefault(product, 0);
        return quantity;
    }


}
