module com.example.renatojava.javasemester {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j;


    opens com.example.renatojava.javasemester to javafx.fxml;
    exports com.example.renatojava.javasemester;
}