/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GeneralPurpose;

import javafx.scene.image.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import Database_Connection.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import Admin.AdminOptionsController;
import Customer.CustomerOptionsController;

public class Login_FormController implements Initializable {

    @FXML
    private StackPane stackPanel;
    @FXML
    private AnchorPane AnchorPanel;
    @FXML
    private MenuItem customer_pick;
    @FXML
    private MenuButton menu_button;
    @FXML
    private MenuItem admin_pick;
    @FXML
    private TextField usrname;
    @FXML
    private PasswordField passwd;
    @FXML
    private Button submitButton;
    @FXML
    private Label warningBox;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }  
    
    public void loginUser(String type,String username,String password) throws SQLException{
       
        warningBox.setVisible(false);
        warningBox.setText("");
        warningBox.setVisible(true);
        
        //Create connection with database
        DatabaseFunctions connection = new DatabaseFunctions("localhost","5432","eidiko_thema","postgres","postgres");
        
        ResultSet rs=connection.selectQuery("SELECT username,password FROM "+type+" C WHERE C.username='"+username+"' and C.password='"+password+"'");

        int setCount = 0;
        while(rs.next()) {
            setCount++;
        }

        // User exists
        if(setCount==1){
            Stage st = (Stage) submitButton.getScene().getWindow();
            st.close();
            if(type.equals("CUSTOMER")){
                try {

                    // Create the customer main panel 
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/Customer/CustomerOptions.fxml"));

                    //Set the scene
                    Scene scene = new Scene(fxmlLoader.load(), 1000, 650);
                    String css = this.getClass().getResource("CustomCSS.css").toExternalForm();
                    scene.getStylesheets().add(css);
                    Stage stage = new Stage();
                    stage.setTitle("Customer Panel");
                    stage.setScene(scene);

                    // We get the username of the customer 
                    CustomerOptionsController controller = fxmlLoader.<CustomerOptionsController>getController();
                    controller.setCustomerID(username);

                    stage.show();
                } catch (IOException e) {
                    Logger logger = Logger.getLogger(getClass().getName());
                    logger.log(Level.SEVERE, "Failed to create new Window.", e);
                } 
            }
            else{
                try {
                
                    // Create the admin main panel 
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/Admin/AdminOptions.fxml"));

                    //Set the scene
                    Scene scene = new Scene(fxmlLoader.load(), 1000, 650);
                    String css = this.getClass().getResource("CustomCSS.css").toExternalForm();
                    scene.getStylesheets().add(css);
                    Stage stage = new Stage();
                    stage.setTitle("Admin Panel");
                    stage.setScene(scene);

                    // We get the username of the  admin
                    AdminOptionsController controller = fxmlLoader.<AdminOptionsController>getController();
                    controller.setAdminID(username);

                    stage.show();
                } catch (IOException e) {
                    Logger logger = Logger.getLogger(getClass().getName());
                    logger.log(Level.SEVERE, "Failed to create new Window.", e);
                } 
            }
            
        }
        else{
            warningBox.setVisible(false);
            warningBox.setText("Invalid Username or Password.");
            warningBox.setVisible(true);
        }
        //Close the connection
        connection.conn.close();
    }
  
    //Check to login
    @FXML
    public void tryToLogin() throws SQLException{
        
        //Get all the values
        String username = usrname.getText();
        String password = passwd.getText();
        String type =  menu_button.getText();
        
        //Check if the parameters are valid
        if(username.length()!=0 && password.length()!=0 && !type.equals("User Type") ){
            if(type.equals("Customer")){
                 loginUser("CUSTOMER",username,password);
            }
            else if(type.equals("Admin")){
                loginUser("ADMIN",username,password);
            }
        }
        else if(type.equals("User Type")){
            
            warningBox.setVisible(false);
            warningBox.setText("User type not selected.");
            warningBox.setVisible(true);
        }
    }
    
    @FXML
    public void changeToCustomer(){
        menu_button.setText("Customer");
    }
    
    @FXML
    public void changeToAdmin(){
        menu_button.setText("Admin");
    }

    @FXML
    private void tryToLoginWithKey(KeyEvent event) throws SQLException {
        if(event.getCode().equals(KeyCode.ENTER)){
            
            //Get all the values
            String username = usrname.getText();
            String password = passwd.getText();
            String type =  menu_button.getText();

            //Check if the parameters are valid
            if(username.length()!=0 && password.length()!=0 && !type.equals("User Type") ){
                if(type.equals("Customer")){
                     loginUser("CUSTOMER",username,password);
                }
                else if(type.equals("Admin")){
                    loginUser("ADMIN",username,password);
                }
            }
            else if(type.equals("User Type")){
                warningBox.setVisible(false);
                warningBox.setText("User type not selected.");
                warningBox.setVisible(true);
            }
        }
    }

    @FXML
    private void createNewAccount(MouseEvent event) {
       try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/Customer/SignUpCustomer.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 490, 800);
            String css = this.getClass().getResource("CustomCSS.css").toExternalForm();
            scene.getStylesheets().add(css);
            Stage stage = new Stage();
            stage.setTitle("Create New Customer Account");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        } 
    }
}
