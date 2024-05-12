package com.example.market;

import com.example.market.models.Price;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarketApp extends Application {

    private Connection connection;
    private TextField usernameField;
    private PasswordField passwordField;
    private Map<String, TableView<Price>> divisionTables = new HashMap<>();
    private Stage loginStage;
    private Stage mainStage = new Stage();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login");
        loginStage = primaryStage;

        loginStage.setTitle("Login");

        GridPane grid = new GridPane();
        grid.setAlignment(javafx.geometry.Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label usernameLabel = new Label("Username:");
        grid.add(usernameLabel, 0, 0);

        usernameField = new TextField();
        grid.add(usernameField, 1, 0);

        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 1);

        passwordField = new PasswordField();
        grid.add(passwordField, 1, 1);

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> login());
        grid.add(loginButton, 1, 2);

        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Authenticate user (dummy authentication for demonstration)
        if (authenticateUser(username, password)) {
            // Fetch prices data
            List<Price> prices = fetchPricesFromDatabase();
            displayPriceTables(prices);
            // Close the login dialog
            loginStage.close();
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }

    private boolean authenticateUser(String username, String password) {
        // Dummy authentication for demonstration
        return username.equals("sample_user") && password.equals("sample_password");
    }

    private List<Price> fetchPricesFromDatabase() {
        List<Price> prices = new ArrayList<>();

        // JDBC URL, username, and password of MySQL server
        String url = "jdbc:mysql://localhost:3306/market";
        String user = "root";
        String pass = "";

        try {
            // Establish connection to MySQL database
            connection = DriverManager.getConnection(url, user, pass);

            // Execute query to fetch prices data
            String query = "SELECT * FROM prices";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Process the result set
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String groceryName = resultSet.getString("grocery_name");
                String marketName = resultSet.getString("market_name");
                String division = resultSet.getString("division");
                Date date = resultSet.getDate("date");
                double basePrice = resultSet.getDouble("base_price");
                double preferredPrice = resultSet.getDouble("preferred_price");

                Price price = new Price(id, groceryName, marketName, division, date, basePrice, preferredPrice);
                prices.add(price);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return prices;
    }

    private void displayPriceTables(List<Price> prices) {
        TabPane tabPane = new TabPane();
        Map<String, ObservableList<Price>> divisionDataMap = new HashMap<>();

        for (Price price : prices) {
            String division = price.getDivision().toLowerCase();
            if (!divisionDataMap.containsKey(division)) {
                divisionDataMap.put(division, FXCollections.observableArrayList());
            }
            divisionDataMap.get(division).add(price);
        }

        for (Map.Entry<String, ObservableList<Price>> entry : divisionDataMap.entrySet()) {
            TableView<Price> tableView = new TableView<>();
            tableView.setItems(entry.getValue());

            TableColumn<Price, String> groceryColumn = new TableColumn<>("Grocery");
            groceryColumn.setCellValueFactory(cellData -> cellData.getValue().groceryNameProperty());

            TableColumn<Price, String> marketColumn = new TableColumn<>("Market");
            marketColumn.setCellValueFactory(cellData -> cellData.getValue().marketNameProperty());

            TableColumn<Price, String> divisionColumn = new TableColumn<>("Division");
            divisionColumn.setCellValueFactory(cellData -> cellData.getValue().divisionProperty());

            TableColumn<Price, String> dateColumn = new TableColumn<>("Date");
            dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

            TableColumn<Price, Number> basePriceColumn = new TableColumn<>("Base Price");
            basePriceColumn.setCellValueFactory(cellData -> cellData.getValue().basePriceProperty());

            TableColumn<Price, Number> preferredPriceColumn = new TableColumn<>("Preferred Price");
            preferredPriceColumn.setCellValueFactory(cellData -> cellData.getValue().preferredPriceProperty());

            tableView.getColumns().addAll(groceryColumn, marketColumn, divisionColumn, dateColumn, basePriceColumn, preferredPriceColumn);

            divisionTables.put(entry.getKey(), tableView);

            Tab tab = new Tab(entry.getKey());
            tab.setContent(tableView);
            tabPane.getTabs().add(tab);
        }

        // Add tab
        Tab addTab = new Tab("ADD");
        addTab.setContent(createAddTabContent());
        tabPane.getTabs().add(addTab);



        VBox vbox = new VBox(tabPane);
        Scene scene = new Scene(vbox, 800, 600);
        Stage stage = new Stage();
        mainStage.setScene(scene);
        mainStage.show();

    }

    private VBox createAddTabContent() {
        GridPane addForm = new GridPane();
        addForm.setAlignment(javafx.geometry.Pos.CENTER);
        addForm.setHgap(10);
        addForm.setVgap(10);
        addForm.setPadding(new Insets(25, 25, 25, 25));

        Label groceryLabel = new Label("Grocery:");
        addForm.add(groceryLabel, 0, 0);

        TextField groceryField = new TextField();
        addForm.add(groceryField, 1, 0);

        Label marketLabel = new Label("Market:");
        addForm.add(marketLabel, 0, 1);

        TextField marketField = new TextField();
        addForm.add(marketField, 1, 1);

        Label divisionLabel = new Label("Division:");
        addForm.add(divisionLabel, 0, 2);

        TextField divisionField = new TextField();
        addForm.add(divisionField, 1, 2);

        Label dateLabel = new Label("Date:");
        addForm.add(dateLabel, 0, 3);

        DatePicker datePicker = new DatePicker();
        addForm.add(datePicker, 1, 3);

        Label basePriceLabel = new Label("Base Price:");
        addForm.add(basePriceLabel, 0, 4);

        TextField basePriceField = new TextField();
        addForm.add(basePriceField, 1, 4);

        Label preferredPriceLabel = new Label("Preferred Price:");
        addForm.add(preferredPriceLabel, 0, 5);

        TextField preferredPriceField = new TextField();
        addForm.add(preferredPriceField, 1, 5);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> saveData(groceryField.getText(), marketField.getText(), divisionField.getText(), datePicker.getValue(), Double.parseDouble(basePriceField.getText()), Double.parseDouble(preferredPriceField.getText())));
        addForm.add(saveButton, 1, 6);

        return new VBox(addForm);
    }

    private void saveData(String grocery, String market, String division, LocalDate date, double basePrice, double preferredPrice) {
        // JDBC URL, username, and password of MySQL server
        String url = "jdbc:mysql://localhost:3306/market";
        String user = "root";
        String pass = "";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Establish connection to MySQL database
            connection = DriverManager.getConnection(url, user, pass);

            // Create SQL INSERT statement
            String sql = "INSERT INTO prices (grocery_name, market_name, division, date, base_price, preferred_price) VALUES (?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);

            // Set parameter values
            preparedStatement.setString(1, grocery);
            preparedStatement.setString(2, market);
            preparedStatement.setString(3, division);
            preparedStatement.setDate(4, Date.valueOf(date));
            preparedStatement.setDouble(5, basePrice);
            preparedStatement.setDouble(6, preferredPrice);

            // Execute INSERT statement
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Data saved successfully.");
                // Fetch prices data
                List<Price> prices = fetchPricesFromDatabase();
                displayPriceTables(prices);

            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save data.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save data: " + e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
