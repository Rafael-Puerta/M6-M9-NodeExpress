import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ControllerListButton {
    @FXML
    private Label title;

    @FXML
    private Label id;

    @FXML
    private ImageView pencil;

    @FXML
    private void handleMenuAction() {

        ControllerList c0 = (ControllerList) UtilsViews.getController("llist");
        c0.clean();
        ControllerEdita c1 = (ControllerEdita) UtilsViews.getController("edit");
        c1.loadUser(this.id.getText());
        UtilsViews.setViewAnimating("edit");
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setId(String id){
        this.id.setText(id);
    }

    @FXML
    public void showPencil(){
        this.pencil.setVisible(true);
    }

    @FXML
    public void hidePencil(){
        this.pencil.setVisible(false);
    }

}
