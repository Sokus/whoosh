package main.java.windows;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import main.java.Airport;
import main.java.Main;

public class ControlWindow {
    Stage stage;
    Scene scene;
    int[] size;

    TabPane tabPane;
    Tab airportsTab;
    Tab planesTab;
    Tab boatsTab;

    ComboBox airportSelectionBox;

    Main main;

    private void basicSetup() {
        VBox vbox = new VBox(tabPane);
        scene = new Scene(vbox, size[0], size[1]);
        stage.setScene(scene);
        stage.onCloseRequestProperty().set(e -> {
            Platform.exit();
        });
        stage.setX(main.mapStage.stage.getX() + main.mapStage.size[0]);
        stage.setY(main.mapStage.stage.getY());
    }

    private void airportsTabSetup() {
        VBox vbox = new VBox();
        airportsTab = new Tab("Airports", vbox);

        VBox addRandom = new VBox();
        {
            addRandom.setPadding(new Insets(10, 10, 10, 10));
            Label randomTitle = new Label("Add random airports:");
            HBox buttons = new HBox();
            buttons.setSpacing(5);
            Button addOneRandomAirport = new Button("Add 1");
            addOneRandomAirport.setOnMouseClicked(e -> {
                main.mapStage.createAirports(1);
            });
            Button addFiveRandomAirports = new Button("Add 5");
            addFiveRandomAirports.setOnMouseClicked(e -> {
                main.mapStage.createAirports(5);
            });
            buttons.getChildren().addAll(addOneRandomAirport, addFiveRandomAirports);
            addRandom.getChildren().addAll(randomTitle, buttons);
        }

        HBox selectAirport = new HBox();
        String notSelectedPrompt = "<unknown>";
        Label idLabel = new Label(notSelectedPrompt);
        Label nameLabel = new Label(notSelectedPrompt);
        Label typeLabel = new Label(notSelectedPrompt);
        Label positionXLabel = new Label(notSelectedPrompt);
        Label positionZLabel = new Label(notSelectedPrompt);
        {
            airportSelectionBox = new ComboBox();
            airportSelectionBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
                        Airport selectedAirport = null;
                        Airport oldAirport = null;
                        for (Airport a : main.airports) {
                            if (oldAirport == null && a.name == oldValue) {
                                oldAirport = a;
                            }
                            if (selectedAirport == null && a.name == newValue) {
                                selectedAirport = a;
                            }
                            if (oldAirport != null && selectedAirport != null) {
                                break;
                            }
                        }
                        if (oldAirport != null) {
                            oldAirport.setDefaultColor();
                        }
                        if (selectedAirport != null) {
                            selectedAirport.getModel().setMaterial(new PhongMaterial(Color.WHITE));
                            idLabel.setText(Integer.toString(selectedAirport.ID));
                            nameLabel.setText(selectedAirport.name);
                            typeLabel.setText(selectedAirport.planeType.name());
                            positionXLabel.setText(Double.toString(selectedAirport.position.x));
                            positionZLabel.setText(Double.toString(selectedAirport.position.z));
                        } else {
                            idLabel.setText(notSelectedPrompt);
                            nameLabel.setText(notSelectedPrompt);
                            typeLabel.setText(notSelectedPrompt);
                            positionXLabel.setText(notSelectedPrompt);
                            positionZLabel.setText(notSelectedPrompt);
                        }
                    }
            );
            
            Button deleteButton = new Button("Delete");
            deleteButton.setOnMouseClicked(e -> {
                Object airportName = airportSelectionBox.getSelectionModel().getSelectedItem();
                if (airportName != null)
                {
                    for(int i=0; i<main.airports.size(); i++)
                    {
                        if(airportName.toString().equals(main.airports.get(i).name))
                        {
                            main.mapStage.airportModels.getChildren().remove(main.airports.get(i).getModel());
                            main.airports.remove(i);
                            main.panelWrap.airportSelectionBox.getItems().remove(main.panelWrap.airportSelectionBox.getValue());
                            break;
                        }
                    }
                }
            });
            Button deleteAllButton = new Button("Delete All");
            deleteAllButton.setOnMouseClicked(e -> {
                main.mapStage.airportModels.getChildren().clear();
                main.airports.clear();
                main.panelWrap.airportSelectionBox.getItems().clear();
            });
            selectAirport.setPadding(new Insets(10, 10, 10, 10));
            selectAirport.setSpacing(5);
            selectAirport.setAlignment(Pos.TOP_LEFT);
            selectAirport.getChildren().addAll(new Label("Airport:"),airportSelectionBox, deleteButton, deleteAllButton);
        }
        resetPlaneSelectionBox();

        GridPane airportDetails = new GridPane();
        {
            airportDetails.setPadding(new Insets(10, 10, 10, 10));
            airportDetails.setVgap(5);
            airportDetails.setHgap(5);
            airportDetails.setAlignment(Pos.TOP_LEFT);
            airportDetails.add(new Label("ID:"), 0, 1);
            airportDetails.add(idLabel, 1, 1);
            airportDetails.add(new Label("Name:"), 0, 2);
            airportDetails.add(nameLabel, 1, 2);
            airportDetails.add(new Label("Type:"), 0, 3);
            airportDetails.add(typeLabel, 1, 3);
        }

        HBox airportPosition = new HBox();
        {
            airportPosition.getChildren().addAll(new Label("X: "),positionXLabel, new Rectangle(10, 10, Color.TRANSPARENT), new Label("Z: "), positionZLabel);
            airportPosition.setAlignment(Pos.TOP_LEFT);
            airportPosition.setPadding(new Insets(10, 10, 10, 10));
        }

        vbox.getChildren().addAll(addRandom, selectAirport, airportDetails, airportPosition);
    }

    private void planesTabSetup() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        planesTab = new Tab("Planes", gridPane);

        ComboBox comboBox = new ComboBox();

        gridPane.add(new Label("Plane:"), 0, 0);
        gridPane.add(comboBox, 1, 0);
    }

    private void shipsTabSetup() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        boatsTab = new Tab("Ships", gridPane);

        ComboBox comboBox = new ComboBox();

        gridPane.add(new Label("Ship:"), 0, 0);
        gridPane.add(comboBox, 1, 0);
    }

    public void resetPlaneSelectionBox() {
        airportSelectionBox.getItems().clear();
        for (Airport airport : main.airports) {
            airportSelectionBox.getItems().add(airport.name);
        }
        if(airportSelectionBox.getItems().size()>0)
        {
            airportSelectionBox.setValue(airportSelectionBox.getItems().get(0));
        }
    }

    public ControlWindow(Main main) {
        tabPane = new TabPane();
        stage = new Stage();
        this.main = main;
        this.size = new int[]{300, 720};
        this.stage.setTitle("whoosh: Control Panel");
        basicSetup();
        airportsTabSetup();
        planesTabSetup();
        shipsTabSetup();
        tabPane.getTabs().addAll(airportsTab, planesTab, boatsTab);
        stage.show();
    }
}
