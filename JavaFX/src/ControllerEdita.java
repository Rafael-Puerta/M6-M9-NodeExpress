import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

public class ControllerEdita {
    @FXML
    private TextField nom;

    @FXML
    private Label title;

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
        UtilsViews.setViewAnimating("llist");
        ControllerList c0 = (ControllerList) UtilsViews.getController("llist");
        c0.loadUsers();
    }

    @FXML
    private void goForm() {
        this.ciutat.setText("");
        this.nom.setText("");
        this.direccio.setText("");
        this.cognom.setText("");
        this.correu.setText("");
        this.telefon.setText("");
        UtilsViews.setViewAnimating("add"); 
    }

    public void loadUser (String id)  {

        JSONObject obj = new JSONObject("{}");
        obj.put("type", "user");
        obj.put("name", id);

        loading.setVisible(true);
        UtilsHTTP.sendPOST(Main.protocol + "://" + Main.host + ":" + Main.port + "/dades", obj.toString(), (response) -> {
            loadUserInfoCallback(response);
            loading.setVisible(false);
        });
    }

    private void loadUserInfoCallback (String response) {

        JSONObject objResponse = new JSONObject(response);
        
        if (objResponse.getString("status").equals("OK")) {

            JSONObject console = objResponse.getJSONObject("result");

            // Fill console info with the received data
            nom.setText(console.getString("name"));
            title.setText(console.getString("name"));
            cognom.setText(console.getString("surname"));
            telefon.setText(console.getString("phone"));
            ciutat.setText(console.getString("city"));
            direccio.setText(console.getString("direction"));
            correu.setText(console.getString("mail"));
    
        } else {
            //showError();
            System.out.println("ERROR#DadesController");
        }
    }
    
}
