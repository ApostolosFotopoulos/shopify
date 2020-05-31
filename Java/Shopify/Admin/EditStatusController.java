/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin;

import Admin.OrdersForAdminController;
import Database_Connection.DatabaseFunctions;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Apostolis
 */
public class EditStatusController implements Initializable {

    public int delivery_id;
    public ScrollPane scroll;
    
    @FXML
    private Button submitButton;
    @FXML
    private Label warningBox;
    @FXML
    private TextField status;
    @FXML
    private Button cancelBut;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setDeliveryID(int id){
        delivery_id=id;
    }
    public void setScrollPanel(ScrollPane sc){
        scroll=sc;
    }

    @FXML
    private void addNewStatusWithKey(KeyEvent event) throws SQLException, IOException {
        if(event.getCode().equals(KeyCode.ENTER)){
            String st = status.getText();
        
            if(st.length()!=0){

                //Update the status
                DatabaseFunctions connection = new DatabaseFunctions("localhost","5432","eidiko_thema","postgres","postgres");
                String queryStr = String.format("UPDATE DELIVERY D SET status='%s' where D.delivery_id=%d",st,delivery_id);
                connection.insertQuery(queryStr);

                AnchorPane anc = new AnchorPane();
                anc.prefWidthProperty().bind(scroll.widthProperty());
                anc.prefHeightProperty().bind(scroll.heightProperty());
                anc.setId("vboxStyle");
                scroll.setContent(anc);

                String getCustomerQuery = String.format("SELECT * FROM CUSTOMER C, \"Order\" O,DELIVERY D ,Product P "+
                "where C.customer_id = O.fk1_customer_id and O.order_id =D.fk1_order_id and O.fk2_product_id =P.product_id ORDER BY(customer_id)");
                ResultSet orders = connection.selectQuery(getCustomerQuery);

                int c_id=-1;

                VBox box = new VBox(10);
                box.setId("vboxStyle");
                //Go through the orders we have
                while(orders.next()){

                    //New client
                    if(c_id!=orders.getInt("customer_id")){

                        c_id=orders.getInt("customer_id");

                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Admin/CustomerDetails.fxml"));

                        AnchorPane stack = (AnchorPane)fxmlLoader.load();
                        stack.setId("vboxStyle");
                        stack.prefWidthProperty().bind(scroll.widthProperty());

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
                    stack.prefWidthProperty().bind(scroll.widthProperty());

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

                }
                anc.getChildren().add(box);

                Stage sta = (Stage)warningBox.getScene().getWindow();
                sta.close();
                connection.conn.close();

            }
            else{
                warningBox.setVisible(false);
                warningBox.setText("A new status was not entered.");
                warningBox.setVisible(true);
            }
        }
    }

    @FXML
    private void addNewStatus(MouseEvent event) throws SQLException, IOException {
        String st = status.getText();
        
        if(st.length()!=0){
            
            //Update the status
            DatabaseFunctions connection = new DatabaseFunctions("localhost","5432","eidiko_thema","postgres","postgres");
            String queryStr = String.format("UPDATE DELIVERY D SET status='%s' where D.delivery_id=%d",st,delivery_id);
            connection.insertQuery(queryStr);
         
            AnchorPane anc = new AnchorPane();
            anc.prefWidthProperty().bind(scroll.widthProperty());
            anc.prefHeightProperty().bind(scroll.heightProperty());
            anc.setId("vboxStyle");
            scroll.setContent(anc);

            String getCustomerQuery = String.format("SELECT * FROM CUSTOMER C, \"Order\" O,DELIVERY D ,Product P "+
            "where C.customer_id = O.fk1_customer_id and O.order_id =D.fk1_order_id and O.fk2_product_id =P.product_id ORDER BY(customer_id)");
            ResultSet orders = connection.selectQuery(getCustomerQuery);

            int c_id=-1;

            VBox box = new VBox(10);
            box.setId("vboxStyle");
            //Go through the orders we have
            while(orders.next()){

                //New client
                if(c_id!=orders.getInt("customer_id")){

                    c_id=orders.getInt("customer_id");

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Admin/CustomerDetails.fxml"));

                    AnchorPane stack = (AnchorPane)fxmlLoader.load();
                    stack.setId("vboxStyle");
                    stack.prefWidthProperty().bind(scroll.widthProperty());

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
                stack.prefWidthProperty().bind(scroll.widthProperty());

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

            }
            anc.getChildren().add(box);

            Stage sta = (Stage)warningBox.getScene().getWindow();
            sta.close();
            connection.conn.close();
            
        }
        else{
            warningBox.setVisible(false);
            warningBox.setText("A new status was not entered.");
            warningBox.setVisible(true);
        }
    }

    @FXML
    private void closeWindow(MouseEvent event) {
        Stage st = (Stage)cancelBut.getScene().getWindow();
        st.close();
    }
    
}
