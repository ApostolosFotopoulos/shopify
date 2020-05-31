/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GeneralPurpose;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;

/**
 * FXML Controller class
 *
 * @author Apostolis
 */
public class ProgressBarController implements Initializable {

    @FXML
    private ProgressBar bar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void bind(Task<Integer> t){
        bar.progressProperty().bind(t.progressProperty());
    }
}
