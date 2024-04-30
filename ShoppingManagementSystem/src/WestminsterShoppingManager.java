import java.io.*;
import java.util.*;

public class WestminsterShoppingManager implements ShoppingManager{

    private List<Product> productList;

    // Constructor initializes the product list as an ArrayList
    public WestminsterShoppingManager() {
        this.productList = new ArrayList<>();
    }

    // Method to add a product
    @Override
    public void addProductToSystem() {
        Scanner input =new Scanner(System.in);
        int option;
        if (productList.size()<50){  //cant be equal to 50
            while (true){
                System.out.print("Enter Product type ( 1 for Electronics, 2 for Clothing): ");
                try{
                    option= input.nextInt();
                    if (option ==1 || option == 2){
                        break;
                    } else {
                        System.out.println("Invalid Input.Please Enter valid input!");
                    }
                }catch (InputMismatchException e){
                    System.out.println("Invalid Input. please enter a valid integer.");
                    input.next(); // consume the invalid input
                }
            }
            // Call appropriate method based on user input
            if (option == 1){
                addElectronicProducts(input);
            }else {
                addClothingProducts(input);
            }
        }else {
            System.out.println("Maximum limit of products reached. Cannot add more.");
        }
    }

    // Method to add electronics product
    private void addElectronicProducts(Scanner input) {
        // Variable declarations
        String proName;
        String proID;
        double price;
        int warrantyPeriod;
        String brand;
        int AvailableProducts;

        // Enter Electronic Product
        System.out.println("Add Electronics Product: ");

        //Enter Valid Id
        System.out.print("Enter Product ID: ");
        proID= input.next();

        //Enter valid product name
        System.out.print("Enter Product Name: ");
        proName= input.next();

        // Validate and get the number of available items
        do {
            System.out.print("Enter Number of Available items: ");
            while (!input.hasNextInt()) {
                System.out.println("Please enter a valid integer.");
                input.next(); // consume the invalid input
            }
            AvailableProducts = input.nextInt();
            if (AvailableProducts <= 0) {
                System.out.println("Please enter a valid number.");
            }
        } while (AvailableProducts <= 0);

        // Validate and get the price
        do {
            System.out.print("Enter the Price: ");
            while (!input.hasNextDouble()) {
                System.out.println("Please enter a valid input for the price.");
                input.next(); // consume the invalid input
            }
            price = input.nextDouble();
            if (price <= 0) {
                System.out.println("Please enter a valid price.");
            }
        } while (price <= 0);

        // Get the brand
        System.out.print("Enter the Brand: ");
        brand= input.next();

        // Validate and get the warranty period
        do {
            System.out.print("Enter the warranty period (in Months): ");
            while (!input.hasNextInt()) {
                System.out.println("Please enter a valid integer for the warranty period.");
                input.next(); // consume the invalid input
            }
            warrantyPeriod = input.nextInt();
            if (warrantyPeriod <= 0) {
                System.out.println("Please enter a valid number.");
            }
        } while (warrantyPeriod <= 0);

        // Create Electronics object and add to the product list
        Electronics electronics = new Electronics(proID,proName,AvailableProducts,price,brand,warrantyPeriod);
        productList.add(electronics);
    }

    // Method to add clothing products
    private void addClothingProducts(Scanner input) {
        // Variable declarations
        String proID;
        String proName;
        double price;
        int AvailableProducts;
        double size;
        String colour;

        // Enter Clothing Product
        System.out.println("Add Clothing Product: ");

        // Get the product ID
        System.out.print("Enter Product ID: ");
        proID= input.next();

        // Get the product name
        System.out.print("Enter Product Name: ");
        proName= input.next();

        // Validate and get the number of available items
        do {
            System.out.print("Enter Number of Available items: ");
            while (!input.hasNextInt()){
                System.out.println("Please enter a valid integer.");
                input.next();
            }
            AvailableProducts = input.nextInt();
            if (AvailableProducts<=0){
                System.out.println("Please enter a valid number.");
            }
        }while (AvailableProducts<=0);

        // Validate and get the price
        do {
            System.out.print("Enter the Price: ");
            while (!input.hasNextDouble()){
                System.out.println("Please enter a valid input for the price.");
                input.next();
            }
            price= input.nextDouble();
            if (price<=0){
                System.out.println("Please enter a valid price.");
            }
        }while (price<=0);

        // Validate and get the size
        do {
            System.out.print("Enter the Size: ");
            while (!input.hasNextDouble()) {
                System.out.println("Please enter a valid input for the size.");
                input.next(); // consume the invalid input
            }
            size = input.nextDouble();
            if (size <= 0) {
                System.out.println("Size must be greater than 0. Please enter a valid size.");
            }
        } while (size <= 0);

        // Get the color
        System.out.print("Enter the colour: ");
        colour= input.next();

        // Create Clothing object and add to the product list
        Clothing clothing =new Clothing(proID,proName,AvailableProducts,price,size,colour);
        productList.add(clothing);
    }

    // Method to remove a product
    @Override
    public void removeProductFromSystem() {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the Product ID to Remove: ");
        String productIdToRemove = input.next();

        boolean found = false;
        // Iterate through the product list to find and remove the product
        for (Product product : productList) {
            if (product.getProductId().equals(productIdToRemove)) {
                productList.remove(product);
                found = true;
                System.out.println("Product removed Successfully");
                System.out.println("Total Number of products left: " + productList.size());
                break; // Exit the loop after removing the product
            }
        }

        // Display message if the product is not found
        if (!found) {
            System.out.println("Product Not Found! Please enter Valid ID.");
        }

        // Display the remaining products after removal
        for (Product product : productList) {
            System.out.println(product);
        }

    }

    // Method to print the product list in the system
    @Override
    public void printProductListInSystem() {
        System.out.println("Products in the list:");
        // Sort the product list based on product ID
        productList.sort(Comparator.comparing(Product::getProductId));

        // Iterate through the product list and print details based on product type
        for (Product product : productList) {
            System.out.println("Product Type: " + product.getProductType());
            System.out.println("Product ID: " + product.getProductId());
            System.out.println("Product Name: " + product.getProductName());
            System.out.println("Available Items: " + product.getAvailableItems());
            System.out.println("Price: " + product.getPrice());

            // Check the product type and print additional details accordingly
            if (product instanceof Electronics) {
                System.out.println("Brand: " + ((Electronics) product).getBrand());
                System.out.println("Warranty Period: " + ((Electronics) product).getWarrantyPeriod() + " months\n");

            } else if (product instanceof Clothing) {
                System.out.println("Size: " + ((Clothing) product).getSize());
                System.out.println("Color: " + ((Clothing) product).getColor() +"\n" );
            }

            System.out.println();
        }

    }

    // Implementation of the saveToFile method from the ShoppingManager interface
    @Override
    public void saveToFile() {
        try (BufferedWriter writer =new BufferedWriter(new FileWriter("saveData.txt"))) {
            // Iterate through the product list and write each product's details to the file
            for (Product product : productList){
                writer.write(product.toString());
                writer.newLine();
            }
            System.out.println("Product list saved to file saveData.txt Successfully.");


        } catch (FileNotFoundException e){
            System.out.println("File not found : saveData.txt");
        } catch (IOException e) {
            System.out.print("Error " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to read product details from a file and update the product list
    public void readFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("saveData.txt"))) {
            String line;

            // Read each line from the file and update the product list
            while ((line = reader.readLine()) != null) {
                // Assuming toString() method in Product class returns a formatted string
                updateProductListFromFile(line);

            }
            System.out.println("Product list loaded from file successfully");
        } catch (FileNotFoundException e){
            System.out.println("saveData.txt File Not Found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to update the product list based on the data read from the file
    private void updateProductListFromFile(String line) {
        // Split the line into parts using a comma as the separator
        try {

            String[] parts = line.split(",\\s*");

            // Check if the number of parts is less than 6
            if (parts.length < 6) {
                System.out.println("Invalid product format: " + line);
                return;
            }

            // Extract product details from the parts array
            String productIdFromFile = parts[0];
            String productNameFromFile = parts[1];
            int availableItemsFromFile = Integer.parseInt(parts[2]);
            double priceFromFile  = Double.parseDouble(parts[3]);


            //determine the product type according to the data type  in the 5th place
            if (parts[4].matches("\\d*\\.?\\d+")){   //if the 5th place is a double it is a Clothing product (size)
                double sizeFromFile = Double.parseDouble(parts[4]);
                String colorFromFile = parts[5];

                // Create a Clothing object and add it to the product list
                Clothing clothing=new Clothing(productIdFromFile, productNameFromFile, availableItemsFromFile, priceFromFile, sizeFromFile, colorFromFile);
                productList.add(clothing);

            }else {
                // If the 5th place is not a double, it is an Electronics product
                String brandFromFile = parts[4];
                int warrantyFormFile = Integer.parseInt(parts[5]);

                // Create an Electronics object and add it to the product list
                Electronics electronics=new Electronics(productIdFromFile, productNameFromFile, availableItemsFromFile, priceFromFile, brandFromFile, warrantyFormFile);
                productList.add(electronics);
            }
        }catch (NumberFormatException e){
            System.out.println("Error passing from file." );
        }

    }

}
