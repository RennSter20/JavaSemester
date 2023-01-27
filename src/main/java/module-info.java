module com.example.renatojava.javasemester {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j;
    requires tornadofx.controls;
    requires org.apache.commons.codec;


    opens com.example.renatojava.javasemester to javafx.fxml;
    exports com.example.renatojava.javasemester;
    exports com.example.renatojava.javasemester.doctorControllers;
    opens com.example.renatojava.javasemester.doctorControllers to javafx.fxml;
    exports com.example.renatojava.javasemester.patientControllers;
    opens com.example.renatojava.javasemester.patientControllers to javafx.fxml;
    opens com.example.renatojava.javasemester.procedureControllers to javafx.fxml;
    exports com.example.renatojava.javasemester.procedureControllers;
    opens com.example.renatojava.javasemester.roomControllers to javafx.fxml;
    exports com.example.renatojava.javasemester.roomControllers;
    exports com.example.renatojava.javasemester.checkups;
    opens com.example.renatojava.javasemester.checkups to javafx.fxml;
    exports com.example.renatojava.javasemester.bills;
    opens com.example.renatojava.javasemester.bills to javafx.fxml;
}