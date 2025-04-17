package lk.ijse.orm.ormproject.util;

import javafx.animation.TranslateTransition;
import javafx.scene.control.TextInputControl;
import javafx.util.Duration;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */
public class RegexUtil {

    public static<T extends TextInputControl> void setShake(T... inputNodes) {
        for (T node : inputNodes) {
            TranslateTransition shake = new TranslateTransition(Duration.millis(50), node);
            shake.setFromX(0);
            shake.setByX(4);
            shake.setCycleCount(6);
            shake.setAutoReverse(true);
            shake.play();
        }
    }


    public static <T extends TextInputControl> void setErrorStyle(boolean shakeEffect , T... inputNodes) {
        String errorStyle = "-fx-border-color: red;";
        for (T node : inputNodes) {
            node.setStyle(errorStyle);
            if (shakeEffect) {
                setShake(node);
            }
        }
    }

    public static<T extends TextInputControl> void resetErrorStyle(T... inputNodes) {
        String resetStyle = "-fx-border-color: #1486cd;";
        for (T node : inputNodes) {
            node.setStyle(resetStyle);
        }

    }




}
