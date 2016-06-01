package org.jwave.view.screens;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.jwave.controller.EditorController;
import org.jwave.controller.PlayerControllerImpl;
import org.jwave.model.editor.GroupedSampleInfo;
import org.jwave.model.playlist.Playlist;
import org.jwave.model.player.Song;
import org.jwave.view.FXEnvironment;
import org.jwave.view.UI;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * Controller for the Player screen.
 * 
 */
public class EditorScreenController implements UI {

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
    @FXML
    private LineChart<Integer, Float> lineChartLeft;
    @FXML
    private LineChart<Integer, Float> lineChartRight;

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
    private void copy() {

    }

    @FXML
    private void cut() {

    }

    @FXML
    private void paste() {

    }

    @FXML
    private void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Audio file", "*.mp3", "*.wav"));
        File openedFile = fileChooser.showOpenDialog(primaryStage);

        try {
            try {
                controller.loadSong(openedFile);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            List<GroupedSampleInfo> samplesList = new ArrayList<GroupedSampleInfo>(this.controller.getEditor()
                    .getAggregatedWaveform(0, this.controller.getEditor().getModifiedSongLength(), 2000));

            XYChart.Series<Integer, Float> leftSeries = new XYChart.Series<>();

            leftSeries.setName("Left Channel");

            for (int i = 0; i < samplesList.size(); i++) {
                leftSeries.getData().add(new XYChart.Data<Integer, Float>(i, samplesList.get(i).getLeftChannelMax()));
                leftSeries.getData().add(new XYChart.Data<Integer, Float>(i, samplesList.get(i).getLeftChannelMin()));
            }

            lineChartLeft.setCreateSymbols(false);
            lineChartLeft.getData().add(leftSeries);

            XYChart.Series<Integer, Float> rightSeries = new XYChart.Series<>();

            rightSeries.setName("Right Channel");

            for (int i = 0; i < samplesList.size(); i++) {
                rightSeries.getData().add(new XYChart.Data<Integer, Float>(i, samplesList.get(i).getRightChannelMax()));
                rightSeries.getData().add(new XYChart.Data<Integer, Float>(i, samplesList.get(i).getRightChannelMin()));
            }

            lineChartRight.setCreateSymbols(false);
            lineChartRight.getData().add(rightSeries);

        } catch (Exception a) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Impossibile aprire il file " + openedFile.getName());
            alert.setContentText("Il file potrebbe essere danneggiato o in un formato non valido.");
            alert.showAndWait();
        }
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
        this.environment.loadScreen(FXMLScreens.PLAYER,
                new PlayerScreenController(this.environment, new PlayerControllerImpl()));
        this.environment.show();
    }

    @Override
    public void updateReproductionInfo(Song song) {
        // TODO Auto-generated method stub

    }

    @FXML
    private void showAboutInfo() {
        System.out.println("asd");
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About JWave");
        alert.setHeaderText("");
        alert.setContentText("Editing   Aleksejs Vlasovs\nView      Alessandro Martignano\nPlayer    Dario Cantarelli");
        alert.showAndWait();
    }

}
