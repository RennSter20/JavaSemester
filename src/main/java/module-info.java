module com.example.renatojava.javasemester {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j;
    requires tornadofx.controls;
    requires org.apache.commons.codec;
    requires okhttp3;
    requires org.json;


    opens com.example.renatojava.javasemester to javafx.fxml;
    exports com.example.renatojava.javasemester.doctors;
    opens com.example.renatojava.javasemester.doctors to javafx.fxml;
    exports com.example.renatojava.javasemester.patient;
    opens com.example.renatojava.javasemester.patient to javafx.fxml;
    opens com.example.renatojava.javasemester.procedure to javafx.fxml;
    exports com.example.renatojava.javasemester.procedure;
    opens com.example.renatojava.javasemester.room to javafx.fxml;
    exports com.example.renatojava.javasemester.room;
    exports com.example.renatojava.javasemester.checkups;
    opens com.example.renatojava.javasemester.checkups to javafx.fxml;
    exports com.example.renatojava.javasemester.bills;
    opens com.example.renatojava.javasemester.bills to javafx.fxml;
    opens com.example.renatojava.javasemester.menus to javafx.fxml;
    exports com.example.renatojava.javasemester.menus;
    exports com.example.renatojava.javasemester;
    exports com.example.renatojava.javasemester.database;
    opens com.example.renatojava.javasemester.database to javafx.fxml;
    exports com.example.renatojava.javasemester.user to javafx.fxml;
    opens com.example.renatojava.javasemester.user to javafx.fxml;
}