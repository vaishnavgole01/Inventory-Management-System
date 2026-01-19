import java.util.*;

// =================== PRODUCT CLASS ===================
class Product implements Comparable<Product> {
    private String sku;
    private String name;
    private String category;
    private double price;
    private int quantity;
    private Date lastUpdated;

    public Product(String sku, String name, String category, double price, int quantity) {
        this.sku = sku;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.lastUpdated = new Date();
    }

    // Natural ordering by SKU
    @Override
    public int compareTo(Product other) {
        return this.sku.compareTo(other.sku);
    }

    // Override equals and hashCode for HashSet uniqueness
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return sku.equals(product.sku);
    }

    @Override
    public int hashCode() {
        return sku.hashCode();
    }

    // Getters
    public String getSku() { return sku; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public Date getLastUpdated() { return lastUpdated; }

    public void setPrice(double price) {
        this.price = price;
        this.lastUpdated = new Date();
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.lastUpdated = new Date();
    }

    public double getInventoryValue() {
        return price * quantity;
    }

    @Override
    public String toString() {
        return String.format("SKU: %s, Name: %s, Category: %s, Price: ₹%.2f, Qty: %d, Value: ₹%.2f",
                sku, name, category, price, quantity, getInventoryValue());
    }
}

// =================== COMPARATORS ===================
class PriceComparator implements Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2) {
        return Double.compare(p1.getPrice(), p2.getPrice());
    }
}

class ValueComparator implements Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2) {
        return Double.compare(p2.getInventoryValue(), p1.getInventoryValue()); // Descending
    }
}

// =================== INVENTORY SYSTEM ===================
class InventoryManagementSystem {
    private HashSet<Product> productSet;
    private TreeSet<Product> sortedProducts;
    private LinkedList<String> transactionHistory;
    private Stack<Product> undoStack;
    private Queue<Product> lowStockQueue;

    private int totalProducts;
    private double totalInventoryValue;

    public InventoryManagementSystem() {
        productSet = new HashSet<>();
        sortedProducts = new TreeSet<>();
        transactionHistory = new LinkedList<>();
        undoStack = new Stack<>();
        lowStockQueue = new LinkedList<>();
        totalProducts = 0;
        totalInventoryValue = 0;
    }

    public void addProduct(Product product) {
        if (productSet.add(product)) {
            sortedProducts.add(product);
            totalProducts++;
            totalInventoryValue += product.getInventoryValue();

            String transaction = String.format("ADD: %s - %s (Qty: %d) at %s",
                    product.getSku(), product.getName(), product.getQuantity(), new Date());
            transactionHistory.addFirst(transaction);

            if (product.getQuantity() < 10) {
                lowStockQueue.add(product);
            }

            System.out.println("Product added successfully!");
        } else {
            System.out.println("Product with SKU " + product.getSku() + " already exists!");
        }
    }

    public void updateProductQuantity(String sku, int newQuantity) {
        for (Product product : productSet) {
            if (product.getSku().equals(sku)) {

                // Save state for undo
                Product copy = new Product(product.getSku(), product.getName(),
                        product.getCategory(), product.getPrice(), product.getQuantity());
                undoStack.push(copy);

                int oldQuantity = product.getQuantity();
                product.setQuantity(newQuantity);

                totalInventoryValue -= product.getPrice() * oldQuantity;
                totalInventoryValue += product.getPrice() * newQuantity;

                String transaction = String.format("UPDATE: %s Quantity %d -> %d at %s",
                        sku, oldQuantity, newQuantity, new Date());
                transactionHistory.addFirst(transaction);

                System.out.println("Quantity updated successfully!");
                return;
            }
        }
        System.out.println("Product with SKU " + sku + " not found!");
    }

    public void undoLastUpdate() {
        if (undoStack.isEmpty()) {
            System.out.println("⚠ No operations to undo!");
            return;
        }

        Product previous = undoStack.pop();
        updateProductQuantity(previous.getSku(), previous.getQuantity());
        System.out.println("Last update undone!");
    }

    public void displayProductsSortedBy(String criteria) {
        System.out.println("\n=== PRODUCTS SORTED BY " + criteria.toUpperCase() + " ===");

        List<Product> productList = new ArrayList<>(productSet);

        switch (criteria.toLowerCase()) {
            case "sku":
                Collections.sort(productList);
                break;
            case "price":
                Collections.sort(productList, new PriceComparator());
                break;
            case "value":
                Collections.sort(productList, new ValueComparator());
                break;
            case "name":
                Collections.sort(productList, (p1, p2) -> p1.getName().compareTo(p2.getName()));
                break;
            default:
                System.out.println("Invalid sort criteria!");
                return;
        }

        displayProductList(productList);
    }

    private void displayProductList(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("⚠ No products found!");
            return;
        }

        System.out.printf("%-10s %-20s %-15s %-10s %-8s %-12s\n",
                "SKU", "Name", "Category", "Price", "Qty", "Value");
        System.out.println("-".repeat(85));

        for (Product product : products) {
            System.out.printf("%-10s %-20s %-15s ₹%-9.2f %-8d ₹%-11.2f\n",
                    product.getSku(), product.getName(), product.getCategory(),
                    product.getPrice(), product.getQuantity(), product.getInventoryValue());
        }
    }

    public void displayLowStockAlerts() {
        System.out.println("\n=== LOW STOCK ALERTS ===");

        if (lowStockQueue.isEmpty()) {
            System.out.println("No low stock items!");
            return;
        }

        int count = 1;
        for (Product product : lowStockQueue) {
            System.out.printf("%d. %s - %s (Stock: %d)\n",
                    count++, product.getSku(), product.getName(), product.getQuantity());
        }
    }

    public void displayTransactionHistory(int count) {
        System.out.println("\n=== LAST " + count + " TRANSACTIONS ===");
        int displayed = 0;

        for (String t : transactionHistory) {
            if (displayed >= count) break;
            System.out.println(t);
            displayed++;
        }
    }

    public void displayInventoryStatistics() {
        System.out.println("\n=== INVENTORY STATISTICS ===");
        System.out.println("Total Products: " + totalProducts);
        System.out.println("Total Inventory Value: ₹" + String.format("%.2f", totalInventoryValue));

        Map<String, Double> categoryValues = new HashMap<>();
        Map<String, Integer> categoryCounts = new HashMap<>();

        for (Product product : productSet) {
            String category = product.getCategory();

            categoryValues.put(category,
                    categoryValues.getOrDefault(category, 0.0) + product.getInventoryValue());

            categoryCounts.put(category,
                    categoryCounts.getOrDefault(category, 0) + 1);
        }

        System.out.println("\nCategory-wise Breakdown:");
        for (String category : categoryValues.keySet()) {
            double value = categoryValues.get(category);
            int count = categoryCounts.get(category);
            double percentage = (totalInventoryValue == 0) ? 0 : (value / totalInventoryValue) * 100;

            System.out.printf("• %s: %d products, Value: ₹%.2f (%.1f%%)\n",
                    category, count, value, percentage);
        }
    }
}

// =================== MAIN APP ===================
public class InventoryApp {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        InventoryManagementSystem ims = new InventoryManagementSystem();

        while (true) {
            System.out.println("\n========= INVENTORY MANAGEMENT SYSTEM =========");
            System.out.println("1. Add Product");
            System.out.println("2. Update Product Quantity");
            System.out.println("3. Display Products (Sorted)");
            System.out.println("4. Low Stock Alerts");
            System.out.println("5. Transaction History");
            System.out.println("6. Inventory Statistics");
            System.out.println("7. Undo Last Update");
            System.out.println("8. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Enter SKU: ");
                    String sku = sc.nextLine();

                    System.out.print("Enter Product Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Category: ");
                    String category = sc.nextLine();

                    System.out.print("Enter Price: ");
                    double price = sc.nextDouble();

                    System.out.print("Enter Quantity: ");
                    int qty = sc.nextInt();

                    Product product = new Product(sku, name, category, price, qty);
                    ims.addProduct(product);
                    break;

                case 2:
                    System.out.print("Enter SKU to Update: ");
                    String updateSku = sc.nextLine();

                    System.out.print("Enter New Quantity: ");
                    int newQty = sc.nextInt();

                    ims.updateProductQuantity(updateSku, newQty);
                    break;

                case 3:
                    System.out.print("Sort by (sku/price/value/name): ");
                    String sort = sc.nextLine();
                    ims.displayProductsSortedBy(sort);
                    break;

                case 4:
                    ims.displayLowStockAlerts();
                    break;

                case 5:
                    System.out.print("How many transactions to display? ");
                    int count = sc.nextInt();
                    ims.displayTransactionHistory(count);
                    break;

                case 6:
                    ims.displayInventoryStatistics();
                    break;

                case 7:
                    ims.undoLastUpdate();
                    break;

                case 8:
                    System.out.println("✅ Exiting... Thank you!");
                    sc.close();
                    return;

                default:
                    System.out.println("❌ Invalid choice! Try again.");
            }
        }
    }
}
