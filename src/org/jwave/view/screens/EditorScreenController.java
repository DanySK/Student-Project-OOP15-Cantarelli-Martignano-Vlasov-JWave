package org.jwave.view.screens;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jwave.controller.EditorController;
import org.jwave.controller.PlayerControllerImpl;
import org.jwave.model.player.MetaData;
import org.jwave.model.playlist.Playlist;
import org.jwave.model.player.Song;
import org.jwave.view.EditorUI;
import org.jwave.view.FXEnvironment;
import org.jwave.view.PlayerUI;
import org.jwave.view.PlayerController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableRow;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Controller for the Player screen.
 * 
 * @author Alessandro Martignano
 *
 */
public class EditorScreenController implements EditorUI {

    private final FXMLScreens FXMLSCREEN = FXMLScreens.PLAYER;
    private final FXEnvironment environment;
    private final EditorController controller;
    private Stage primaryStage;
    private boolean lockedPositionSlider;

    @FXML
    private MenuItem btnEditor;
    @FXML
    private Label labelLeft, labelRight, labelSong;
    @FXML
    private Button btnPlay, btnNewPlaylist;
    @FXML
    private volatile Slider positionSlider, volumeSlider;
    @FXML
    private ListView<Playlist> listView;
    @FXML
    private TableView<Song> tableView;
    @FXML
    private TableColumn<Song, String> columnFile, columnTitle, columnAuthor, columnAlbum, columnGenre;

    public EditorScreenController(FXEnvironment environment, EditorController controller) {
        this.controller = controller;
        this.environment = environment;
        this.environment.loadScreen(FXMLSCREEN, this);
        this.lockedPositionSlider = false;

        
    }

    @Override
    public void show() {
        this.primaryStage = this.environment.getMainStage();
        // this.primaryStage.setOnCloseRequest(e->observer.terminate());
        this.primaryStage.setOnCloseRequest(e -> System.exit(0));
        this.environment.displayScreen(FXMLSCREEN);
    }

    @Override
    public void setObserver(PlayerController observer) {

        // this.observer = observer;

    }

    public void updatePosition() {
        System.out.println("Pos");
    }


    @FXML
    private void play() {
        controller.play();
    }

    @FXML
    private void stopPlay() {
        controller.stop();
    }


    @FXML
    private void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Audio file", "*.mp3", "*.wav"));
        List<File> openedFiles = fileChooser.showOpenMultipleDialog(primaryStage);
        if (openedFiles != null)
            openedFiles.forEach(f -> {
                try {
                    controller.loadSong(f);
                } catch (Exception a) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Errore");
                    alert.setHeaderText("Impossibile aprire il file " + f.getName());
                    alert.setContentText("Il file potrebbe essere danneggiato o in un formato non valido.");
                    alert.showAndWait();
                }
            });
    }

    @FXML
    private void changePosition() {
        controller.moveToMoment(positionSlider.getValue());
        lockedPositionSlider = false;
    }

    @Override
    public void updatePosition(Integer ms, Integer lenght) {
        if (!positionSlider.isValueChanging() && lockedPositionSlider == false)
            positionSlider.setValue((ms * 10000) / lenght);
    }

    @FXML
    private void lockSlider() {
        lockedPositionSlider = true;
    }
    
    
    @FXML
    private void gotoPlayer() {
        this.environment.loadScreen(FXMLSCREEN.PLAYER, new PlayerScreenController(this.environment, new PlayerControllerImpl()));
        this.environment.show();
    }

}
