/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shopify;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 *
 * @author Apostolis
 */
public class Shopify extends Application {
   
    @Override
    public void start(Stage primaryStage) throws IOException {
   
        /* We show the login form */
        Parent root =FXMLLoader.load(getClass().getResource("/GeneralPurpose/Login_Form.fxml"));
        
        Scene scene = new Scene(root, 420, 420);
        String css = this.getClass().getResource("CustomCSS.css").toExternalForm(); 
        scene.getStylesheets().add(css);
        
        primaryStage.setTitle("Login Form");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
