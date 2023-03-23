import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    private Button button;

    @FXML
    private void send(){
         try{
            this.loading.setVisible(true);
            this.button.setVisible(false);
            JSONObject obj = new JSONObject("{}");
            obj.put("type", "add");

            obj.put("city", this.ciutat.getText());
            obj.put("name", this.nom.getText());
            obj.put("surname", this.cognom.getText());
            obj.put("direction", this.direccio.getText());
            obj.put("mail", this.correu.getText());
            obj.put("phone", this.telefon.getText());

            UtilsHTTP.sendPOST(Main.protocol + "://" + Main.host + ":" + Integer.valueOf( Main.port ) + "/dades", obj.toString(), (response) -> {
                
                JSONObject objResponse = new JSONObject(response);
                try {
                    if (objResponse.getString("status").equals("OK")) {
                        
                        UtilsViews.setViewAnimating("llist");
                        ControllerList c0 = (ControllerList) UtilsViews.getController("llist");
                        c0.loadUsers();
                    }
                    else{

                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    System.out.println(e);
                }
                this.loading.setVisible(false);
                this.button.setVisible(true);
            });
        }
        catch (Exception w) {
            // TODO: handle exception
            this.loading.setVisible(false);
            this.button.setVisible(true);
        }

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
