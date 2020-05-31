/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin;

import Database_Connection.DatabaseFunctions;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Apostolis
 */
public class AddFieldController implements Initializable {

    @FXML
    private TextField tableName;
    @FXML
    private Button submitButton;
    @FXML
    private Label warningBox;
    @FXML
    private TextField fieldName;
    @FXML
    private MenuButton fieldType;
    @FXML
    private MenuItem integerType;
    @FXML
    private MenuItem doubleType;
    @FXML
    private MenuItem stringType;
    @FXML
    private Button cancelBut;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void addFieldToTable(MouseEvent event) throws SQLException {
        String tName = tableName.getText();
        String fName = fieldName.getText();
        String fType =fieldType.getText();
        
        
        if(tName.length()!=0 && fName.length()!=0  && !fType.equals("Field Type")){
            DatabaseFunctions connection = new DatabaseFunctions("localhost","5432","eidiko_thema","postgres","postgres");
            String queryStr = String.format("SELECT table_name FROM information_schema.tables WHERE table_schema='public' and table_name='%s'",tName);
            ResultSet selectedTable = connection.selectQuery(queryStr);
           
            if(selectedTable.next()){
                if(tName.equals("User") || tName.equals("Order")){
                    
                    if(fType.equals("String")){
                        queryStr = String.format("ALTER TABLE \""+tName+"\" ADD "+fName+" CHAR(255)");
                    }
                    else if(fType.equals("Integer")){
                        queryStr = String.format("ALTER TABLE \""+tName+"\" ADD "+fName+" INTEGER");
                    }
                    else{
                        queryStr = String.format("ALTER TABLE \""+tName+"\" ADD "+fName+" DOUBLE PRECISION");
                    }
                    
                }
                else{
                    if(fType.equals("String")){
                        queryStr = String.format("ALTER TABLE "+tName+" ADD "+fName+" CHAR(255)");
                    }
                    else if(fType.equals("Integer")){
                        queryStr = String.format("ALTER TABLE "+tName+" ADD "+fName+" INTEGER");
                    }
                    else{
                        queryStr = String.format("ALTER TABLE "+tName+" ADD "+fName+" DOUBLE PRECISION");
                    }
                }
                connection.insertQuery(queryStr);
                
                Stage st= (Stage)warningBox.getScene().getWindow();
                st.close();
            }
            else{
                warningBox.setVisible(false);
                warningBox.setText("The selected table doesn't exist.");
                warningBox.setVisible(true);
            }
            
            connection.conn.close();
        }
        else if(tName.length()==0 || fName.length()==0){
            warningBox.setVisible(false);
            warningBox.setText("Invalid Data.");
            warningBox.setVisible(true);
        }
        else{
            warningBox.setVisible(false);
            warningBox.setText("Field type not selected.");
            warningBox.setVisible(true);
        }
    }

    @FXML
    private void addFieldToTableWithKey(KeyEvent event) throws SQLException {
        if(event.getCode().equals(KeyCode.ENTER)){
            String tName = tableName.getText();
            String fName = fieldName.getText();
            String fType =fieldType.getText();

            if(tName.length()!=0 && fName.length()!=0  && !fType.equals("Field Type")){
                DatabaseFunctions connection = new DatabaseFunctions("localhost","5432","eidiko_thema","postgres","postgres");
                String queryStr = String.format("SELECT table_name FROM information_schema.tables WHERE table_schema='public' and table_name='%s'",tName);
                ResultSet selectedTable = connection.selectQuery(queryStr);

                if(selectedTable.next()){
                    if(tName.equals("User") || tName.equals("Order")){

                        if(fType.equals("String")){
                            queryStr = String.format("ALTER TABLE \""+tName+"\" ADD "+fName+" CHAR(255)");
                        }
                        else if(fType.equals("Integer")){
                            queryStr = String.format("ALTER TABLE \""+tName+"\" ADD "+fName+" INTEGER");
                        }
                        else{
                            queryStr = String.format("ALTER TABLE \""+tName+"\" ADD "+fName+" DOUBLE PRECISION");
                        }

                    }
                    else{
                        if(fType.equals("String")){
                            queryStr = String.format("ALTER TABLE "+tName+" ADD "+fName+" CHAR(255)");
                        }
                        else if(fType.equals("Integer")){
                            queryStr = String.format("ALTER TABLE "+tName+" ADD "+fName+" INTEGER");
                        }
                        else{
                            queryStr = String.format("ALTER TABLE "+tName+" ADD "+fName+" DOUBLE PRECISION");
                        }
                    }
                    connection.insertQuery(queryStr);

                    Stage st= (Stage)warningBox.getScene().getWindow();
                    st.close();
                }
                else{
                    warningBox.setVisible(false);
                    warningBox.setText("The selected table doesn't exist.");
                    warningBox.setVisible(true);
                }

                connection.conn.close();
            }
            else if(tName.length()==0 || fName.length()==0){
                warningBox.setVisible(false);
                warningBox.setText("Invalid Data.");
                warningBox.setVisible(true);
            }
            else{
                warningBox.setVisible(false);
                warningBox.setText("Field type not selected.");
                warningBox.setVisible(true);
            }
        }
    }

    @FXML
    private void changeToString(ActionEvent event) {
        fieldType.setText("String");
    }

    @FXML
    private void changeToInteger(ActionEvent event) {
        fieldType.setText("Integer");
    }

    @FXML
    private void changeToDouble(ActionEvent event) {
        fieldType.setText("Double");
    }

    @FXML
    private void closeWindow(MouseEvent event) {
        Stage st = (Stage)cancelBut.getScene().getWindow();
        st.close();
    }
    
}
