import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

public class ControllerForm {
    
    @FXML
    private TextField nom;

    @FXML
    private TextField cognom;

    @FXML
    private TextField correu;

    @FXML
    private TextField telefon;

    @FXML
    private TextField direccio;

    @FXML
    private TextField ciutat;

    @FXML
    private ProgressIndicator loading;

    @FXML
    private void send(){

         //TODO send and endpoint

    }

    @FXML
    private void back() {
        this.ciutat.setText("");
        this.nom.setText("");
        this.direccio.setText("");
        this.cognom.setText("");
        this.correu.setText("");
        this.telefon.setText("");
        UtilsViews.setViewAnimating("login");
    }

    @FXML
    private void goList() {
        this.ciutat.setText("");
        this.nom.setText("");
        this.direccio.setText("");
        this.cognom.setText("");
        this.correu.setText("");
        this.telefon.setText("");
        UtilsViews.setViewAnimating("llist"); 
        ControllerList c0 = (ControllerList) UtilsViews.getController("llist");
        c0.loadUsers();
    }

}
