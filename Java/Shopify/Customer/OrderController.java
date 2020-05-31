/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Customer;

import Customer.TotalCostController;
import Customer.ProductController;
import Database_Connection.DatabaseFunctions;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author Apostolis
 */
public class OrderController implements Initializable {

    public int customer_id;
    public int order_id;
    
    @FXML
    private Label product;
    @FXML
    private Label supplier;
    @FXML
    private ImageView img;
    @FXML
    private Label price;
    @FXML
    private Label quantity;
    @FXML
    private Button cancelButton;
    @FXML
    private AnchorPane anchorPane;
    
    private ScrollPane scroll;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void changeProductParameters(int p_id) throws SQLException, IOException{
        DatabaseFunctions connection = new DatabaseFunctions("localhost","5432","eidiko_thema","postgres","postgres");
        String queryStr=String.format("SELECT * FROM PRODUCT P where P.product_id='%d'",p_id);
        ResultSet rs=connection.selectQuery(queryStr);
        if(rs.next()){
            product.setText(rs.getString("product_name").trim());
            supplier.setText(rs.getString("fk1_supplier_name").trim());
            String temp =String.format ("%.2f", rs.getDouble("product_price"));
            price.setText("Price: "+temp+"$");
            
            //Get the image from binary
            byte [] imgBytes = null;
            imgBytes=rs.getBytes("product_img");

            InputStream in = new ByteArrayInputStream(imgBytes);
            BufferedImage imgBuf = ImageIO.read(in);
            Image image = SwingFXUtils.toFXImage(imgBuf, null);
            img.setImage(image);
        }
        
        connection.conn.close();
    }
 
    public void changeQuantity(int q){
        quantity.setText("Quantity: "+q);
    }   
    
    public void setCustomerID(int id){
        customer_id=id;
    }

    public void setScrollPane(ScrollPane p ){
        scroll=p;
    }
    public void changeOrderID(int id){
        order_id=id;
    }
    
    @FXML
    private void cancelTheOrder(MouseEvent event) throws SQLException, IOException {
        
        
        DatabaseFunctions connection = new DatabaseFunctions("localhost","5432","eidiko_thema","postgres","postgres");
        
        //Delete the order
        String queryStr = String.format("DELETE FROM \"Order\" O where O.order_id = %d",order_id);
        connection.insertQuery(queryStr);
        
        // Recreate the list of cart 
        queryStr = String.format("SELECT * FROM \"Order\" O, PRODUCT P where O.fk1_customer_id =%d and P.product_id =O.fk2_product_id",customer_id);
        ResultSet rs;
        
        
        AnchorPane anc = new AnchorPane();
        anc.prefWidthProperty().bind(scroll.widthProperty());
        anc.prefHeightProperty().bind(scroll.heightProperty());
        anc.setId("vboxStyle");
        scroll.setContent(anc);
        
        try {
            rs = connection.selectQuery(queryStr);
            
            //Calculate the total cost
            queryStr=String.format("SELECT SUM(product_price * product_quantity_per_product) FROM PRODUCT P,\"Order\" O " +
            "WHERE P.product_id = O.fk2_product_id and O.fk1_customer_id=%d",customer_id);
            
            ResultSet tCost = connection.selectQuery(queryStr);
            tCost.next();
            String totalCost =String.format("%.3f",tCost.getDouble(1));
            
            
            VBox box = new VBox(10);
            box.setId("back");
            String css = this.getClass().getResource("CustomCSS.css").toExternalForm(); 
            box.getStylesheets().add(css);
            
            //Add the total cost to VBox
            FXMLLoader fxmlTotalCost = new FXMLLoader(getClass().getResource("/Customer/totalCost.fxml"));
                
            AnchorPane totalCostStack = (AnchorPane)fxmlTotalCost.load();
            totalCostStack.setId("back");
            css = this.getClass().getResource("CustomCSS.css").toExternalForm(); 
            totalCostStack.getStylesheets().add(css);
            
            totalCostStack.prefWidthProperty().bind(scroll.widthProperty());
                
            totalCostStack.setPrefHeight(80);  
            
            TotalCostController control;
            control = fxmlTotalCost.<TotalCostController>getController();
            control.setTotalCost(totalCost);
           
            box.getChildren().add(totalCostStack);
            
           
            while(rs.next()){
                
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Customer/Order.fxml"));
                
                AnchorPane stack = (AnchorPane)fxmlLoader.load();
                stack.setId("back");
                css = this.getClass().getResource("CustomCSS.css").toExternalForm(); 
                stack.getStylesheets().add(css);
                
                stack.prefWidthProperty().bind(scroll.widthProperty());
                
                stack.setPrefHeight(280);     
                
                //Fix the product details
                OrderController controller;
                controller = fxmlLoader.<OrderController>getController();
                controller.changeProductParameters(rs.getInt("fk2_product_id"));
                controller.changeQuantity(rs.getInt("product_quantity_per_product"));
                controller.changeOrderID(rs.getInt("order_id"));
                controller.setCustomerID(customer_id);
                controller.setScrollPane(scroll);
                
                
                box.getChildren().add(stack);
            }
            anc.getChildren().add(box);
          
        } catch (SQLException ex) {
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        connection.conn.close();
    }
}
