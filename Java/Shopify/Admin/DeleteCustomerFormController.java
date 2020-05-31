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
 * @author justdemo
 */
public class DeleteCustomerFormController implements Initializable {

    @FXML
    private Button submitButton;
    @FXML
    private Label warningBox;
    @FXML
    private TextField customer_id;
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
    private void deleteCustomer(MouseEvent event) throws SQLException {
        String c_id =customer_id.getText();
        
        if(c_id.length()!=0){
            
            //Search if the customer exists
            DatabaseFunctions connection = new DatabaseFunctions("localhost","5432","eidiko_thema","postgres","postgres");
            String customer = String.format("SELECT customer_id FROM CUSTOMER WHERE customer_id=%d",Integer.parseInt(c_id));
            ResultSet rs=connection.selectQuery(customer);
            
            //Delete if exists
            if(rs.next()){
                
               customer = String.format("DELETE FROM CUSTOMER C WHERE C.customer_id=%d",Integer.parseInt(c_id));
               connection.insertQuery(customer);
               Stage st = (Stage) warningBox.getScene().getWindow();
               st.close();
            }
            else{
                warningBox.setVisible(false);
                warningBox.setText("Customer doesn't exist.");
                warningBox.setVisible(true);
            }
            
            connection.conn.close();
        }
        else{
            warningBox.setVisible(false);
            warningBox.setText("Invalid Data.");
            warningBox.setVisible(true);
        }
    }

    @FXML
    private void deleteCustomerWithKey(KeyEvent event) throws SQLException {
        if(event.getCode().equals(KeyCode.ENTER)){
            String c_id =customer_id.getText();
        
            if(c_id.length()!=0){

                //Search if the customer exists
                DatabaseFunctions connection = new DatabaseFunctions("localhost","5432","eidiko_thema","postgres","postgres");
                String customer = String.format("SELECT customer_id FROM CUSTOMER WHERE customer_id=%d",Integer.parseInt(c_id));
                ResultSet rs=connection.selectQuery(customer);

                //Delete if exists
                if(rs.next()){

                   customer = String.format("DELETE FROM CUSTOMER C WHERE C.customer_id=%d",Integer.parseInt(c_id));
                   connection.insertQuery(customer);
                   Stage st = (Stage) warningBox.getScene().getWindow();
                   st.close();
                }
                else{
                    warningBox.setVisible(false);
                    warningBox.setText("Customer doesn't exist.");
                    warningBox.setVisible(true);
                }

                connection.conn.close();
            }
            else{
                warningBox.setVisible(false);
                warningBox.setText("Invalid Data.");
                warningBox.setVisible(true);
            }
        }
    }

    @FXML
    private void closeWindow(MouseEvent event) {
        Stage st = (Stage) cancelBut.getScene().getWindow();
        st.close();
    }
    
}
