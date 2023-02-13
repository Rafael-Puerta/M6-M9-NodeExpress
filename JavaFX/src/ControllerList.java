import java.io.IOException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
public class ControllerList {
    
    @FXML
    private VBox yPane = new VBox();
    
    @FXML
    private ProgressIndicator loading;

    private void loadUsersCallback (String response) {

        JSONObject objResponse = new JSONObject(response);

        if (objResponse.getString("status").equals("OK")) {

            JSONArray JSONlist = objResponse.getJSONArray("result");
            URL resource = this.getClass().getResource("./assets/item.fxml");
            
            // Clear the list of consoles
            yPane.getChildren().clear();

            // Add received consoles from the JSON to the list
            for (int i = 0; i < JSONlist.length(); i++) {

                JSONObject console = JSONlist.getJSONObject(i);

                try {
                    // Load template and set controller
                    FXMLLoader loader = new FXMLLoader(resource);
                    Parent itemTemplate = loader.load();
                    ControllerListButton itemController = loader.getController();
                
                    // Fill template with console information
                    itemController.setTitle(console.getString("name"));
                    itemController.setId(console.getString("id"));
                    
                    // Add template to the list
                    yPane.getChildren().add(itemTemplate);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {
            System.out.println("ERROR#ListController");
        }
    }
    public void loadUsers(){
        JSONObject obj = new JSONObject("{}");
        obj.put("type", "users");

        loading.setVisible(true);
        UtilsHTTP.sendPOST(Main.protocol + "://" + Main.host + ":" + Main.port + "/dades", obj.toString(), (response) -> {
           
            this.loadUsersCallback(response);
            loading.setVisible(false);
        });
    }

    @FXML
    private void back() {
        yPane.getChildren().clear();
        UtilsViews.setViewAnimating("login");
    }
    
    public void clean() {
        this.yPane.getChildren().clear();
    }

    @FXML
    private void goForm() {
        yPane.getChildren().clear();
        UtilsViews.setViewAnimating("add"); 
    }
}
