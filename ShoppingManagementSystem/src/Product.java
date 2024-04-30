
abstract class Product {

    //    variables
    private String proId;
    private String proName;
    private double price;
    private int AvailItems;


    //constructor
    public Product(String proId, String proName, int AvailItems, double price) {
        this.proId = proId;
        this.proName = proName;
        this.AvailItems = AvailItems;
        this.price = price;
    }

    public abstract String getProductType();


    //getters and setters
    //ProductID
    public String getProductId() {
        return proId;
    }

    public void setProductId(String productId) {
        this.proId = productId;
    }

    //ProductName
    public String getProductName() {
        return proName;
    }

    public void setProductName(String productName) {
        this.proName = productName;
    }

    //Price
    public double getPrice() {

        return price;
    }

    public void setPrice(double price) {

        this.price = price;
    }

    //AvailableItems
    public int getAvailableItems() {
        return AvailItems;
    }

    public void setAvailableItems(int noOfAvailableItems) {

        this.AvailItems = noOfAvailableItems;
    }

    public String toString(){  //to write the file as a human-readable manner
        return String.format("%s, %s, %d, %.2f",
                getProductId(), getProductName(), getAvailableItems(), getPrice());
    }

}
