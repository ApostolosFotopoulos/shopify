/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Customer;

import Customer.OrderController;
import Customer.TotalCostController;
import Customer.ProductController;
import GeneralPurpose.ProgressBarController;
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
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import GeneralPurpose.DeleteAccountConfirmationController;

/**
 * FXML Controller class
 *
 * @author Apostolis
 */
public class CustomerOptionsController implements Initializable {

    
    public int customer_id;
    @FXML
    private AnchorPane anchorPanel;
    @FXML
    private StackPane stackPanelMain;
    @FXML
    private MenuItem showProducts;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    public void setCustomerID(String uname) throws SQLException{
        
        // Get the id
        DatabaseFunctions connection = new DatabaseFunctions("localhost","5432","eidiko_thema","postgres","postgres");
        String queryStr=String.format("SELECT customer_id FROM CUSTOMER C where C.username='%s'",uname);
        ResultSet rs=connection.selectQuery(queryStr);
        if(rs.next()){
            customer_id=rs.getInt(1);
            //System.out.println("Customer ID: "+customer_id);
        }
        connection.conn.close();
    }

    @FXML
    private void makeTheList(ActionEvent event) throws IOException {
        
        //Make a scrollPane at stackpanel
        ScrollPane scroll = new ScrollPane(new Label("Processing"));
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setId("vboxStyle");
        scroll.prefWidthProperty().bind(anchorPanel.widthProperty());
        scroll.prefHeightProperty().bind(anchorPanel.heightProperty());
        
      
        //AnchorPane with the loading bar
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GeneralPurpose/ProgressBar.fxml"));
        AnchorPane progBar = (AnchorPane)fxmlLoader.load();
        progBar.prefWidthProperty().bind(anchorPanel.widthProperty());
        progBar.prefHeightProperty().bind(anchorPanel.heightProperty());
        
        scroll.setContent(progBar);
        
        //AnchorPane for the results
        AnchorPane anc = new AnchorPane();
        anc.prefWidthProperty().bind(anchorPanel.widthProperty());
        anc.prefHeightProperty().bind(anchorPanel.heightProperty());
        anc.setId("vboxStyle");
        
        //Add to stackPanel
        stackPanelMain.getChildren().setAll(scroll);
        
        Task<Integer> task = new Task<Integer>() {
            @Override protected Integer call() throws Exception {
                
                DatabaseFunctions connection = new DatabaseFunctions("localhost","5432","eidiko_thema","postgres","postgres");
                
                String queryStr = String.format("SELECT * FROM product");
                ResultSet products = connection.selectQuery(queryStr);
                
                //Count the number of orders
                int totalProducts=0;
                while(products.next()){
                    totalProducts++;
                }
                
                queryStr = String.format("SELECT * FROM product");
                products = connection.selectQuery(queryStr);

                int c_id=-1;

                VBox box = new VBox(10);
                box.setId("vboxStyle");
                
                int counterProducts=0;
                //Go through the orders we have
                while(products.next()){
                    
                    counterProducts++;
                    
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Customer/Product.fxml"));

                    AnchorPane stack = (AnchorPane)fxmlLoader.load();

                    stack.prefWidthProperty().bind(stackPanelMain.widthProperty());

                    stack.setPrefHeight(280);     

                    //Fix the product details
                    ProductController controller;
                    controller = fxmlLoader.<ProductController>getController();
                    controller.changeLabel(products.getString("product_name").trim());
                    controller.changeSupplier(products.getString("fk1_supplier_name").trim());
                    controller.changePrice(products.getDouble("product_price"));
                    controller.changeDesc(products.getString("product_desc").trim());
                    controller.setCustomerID(customer_id);

                    //Get the image from binary
                    byte [] imgBytes = null;
                    imgBytes=products.getBytes("product_img");

                    InputStream in = new ByteArrayInputStream(imgBytes);
                    BufferedImage img = ImageIO.read(in);

                    controller.changeImage(img);

                    box.getChildren().add(stack);
                     
                    
                    //Upgrade the progressbar
                    updateProgress(counterProducts,totalProducts);
                    //System.out.println("CounterOrders: "+counterProducts);

                }
                anc.getChildren().add(box);

                connection.conn.close();
               return 10;
            }

            @Override protected void succeeded() {
                //System.out.println("Success");
                scroll.setContent(anc);
            }
        };
        
        //Bind the progressBar with the task
        ProgressBarController controller;
        controller = fxmlLoader.<ProgressBarController>getController();
        controller.bind(task);
        
        //Run the thread to create the windows
        Thread t = new Thread(task);
        t.start();

    }

    @FXML
    private void goToCart(ActionEvent event) throws SQLException, IOException {
        
        
        //Make a scrollPane at stackpanel
        ScrollPane scroll = new ScrollPane(new Label("Processing"));
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setId("vboxStyle");
        scroll.prefWidthProperty().bind(anchorPanel.widthProperty());
        scroll.prefHeightProperty().bind(anchorPanel.heightProperty());
        
      
        //AnchorPane with the loading bar
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GeneralPurpose/ProgressBar.fxml"));
        AnchorPane progBar = (AnchorPane)fxmlLoader.load();
        progBar.prefWidthProperty().bind(anchorPanel.widthProperty());
        progBar.prefHeightProperty().bind(anchorPanel.heightProperty());
        
        scroll.setContent(progBar);
        
        //AnchorPane for the results
        AnchorPane anc = new AnchorPane();
        anc.prefWidthProperty().bind(anchorPanel.widthProperty());
        anc.prefHeightProperty().bind(anchorPanel.heightProperty());
        anc.setId("vboxStyle");
        
        //Add to stackPanel
        stackPanelMain.getChildren().setAll(scroll);
        
        Task<Integer> task = new Task<Integer>() {
            @Override protected Integer call() throws Exception {
                
                DatabaseFunctions connection = new DatabaseFunctions("localhost","5432","eidiko_thema","postgres","postgres");
                
                String queryStr = String.format("SELECT * FROM \"Order\" O, PRODUCT P where O.fk1_customer_id =%d and P.product_id =O.fk2_product_id",customer_id);
                ResultSet orders= connection.selectQuery(queryStr);
                
                //Count the number of orders
                int totalOrders=0;
                while(orders.next()){
                    totalOrders++;
                }
                
                queryStr = String.format("SELECT * FROM \"Order\" O, PRODUCT P where O.fk1_customer_id =%d and P.product_id =O.fk2_product_id",customer_id);
                orders= connection.selectQuery(queryStr);
                
                
                //Calculate the total cost
                queryStr=String.format("SELECT SUM(product_price * product_quantity_per_product) FROM PRODUCT P,\"Order\" O " +
                "WHERE P.product_id = O.fk2_product_id and O.fk1_customer_id=%d",customer_id);

                ResultSet tCost = connection.selectQuery(queryStr);
                tCost.next();
                String totalCost =String.format("%.2f",tCost.getDouble(1));


                VBox box = new VBox(10);
                box.setId("vboxStyle");
                
                //Add the total cost to VBox
                FXMLLoader fxmlTotalCost = new FXMLLoader(getClass().getResource("/Customer/totalCost.fxml"));

                AnchorPane totalCostStack = (AnchorPane)fxmlTotalCost.load();

                totalCostStack.prefWidthProperty().bind(anchorPanel.widthProperty());

                TotalCostController control;
                control = fxmlTotalCost.<TotalCostController>getController();
                control.setTotalCost(totalCost);

                box.getChildren().add(totalCostStack);
                
                int counterOrders=0;
                
                while(orders.next()){
                    counterOrders++;
                    
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Customer/Order.fxml"));

                    AnchorPane stack = (AnchorPane)fxmlLoader.load();
                    stack.setId("vboxStyle");
                    stack.prefWidthProperty().bind(anchorPanel.widthProperty());

                    //Fix the product details
                    OrderController controller;
                    controller = fxmlLoader.<OrderController>getController();
                    controller.changeProductParameters(orders.getInt("fk2_product_id"));
                    controller.changeQuantity(orders.getInt("product_quantity_per_product"));
                    controller.changeOrderID(orders.getInt("order_id"));
                    controller.setCustomerID(customer_id);
                    controller.setScrollPane(scroll);

                    //Upgrade the progressbar
                    updateProgress(counterOrders,totalOrders);
                    //System.out.println("CounterOrders: "+counterProducts);
                    
                    box.getChildren().add(stack);
                }
                anc.getChildren().add(box);
                return 10;
            }

            @Override protected void succeeded() {
                //System.out.println("Success");
                scroll.setContent(anc);
            }
        };
        
        //Bind the progressBar with the task
        ProgressBarController controller;
        controller = fxmlLoader.<ProgressBarController>getController();
        controller.bind(task);
        
        //Run the thread to create the windows
        Thread t = new Thread(task);
        t.start();
    }

    @FXML
    private void logout(ActionEvent event) throws IOException {
        
        //Close main window
        Stage oldStage = (Stage) anchorPanel.getScene().getWindow();
        oldStage.close();
        
        Parent root =FXMLLoader.load(getClass().getResource("/GeneralPurpose/Login_Form.fxml"));
        
        Scene scene = new Scene(root, 420, 420);
        String css = this.getClass().getResource("CustomCSS.css").toExternalForm(); 
        scene.getStylesheets().add(css);
        
        Stage primaryStage= new Stage();
        primaryStage.setTitle("Login Form");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @FXML
    private void deletePermantlyAccount(ActionEvent event) throws IOException {
         
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/GeneralPurpose/DeleteAccountConfirmation.fxml"));
        
        Scene scene = new Scene(fxmlLoader.load(), 500, 180);
       
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle("Confirmation");
        stage.setScene(scene);
        
        DeleteAccountConfirmationController controller;
        controller=fxmlLoader.<DeleteAccountConfirmationController>getController();
        controller.setID(customer_id);
        controller.setRole("CUSTOMER");
        controller.getMainWindow(anchorPanel);
        
        
        stage.show();
            
    }


    
}
