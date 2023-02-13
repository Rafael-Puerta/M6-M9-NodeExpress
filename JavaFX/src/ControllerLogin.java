import org.json.JSONException;
import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

public class ControllerLogin {

    @FXML
    private TextField port;

    @FXML
    private TextField protocol;

    @FXML
    private Button button;

    @FXML
    private ProgressIndicator loading;

    @FXML
    private TextField host;

    @FXML
    private Label Info;

    @FXML
    private void connect() {
        try{
            this.loading.setVisible(true);
            this.button.setVisible(false);
            JSONObject obj = new JSONObject("{}");
            obj.put("type", "ping");
            UtilsHTTP.sendPOST(this.protocol.getText() + "://" + this.host.getText() + ":" + Integer.valueOf( this.port.getText() ) + "/dades", obj.toString(), (response) -> {
                
                JSONObject objResponse = new JSONObject(response);
                try {
                    if (objResponse.getString("status").equals("OK")) {
                        Info.setVisible(false);
                        Main.port=Integer.valueOf( this.port.getText() );
                        Main.host=this.host.getText();
                        Main.protocol=this.protocol.getText();
                        UtilsViews.setViewAnimating("llist");
                        ControllerList c0 = (ControllerList) UtilsViews.getController("llist");
                        c0.loadUsers();
                    }
                    else{
                        Info.setVisible(true);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    Info.setVisible(true);
                    System.out.println(e);
                }
                this.loading.setVisible(false);
                this.button.setVisible(true);
            });
        }
        catch (Exception w) {
            // TODO: handle exception
            Info.setText("ERROR-No s'ha pogut realitzar la conexió");
            this.loading.setVisible(false);
            this.button.setVisible(true);
        }
    }
}
