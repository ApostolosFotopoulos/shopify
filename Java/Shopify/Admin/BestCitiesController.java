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

/**
 * FXML Controller class
 *
 * @author Apostolis
 */
public class BestCitiesController implements Initializable {

    @FXML
    private StackedBarChart<?, ?> bestCitiesChart;
    @FXML
    private NumberAxis y;
    @FXML
    private CategoryAxis x;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DatabaseFunctions connection = new DatabaseFunctions("localhost","5432","eidiko_thema","postgres","postgres");
        String queryStr=String.format("SELECT address_city,COUNT(*) AS Count FROM CUSTOMER C,ADDRESS A WHERE C.fk1_address_id =A.address_id "+
        "GROUP BY A.address_city ORDER BY Count DESC LIMIT 10"); 
        try {
            ResultSet rs=connection.selectQuery(queryStr);
            XYChart.Series set1 = new XYChart.Series<>();
            bestCitiesChart.setLegendVisible(false);
            
            while(rs.next()){
                String cit =rs.getString("address_city").trim();
                int num = rs.getInt("count");
                set1.getData().add(new XYChart.Data(cit,num));
            }
            bestCitiesChart.getData().addAll(set1);
            connection.conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(BestCitiesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
