module com.example.renatojava.javasemester {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.renatojava.javasemester to javafx.fxml;
    exports com.example.renatojava.javasemester;
}