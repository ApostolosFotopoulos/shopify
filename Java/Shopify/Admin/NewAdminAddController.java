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
public class NewAdminAddController implements Initializable {

    @FXML
    private TextField username;
    @FXML
    private TextField password;
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
    private void addNewAdmin(MouseEvent event) throws SQLException {
        String uname=username.getText();
        String pass=password.getText(); 
        
        //If the textfields are filled
        if(uname.length()!=0 && pass.length()!=0){
            
            //Check if the account exists
            DatabaseFunctions connection = new DatabaseFunctions("localhost","5432","eidiko_thema","postgres","postgres");
            String queryStr = String.format("SELECT username FROM ADMIN A where A.username = '%s'",uname);
            ResultSet rs = connection.selectQuery(queryStr);
            
            //If the account doesnt exist then create it
            if(!rs.next()){
                queryStr= String.format("SELECT max(admin_id) FROM ADMIN");
                rs=connection.selectQuery(queryStr);
                
                //Create the new admin id
                rs.next();
                int admin_id = rs.getInt(1)+1;
                
                //Create the admin
                queryStr = String.format("INSERT INTO ADMIN(password,username,admin_id) VALUES('%s','%s',%d)",pass,uname,admin_id);
                connection.insertQuery(queryStr);
                
                Stage st = (Stage)warningBox.getScene().getWindow();
                st.close();
            }
            else{
                warningBox.setVisible(false);
                warningBox.setText("Username already exists.");
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
    private void addNewAdminWithKey(KeyEvent event) throws SQLException {
        if(event.getCode().equals(KeyCode.ENTER)){
            String uname=username.getText();
            String pass=password.getText();

            //If the textfields are filled
            if(uname.length()!=0 && pass.length()!=0){

                //Check if the account exists
                DatabaseFunctions connection = new DatabaseFunctions("localhost","5432","eidiko_thema","postgres","postgres");
                String queryStr = String.format("SELECT username FROM ADMIN A where A.username = '%s'",uname);
                ResultSet rs = connection.selectQuery(queryStr);

                //If the account doesnt exist then create it
                if(!rs.next()){
                    queryStr= String.format("SELECT max(admin_id) FROM ADMIN");
                    rs=connection.selectQuery(queryStr);

                    //Create the new admin id
                    rs.next();
                    int admin_id = rs.getInt(1)+1;

                    //Create the admin
                    queryStr = String.format("INSERT INTO ADMIN(password,username,admin_id) VALUES('%s','%s',%d)",pass,uname,admin_id);
                    connection.insertQuery(queryStr);

                    Stage st = (Stage)warningBox.getScene().getWindow();
                    st.close();
                }
                else{
                    warningBox.setVisible(false);
                    warningBox.setText("Username already exists.");
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
        Stage st =(Stage)cancelBut.getScene().getWindow();
        st.close();
    }
    
}
