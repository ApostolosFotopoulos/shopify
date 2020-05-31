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
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Apostolis
 */
public class CustomerDetailsController implements Initializable {

    @FXML
    private Label customer_id;
    @FXML
    private Label lastname;
    @FXML
    private Label firstname;
    @FXML
    private Label numOfOrders;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setCustomerID(int id) throws SQLException{
        customer_id.setText("ID:"+id);
        
        DatabaseFunctions connection = new DatabaseFunctions("localhost","5432","eidiko_thema","postgres","postgres");
        String queryStr = String.format("SELECT COUNT(order_id) FROM \"Order\" O,CUSTOMER C\n WHERE O.fk1_customer_id=C.customer_id and C.customer_id=%d",id);
        ResultSet rs=connection.selectQuery(queryStr);
        rs.next();
        numOfOrders.setText("Orders made:"+rs.getInt(1));
        connection.conn.close();
    }
    
    public void setLastname(String lname){
        lastname.setText(lname);
    }
    
    public void setFirstname(String fname){
        firstname.setText(fname);
    }
   
}
