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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
public class DeleteTableFormController implements Initializable {

    @FXML
    private TextField tableName;
    @FXML
    private Button submitButton;
    @FXML
    private Label warningBox;
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
    private void deleteTable(MouseEvent event) throws SQLException {
        String tName = tableName.getText();
        
        if(tName.length()!=0){
            DatabaseFunctions connection = new DatabaseFunctions("localhost","5432","eidiko_thema","postgres","postgres");
            String queryStr = String.format("SELECT table_name FROM information_schema.tables WHERE table_schema='public' and table_name='%s'",tName);
            ResultSet selectedTable = connection.selectQuery(queryStr);
           
            if(selectedTable.next()){
                if(tName.equals("User") || tName.equals("Order")){
                    queryStr = String.format("DROP TABLE \""+tName+"\" CASCADE");
                }
                else{
                    queryStr = String.format("DROP TABLE  "+tName+" CASCADE");
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
        else{
            warningBox.setVisible(false);
            warningBox.setText("No table was chosen.");
            warningBox.setVisible(true);
        }
    }

    @FXML
    private void deleteTableWithKey(KeyEvent event) throws SQLException {
        if(event.getCode().equals(KeyCode.ENTER)){
            String tName = tableName.getText();
        
            if(tName.length()!=0){
                DatabaseFunctions connection = new DatabaseFunctions("localhost","5432","eidiko_thema","postgres","postgres");
                String queryStr = String.format("SELECT table_name FROM information_schema.tables WHERE table_schema='public' and table_name='%s'",tName);
                ResultSet selectedTable = connection.selectQuery(queryStr);

                if(selectedTable.next()){
                    if(tName.equals("User") || tName.equals("Order")){
                        queryStr = String.format("DROP TABLE \""+tName+"\" CASCADE");
                    }
                    else{
                        queryStr = String.format("DROP TABLE  "+tName+" CASCADE");
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
            else{
                warningBox.setVisible(false);
                warningBox.setText("No table was chosen.");
                warningBox.setVisible(true);
            }
        }
    }

    @FXML
    private void closeWindow(MouseEvent event) {
        Stage st =(Stage)cancelBut.getScene().getWindow();
        st.close();
    }
    
}
