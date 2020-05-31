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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author justdemo
 */
public class BestCustomerDetailController implements Initializable {
    
    
    @FXML
    private NumberAxis y;
    @FXML
    private CategoryAxis x;
    @FXML
    private StackedBarChart<?, ?> bestCustomersChart;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        DatabaseFunctions connection = new DatabaseFunctions("localhost","5432","eidiko_thema","postgres","postgres");
        
        //Calculate the top customers
        String queryStr = String.format("SELECT customer_fname,customer_lname,customer_id, SUM(P.product_price * O.product_quantity_per_product) AS SUM_RESULT "+
	"FROM CUSTOMER C,PRODUCT P,\"Order\" O WHERE O.fk1_customer_id = C.customer_id and O.fk2_product_id=P.product_id "+
	"GROUP BY (customer_fname,customer_lname,customer_id) ORDER BY SUM_RESULT DESC LIMIT 10");
        
        try {
            
            XYChart.Series set1 = new XYChart.Series<>();
            bestCustomersChart.setLegendVisible(false);
            
            ResultSet rs=connection.selectQuery(queryStr);
            while(rs.next()){
                //Get customer parameters
                String customer_fname=rs.getString("customer_fname").trim();
                String customer_lname=rs.getString("customer_lname").trim();
                int customer_id=rs.getInt("customer_id");
                
                //Calculate the total cost of the customer
                String calculate_cost = String.format("SELECT SUM(product_price * product_quantity_per_product) FROM PRODUCT P,\"Order\" O	"+
                "WHERE P.product_id = O.fk2_product_id and O.fk1_customer_id=%d",customer_id);
            
                ResultSet totalCost =connection.selectQuery(calculate_cost);
                totalCost.next();
            
                double total_cost_client = totalCost.getDouble(1);
                
                
                set1.getData().add(new XYChart.Data(customer_fname.charAt(0)+"."+customer_lname,total_cost_client));
            }
            bestCustomersChart.getData().addAll(set1);
            connection.conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(BestCustomerDetailController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }        
}
