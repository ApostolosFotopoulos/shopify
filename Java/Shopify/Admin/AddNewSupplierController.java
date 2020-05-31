/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin;

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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Apostolis
 */
public class AddNewSupplierController implements Initializable {

    public String product;
    public double pr;
    public String sup;
    public String des;
    public Label parentWind;
    public File imgFile;
     
     
    @FXML
    private TextField supplierPhone;
    @FXML
    private Label warningBox;
    @FXML
    private Button cancelBut;
    @FXML
    private Button submitButton1;

    public void setProduct(String prod){
        product=prod;
    }
    public void setPrice(double p){
        pr=p;
    }
    public void setSupplier(String s){
        sup=s;
    }
    public void setDescription(String d){
        des=d;
    }
    
    public void getPanel(Label lab){
        parentWind=lab;
    }
    public void setFile(File f){
        imgFile=f;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void addProductWithKey(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER)){
            
        }
    }

    @FXML
    private void addProduct(MouseEvent event) throws SQLException, IOException {
        String phone =supplierPhone.getText();
        
        if(phone.length()!=0 && phone.length()>=10 && phone.length()<=13){
            DatabaseFunctions connection = new DatabaseFunctions("localhost","5432","eidiko_thema","postgres","postgres");
            
            //Add the supplier
            String queryStr = String.format("INSERT INTO SUPPLIER(supplier_name,supplier_phone)"+
            "VALUES('%s','%s')",sup,phone);
            connection.insertQuery(queryStr);
            
            //Add the product
            queryStr = String.format("SELECT max(product_id) FROM Product ");
            ResultSet rs=connection.selectQuery(queryStr);
            rs.next();
            int product_id = rs.getInt(1)+1;

            //Fix the description
            if(des.length()==0){
                des="";
            }

            try (FileInputStream fis = new FileInputStream(imgFile)) {
                queryStr = String.format("INSERT INTO PRODUCT(product_id,product_price,product_desc,product_name,fk1_supplier_name,product_img_name,product_img) "+
                "VALUES(%d,%f,'%s','%s','%s',?,?)",product_id,pr,des,product,sup);
                PreparedStatement ps =connection.conn.prepareStatement(queryStr);
                ps.setString(1, imgFile.getName());
                ps.setBinaryStream(2, fis, (int)imgFile.length());
                ps.executeUpdate();
                ps.close();
            }
            
            //Close the windows
            Stage oldSt =(Stage)parentWind.getScene().getWindow();
            Stage st = (Stage) warningBox.getScene().getWindow();
            st.close();
            oldSt.close();
            
            connection.conn.close();
        }
        else{
            warningBox.setVisible(false);
            warningBox.setText("Invalid Phone.");
            warningBox.setVisible(true);
        }
    }

    @FXML
    private void closeWindow(MouseEvent event) {
        Stage st = (Stage)cancelBut.getScene().getWindow();
        st.close();
    }

    
}
