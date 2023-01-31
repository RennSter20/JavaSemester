package com.example.renatojava.javasemester.procedure;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.database.ProcedureData;
import com.example.renatojava.javasemester.entity.ChangeWriter;
import com.example.renatojava.javasemester.entity.Procedure;
import com.example.renatojava.javasemester.exceptions.ObjectExistsException;
import com.example.renatojava.javasemester.util.CheckObjects;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class CreateProcedureController implements ProcedureData {

    @FXML
    private TextField descriptionField, priceField;


    public void createProcedure(){
        String descText = descriptionField.getText();
        String priceText = priceField.getText();

        if(descText.equals("") || priceText.equals("")){
            Alert emptyAlert = new Alert(Alert.AlertType.ERROR);
            emptyAlert.setTitle("ERROR");
            emptyAlert.setHeaderText("Empty fields!");
            emptyAlert.setContentText("All fields must be filled!");
            emptyAlert.show();
        }else{
            try{
                CheckObjects.checkIfProcedureExists(descText, Double.valueOf(priceText));
                ProcedureData.createProcedure(descText, priceText);
                descriptionField.setText("");
                priceField.setText("");

                ChangeWriter changeWriter = new ChangeWriter(new Procedure(0, "-", Double.valueOf(0)), ProcedureData.getProcedureFromDescription(descText));
                changeWriter.addChange(Application.getLoggedUser().getRole());

            }catch (ObjectExistsException e){
                Alert emptyAlert = new Alert(Alert.AlertType.ERROR);
                emptyAlert.setTitle("ERROR");
                emptyAlert.setHeaderText("Error while creating procedure.");
                emptyAlert.setContentText(e.getMessage());
                emptyAlert.show();
                Application.logger.error(e.getMessage(), e);
            }
        }
    }

}
