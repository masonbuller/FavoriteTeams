module org.example.mbuller_module10 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires java.desktop;

    opens org.example.mbuller_module10 to javafx.fxml;
    exports org.example.mbuller_module10;
}