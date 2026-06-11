import java.util.*;
import java.time.*;

// ======================================================
// ENUMS
// ======================================================

enum ProductCategory {
    DRINK,
    SNACK,
    MEDICINE
}

enum PaymentType {
    CASH,
    CARD,
    UPI
}

enum MachineMode {
    ACTIVE,
    SLEEP,
    OFFLINE
}

// ======================================================
// SINGLETON LOGGER
// ======================================================

static class Logger {

    private static Logger instance;

    private Logger() {}

    public static synchronized Logger getInstance() {

        if(instance == null) {
            instance = new Logger();
        }

        return instance;
    }

    public void log(String message) {

        System.out.println(
                "[LOG] "
                        + LocalDateTime.now()
                        + " -> "
                        + message);
    }
}

// ======================================================
// PRODUCT ENTITY
// ======================================================

static class Product {

    private int id;
    private String name;
    private double price;
    private int quantity;
    private ProductCategory category;
    private LocalDate expiryDate;

    public Product(
            int id,
            String name,
            double price,
            int quantity,
            ProductCategory category,
            LocalDate expiryDate) {

        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.expiryDate = expiryDate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void display() {

        System.out.println(
                id + " | "
                        + name
                        + " | ₹"
                        + price
                        + " | Qty="
                        + quantity
                        + " | Exp="
                        + expiryDate);
    }
}

// ======================================================
// FACTORY PATTERN
// ======================================================

static class ProductFactory {

    public static Product createProduct(
            int id,
            String name,
            double price,
            int quantity,
            ProductCategory category,
            LocalDate expiryDate) {

        return new Product(
                id,
                name,
                price,
                quantity,
                category,
                expiryDate);
    }
}

// ======================================================
// CUSTOMER
// ======================================================

static class Customer {

    private String customerId;
    private String name;

    public Customer(
            String customerId,
            String name) {

        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }
}

// ======================================================
// TRANSACTION
// ======================================================

static class Transaction {

    private int transactionId;
    private Product product;
    private PaymentType paymentType;
    private boolean success;

    public Transaction(
            int transactionId,
            Product product,
            PaymentType paymentType,
            boolean success) {

        this.transactionId = transactionId;
        this.product = product;
        this.paymentType = paymentType;
        this.success = success;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public Product getProduct() {
        return product;
    }

    public boolean isSuccess() {
        return success;
    }

    public void display() {

        System.out.println(
                "Transaction ID : "
                        + transactionId
                        + " | Product : "
                        + product.getName()
                        + " | Payment : "
                        + paymentType
                        + " | Status : "
                        + (success ? "SUCCESS" : "FAILED"));
    }
}

// ======================================================
// SALES RECORD
// ======================================================

static class SalesRecord {

    private String productName;
    private int quantity;
    private LocalDateTime timestamp;

    public SalesRecord(
            String productName,
            int quantity) {

        this.productName = productName;
        this.quantity = quantity;
        this.timestamp = LocalDateTime.now();
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}

// ======================================================
// PRODUCT REPOSITORY
// ======================================================

static class ProductRepository {

    private Map<Integer, Product> products =
            new HashMap<>();

    public void addProduct(Product product) {

        products.put(
                product.getId(),
                product);

        Logger.getInstance().log(
                product.getName()
                        + " added to inventory");
    }

    public Product findProduct(int id) {

        return products.get(id);
    }

    public Collection<Product> getAllProducts() {

        return products.values();
    }

    public void displayProducts() {

        System.out.println(
                "\n===== INVENTORY =====");

        for(Product p :
                products.values()) {

            p.display();
        }
    }
}

// ======================================================
// INVENTORY SERVICE
// ======================================================

static class InventoryService {

    private ProductRepository repository;

    public InventoryService(
            ProductRepository repository) {

        this.repository = repository;
    }

    public boolean isAvailable(
            int productId) {

        Product product =
                repository.findProduct(
                        productId);

        return product != null
                &&
                product.getQuantity() > 0;
    }

    public void reduceStock(
            int productId) {

        Product product =
                repository.findProduct(
                        productId);

        if(product != null) {

            product.setQuantity(
                    product.getQuantity() - 1);
        }
    }

    public void displayInventory() {

        repository.displayProducts();
    }
}

// ======================================================
// PAYMENT STRATEGY PATTERN
// ======================================================

interface PaymentStrategy {

    boolean pay(double amount);
}

// CASH

static class CashPayment
        implements PaymentStrategy {

    @Override
    public boolean pay(
            double amount) {

        System.out.println(
                "Cash Payment Successful : ₹"
                        + amount);

        return true;
    }
}

// CARD

static class CardPayment
        implements PaymentStrategy {

    @Override
    public boolean pay(
            double amount) {

        System.out.println(
                "Card Payment Successful : ₹"
                        + amount);

        return true;
    }
}

// UPI

static class UpiPayment
        implements PaymentStrategy {

    @Override
    public boolean pay(
            double amount) {

        System.out.println(
                "UPI Payment Successful : ₹"
                        + amount);

        return true;
    }
}

// ======================================================
// PAYMENT SERVICE
// ======================================================

static class PaymentService {

    private PaymentStrategy strategy;

    public PaymentService(
            PaymentStrategy strategy) {

        this.strategy = strategy;
    }

    public boolean processPayment(
            double amount) {

        return strategy.pay(amount);
    }
}
// ======================================================
// DEMAND PREDICTION MODULE
// ======================================================

static class DemandPredictionService {

    private Map<String, List<Integer>>
            salesHistory =
            new HashMap<>();

    public void addSalesRecord(
            String productName,
            int quantity) {

        salesHistory
                .computeIfAbsent(
                        productName,
                        k -> new ArrayList<>())

                .add(quantity);
    }

    public int predictDemand(
            String productName) {

        List<Integer> history =
                salesHistory.get(
                        productName);

        if(history == null
                || history.size() < 2) {

            return 0;
        }

        int last =
                history.get(
                        history.size() - 1);

        int previous =
                history.get(
                        history.size() - 2);

        return last + (last - previous);
    }

    public void showPrediction(
            String productName) {

        System.out.println(
                "\nPredicted Demand for "
                        + productName
                        + " = "
                        + predictDemand(
                        productName));
    }
}

// ======================================================
// EXPIRY MANAGEMENT
// ======================================================

class ExpiryProduct {

    private String productName;
    private int daysToExpire;

    public ExpiryProduct(
            String productName,
            int daysToExpire) {

        this.productName =
                productName;

        this.daysToExpire =
                daysToExpire;
    }

    public String getProductName() {
        return productName;
    }

    public int getDaysToExpire() {
        return daysToExpire;
    }
}

class ExpiryComparator
        implements Comparator<ExpiryProduct> {

    @Override
    public int compare(
            ExpiryProduct p1,
            ExpiryProduct p2) {

        return Integer.compare(
                p1.getDaysToExpire(),
                p2.getDaysToExpire());
    }
}

class ExpiryManager {

    private PriorityQueue<ExpiryProduct>
            products =
            new PriorityQueue<>(
                    new ExpiryComparator());

    public void addProduct(
            String name,
            int expiryDays) {

        products.offer(
                new ExpiryProduct(
                        name,
                        expiryDays));
    }

    public void dispenseNearestExpiry() {

        ExpiryProduct p =
                products.poll();

        if(p != null) {

            System.out.println(
                    "\nDispensing Product Near Expiry");

            System.out.println(
                    p.getProductName()
                            + " | Expires in "
                            + p.getDaysToExpire()
                            + " days");
        }
    }
}

// ======================================================
// RECOMMENDATION ENGINE
// ======================================================

interface RecommendationStrategy {

    List<String> recommend(
            String productName);
}

class FoodRecommendationStrategy
        implements RecommendationStrategy {

    @Override
    public List<String> recommend(
            String productName) {

        return Arrays.asList(
                "Cold Coffee",
                "Water Bottle",
                "Juice");
    }
}

class SnackRecommendationStrategy
        implements RecommendationStrategy {

    @Override
    public List<String> recommend(
            String productName) {

        return Arrays.asList(
                "Chocolate",
                "Chips",
                "Soft Drink");
    }
}

class RecommendationService {

    private RecommendationStrategy
            strategy;

    public RecommendationService(
            RecommendationStrategy strategy) {

        this.strategy =
                strategy;
    }

    public void showRecommendations(
            String productName) {

        System.out.println(
                "\nRecommended Products");

        List<String> list =
                strategy.recommend(
                        productName);

        for(String item : list) {

            System.out.println(
                    "-> " + item);
        }
    }
}

// ======================================================
// FRAUD DETECTION
// ======================================================

class FraudDetectionService {

    private Map<String,Integer>
            userAttempts =
            new HashMap<>();

    public void recordFailure(
            String userId) {

        userAttempts.put(

                userId,

                userAttempts.getOrDefault(
                        userId,
                        0)

                        + 1);
    }

    public boolean isBlocked(
            String userId) {

        return userAttempts
                .getOrDefault(
                        userId,
                        0)
                >= 3;
    }

    public void displayStatus(
            String userId) {

        if(isBlocked(userId)) {

            System.out.println(
                    "\nFRAUD ALERT");

            System.out.println(
                    userId
                            + " temporarily blocked");
        }

        else {

            System.out.println(
                    "\nUser "
                            + userId
                            + " is safe");
        }
    }
}

// ======================================================
// PRODUCT POPULARITY RANKING
// ======================================================

class PopularityRankingService {

    private Map<String,Integer>
            salesCounter =
            new HashMap<>();

    public void recordSale(
            String productName) {

        salesCounter.put(

                productName,

                salesCounter.getOrDefault(
                        productName,
                        0)

                        + 1);
    }

    public void showRanking() {

        System.out.println(
                "\n===== PRODUCT POPULARITY =====");

        salesCounter.entrySet()

                .stream()

                .sorted(
                        (a,b) ->
                                b.getValue()
                                        - a.getValue())

                .forEach(

                        e -> System.out.println(

                                e.getKey()
                                        + " -> "
                                        + e.getValue()

                        ));
    }
}

// ======================================================
// PEAK HOUR ANALYTICS
// ======================================================

class AnalyticsService {

    private List<LocalDateTime>
            purchases =
            new ArrayList<>();

    public void recordPurchase() {

        purchases.add(
                LocalDateTime.now());
    }

    public void showPeakHourReport() {

        Map<Integer,Integer>
                hourlySales =
                new TreeMap<>();

        for(LocalDateTime time :
                purchases) {

            int hour =
                    time.getHour();

            hourlySales.put(

                    hour,

                    hourlySales.getOrDefault(
                            hour,
                            0)

                            + 1);
        }

        System.out.println(
                "\n===== PEAK HOUR REPORT =====");

        for(Map.Entry<Integer,Integer>
                entry :

                hourlySales.entrySet()) {

            System.out.println(

                    entry.getKey()
                            + ":00 -> "
                            + entry.getValue()
                            + " sales");
        }
    }
}
// ======================================================
// OBSERVER PATTERN - ALERT SYSTEM
// ======================================================

interface AlertObserver {

    void update(String message);
}

class AdminAlert implements AlertObserver {

    @Override
    public void update(String message) {

        System.out.println(
                "[ADMIN ALERT] "
                        + message);
    }
}

class MaintenanceAlert
        implements AlertObserver {

    @Override
    public void update(String message) {

        System.out.println(
                "[MAINTENANCE ALERT] "
                        + message);
    }
}

class AlertManager {

    private List<AlertObserver>
            observers =
            new ArrayList<>();

    public void addObserver(
            AlertObserver observer) {

        observers.add(observer);
    }

    public void notifyObservers(
            String message) {

        for(AlertObserver observer :
                observers) {

            observer.update(message);
        }
    }
}

// ======================================================
// COMMAND PATTERN
// ======================================================

interface Command {

    void execute();
}

class PurchaseCommand
        implements Command {

    private String productName;

    public PurchaseCommand(
            String productName) {

        this.productName =
                productName;
    }

    @Override
    public void execute() {

        System.out.println(
                "Purchase Executed : "
                        + productName);
    }
}

class RefundCommand
        implements Command {

    @Override
    public void execute() {

        System.out.println(
                "Refund Successfully Processed");
    }
}

class CommandInvoker {

    public void executeCommand(
            Command command) {

        command.execute();
    }
}

// ======================================================
// PREDICTIVE MAINTENANCE
// ======================================================

class MaintenanceRecord {

    private int motorUsage;
    private double temperature;

    public MaintenanceRecord(
            int motorUsage,
            double temperature) {

        this.motorUsage =
                motorUsage;

        this.temperature =
                temperature;
    }

    public int getMotorUsage() {
        return motorUsage;
    }

    public double getTemperature() {
        return temperature;
    }
}

class MaintenanceService {

    private static final int
            MOTOR_THRESHOLD = 30000;

    private static final double
            TEMP_THRESHOLD = 70.0;

    public void evaluate(
            MaintenanceRecord record) {

        System.out.println(
                "\n===== MAINTENANCE CHECK =====");

        if(record.getMotorUsage()
                >= MOTOR_THRESHOLD) {

            System.out.println(
                    "Motor nearing failure");
        }

        if(record.getTemperature()
                >= TEMP_THRESHOLD) {

            System.out.println(
                    "Temperature too high");
        }

        if(record.getMotorUsage()
                < MOTOR_THRESHOLD
                &&
                record.getTemperature()
                        < TEMP_THRESHOLD) {

            System.out.println(
                    "Machine Healthy");
        }
    }
}

// ======================================================
// SELF HEALING TRANSACTION RECOVERY
// ======================================================

class TransactionSnapshot {

    private int transactionId;
    private String productName;
    private double amount;

    public TransactionSnapshot(
            int transactionId,
            String productName,
            double amount) {

        this.transactionId =
                transactionId;

        this.productName =
                productName;

        this.amount =
                amount;
    }

    public void display() {

        System.out.println(
                "\n===== RECOVERY SNAPSHOT =====");

        System.out.println(
                "Transaction ID : "
                        + transactionId);

        System.out.println(
                "Product : "
                        + productName);

        System.out.println(
                "Amount : ₹"
                        + amount);
    }
}

class RecoveryService {

    private TransactionSnapshot
            snapshot;

    public void saveSnapshot(
            TransactionSnapshot snapshot) {

        this.snapshot =
                snapshot;

        System.out.println(
                "\nTransaction State Saved");
    }

    public void recover() {

        System.out.println(
                "\n===== POWER FAILURE RECOVERY =====");

        if(snapshot != null) {

            snapshot.display();

            System.out.println(
                    "Transaction Resumed Successfully");
        }

        else {

            System.out.println(
                    "No Transaction Found");
        }
    }
}

// ======================================================
// EMERGENCY OFFLINE MODE
// ======================================================

class OfflineModeManager {

    private boolean networkAvailable =
            true;

    public void disableNetwork() {

        networkAvailable =
                false;
    }

    public void enableNetwork() {

        networkAvailable =
                true;
    }

    public void dispenseEssentialItem(
            String productName) {

        if(!networkAvailable) {

            System.out.println(
                    "\nOFFLINE MODE ACTIVE");

            System.out.println(
                    "Dispensed Essential Item : "
                            + productName);
        }

        else {

            System.out.println(
                    "\nNetwork Active");
        }
    }
}

// ======================================================
// ENERGY OPTIMIZATION
// STATE PATTERN
// ======================================================

interface EnergyState {

    void handle();
}

class ActiveState
        implements EnergyState {

    @Override
    public void handle() {

        System.out.println(
                "Machine Running Normally");
    }
}

class SleepState
        implements EnergyState {

    @Override
    public void handle() {

        System.out.println(
                "Display Brightness Reduced");

        System.out.println(
                "Cooling Optimized");

        System.out.println(
                "Sleep Mode Activated");
    }
}

class EnergyOptimizer {

    private EnergyState state;

    public void setState(
            EnergyState state) {

        this.state = state;
    }

    public void optimize() {

        if(state != null) {

            state.handle();
        }
    }
}
// ======================================================
// THREAD SAFE INVENTORY
// ======================================================

class SafeInventory {

    private int stock;

    public SafeInventory(int stock) {
        this.stock = stock;
    }

    public synchronized void purchase(
            String customer) {

        if(stock > 0) {

            stock--;

            System.out.println(
                    customer
                            + " purchased product"
                            + " | Remaining Stock = "
                            + stock);
        }

        else {

            System.out.println(
                    customer
                            + " -> OUT OF STOCK");
        }
    }
}

class CustomerThread
        extends Thread {

    private SafeInventory inventory;
    private String customerName;

    public CustomerThread(
            SafeInventory inventory,
            String customerName) {

        this.inventory =
                inventory;

        this.customerName =
                customerName;
    }

    @Override
    public void run() {

        inventory.purchase(
                customerName);
    }
}

// ======================================================
// SALES TRACKER
// ======================================================

static class SalesTracker {

    private List<SalesRecord>
            sales =
            new ArrayList<>();

    public void addSale(
            SalesRecord record) {

        sales.add(record);
    }

    public void displaySales() {

        System.out.println(
                "\n===== SALES HISTORY =====");

        for(SalesRecord sale :
                sales) {

            System.out.println(

                    sale.getProductName()
                            + " | Qty="
                            + sale.getQuantity()
                            + " | "
                            + sale.getTimestamp()

            );
        }
    }
}

// ======================================================
// SMART DASHBOARD
// ======================================================

static class SmartDashboard {

    public static void display() {

        System.out.println(
                "\n======================================");

        System.out.println(
                " INTELLIGENT SELF-HEALING");

        System.out.println(
                " VENDING MACHINE SYSTEM");

        System.out.println(
                "======================================");

        System.out.println(
                "✔ Product Management");

        System.out.println(
                "✔ Inventory Tracking");

        System.out.println(
                "✔ Demand Prediction");

        System.out.println(
                "✔ Expiry Management");

        System.out.println(
                "✔ Recommendation Engine");

        System.out.println(
                "✔ Fraud Detection");

        System.out.println(
                "✔ Maintenance Prediction");

        System.out.println(
                "✔ Peak Hour Analytics");

        System.out.println(
                "✔ Offline Mode");

        System.out.println(
                "✔ Energy Optimization");

        System.out.println(
                "✔ Transaction Recovery");

        System.out.println(
                "======================================");
    }
}

// ======================================================
// DEMO DATA LOADER
// ======================================================

class DemoData {

    public static void loadProducts(
            ProductRepository repository) {

        repository.addProduct(

                ProductFactory.createProduct(

                        101,
                        "Coke",
                        40,
                        20,
                        ProductCategory.DRINK,
                        LocalDate.now().plusDays(30)

                ));

        repository.addProduct(

                ProductFactory.createProduct(

                        102,
                        "Water",
                        20,
                        30,
                        ProductCategory.DRINK,
                        LocalDate.now().plusDays(60)

                ));

        repository.addProduct(

                ProductFactory.createProduct(

                        103,
                        "Sandwich",
                        60,
                        10,
                        ProductCategory.SNACK,
                        LocalDate.now().plusDays(5)

                ));

        repository.addProduct(

                ProductFactory.createProduct(

                        104,
                        "Paracetamol",
                        25,
                        15,
                        ProductCategory.MEDICINE,
                        LocalDate.now().plusDays(365)

                ));
    }
}

// ======================================================
// MAIN CLASS
// ======================================================

public static void main(String[] args) throws Exception {

    Scanner sc = new Scanner(System.in);

    SmartDashboard.display();

    Logger.getInstance().log("System Started");

    ProductRepository repository =
            new ProductRepository();

    InventoryService inventory =
            new InventoryService(repository);

    SalesTracker tracker =
            new SalesTracker();

    boolean running = true;

    while(running) {

        System.out.println("\n============================");
        System.out.println("1. Add Product");
        System.out.println("2. Display Inventory");
        System.out.println("3. Purchase Product");
        System.out.println("4. View Sales History");
        System.out.println("5. Demand Prediction Demo");
        System.out.println("6. Exit");
        System.out.println("============================");

        System.out.print("Enter Choice : ");

        int choice = sc.nextInt();
        sc.nextLine();

        switch(choice) {

            case 1:

                System.out.print("Product ID : ");
                int id = sc.nextInt();
                sc.nextLine();

                System.out.print("Product Name : ");
                String name = sc.nextLine();

                System.out.print("Price : ");
                double price = sc.nextDouble();

                System.out.print("Quantity : ");
                int qty = sc.nextInt();

                System.out.println(
                        "Category : 1.DRINK  2.SNACK  3.MEDICINE");

                int catChoice = sc.nextInt();

                ProductCategory category;

                if(catChoice == 1)
                    category = ProductCategory.DRINK;

                else if(catChoice == 2)
                    category = ProductCategory.SNACK;

                else
                    category = ProductCategory.MEDICINE;

                System.out.print(
                        "Expiry Days From Today : ");

                int days = sc.nextInt();

                Product product =
                        ProductFactory.createProduct(
                                id,
                                name,
                                price,
                                qty,
                                category,
                                LocalDate.now()
                                        .plusDays(days)
                        );

                repository.addProduct(product);

                System.out.println(
                        "Product Added Successfully");

                break;

            case 2:

                inventory.displayInventory();

                break;

            case 3:

                inventory.displayInventory();

                System.out.print(
                        "\nEnter Product ID : ");

                int productId =
                        sc.nextInt();

                Product selected =
                        repository.findProduct(
                                productId);

                if(selected == null) {

                    System.out.println(
                            "Invalid Product");

                    break;
                }

                if(selected.getQuantity() <= 0) {

                    System.out.println(
                            "Out Of Stock");

                    break;
                }

                System.out.println(
                        "\nSelect Payment");

                System.out.println(
                        "1. Cash");

                System.out.println(
                        "2. Card");

                System.out.println(
                        "3. UPI");

                int payChoice =
                        sc.nextInt();

                PaymentStrategy strategy;

                if(payChoice == 1) {

                    strategy =
                            new CashPayment();
                }

                else if(payChoice == 2) {

                    strategy =
                            new CardPayment();
                }

                else {

                    strategy =
                            new UpiPayment();
                }

                PaymentService payment =
                        new PaymentService(
                                strategy);

                boolean success =
                        payment.processPayment(
                                selected.getPrice());

                if(success) {

                    inventory.reduceStock(
                            productId);

                    tracker.addSale(

                            new SalesRecord(
                                    selected.getName(),
                                    1));

                    System.out.println(
                            "\nProduct Dispensed : "
                                    + selected.getName());
                }

                break;

            case 4:

                tracker.displaySales();

                break;

            case 5:

                DemandPredictionService
                        demand =
                        new DemandPredictionService();

                demand.addSalesRecord(
                        "Coke",
                        40);

                demand.addSalesRecord(
                        "Coke",
                        45);

                demand.addSalesRecord(
                        "Coke",
                        50);

                demand.showPrediction(
                        "Coke");

                break;

            case 6:

                running = false;

                Logger.getInstance()
                        .log(
                                "System Closed");

                break;

            default:

                System.out.println(
                        "Invalid Choice");
        }
    }

    sc.close();

    System.out.println(
            "\n===== THANK YOU =====");
}