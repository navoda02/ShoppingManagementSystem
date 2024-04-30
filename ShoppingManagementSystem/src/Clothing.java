public class Clothing extends Product {

    private  double size;
    private String color;

    public Clothing(String productId, String productName, int noOfAvailableItems, double price, double size, String color) {
        super(productId, productName, noOfAvailableItems, price);
        this.size = size;
        this.color = color;
    }

    @Override
    public String getProductType() {
        return "Clothing Products";
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %d, %.2f, %.2f, %s",
                getProductId(), getProductName(), getAvailableItems(), getPrice(), getSize(), getColor());
    }

}
