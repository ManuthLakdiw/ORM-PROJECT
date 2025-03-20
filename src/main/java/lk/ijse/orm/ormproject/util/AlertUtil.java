package lk.ijse.orm.ormproject.util;

import javafx.animation.PauseTransition;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.util.Duration;

import java.util.Optional;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */
public class AlertUtil {

    public static void setInformationAlert(Class currentClass, String header, String message, boolean autoClose){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(header);
        alert.setContentText(message);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(currentClass.getResource("/styles/alert.css").toExternalForm());


        if(autoClose){

            PauseTransition delay = new PauseTransition(Duration.seconds(1.4));
            delay.setOnFinished(event -> alert.close());
            delay.play();
            alert.show();
            return;

        }

        alert.showAndWait();
    }


    public static void setErrorAlert(Class currentClass, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText("Fail to load "+content+" !");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(currentClass.getResource("/assets/styles/alert.css").toExternalForm());
        alert.showAndWait();
    }


    public static boolean setConfirmationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(AlertUtil.class.getResource("/styles/alert.css").toExternalForm());

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }



}
