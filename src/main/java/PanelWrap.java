package main.java;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class PanelWrap {
    Stage stage;
    Scene scene;

    TabPane tabPane;
    Tab airportsTab;
    Tab planesTab;
    Tab boatsTab;

    int[] size;

    Main main;

    private void basicSetup()
    {
        VBox vbox = new VBox(tabPane);
        scene = new Scene(vbox, size[0],size[1]);
        stage.setScene(scene);
        stage.onCloseRequestProperty().set(e->{ Platform.exit(); });
        stage.setX(main.mapWrap.stage.getX()+main.mapWrap.size[0]);
        stage.setY(main.mapWrap.stage.getY());
    }

    private void airportsTabSetup() {
        VBox vbox = new VBox();
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        airportsTab = new Tab("Airports", vbox);

        ComboBox comboBox = new ComboBox();

        String notSelectedPrompt = "<unknown>";
        Label idLabel = new Label(notSelectedPrompt);
        Label nameLabel = new Label(notSelectedPrompt);
        Label typeLabel = new Label(notSelectedPrompt);
        Label positionXLabel = new Label(notSelectedPrompt);
        Label positionZLabel = new Label(notSelectedPrompt);

        for (Airport airport : main.airports) {
            comboBox.getItems().add(airport.name);
        }
        comboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
                    Airport selectedAirport = null;
                    for (Airport a : main.airports) {
                        if (a.name == newValue) {
                            selectedAirport = a;
                            break;
                        }
                    }
                    if (selectedAirport != null) {
                        idLabel.setText(Integer.toString(selectedAirport.ID));
                        nameLabel.setText(selectedAirport.name);
                        typeLabel.setText(selectedAirport.planeType.name());
                        positionXLabel.setText(Double.toString(selectedAirport.position[0]));
                        positionZLabel.setText(Double.toString(selectedAirport.position[1]));
                    } else {
                        idLabel.setText(notSelectedPrompt);
                        nameLabel.setText(notSelectedPrompt);
                        typeLabel.setText(notSelectedPrompt);
                        positionXLabel.setText(notSelectedPrompt);
                        positionZLabel.setText(notSelectedPrompt);
                    }
                }
        );

        gridPane.add(new Label("Airport:"), 0, 0);
        gridPane.add(comboBox, 1, 0);
        Rectangle spacer = new Rectangle();
        spacer.setHeight(10);
        spacer.setWidth(10);
        spacer.setFill(Color.TRANSPARENT);
        GridPane gridPane2 = new GridPane();
        gridPane2.setPadding(new Insets(10, 10, 10, 10));
        gridPane2.setVgap(5);
        gridPane2.setHgap(5);
        gridPane2.setAlignment(Pos.TOP_LEFT);
        gridPane2.add(spacer, 0, 1);
        gridPane2.add(new Label("ID:"), 0, 1);
        gridPane2.add(idLabel, 1, 1);
        gridPane2.add(new Label("Name:"), 0, 2);
        gridPane2.add(nameLabel, 1, 2);
        gridPane2.add(new Label("Type:"), 0, 3);
        gridPane2.add(typeLabel, 1, 3);
        HBox position = new HBox();
        position.getChildren().addAll(new Label("X: "), positionXLabel, spacer, new Label("Z: "), positionZLabel);
        position.setAlignment(Pos.TOP_LEFT);
        position.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(gridPane, gridPane2, position);
    }

    private void planesTabSetup(){
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        planesTab = new Tab("Planes", gridPane);

        ComboBox comboBox = new ComboBox();

        gridPane.add(new Label("Plane:"),0,0);
        gridPane.add(comboBox,1,0);
    }

    private void shipsTabSetup() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        boatsTab = new Tab("Ships", gridPane);

        ComboBox comboBox = new ComboBox();

        gridPane.add(new Label("Ship:"),0,0);
        gridPane.add(comboBox,1,0);
    }

    public PanelWrap(Main main) {
        tabPane = new TabPane();
        stage = new Stage();
        this.main = main;
        this.size = new int[] {300,720};
        this.stage.setTitle("whoosh: Control Panel");
        basicSetup();
        airportsTabSetup();
        planesTabSetup();
        shipsTabSetup();
        tabPane.getTabs().addAll(airportsTab, planesTab, boatsTab);
        stage.show();
    }
}
