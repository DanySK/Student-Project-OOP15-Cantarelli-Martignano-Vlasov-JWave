package org.jwave.view.screens;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jwave.controller.Controller;
import org.jwave.model.player.Playlist;
import org.jwave.model.player.Song;
import org.jwave.view.FXEnvironment;
import org.jwave.view.PlayerUI;
import org.jwave.view.PlayerUIObserver;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 * Controller for the Player screen.
 * 
 * @author Alessandro Martignano
 *
 */
public class PlayerScreenController implements PlayerUI {

    private static double MIN_CHANGE = 0.5;

    private final FXMLScreens FXMLSCREEN = FXMLScreens.PLAYER;
    private final FXEnvironment environment;
    private Stage primaryStage;
    private final PlayerUIObserver observer;

    @FXML
    private Button btnPlay, btnNewPlaylist;
    @FXML
    private Slider positionSlider, volumeSlider;
    @FXML
    private ListView<Playlist> listView;
    @FXML
    private TableView<?> tableView;

    public PlayerScreenController(FXEnvironment environment, PlayerUIObserver controller) {
        this.observer = controller;
        this.environment = environment;
        this.environment.loadScreen(FXMLSCREEN, this);
        this.tableView.setPlaceholder(new Label("Nessun brano caricato"));

        this.listView.setItems(this.observer.getObservablePlaylists());
        this.listView.setOnMouseClicked(e -> {
            System.out.println("SELECTED PLAYLIST: " + listView.getSelectionModel().getSelectedItem().getName());
            System.out.println(
                    observer.getObservablePlaylistContent(listView.getSelectionModel().getSelectedItem()).toString());
        });

        listView.setCellFactory(new Callback<ListView<Playlist>, ListCell<Playlist>>() {
            @Override
            public ListCell<Playlist> call(ListView<Playlist> lv) {
                return new ListCell<Playlist>() {
                    @Override
                    public void updateItem(Playlist item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            setText(null);
                        } else {
                            if (item.getName() == "default") {
                                setText("Tutti i brani");
                            } else {
                                setText(item.getName());
                            }
                        }
                    }
                };
            }
        });

        this.volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                System.out.println("VOLUME: " + new_val);
            }
        });
    }

    @Override
    public void show() {
        this.primaryStage = this.environment.getMainStage();
        this.environment.displayScreen(FXMLSCREEN);
    }

    @Override
    public void setObserver(PlayerUIObserver observer) {

        // this.observer = observer;

    }

    @FXML
    private void newPlaylist() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nuova playlist");
        dialog.setHeaderText("Inserire il nome della nuova palylist");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            System.out.println("NEW PLAYLIST: " + result.get());
            this.observer.newPlaylist(result.get());
        }

        // result.ifPresent(name -> System.out.println("Nuova Playlist: " +
        // name));
    }

    @FXML
    private void play() {

        System.out.println("play");
        this.observer.play();
        // AudioSystem.getAudioSystem().getDynamicPlayer().play();

    }

    @FXML
    private void stopPlay() {
        System.out.println("stop");
        // AudioSystem.getAudioSystem().getDynamicPlayer().pause();
    }

    @FXML
    private void next() {
        System.out.println("next");
        observer.next();
    }

    @FXML
    private void prev() {
        System.out.println("prev");
        observer.previous();
    }

    @FXML
    private void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Audio file", "*.mp3", "*.wav"));
        List<File> openedFiles = fileChooser.showOpenMultipleDialog(primaryStage);
        openedFiles.forEach(f -> {
            try {
                observer.loadSong(f);
            } catch (Exception e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Errore");
                alert.setHeaderText("Impossibile aprire il file " + f.getName());
                alert.setContentText("Il file potrebbe essere danneggiato o in un formato non valido.");
                alert.showAndWait();
            }
        });
    }

    @FXML
    private void positionChanged() {
        System.out.println("SET POSITION: " + positionSlider.getValue());
        observer.moveToMoment(positionSlider.getValue());
    }

}
