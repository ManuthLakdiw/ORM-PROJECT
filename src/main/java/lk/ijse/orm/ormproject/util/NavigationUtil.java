package lk.ijse.orm.ormproject.util;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;

public class NavigationUtil {

    private static FXMLLoader fxmlLoader;

    public static <T>T getController(){
        if (fxmlLoader != null) {
            return fxmlLoader.getController();
        }
        return null;
    }

    public static void getNewStage(Stage currentStage, Class currentClass, String title, String navPath) throws IOException {
        if (currentStage != null) {
            currentStage.close();
        }

        fxmlLoader = new FXMLLoader(currentClass.getResource(navPath));
        Parent load = fxmlLoader.load();

        Stage newStage = new Stage();
        Scene scene = new Scene(load);
        newStage.setScene(scene);
        newStage.setTitle(title);
        newStage.setResizable(false);

        // Smooth fade transition
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), load);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        newStage.show();


    }


    public static void loadPane(Class currentClass, Pane pane, String stageTitle, String resource) {
        try {
            pane.getChildren().clear();
            Pane load = FXMLLoader.load(currentClass.getResource(resource));
            pane.getChildren().add(load);
            loadPaneEffect(load);
            Stage stage = (Stage) pane.getScene().getWindow();
            stage.setTitle("Serenity MHC - "+stageTitle);

        }catch (IOException e){
            e.printStackTrace();
            AlertUtil.setErrorAlert(currentClass,null,stageTitle);
        }
    }

    public static <T> T loadSubStage(Class currentClass, String resource, String title, Window owner) {
        FXMLLoader loader = new FXMLLoader(currentClass.getResource(resource));
        Parent load = null;
        try {
            load = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Can't load sub window: " + resource);
            return null;
        }

        T controller = loader.getController();

        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(title);
        stage.setScene(new Scene(load));

        if (owner != null) {
            stage.initOwner(owner);
        }

        stage.setResizable(false);
        stage.show();
       loadPaneEffect((Pane) load);

        return controller;
    }

    public static void loadPaneEffect(Pane load) {
        load.setScaleX(0.8);
        load.setScaleY(0.8);
        load.setOpacity(0);
        load.setTranslateX(-100);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(200), load);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), load);
        scaleTransition.setFromX(0.8);
        scaleTransition.setFromY(0.8);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(200), load);
        translateTransition.setFromX(-100);
        translateTransition.setToX(0);

        ParallelTransition parallelTransition = new ParallelTransition(fadeTransition, scaleTransition, translateTransition);
        parallelTransition.play();
    }




}
