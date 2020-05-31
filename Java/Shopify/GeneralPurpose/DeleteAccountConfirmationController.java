/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GeneralPurpose;

import Database_Connection.DatabaseFunctions;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Apostolis
 */
public class DeleteAccountConfirmationController implements Initializable {
    
    public String role;
    public int id;
    public AnchorPane anchorPanel;
    
    @FXML
    private Button yesButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
  
    public void setRole(String r){
        role=r;
    }
    public void setID(int id){
        this.id=id;
    }
    public void getMainWindow(AnchorPane p){
        anchorPanel=p;
    }
    
    @FXML
    private void deleteAccount(MouseEvent event) throws SQLException {
       if(role.equals("CUSTOMER")){
           
           DatabaseFunctions connection = new DatabaseFunctions("localhost","5432","eidiko_thema","postgres","postgres");
           
           String queryStr=String.format("DELETE FROM CUSTOMER C where C.customer_id=%d",id);
           connection.insertQuery(queryStr);
           
           //Close the window
           Stage st=(Stage)anchorPanel.getScene().getWindow();
           st.close();
           st=(Stage)yesButton.getScene().getWindow();
           st.close();
           
           connection.conn.close();
           
       }
       else{
           DatabaseFunctions connection = new DatabaseFunctions("localhost","5432","eidiko_thema","postgres","postgres");
           
           String queryStr=String.format("DELETE FROM ADMIN A where A.admin_id=%d",id);
           connection.insertQuery(queryStr);
           
           //Close the window
           Stage st=(Stage)anchorPanel.getScene().getWindow();
           st.close();
           st=(Stage)yesButton.getScene().getWindow();
           st.close();
           
           connection.conn.close();
       }
    }

    @FXML
    private void dontDeleteAccount(MouseEvent event) {
        Stage st = (Stage)yesButton.getScene().getWindow();
        st.close();
    }
    
}
