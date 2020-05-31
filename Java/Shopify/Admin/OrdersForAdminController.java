/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin;

import Database_Connection.DatabaseFunctions;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author Apostolis
 */
public class OrdersForAdminController implements Initializable {

    public int customer_id;
    public int order_id;
    public int delivery_id;
    
    public ScrollPane scroll;
    @FXML
    private AnchorPane anchorPane;
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
    private Button editDeliveryStatus;
    @FXML
    private Label status;

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
    
    public void setDeliveryID(int id){
        delivery_id=id;
    }
    public void setScrollPane(ScrollPane p ){
        scroll=p;
    }
    public void changeOrderID(int id){
        order_id=id;
    }
    
    public void changeStatus(String st){
        status.setText(st);
    }
    
    @FXML
    private void editTheStatus(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/Admin/EditStatus.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 400, 278);


        String css = this.getClass().getResource("CustomCSS.css").toExternalForm(); 
        scene.getStylesheets().add(css);
        Stage stage = new Stage();
        stage.setTitle("Edit Status");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        
        EditStatusController controller;
        controller = fxmlLoader.<EditStatusController>getController();
        controller.setDeliveryID(delivery_id);
        controller.setScrollPanel(scroll);
       

        stage.show();
    }
    
}
