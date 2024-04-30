
public class Electronics extends Product {

    private String brand;
    private int warrantyPeriod;

    public Electronics(String productId, String productName, int noOfAvailableItems, double price , String brand, int warrantyPeriod) {
        super(productId, productName, noOfAvailableItems, price);
        this.brand = brand;
        this.warrantyPeriod = warrantyPeriod;
    }

    @Override
    public String getProductType() {
        return "Electronics Products";
    }

    public int getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(int warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %d, %.2f, %s, %d",
                getProductId(), getProductName(), getAvailableItems(), getPrice(), getBrand(), getWarrantyPeriod());
    }

}
