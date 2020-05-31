/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Customer;

import Database_Connection.DatabaseFunctions;
import java.net.URL;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
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
public class OrderFormController implements Initializable {
    
    public int product_id;
    
    public int customer_id;
    @FXML
    private MenuButton menu_button;
    @FXML
    private MenuItem credit_card;
    @FXML
    private MenuItem paypal;
    @FXML
    private MenuItem paysafe;
    @FXML
    private MenuItem cash;
    @FXML
    private Button submitButton;
    @FXML
    private Label warningBox;
    @FXML
    private TextField quantity;
    @FXML
    private TextArea comments;
    @FXML
    private Button cancelBut;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setCustomerID(int cust_id){
        customer_id=cust_id;
    }
    
    public void setProductID(String product_name) throws SQLException{
        DatabaseFunctions connection = new DatabaseFunctions("localhost","5432","eidiko_thema","postgres","postgres");
        String queryStr=String.format("SELECT product_id FROM PRODUCT C where C.product_name='%s'",product_name);
        ResultSet rs=connection.selectQuery(queryStr);
        if(rs.next()){
            product_id=rs.getInt(1);
            //System.out.println("Product ID: "+product_id);
        }
        connection.conn.close();
    }
    
    @FXML
    private void changeToCredit(ActionEvent event) {
        menu_button.setText("Credit Card");
    }

    @FXML
    private void changeToPaypal(ActionEvent event) {
        menu_button.setText("Paypal");
    }

    @FXML
    private void changeToPaysafe(ActionEvent event) {
        menu_button.setText("Paysafe");
    }

    @FXML
    private void changeToCash(ActionEvent event) {
        menu_button.setText("Cash");
    }

    @FXML
    private void makeNewOrderWithKey(KeyEvent event) throws SQLException {
        if(event.getCode().equals(KeyCode.ENTER)){
            //Get the required fields
            String payment_method =menu_button.getText();
            String com = comments.getText();

            if(!payment_method.equals("Payment Options")){
                if(quantity.getText().length()!=0){
                    if(Integer.parseInt(quantity.getText()) > 0){

                        //Fix the comments
                        if(com.length()==0){
                            com="No comments";
                        }

                        DatabaseFunctions connection = new DatabaseFunctions("localhost","5432","eidiko_thema","postgres","postgres");

                        //Make the order id
                        ResultSet rs=connection.selectQuery("SELECT max(order_id) FROM \"Order\"");

                        int newOrderID=0;

                        if(rs.next()){
                            //Insert the customer
                            newOrderID= rs.getInt(1)+1;
                        }

                        //Make order 
                        String queryStr=String.format("INSERT INTO \"Order\" (order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) "
                        +"VALUES(%d,'%s',%d,%d,'%s',%d)",newOrderID,payment_method,customer_id,product_id,com,Integer.parseInt(quantity.getText()));
                        connection.insertQuery(queryStr);

                        //Fix the delivery
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                        Date date = new Date();
                        //System.out.println(dateFormat.format(date)); 

                        //Make the delivery
                        queryStr=String.format("INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) "+
                        "VALUES(%d,DATE '%s',%d,'Arrival in 2 days')",newOrderID,dateFormat.format(date),newOrderID);
                        connection.insertQuery(queryStr);

                        Stage st = (Stage)submitButton.getScene().getWindow();
                        st.close();

                        connection.conn.close();
                    }
                    else{
                        warningBox.setVisible(false);
                        warningBox.setText("Invalid Quantity.");
                        warningBox.setVisible(true);
                    }
                }
                else{
                    warningBox.setVisible(false);
                    warningBox.setText("Invalid Data.");
                    warningBox.setVisible(true);
                }
            }
            else{
                warningBox.setVisible(false);
                warningBox.setText("No payment option selected.");
                warningBox.setVisible(true);
            }
        }
    }

    @FXML
    private void makeNewOrder(MouseEvent event) throws SQLException {
        
        //Get the required fields
        String payment_method =menu_button.getText();
        String com = comments.getText();
       
        if(!payment_method.equals("Payment Options")){
            if(quantity.getText().length()!=0){
                if(Integer.parseInt(quantity.getText()) > 0){
                    
                    //Fix the comments
                    if(com.length()==0){
                        com="No comments";
                    }
                    
                    DatabaseFunctions connection = new DatabaseFunctions("localhost","5432","eidiko_thema","postgres","postgres");
                    
                    //Make the order id
                    ResultSet rs=connection.selectQuery("SELECT max(order_id) FROM \"Order\"");
                    
                    int newOrderID=0;
                
                    if(rs.next()){
                        //Insert the customer
                        newOrderID= rs.getInt(1)+1;
                    }
                    
                    //Make order 
                    String queryStr=String.format("INSERT INTO \"Order\" (order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) "
                    +"VALUES(%d,'%s',%d,%d,'%s',%d)",newOrderID,payment_method,customer_id,product_id,com,Integer.parseInt(quantity.getText()));
                    connection.insertQuery(queryStr);
                    
                    //Fix the delivery
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    Date date = new Date();
                    //System.out.println(dateFormat.format(date)); 
                    
                    //Make the delivery
                    queryStr=String.format("INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) "+
                    "VALUES(%d,DATE '%s',%d,'Arrival in 2 days')",newOrderID,dateFormat.format(date),newOrderID);
                    connection.insertQuery(queryStr);

                    Stage st = (Stage)submitButton.getScene().getWindow();
                    st.close();
                    
                    connection.conn.close();
                }
                else{
                    warningBox.setVisible(false);
                    warningBox.setText("Invalid Quantity.");
                    warningBox.setVisible(true);
                }
            }
            else{
                warningBox.setVisible(false);
                warningBox.setText("Invalid Data.");
                warningBox.setVisible(true);
            }
        }
        else{
            warningBox.setVisible(false);
            warningBox.setText("No payment option selected.");
            warningBox.setVisible(true);
        }
    }

    @FXML
    private void closeWindow(MouseEvent event) {
        Stage st = (Stage)cancelBut.getScene().getWindow();
        st.close();
    }
    
}
