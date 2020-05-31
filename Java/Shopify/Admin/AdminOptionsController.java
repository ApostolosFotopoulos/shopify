/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin;

import Admin.CustomerDetailsController;
import Admin.OrdersForAdminController;
import GeneralPurpose.ProgressBarController;
import Database_Connection.DatabaseFunctions;
import java.io.IOException;
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
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import GeneralPurpose.DeleteAccountConfirmationController;

/**
 * FXML Controller class
 *
 * @author Apostolis
 */
public class AdminOptionsController implements Initializable {

    public int adminID;
    @FXML
    private AnchorPane anchorPanel;
    @FXML
    private StackPane stackPanelMain;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setAdminID(String uname) throws SQLException{
        
        // Get the id
        DatabaseFunctions connection = new DatabaseFunctions("localhost","5432","eidiko_thema","postgres","postgres");
        String queryStr=String.format("SELECT admin_id FROM ADMIN A where A.username='%s'",uname);
        ResultSet rs=connection.selectQuery(queryStr);
        if(rs.next()){
            adminID=rs.getInt(1);
            //System.out.println("Customer ID: "+adminID);
        }
        connection.conn.close();
    }

    @FXML
    private void addProduct(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/Admin/AddProduct.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 490, 761);
            String css = this.getClass().getResource("CustomCSS.css").toExternalForm();
            scene.getStylesheets().add(css);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Create New Product");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        } 
    }



    @FXML
    private void showBestCustomers(ActionEvent event) throws SQLException, IOException {
        
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/Admin/BestCustomerDetail.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 763, 552);
            String css = this.getClass().getResource("CustomCSS.css").toExternalForm();
            scene.getStylesheets().add(css);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Best Customers");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        } 
    }

    @FXML
    private void showBestCities(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/Admin/BestCities.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 763, 552);
            String css = this.getClass().getResource("CustomCSS.css").toExternalForm();
            scene.getStylesheets().add(css);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Best Cities");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        } 
    }

    @FXML
    private void deleteTable(ActionEvent event) {
         try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/Admin/DeleteTableForm.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 437, 293);
            String css = this.getClass().getResource("CustomCSS.css").toExternalForm();
            scene.getStylesheets().add(css);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Delete table");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        } 
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
        controller.setID(adminID);
        controller.setRole("ADMIN");
        controller.getMainWindow(anchorPanel);
       
        stage.show();
    }
    
    @FXML
    private void showAllOrders(ActionEvent event) throws SQLException, IOException {
        
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
                
                String getCustomerQuery = String.format("SELECT * FROM CUSTOMER C, \"Order\" O,DELIVERY D ,Product P "+
                "where C.customer_id = O.fk1_customer_id and O.order_id =D.fk1_order_id and O.fk2_product_id =P.product_id ORDER BY(customer_id)");
                ResultSet orders = connection.selectQuery(getCustomerQuery);
                
                //Count the number of orders
                int totalOrders=0;
                while(orders.next()){
                    totalOrders++;
                }
                
                //Reset the result pointer
                getCustomerQuery = String.format("SELECT * FROM CUSTOMER C, \"Order\" O,DELIVERY D ,Product P "+
                "where C.customer_id = O.fk1_customer_id and O.order_id =D.fk1_order_id and O.fk2_product_id =P.product_id ORDER BY(customer_id)");
                orders = connection.selectQuery(getCustomerQuery);

                int c_id=-1;

                VBox box = new VBox(10);
                box.setId("vboxStyle");
                
                int counterOrders=0;
                //Go through the orders we have
                while(orders.next()){
                    
                    counterOrders++;
                    
                    //New client
                    if(c_id!=orders.getInt("customer_id")){

                        c_id=orders.getInt("customer_id");

                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Admin/CustomerDetails.fxml"));

                        AnchorPane stack = (AnchorPane)fxmlLoader.load();
                        stack.setId("vboxStyle");
                        stack.prefWidthProperty().bind(anchorPanel.widthProperty());

                        CustomerDetailsController controller;
                        controller = fxmlLoader.<CustomerDetailsController>getController();
                        controller.setCustomerID(c_id);
                        controller.setFirstname(orders.getString("customer_fname").trim());
                        controller.setLastname(orders.getString("customer_lname").trim());
                        box.getChildren().add(stack);
                    }


                    //Make the order form
                    FXMLLoader orderfxml = new FXMLLoader(getClass().getResource("/Admin/OrdersForAdmin.fxml"));

                    AnchorPane stack = (AnchorPane)orderfxml.load();
                    stack.setId("vboxStyle");
                    stack.prefWidthProperty().bind(anchorPanel.widthProperty());

                    OrdersForAdminController controller;
                    controller = orderfxml.<OrdersForAdminController>getController();
                    controller.setCustomerID(c_id);
                    controller.changeQuantity(orders.getInt("product_quantity_per_product"));
                    controller.setScrollPane(scroll);
                    controller.changeOrderID(orders.getInt("order_id"));
                    controller.changeProductParameters(orders.getInt("product_id"));
                    controller.changeStatus(orders.getString("status"));
                    controller.setCustomerID(c_id);
                    controller.setDeliveryID(orders.getInt("delivery_id"));


                    box.getChildren().add(stack);
                    
                    //Upgrade the progressbar
                    updateProgress(counterOrders,totalOrders);
                    //System.out.println("CounterOrders: "+counterOrders);

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
    private void addFieldToTable(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/Admin/AddField.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 437, 396);
            String css = this.getClass().getResource("CustomCSS.css").toExternalForm();
            scene.getStylesheets().add(css);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Field To Table");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        } 
    }

    @FXML
    private void createNewAdmin(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/Admin/NewAdminAdd.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 461, 376);
            String css = this.getClass().getResource("CustomCSS.css").toExternalForm();
            scene.getStylesheets().add(css);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add New Admin");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        } 
    }

    @FXML
    private void deleteFieldFromTable(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/Admin/DeleteField.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 437, 294);
            String css = this.getClass().getResource("CustomCSS.css").toExternalForm();
            scene.getStylesheets().add(css);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Delete Field From Table");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        } 
    }

    @FXML
    private void deleteCustomer(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/Admin/DeleteCustomerForm.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 547, 308);
            String css = this.getClass().getResource("CustomCSS.css").toExternalForm();
            scene.getStylesheets().add(css);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Delete Field From Table");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        } 
    }
    
}
