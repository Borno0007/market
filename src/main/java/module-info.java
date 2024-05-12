module com.example.market {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.market to javafx.fxml;
    exports com.example.market;
}