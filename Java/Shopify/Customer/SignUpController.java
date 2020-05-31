/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Customer;

import Database_Connection.DatabaseFunctions;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SignUpController implements Initializable {
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TextField firstname;
    @FXML
    private TextField lastname;
    @FXML
    private TextField email;
    @FXML
    private TextField phonenumber;
    @FXML
    private TextField country;
    @FXML
    private TextField addressname;
    @FXML
    private TextField city;
    @FXML
    private TextField addressnumber;
    @FXML
    private TextField zipcode;
    @FXML
    private Button submitButton;
    @FXML
    private Label warningBox;
    @FXML
    private Button cancelBut;

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        username.setPromptText("Username");
        password.setPromptText("Password");
        firstname.setPromptText("Firstname");
        lastname.setPromptText("Lastname");
        email.setPromptText("E-mail");
        phonenumber.setPromptText("Phone");
        country.setPromptText("Country");
        addressname.setPromptText("Address");
        city.setPromptText("City");
        addressnumber.setPromptText("Number");
        zipcode.setPromptText("Zipcode");
    }    

    @FXML
    private void tryToSignUp(MouseEvent event) throws SQLException {
        String usrname = username.getText();
        String passwd = password.getText();
        String fname =  firstname.getText();
        String lname =  lastname.getText();
        String mail =  email.getText();
        String phone =  phonenumber.getText();
        String cntry =  country.getText();
        String cit =  city.getText();
        String address =  addressname.getText();
        String addnumber =  addressnumber.getText();
        String zip =  zipcode.getText();
        
        if(usrname.length()!=0 && passwd.length()!=0 && fname.length()!=0 && lname.length()!=0 && mail.length()!=0 
        && phone.length()!=0 && cntry.length()!=0 && cit.length()!=0 && address.length()!=0 && addnumber.length()!=0 
        && zip.length()!=0){
            
            DatabaseFunctions connection = new DatabaseFunctions("localhost","5432","eidiko_thema","postgres","postgres");

            System.out.println(usrname);
            ResultSet rs=connection.selectQuery("SELECT username FROM CUSTOMER C where C.username = '"+usrname+"'");

            System.out.println("Here username"+usrname);
            if(rs.next()){
                warningBox.setVisible(false);
                warningBox.setText("Username is already taken");
                warningBox.setVisible(true);
            }
            else{
                //Find the max id
                rs=connection.selectQuery("SELECT max(customer_id) FROM CUSTOMER");
                
                //Insert the customer
                int newCustomerId=0;
                
                if(rs.next()){
                    //Insert the customer
                    newCustomerId= rs.getInt(1)+1;
                }
                
                String queryStr=String.format("INSERT INTO CUSTOMER(password,username,customer_email,customer_lname,customer_fname,customer_id,customer_phone) "
                + "VALUES('%s','%s','%s','%s','%s',%d,'%s')",passwd,usrname,mail,lname,fname,newCustomerId,phone);
                connection.insertQuery(queryStr);

                //Check if address exists
                queryStr = String.format("SELECT address_id FROM ADDRESS A where A.address_name ='%s' and A.address_number=%d "
                + "and A.address_zipcode =%d and A.address_city = '%s' and A.address_country='%s'",address,Integer.parseInt(addnumber),Integer.parseInt(zip),cit,cntry);

                rs=connection.selectQuery(queryStr);
                if(rs.next()){
                    int address_id= rs.getInt(1);
                    queryStr=String.format("UPDATE CUSTOMER C SET fk1_address_id=%d where C.customer_id=%d",address_id,newCustomerId);
                    connection.insertQuery(queryStr);
                }
                else{

                    queryStr=String.format("SELECT max(address_id) FROM ADDRESS");
                    rs=connection.selectQuery(queryStr);
                    if(rs.next()){
                        int new_address_id=rs.getInt(1)+1;

                        //Insert new address
                        queryStr=String.format("INSERT INTO ADDRESS(address_country,address_city,address_zipcode,address_number,address_name,address_id)"
                        + " VALUES('%s','%s',%d,%d,'%s',%d)",cntry,cit,Integer.parseInt(zip),Integer.parseInt(addnumber),address,new_address_id);
                        connection.insertQuery(queryStr);

                        //Update Customer
                        queryStr=String.format("UPDATE CUSTOMER C SET fk1_address_id=%d where C.customer_id=%d",new_address_id,newCustomerId);
                        connection.insertQuery(queryStr);
                    }

                }
                
                //Close the stage
                Stage st = (Stage) submitButton.getScene().getWindow();
                st.close();
            }
             
            //Close the connection
            connection.conn.close();
        }
        else{
            warningBox.setVisible(false);
            warningBox.setText("Invalid Data.");
            warningBox.setVisible(true);
        }
    }

    @FXML
    private void tryToSignUpWithKey(KeyEvent event) throws SQLException {
        if(event.getCode().equals(KeyCode.ENTER)){
            
            String usrname = username.getText();
            String passwd = password.getText();
            String fname =  firstname.getText();
            String lname =  lastname.getText();
            String mail =  email.getText();
            String phone =  phonenumber.getText();
            String cntry =  country.getText();
            String cit =  city.getText();
            String address =  addressname.getText();
            String addnumber =  addressnumber.getText();
            String zip =  zipcode.getText();

            if(usrname.length()!=0 && passwd.length()!=0 && fname.length()!=0 && lname.length()!=0 && mail.length()!=0 
            && phone.length()!=0 && cntry.length()!=0 && cit.length()!=0 && address.length()!=0 && addnumber.length()!=0 
            && zip.length()!=0){

                DatabaseFunctions connection = new DatabaseFunctions("localhost","5432","eidiko_thema","postgres","postgres");

                System.out.println(usrname);
                ResultSet rs=connection.selectQuery("SELECT username FROM CUSTOMER C where C.username = '"+usrname+"'");

                if(rs.next()){
                    warningBox.setVisible(false);
                    warningBox.setText("Username is already taken");
                    warningBox.setVisible(true);
                }
                else{
                    //Find the max id
                    rs=connection.selectQuery("SELECT max(customer_id) FROM CUSTOMER");

                    if(rs.next()){

                        //Insert the customer
                        int newCustomerId= rs.getInt(1)+1;
                        String queryStr=String.format("INSERT INTO CUSTOMER(password,username,customer_email,customer_lname,customer_fname,customer_id,customer_phone) "
                        + "VALUES('%s','%s','%s','%s','%s',%d,'%s')",passwd,usrname,mail,lname,fname,newCustomerId,phone);
                        connection.insertQuery(queryStr);

                        //Check if address exists
                        queryStr = String.format("SELECT address_id FROM ADDRESS A where A.address_name ='%s' and A.address_number=%d "
                        + "and A.address_zipcode =%d and A.address_city = '%s' and A.address_country='%s'",address,Integer.parseInt(addnumber),Integer.parseInt(zip),cit,cntry);

                        rs=connection.selectQuery(queryStr);
                        if(rs.next()){
                            int address_id= rs.getInt(1);
                            queryStr=String.format("UPDATE CUSTOMER C SET fk1_address_id=%d where C.customer_id=%d",address_id,newCustomerId);
                            connection.insertQuery(queryStr);
                        }
                        else{

                            queryStr=String.format("SELECT max(address_id) FROM ADDRESS");
                            rs=connection.selectQuery(queryStr);
                            if(rs.next()){
                                int new_address_id=rs.getInt(1)+1;

                                //Insert new address
                                queryStr=String.format("INSERT INTO ADDRESS(address_country,address_city,address_zipcode,address_number,address_name,address_id)"
                                + " VALUES('%s','%s',%d,%d,'%s',%d)",cntry,cit,Integer.parseInt(zip),Integer.parseInt(addnumber),address,new_address_id);
                                connection.insertQuery(queryStr);

                                //Update Customer
                                queryStr=String.format("UPDATE CUSTOMER C SET fk1_address_id=%d where C.customer_id=%d",new_address_id,newCustomerId);
                                connection.insertQuery(queryStr);
                            }

                        }
                    }
                }

                //Close the stage
                Stage st = (Stage) submitButton.getScene().getWindow();
                st.close();

                //Close the connection
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
        Stage st = (Stage)cancelBut.getScene().getWindow();
        st.close();
    }
    
}
