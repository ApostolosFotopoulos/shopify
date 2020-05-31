/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin;

import Admin.AddNewSupplierController;
import Database_Connection.DatabaseFunctions;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.naming.spi.DirStateFactory.Result;
import javax.swing.JFileChooser;

/**
 * FXML Controller class
 *
 * @author Apostolis
 */
public class AddProductController implements Initializable {

    public File imgFile;
    @FXML
    private TextField productname;
    @FXML
    private TextField price;
    @FXML
    private TextField supplier;
    @FXML
    private Button submitButton;
    @FXML
    private TextArea description;
    @FXML
    private Label warningBox;
    @FXML
    private Button fileButton;
    @FXML
    private Label filename;
    @FXML
    private Button cancelBut;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        productname.setPromptText("Product Name");
        price.setPromptText("Price");
        supplier.setPromptText("Supplier");
        description.setPromptText("Add Product Description...");
        imgFile=new File("");
    }    

    @FXML
    private void addNewProductWithKey(KeyEvent event) throws SQLException, IOException {
        if(event.getCode().equals(KeyCode.ENTER)){
            String product=productname.getText();
            String pr=price.getText();
            String sup=supplier.getText();
            String des=description.getText();

            if(product.length()!=0 && pr.length()!=0 && sup.length()!=0 && imgFile.exists()){

                DatabaseFunctions connection = new DatabaseFunctions("localhost","5432","eidiko_thema","postgres","postgres");

                String queryStr = String.format("SELECT product_id FROM PRODUCT P where P.product_name='%s'",product);
                ResultSet rs=connection.selectQuery(queryStr);

                if(!rs.next()){
                    queryStr = String.format("SELECT supplier_name FROM SUPPLIER S where S.supplier_name='%s'",sup);
                    rs=connection.selectQuery(queryStr);

                    if(rs.next()){
                        queryStr = String.format("SELECT max(product_id) FROM Product ");
                        rs=connection.selectQuery(queryStr);
                        rs.next();
                        int product_id = rs.getInt(1)+1;

                        //Fix the description
                        if(des.length()==0){
                            des="";
                        }

                        try (FileInputStream fis = new FileInputStream(imgFile)) {
                            queryStr = String.format("INSERT INTO PRODUCT(product_id,product_price,product_desc,product_name,fk1_supplier_name,product_img_name,product_img) "+
                            "VALUES(%d,%f,'%s','%s','%s',?,?)",product_id,Double.parseDouble(pr),des,product,sup);
                            PreparedStatement ps =connection.conn.prepareStatement(queryStr);
                            ps.setString(1, imgFile.getName());
                            ps.setBinaryStream(2, fis, (int)imgFile.length());
                            ps.executeUpdate();
                            ps.close();
                        }


                        Stage st = (Stage)warningBox.getScene().getWindow();
                        st.close();
                    }
                    else{

                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource("AddNewSupplier.fxml"));

                        Scene scene = new Scene(fxmlLoader.load(), 400, 300);


                        String css = this.getClass().getResource("CustomCSS.css").toExternalForm(); 
                        scene.getStylesheets().add(css);
                        Stage stage = new Stage();
                        stage.setTitle("Add New Supplier");
                        stage.setScene(scene);
                        stage.initModality(Modality.APPLICATION_MODAL);

                        AddNewSupplierController controller;
                        controller=fxmlLoader.<AddNewSupplierController>getController();
                        controller.setProduct(product);
                        controller.setPrice(Double.parseDouble(pr));
                        controller.setSupplier(sup);
                        controller.getPanel(warningBox);
                        controller.setDescription(des);
                        controller.setFile(imgFile);

                        stage.show();

                    }
                }
                else{
                    warningBox.setVisible(false);
                    warningBox.setText("Product already exists.");
                    warningBox.setVisible(true);
                }   
                connection.conn.close();

            }
            else{
                if(product.length()==0 ||  pr.length()==0 || sup.length()==0){
                    warningBox.setVisible(false);
                    warningBox.setText("Invalid Data.");
                    warningBox.setVisible(true);
                }
                else if(!imgFile.exists()){
                    warningBox.setVisible(false);
                    warningBox.setText("No image was chosen.");
                    warningBox.setVisible(true);
                }

            }
        }
    }

    @FXML
    private void addNewProduct(MouseEvent event) throws SQLException, IOException {
        String product=productname.getText();
        String pr=price.getText();
        String sup=supplier.getText();
        String des=description.getText();
        
        if(product.length()!=0 && pr.length()!=0 && sup.length()!=0 && imgFile.exists()){
           
            DatabaseFunctions connection = new DatabaseFunctions("localhost","5432","eidiko_thema","postgres","postgres");
            
            String queryStr = String.format("SELECT product_id FROM PRODUCT P where P.product_name='%s'",product);
            ResultSet rs=connection.selectQuery(queryStr);
              
            if(!rs.next()){
                queryStr = String.format("SELECT supplier_name FROM SUPPLIER S where S.supplier_name='%s'",sup);
                rs=connection.selectQuery(queryStr);

                if(rs.next()){
                    queryStr = String.format("SELECT max(product_id) FROM Product ");
                    rs=connection.selectQuery(queryStr);
                    rs.next();
                    int product_id = rs.getInt(1)+1;

                    //Fix the description
                    if(des.length()==0){
                        des="";
                    }

                    try (FileInputStream fis = new FileInputStream(imgFile)) {
                        queryStr = String.format("INSERT INTO PRODUCT(product_id,product_price,product_desc,product_name,fk1_supplier_name,product_img_name,product_img) "+
                        "VALUES(%d,%f,'%s','%s','%s',?,?)",product_id,Double.parseDouble(pr),des,product,sup);
                        PreparedStatement ps =connection.conn.prepareStatement(queryStr);
                        ps.setString(1, imgFile.getName());
                        ps.setBinaryStream(2, fis, (int)imgFile.length());
                        ps.executeUpdate();
                        ps.close();
                    }
                    

                    Stage st = (Stage)warningBox.getScene().getWindow();
                    st.close();
                }
                else{

                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("AddNewSupplier.fxml"));

                    Scene scene = new Scene(fxmlLoader.load(), 400, 300);


                    String css = this.getClass().getResource("CustomCSS.css").toExternalForm(); 
                    scene.getStylesheets().add(css);
                    Stage stage = new Stage();
                    stage.setTitle("Add New Supplier");
                    stage.setScene(scene);
                    stage.initModality(Modality.APPLICATION_MODAL);

                    AddNewSupplierController controller;
                    controller=fxmlLoader.<AddNewSupplierController>getController();
                    controller.setProduct(product);
                    controller.setPrice(Double.parseDouble(pr));
                    controller.setSupplier(sup);
                    controller.getPanel(warningBox);
                    controller.setDescription(des);
                    controller.setFile(imgFile);

                    stage.show();

                }
            }
            else{
                warningBox.setVisible(false);
                warningBox.setText("Product already exists.");
                warningBox.setVisible(true);
            }   
            connection.conn.close();
            
        }
        else{
            if(product.length()==0 ||  pr.length()==0 || sup.length()==0){
                warningBox.setVisible(false);
                warningBox.setText("Invalid Data.");
                warningBox.setVisible(true);
            }
            else if(!imgFile.exists()){
                warningBox.setVisible(false);
                warningBox.setText("No image was chosen.");
                warningBox.setVisible(true);
            }
            
        }
    }

    @FXML
    private void chooseImage(MouseEvent event) {
        
        //Get the stage
        Stage st = (Stage) warningBox.getScene().getWindow();
        
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);
        
        imgFile = fileChooser.showOpenDialog(st);
        
        if(imgFile.exists()){
            filename.setVisible(false);
            filename.setText(imgFile.getName());
            filename.setVisible(true);
        }
    }

    @FXML
    private void closeWindow(MouseEvent event) {
        Stage st = (Stage)cancelBut.getScene().getWindow();
        st.close();
    }
    
}
