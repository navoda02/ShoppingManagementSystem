
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShoppingGUI extends JFrame{
    private JComboBox<String> productTypeComboBox;

    //Declare the variable that is to be used to populate the products table with the drop-down menu
    private JTable productTable;

    // Create variable for shopping cart button
    private JButton shoppingCartButton;

    // Create variable for shoppingCartWindow
    private final ShoppingCartWindow shoppingCartWindow;

    // Create variable for the product details panel
    private JPanel detailsPanel;

    // Create variable for shopping cart button
    private final ShoppingCart shoppingCart;

    // Constructor for loading the parts
    public ShoppingGUI(WestminsterShoppingManager westminsterShoppingManager) {
        shoppingCart = new ShoppingCart();
        shoppingCartWindow = new ShoppingCartWindow(shoppingCart);
        initializeUI();
        // Display all products by default
        loadAndDisplayProducts("All");
    }

    private void initializeUI() {

        //set layout and initial size
        setLayout(new BorderLayout());
        setSize(800, 700);

        // set title of the system
        setTitle("Westminster Shopping Centre");

        //Use FlowLayout to create a JPanel for the combo box and label
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 0, 0));

        // create a JLabel for show product category
        JLabel categoryLabel1 = new JLabel("Select Product Category ");
        topPanel.add(categoryLabel1);

        //Create for Product types and set initial product types
        productTypeComboBox = new JComboBox<>();
        setProductTypes();// set initial products
        topPanel.add(productTypeComboBox);

        //create productTypeBox for add an action listener to the JComboBox
        productTypeComboBox.addActionListener((ActionEvent e) -> {
            String selectedCategory = (String) productTypeComboBox.getSelectedItem();
            loadAndDisplayProducts(selectedCategory);
        });

        add(topPanel, BorderLayout.WEST);// the topPanel to the WEST position

        JPanel verticalPanel = new JPanel(new BorderLayout());

        //create JTable to display product information
        productTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(productTable);
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        productTable.setRowHeight(30);// row height

        // the column names with bold
        JTableHeader tableHeader = productTable.getTableHeader();
        Font headerFont = new Font(tableHeader.getFont().getName(), Font.BOLD, tableHeader.getFont().getSize());
        tableHeader.setFont(headerFont);

        // Center-align cell contents in the product table
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        productTable.setDefaultRenderer(Object.class, centerRenderer);

        // Add list selection listener to update details panel when a row is selected
        productTable.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting() && productTable.getSelectedRow() != -1) {
                int selectedRow = productTable.getSelectedRow();
                String productId = (String) productTable.getValueAt(selectedRow, 0);
                String category = (String) productTable.getValueAt(selectedRow, 2);
                String name1 = (String) productTable.getValueAt(selectedRow, 1);
                String[] values = productTable.getValueAt(selectedRow, 4).toString().split(", "); // Split the information  string
                // the itemsAvailable directly from the save file
                String itemsAvailable = getItemsAvailable(productId);
                updateDetailsPanel(productId, category, name1, values, itemsAvailable);
            }
        });

        // Add the product table to the center of the frame
        verticalPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Create a panel for displaying product details
        detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridLayout(6, 2));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        // Add the details panel to the south of the vertical panel
        verticalPanel.add(detailsPanel, BorderLayout.SOUTH);

        // Add the vertical panel to the south of the frame
        add(verticalPanel, BorderLayout.SOUTH);

        shoppingCartButton = new JButton("Shopping Cart");

        // Create a button for accessing the shopping cart
        Dimension buttonSize = new Dimension(150, 40);
        shoppingCartButton.setPreferredSize(buttonSize);

        // Create a panel for the shopping cart button and add it to the north of the frame
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(shoppingCartButton);

        add(buttonPanel, BorderLayout.NORTH);

        // Add action listener to the shopping cart button to open the shopping cart window
        shoppingCartButton.addActionListener((ActionEvent e) -> {
            shoppingCartWindow.updateShoppingCartTable(shoppingCart.getCartItems());
            shoppingCartWindow.setVisible(true);
        });

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    // Method to set product types in the combo box
    private void setProductTypes() {
        // Add product types to the JComboBox
        productTypeComboBox.addItem("All");
        productTypeComboBox.addItem("Electronics");
        productTypeComboBox.addItem("Clothing");
    }

    // Method to load and display products based on the selected category
    private void loadAndDisplayProducts(String category) {
        // Create a table model for the product table
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Product ID");
        model.addColumn("Name");
        model.addColumn("Category");
        model.addColumn("Price($)");
        model.addColumn("Info"); // This will include size, color, model, warranty

        // Read product information from the "saveData.txt" file
        try (BufferedReader reader = new BufferedReader(new FileReader("saveData.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (category.equals("All") || getCategory(values[5]).equals(category)) {
                    String productId = values[0];
                    String name = values[1];
                    String productCategory = getCategory(values[5]);
                    String price = values[3];
                    String info = getInfoString(productCategory, values);
                    model.addRow(new Object[]{productId, name, productCategory, price, info});
                }
            }
        } catch (IOException e) {
            // Handle IO exception
        }

        // Create a table sorter and set it to the product table
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        productTable.setRowSorter(sorter);

        // Add mouse listener to table header for sorting
        productTable.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int columnIndex = productTable.columnAtPoint(e.getPoint());
                sortProductTable(columnIndex, SortOrder.ASCENDING, sorter);
            }
        });

        // Set the model for the product table
        productTable.setModel(model);

        // Apply a custom cell renderer for availability coloring
        for (int i = 0; i < model.getColumnCount(); i++) {
            productTable.getColumnModel().getColumn(i).setCellRenderer(new AvailabilityRenderer());
        }
    }


    // Method to determine the category based on a string
    private String getCategory(String a){
        try{
            Double.parseDouble(a);
            return "Electronics";
        }catch(NumberFormatException e){
            return "Clothing";
        }
    }

    // Method to sort the product table based on a column index and sort order
    private void sortProductTable(int columnIndex, SortOrder sortOrder, TableRowSorter<TableModel> sorter) {
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(columnIndex, sortOrder));
        sorter.setSortKeys(sortKeys);
        sorter.sort();
    }

    // Method to generate an information string based on the category and values
    private String getInfoString(String category, String[] values) {
        switch (category) {
            case "Electronics":
                return values[4] + ", " + values[5] + " months warranty";
            case "Clothing":
                return values[4] + ", " + values[5];
            default:
                return "";
        }
    }

    // Method to update the details panel based on the selected product
    private void updateDetailsPanel(String productId, String category, String name, String[] values, String itemsAvailable) {

        // Remove all components from the details panel
        detailsPanel.removeAll();
        detailsPanel.setLayout(new GridLayout(0, 1));

        // Add labels with left alignment and increased line spacing
        addLabelWithMargin(detailsPanel, "<html><b>Selected Product - Details</b></html>", SwingConstants.LEFT);
        addLabelWithMargin(detailsPanel, "Product Id: " + productId, SwingConstants.LEFT);
        addLabelWithMargin(detailsPanel, "Category: " + category, SwingConstants.LEFT);
        addLabelWithMargin(detailsPanel, "Name: " + name, SwingConstants.LEFT);

        // Add specific details based on the category
        switch (category) {
            case "Electronics":
                addLabelWithMargin(detailsPanel, "Brand: " + values[0], SwingConstants.LEFT);
                addLabelWithMargin(detailsPanel, "Warranty: " + values[1], SwingConstants.LEFT);
                addLabelWithMargin(detailsPanel, "Items Available: " + itemsAvailable, SwingConstants.LEFT);
                break;
            case "Clothing":
                addLabelWithMargin(detailsPanel, "Size: " + values[0], SwingConstants.LEFT);
                addLabelWithMargin(detailsPanel, "Color: " + values[1], SwingConstants.LEFT);
                addLabelWithMargin(detailsPanel, "Items Available: " + itemsAvailable, SwingConstants.LEFT);
                break;

            default:
                break;
        }

        // Add button to add the product to the shopping cart
        JButton addToShoppingCartButton = new JButton("Add to Shopping Cart");
        Dimension addToCartButtonSize = new Dimension(200, 40);
        addToShoppingCartButton.setBounds(10, 150, addToCartButtonSize.width, addToCartButtonSize.height);

        // Add action listener to the button to add the product to the shopping cart
        addToShoppingCartButton.addActionListener((var e) -> {
            // Get the selected product details
            String productId1 = productTable.getValueAt(productTable.getSelectedRow(), 0).toString();
            String name1 = productTable.getValueAt(productTable.getSelectedRow(), 1).toString();
            String category1 = productTable.getValueAt(productTable.getSelectedRow(), 2).toString();
            double price = Double.parseDouble(productTable.getValueAt(productTable.getSelectedRow(), 3).toString());
            String infoColumnValue = productTable.getValueAt(productTable.getSelectedRow(), 4).toString();
            String[] infoValues = infoColumnValue.split(", ");
            String firstInfo = infoValues[0];
            String secondInfo = infoValues[1];
            int productAvailability = Integer.parseInt(getItemsAvailable(productId1).trim());

            // Create a product object based on the category
            Product selectedProduct = null;
            switch (category1) {
                case "Electronics":
                    selectedProduct = new Electronics(productId1, name1, productAvailability, price, firstInfo, extractWarrantyPeriod(secondInfo));
                    break;
                case "Clothing":
                    selectedProduct = new Clothing(productId1, name1, productAvailability, price, Double.parseDouble(firstInfo), secondInfo);
                    break;
                default:
                    System.out.println("Invalid product type. Please try again.");
            }

            // Add the selected product to the shopping cart
            shoppingCart.addProduct(selectedProduct);

            // Print a message indicating the product has been added to the shopping cart
            System.out.println("Product added to the shopping cart: " + selectedProduct.getProductId());
        });

        // Add the "Add to Shopping Cart" button to the details panel
        detailsPanel.add(addToShoppingCartButton);

        // Update the details panel
        detailsPanel.revalidate();
        detailsPanel.repaint();
    }


    // Method to extract the warranty period from a string
    private int extractWarrantyPeriod(String secondInfo) {
        // Assuming secondInfo is "3 months warranty"
        Pattern pattern = Pattern.compile("\\b(\\d+)\\b");
        Matcher matcher = pattern.matcher(secondInfo);

        // Check if there is a match
        int warrantyPeriod = 0;
        if (matcher.find()) {
            String extractedNumber = matcher.group(1);
            warrantyPeriod = Integer.parseInt(extractedNumber);
            System.out.println("Warranty Period: " + warrantyPeriod);
        } else {
            System.out.println("No warranty information found");
        }
        return warrantyPeriod;
    }

    // Method to add a labeled component to a panel with margin
    private void addLabelWithMargin(JPanel panel, String text, int alignment) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(alignment);
        label.setBorder(BorderFactory.createEmptyBorder(12, 30, 0, 0));
        panel.add(label);
    }


    // Method to get the available items for a given product ID
    private String getItemsAvailable(String productId) {
        try (BufferedReader reader = new BufferedReader(new FileReader("saveData.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (productId.equals(values[0])) {
                    return values[2];
                }
            }
        } catch (IOException e) {
            // Handle IO exception
        }
        return "";
    }


    // Custom cell renderer for highlighting low availability with a light red color
    private class AvailabilityRenderer extends DefaultTableCellRenderer {

        private final Color LIGHT_RED = new Color(255, 200, 200);

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Get the product ID for the current row
            String productId = table.getValueAt(row, 0).toString();
            String availabilityString = getItemsAvailable(productId);

            // Parse availability as an integer (default to 0 if parsing fails)
            int availability = 0;
            try {
                availability = Integer.parseInt(availabilityString.trim());
            } catch (NumberFormatException e) {
            }

            // Set background color based on availability
            if (availability < 3) {
                cellComponent.setBackground(LIGHT_RED);
            } else {
                cellComponent.setBackground(table.getBackground());
            }

            return cellComponent;
        }
    }

}
