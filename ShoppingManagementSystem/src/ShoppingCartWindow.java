
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCartWindow extends JFrame {

    //  Swing components for the UI
    private JTable cartTable;
    private DefaultTableModel cartTableModel;
    private final ShoppingCart shoppingCart;
    private JLabel finalTotalLabel;
    private JLabel threeItemsDiscountLabel;
    private JPanel discountPanel;
    private JLabel totalLabel;


    // Constructor with the ShoppingCart parameter
    public ShoppingCartWindow(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;

        initializeUI();
    }

    // Method to initialize the UI components
    private void initializeUI() {
        // Set  title and the size of window
        setTitle("Shopping Cart");
        setSize(700, 500);

        //configure the table model for the cart
        cartTableModel = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Return the column class as Object
                return Object.class;
            }
//

            //override
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make cells in non-editable
                return false;
            }
        };

        // get table col names
        cartTableModel.addColumn("Product");
        cartTableModel.addColumn("Quantity");
        cartTableModel.addColumn("Price");

        // the cart table with the table model and set row height
        cartTable = new JTable(cartTableModel);
        cartTable.setRowHeight(40);

        // create table headlines with bold
        JTableHeader tableHeader = cartTable.getTableHeader();
        tableHeader.setFont(tableHeader.getFont().deriveFont(Font.BOLD));

        // Set layout
        setLayout(new BorderLayout());

        //  add scroll panel to the cart table
        JScrollPane tableScrollPane = new JScrollPane(cartTable);

        // Add the scroll pane to the center of the frame
        add(tableScrollPane, BorderLayout.CENTER);

        //displaying discount information
        totalLabel = new JLabel("Total");
        threeItemsDiscountLabel = new JLabel("Three Items in same Category Discount(20%)");
        finalTotalLabel = new JLabel("Final Total");

        // the discount labels to set layout manager
        discountPanel = new JPanel(new GridLayout(4, 2));
        discountPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        discountPanel.add(totalLabel);
        discountPanel.add(threeItemsDiscountLabel);
        discountPanel.add(finalTotalLabel);
        // set discount panel position in to south
        add(discountPanel, BorderLayout.SOUTH);

        // Set close operation
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

    }

    // Method the cart table with the current shopping cart items
    public void updateShoppingCartTable(List<Product> cartItems) {
        // Clear table model using the existing data
        DefaultTableModel model = (DefaultTableModel) cartTable.getModel();
        model.setRowCount(0);

        // Make a mapping to monitor the table's product rows.
        Map<String, Integer> productRowMap = new HashMap<>();
        // key= productID, Value

        // Add/update items in the table as iterate through the cart
        for (Product product : cartItems) {
            String productId = product.getProductId();
            int quantityOfProduct = shoppingCart.getProductQuantity(product);
            double totalPrice = product.getPrice() * quantityOfProduct;

            // Verify if the product is already in the table
            if (productRowMap.containsKey(productId)) {
                //If yes, Total price
                int rowIndex = productRowMap.get(productId);
                model.setValueAt(3, rowIndex, 1);  // Quantity
                model.setValueAt(totalPrice, rowIndex, 2);  // Total Price
            } else {
                // If no, make a new row and put the product's custom string in it.
                String productInformation = getProductInformationString(product);
                Object[] rowData = new Object[]{productInformation, quantityOfProduct, totalPrice};
                model.addRow(rowData);
                productRowMap.put(productId, model.getRowCount() - 1);
            }
        }

        // Remove all the current discountPanel components.
        discountPanel.removeAll();

        // Calculate given discounts and final total
        double totalCost = shoppingCart.calculateTotalCost();

        // Add labels with discount-related information to the discountPanel
        addLabelWithMargin(discountPanel, "Total", SwingConstants.RIGHT);
        addLabelWithMargin(discountPanel, String.format("%.2f$", totalCost), SwingConstants.LEFT);

        double categoryOfDiscount = shoppingCart.hasThreeItemsInSameCategory() ? 0.2 * totalCost : 0;
        double finalTotal = totalCost - categoryOfDiscount;

        addLabelWithMargin(discountPanel, "Three Items in Same Category Discount (20%)", SwingConstants.RIGHT);
        addLabelWithMargin(discountPanel, String.format("- %.2f$", categoryOfDiscount), SwingConstants.LEFT);

        addLabelWithMargin(discountPanel, "Final Total", SwingConstants.RIGHT);
        addLabelWithMargin(discountPanel, String.format("%.2f$", finalTotal), SwingConstants.LEFT);

        discountPanel.revalidate();
        discountPanel.repaint();
    }

    // create label within the margin
    private void addLabelWithMargin(JPanel panel, String text, int alignment) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(alignment);
        label.setBorder(BorderFactory.createEmptyBorder(15, 35, 0, 0));
        panel.add(label);
    }

    // Helper method to get a custom string based on product category
    private String getProductInformationString(Product product) {
        if (product instanceof Electronics) {
            Electronics electronicsProduct = (Electronics) product;
            return String.format("%s, %s, %s, %s",
                    electronicsProduct.getProductId(),
                    electronicsProduct.getProductName(),
                    electronicsProduct.getBrand(),
                    electronicsProduct.getWarrantyPeriod());
        } else if (product instanceof Clothing) {
            Clothing clothingProduct = (Clothing) product;
            return String.format("%s, %s, %s, %s",
                    clothingProduct.getProductId(),
                    clothingProduct.getProductName(),
                    clothingProduct.getSize(),
                    clothingProduct.getColor());
        } else {
            return "";
        }
    }

}
