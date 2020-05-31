/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Customer;

import Customer.OrderFormController;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Apostolis
 */
public class ProductController implements Initializable {

    @FXML
    private Label productName;
    @FXML
    private Label supplierName;
    @FXML
    private Label price;
    @FXML
    private Button buyButton;
    @FXML
    private TextArea description;
    @FXML
    private ImageView productImage;
    
    public int customer_id;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void changeLabel(String newLabel){
        productName.setText(newLabel);
    }
    
    public void changeSupplier(String Supplier){
        supplierName.setText(Supplier);
    }
    
    public void changePrice(double Price){
        price.setText("Price: "+Price+"$");
    }
    
    public void changeDesc(String desc){
        description.setText(desc);
    }
   
    public void changeImage(BufferedImage img) throws SQLException{
        Image image = SwingFXUtils.toFXImage(img, null);
        productImage.setImage(image);
    }
    
    public void setCustomerID(int cust_id){
        customer_id=cust_id;
    }


    @FXML
    private void addToCart(MouseEvent event) throws IOException, SQLException {
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Customer/OrderForm.fxml"));
        
        Scene scene = new Scene(fxmlLoader.load(), 500, 650);
        String css = this.getClass().getResource("CustomCSS.css").toExternalForm(); 
        scene.getStylesheets().add(css);
        
        OrderFormController controller;
        controller = fxmlLoader.<OrderFormController>getController();
        controller.setCustomerID(customer_id);
        controller.setProductID(productName.getText());
        
        Stage stage= new Stage();
        stage.setTitle("Make order");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
}
