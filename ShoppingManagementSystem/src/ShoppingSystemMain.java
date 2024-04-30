
import javax.swing.*;
import java.util.Scanner;

public class ShoppingSystemMain {
    private static void customer(WestminsterShoppingManager westminsterShoppingManager) {
        SwingUtilities.invokeLater(() -> {
            ShoppingGUI shoppingGUI = new ShoppingGUI(westminsterShoppingManager);
            shoppingGUI.setVisible(true);
        });
    }

    public static void main(String[] args) {
        System.out.println("******Welcome to Shopping Manager System******");
        shoppingManager();
    }

    //Main method to the system for run
    private static void shoppingManager() {
        WestminsterShoppingManager shoppingSystemManager =new WestminsterShoppingManager();
        Scanner input=new Scanner(System.in);
        int option=1;
        ( shoppingSystemManager).readFromFile();// read file form here
        while (option !=0){
            System.out.println("\n***************************************************");
            System.out.println("Please Select The option: ");
            System.out.println("1. Add a new Product to the system: ");
            System.out.println("2. Delete a Product from the system:");
            System.out.println("3. Display the list of products");
            System.out.println("4. Save in to the file ");
            System.out.println("5. Customer(GUI)");
            System.out.println("***************************************************");
            do{ //option for the validation
                System.out.print("Enter option: ");
                if (input.hasNextInt()){
                    option= input.nextInt();
                    if (option<6 && option>-1){
                        break;
                    }
                }input.nextLine();
                System.out.println("Invalid input.Please enter again.");
            }while (true);
            switch (option) {
                case 1 -> shoppingSystemManager.addProductToSystem();
                case 2 -> shoppingSystemManager.removeProductFromSystem();
                case 3 -> shoppingSystemManager.printProductListInSystem();
                case 4 -> shoppingSystemManager.saveToFile();
                case 5 -> customer(shoppingSystemManager);
            }
        }
    }
}
