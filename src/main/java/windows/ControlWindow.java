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
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import main.java.Airport;
import main.java.Main;
import main.java.utility.Vector3D;
import main.java.vehicles.*;

import java.util.Vector;

public class ControlWindow {
    Stage stage;
    Scene scene;
    int[] size;

    TabPane tabPane;
    Tab airportsTab;
    Tab planesTab;
    Tab shipsTab;

    ComboBox airportsSelectionBox;
    ComboBox planesSelectionBox;
    ComboBox shipsSelectionBox;

    Main main;

    /**
     * Creates main elements of the Control Panel and initializes them with properties.
     */
    private void basicSetup() {
        VBox vbox = new VBox(tabPane);
        scene = new Scene(vbox, size[0], size[1]);
        stage.setScene(scene);
        stage.onCloseRequestProperty().set(e -> {
            main.stop();
            Platform.exit();
        });
        stage.setX(main.mapStage.stage.getX() + main.mapStage.size[0]);
        stage.setY(main.mapStage.stage.getY());
    }

    /**
     * Setups the Airports tab - here all of the buttons as well as their functions and layouts are described.
     */
    private void airportsTabSetup() {
        VBox vbox = new VBox();
        airportsTab = new Tab("Airports", vbox);

        VBox addRandom = new VBox();
        {
            addRandom.setPadding(new Insets(10, 10, 10, 10));
            HBox addRandomHBox = new HBox();
            addRandomHBox.setSpacing(5);
            ComboBox typeSelectionBox = new ComboBox();
            typeSelectionBox.getItems().addAll("Passenger", "Army", "Random");
            typeSelectionBox.setValue("Random");

            Button addOneRandomAirport = new Button("Add 1");
            addOneRandomAirport.setOnMouseClicked(e -> {
                PlaneType planeType = null;
                switch(typeSelectionBox.getSelectionModel().getSelectedItem().toString()) {
                    case "Passenger" -> planeType = PlaneType.PASSENGER;
                    case "Army" -> planeType = PlaneType.ARMY;
                }
                main.mapStage.createRandomAirport(planeType);
            });
            Button addFiveRandomAirports = new Button("Add 5");
            addFiveRandomAirports.setOnMouseClicked(e -> {
                for(int i=0; i<5; i++) {
                    PlaneType planeType = null;
                    switch(typeSelectionBox.getSelectionModel().getSelectedItem().toString()) {
                        case "Passenger" -> planeType = PlaneType.PASSENGER;
                        case "Army" -> planeType = PlaneType.ARMY;
                    }
                    main.mapStage.createRandomAirport(planeType);
                }
            });
            addRandomHBox.getChildren().addAll(new Label("Type:"), typeSelectionBox, addOneRandomAirport, addFiveRandomAirports);

            addRandom.getChildren().addAll(new Label("Add random airports:"), addRandomHBox);
        }

        HBox selectAirport = new HBox();
        String notSelectedPrompt = "<unknown>";
        Label idLabel = new Label(notSelectedPrompt);
        Label nameLabel = new Label(notSelectedPrompt);
        Label typeLabel = new Label(notSelectedPrompt);
        Label positionXLabel = new Label(notSelectedPrompt);
        Label positionZLabel = new Label(notSelectedPrompt);
        VBox planesVisitingList = new VBox();
        {
            planesVisitingList.setPadding(new Insets(10, 10, 10, 10));
            planesVisitingList.setAlignment(Pos.TOP_LEFT);
            planesVisitingList.setMinHeight(700);
            airportsSelectionBox = new ComboBox();
            airportsSelectionBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
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
                            oldAirport.setDarkColor();
                        }
                        if (selectedAirport != null) {
                            selectedAirport.setBrightColor();
                            idLabel.setText(Integer.toString(selectedAirport.ID));
                            nameLabel.setText(selectedAirport.name);
                            typeLabel.setText(selectedAirport.planeType.name());
                            positionXLabel.setText(Double.toString(selectedAirport.position.x));
                            positionZLabel.setText(Double.toString(selectedAirport.position.z));

                            Vector<Plane> planesVisiting = new Vector<>();
                            switch (selectedAirport.planeType) {
                                case PASSENGER -> {
                                    for (int i = 0; i < main.passengerPlanes.size(); i++) {
                                        Plane plane = main.passengerPlanes.get(i);
                                        for (int ii = 0; ii < plane.path.size(); ii++) {
                                            Airport airport = plane.path.get(ii);
                                            if (airport == selectedAirport) {
                                                planesVisiting.add(plane);
                                                break;
                                            }
                                        }
                                    }
                                }
                                case ARMY -> {
                                    for (int i = 0; i < main.fighterPlanes.size(); i++) {
                                        Plane plane = main.fighterPlanes.get(i);
                                        for (int ii = 0; ii < plane.path.size(); ii++) {
                                            Airport airport = plane.path.get(ii);
                                            if (airport == selectedAirport) {
                                                planesVisiting.add(plane);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            planesVisitingList.getChildren().clear();
                            if (!planesVisiting.isEmpty()) {
                                planesVisitingList.getChildren().add(new Label("Planes visiting this airport:"));
                                for (Plane plane : planesVisiting) {
                                    planesVisitingList.getChildren().add(new Label(plane.UID + " " + plane.name));
                                }
                            }

                        } else {
                            idLabel.setText(notSelectedPrompt);
                            nameLabel.setText(notSelectedPrompt);
                            typeLabel.setText(notSelectedPrompt);
                            positionXLabel.setText(notSelectedPrompt);
                            positionZLabel.setText(notSelectedPrompt);
                            planesVisitingList.getChildren().clear();
                        }
                    }
            );

            Button deleteButton = new Button("Delete");
            deleteButton.setOnMouseClicked(e -> {
                String airportName = (String) airportsSelectionBox.getSelectionModel().getSelectedItem();
                DeleteAirport(airportName);
            });
            Button deleteAllButton = new Button("Delete All");
            deleteAllButton.setOnMouseClicked(e -> {
                while (main.airports.size() > 0) {
                    DeleteAirport(main.airports.get(main.airports.size() - 1).name);
                }
            });
            selectAirport.setPadding(new Insets(10, 10, 10, 10));
            selectAirport.setSpacing(5);
            selectAirport.setAlignment(Pos.TOP_LEFT);
            selectAirport.getChildren().addAll(new Label("Airport:"), airportsSelectionBox);
            selectAirport.getChildren().addAll(deleteButton, deleteAllButton);
        }
        resetAirportSelectionBox();

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
            airportPosition.getChildren().addAll(new Label("X: "), positionXLabel, new Rectangle(10, 10, Color.TRANSPARENT), new Label("Z: "), positionZLabel);
            airportPosition.setAlignment(Pos.TOP_LEFT);
            airportPosition.setPadding(new Insets(10, 10, 10, 10));
        }

        vbox.getChildren().addAll(addRandom, selectAirport, airportDetails, airportPosition, planesVisitingList);
    }

    /**
     * Setups the Planes tab - here all of the buttons as well as their functions and layouts are described.
     */
    private void planesTabSetup() {
        VBox vbox = new VBox();
        planesTab = new Tab("Planes", vbox);

        VBox addRandom = new VBox();
        {
            addRandom.setPadding(new Insets(10, 10, 10, 10));
            HBox addRandomHBox = new HBox();
            addRandomHBox.setSpacing(5);
            ComboBox typeSelectionBox = new ComboBox();
            typeSelectionBox.getItems().addAll("Passenger", "Fighter", "Random");
            typeSelectionBox.setValue("Random");

            Button addOneRandomPlane = new Button("Add 1");
            addOneRandomPlane.setOnMouseClicked(e -> {
                PlaneType planeType = null;
                switch(typeSelectionBox.getSelectionModel().getSelectedItem().toString()) {
                    case "Passenger" -> planeType = PlaneType.PASSENGER;
                    case "Fighter" -> planeType = PlaneType.ARMY;
                }
                main.mapStage.createRandomPlane(planeType, null);
            });
            Button addFiveRandomPlanes = new Button("Add 5");
            addFiveRandomPlanes.setOnMouseClicked(e -> {
                for(int i=0; i<5; i++) {
                    PlaneType planeType = null;
                    switch(typeSelectionBox.getSelectionModel().getSelectedItem().toString()) {
                        case "Passenger" -> planeType = PlaneType.PASSENGER;
                        case "Fighter" -> planeType = PlaneType.ARMY;
                    }
                    main.mapStage.createRandomPlane(planeType, null);
                }
            });
            addRandomHBox.getChildren().addAll(new Label("Type:"), typeSelectionBox, addOneRandomPlane, addFiveRandomPlanes);

            addRandom.getChildren().addAll(new Label("Add random planes:"), addRandomHBox);
        }

        HBox selectPlane = new HBox();
        String notSelectedPrompt = "<unknown>";
        Label idLabel = new Label(notSelectedPrompt);
        Label nameLabel = new Label(notSelectedPrompt);
        Label typeLabel = new Label(notSelectedPrompt);
        Label fuelLabel = new Label(notSelectedPrompt);
        Label speedLabel = new Label(notSelectedPrompt);
        Label passengersLabel = new Label(notSelectedPrompt);
        Label weaponTypeLabel = new Label(notSelectedPrompt);
        Label nextAirportLabel = new Label(notSelectedPrompt);
        VBox pathList = new VBox();
        {
            pathList.setPadding(new Insets(10, 10, 10, 10));
            pathList.setAlignment(Pos.TOP_LEFT);
            pathList.setMinHeight(700);
            planesSelectionBox = new ComboBox();
            planesSelectionBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
                        Plane selectedPlane = null;
                        Plane oldPlane = null;
                        for (Plane p : main.passengerPlanes) {
                            if (oldPlane == null && p.name == oldValue) {
                                oldPlane = p;
                            }
                            if (selectedPlane == null && p.name == newValue) {
                                selectedPlane = p;
                            }
                            if (oldPlane != null && selectedPlane != null) {
                                break;
                            }
                        }
                        for (Plane p : main.fighterPlanes) {
                            if (oldPlane == null && p.name == oldValue) {
                                oldPlane = p;
                            }
                            if (selectedPlane == null && p.name == newValue) {
                                selectedPlane = p;
                            }
                            if (oldPlane != null && selectedPlane != null) {
                                break;
                            }
                        }
                        if (oldPlane != null) {
                            oldPlane.setDarkColor();
                        }
                        if (selectedPlane != null) {
                            PlaneType planeType = selectedPlane.path.get(0).planeType;
                            selectedPlane.setBrightColor();
                            idLabel.setText(Integer.toString(selectedPlane.UID));
                            nameLabel.setText(selectedPlane.name);
                            typeLabel.setText(planeType.name());
                            int fuelPercentage = 100*selectedPlane.currentFuel/selectedPlane.fuelCapacity;
                            fuelLabel.setText(fuelPercentage + "%");
                            speedLabel.setText(selectedPlane.maxSpeed+" u/s");
                            switch(planeType) {
                                case PASSENGER -> {
                                    int passengers = ((PassengerPlane)selectedPlane).currentPassengers;
                                    passengersLabel.setText(Integer.toString(passengers));
                                    weaponTypeLabel.setText("<only in fighter jets>");
                                }
                                case ARMY -> {
                                    passengersLabel.setText("<only in passenger planes>");
                                    WeaponType weaponType = ((FighterPlane) selectedPlane).weaponType;
                                    weaponTypeLabel.setText(weaponType.name());
                                }
                            }
                            nextAirportLabel.setText(selectedPlane.nextAirport.name);

                            pathList.getChildren().clear();
                            pathList.getChildren().add(new Label("Path:"));
                            for (int i = 0; i < selectedPlane.path.size(); i++) {
                                String currentPositionMark = selectedPlane.currentAirportIndex == i ? " <-" : "";
                                pathList.getChildren().add(new Label((i + 1) + " " + selectedPlane.path.get(i).name + currentPositionMark));
                            }
                        } else {
                            idLabel.setText(notSelectedPrompt);
                            nameLabel.setText(notSelectedPrompt);
                            typeLabel.setText(notSelectedPrompt);
                            fuelLabel.setText(notSelectedPrompt);
                            speedLabel.setText(notSelectedPrompt);
                            passengersLabel.setText(notSelectedPrompt);
                            weaponTypeLabel.setText(notSelectedPrompt);
                            nextAirportLabel.setText(notSelectedPrompt);
                            pathList.getChildren().clear();
                        }
                    }
            );

            Button emergencyButton = new Button("Call Emergency");
            emergencyButton.setOnMouseClicked(e -> {
                String planeName = (String) planesSelectionBox.getSelectionModel().getSelectedItem();
                CallEmergency(planeName);
            });

            Button deleteButton = new Button("Delete");
            deleteButton.setOnMouseClicked(e -> {
                String planeName = (String) planesSelectionBox.getSelectionModel().getSelectedItem();
                DeletePlane(planeName);
            });
            Button deleteAllButton = new Button("Delete All");
            deleteAllButton.setOnMouseClicked(e -> {
                while (main.passengerPlanes.size() > 0) {
                    DeletePlane(main.passengerPlanes.get(0).name);
                }
                while (main.fighterPlanes.size() > 0) {
                    DeletePlane(main.fighterPlanes.get(0).name);
                }
            });

            selectPlane.setPadding(new Insets(10, 10, 10, 10));
            selectPlane.setSpacing(5);
            selectPlane.setAlignment(Pos.TOP_LEFT);
            selectPlane.getChildren().addAll(new Label("Plane:"), planesSelectionBox, emergencyButton, deleteButton, deleteAllButton);
        }
        resetPlaneSelectionBox();

        GridPane planeDetails = new GridPane();
        {
            planeDetails.setPadding(new Insets(10, 10, 10, 10));
            planeDetails.setVgap(5);
            planeDetails.setHgap(5);
            planeDetails.setAlignment(Pos.TOP_LEFT);
            planeDetails.add(new Label("ID:"), 0, 1);
            planeDetails.add(idLabel, 1, 1);
            planeDetails.add(new Label("Name:"), 0, 2);
            planeDetails.add(nameLabel, 1, 2);
            planeDetails.add(new Label("Type:"), 0, 3);
            planeDetails.add(typeLabel, 1, 3);
            planeDetails.add(new Label("Fuel:"), 0, 4);
            planeDetails.add(fuelLabel, 1, 4);
            planeDetails.add(new Label("Speed:"), 0, 5);
            planeDetails.add(speedLabel, 1, 5);
            planeDetails.add(new Label("Passengers:"), 0, 6);
            planeDetails.add(passengersLabel, 1, 6);
            planeDetails.add(new Label("Weapon type:"), 0, 7);
            planeDetails.add(weaponTypeLabel, 1, 7);
            planeDetails.add(new Label("Next airport:"), 0, 8);
            planeDetails.add(nextAirportLabel, 1, 8);
        }

        vbox.getChildren().addAll(addRandom, selectPlane, planeDetails, pathList);
    }

    /**
     * Setups the Ships tab - here all of the buttons as well as their functions and layouts are described.
     */
    private void shipsTabSetup() {
        VBox vbox = new VBox();
        shipsTab = new Tab("Ships", vbox);

        VBox addRandom = new VBox();
        {
            addRandom.setPadding(new Insets(10, 10, 10, 10));
            HBox addRandomHBox = new HBox();
            addRandomHBox.setSpacing(5);
            ComboBox typeSelectionBox = new ComboBox();
            typeSelectionBox.getItems().addAll("Ferry", "Carrier", "Random");
            typeSelectionBox.setValue("Random");

            Button addOneRandomPlane = new Button("Add 1");
            addOneRandomPlane.setOnMouseClicked(e -> {
                ShipType shipType = null;
                switch(typeSelectionBox.getSelectionModel().getSelectedItem().toString()) {
                    case "Ferry" -> shipType = ShipType.FERRY;
                    case "Carrier" -> shipType = ShipType.CARRIER;
                }
                main.mapStage.createRandomShip(shipType);
            });
            Button addFiveRandomShips = new Button("Add 5");
            addFiveRandomShips.setOnMouseClicked(e -> {
                for(int i=0; i<5; i++) {
                    ShipType shipType = null;
                    switch(typeSelectionBox.getSelectionModel().getSelectedItem().toString()) {
                        case "Ferry" -> shipType = ShipType.FERRY;
                        case "Carrier" -> shipType = ShipType.CARRIER;
                    }
                    main.mapStage.createRandomShip(shipType);
                }
            });
            addRandomHBox.getChildren().addAll(new Label("Type:"), typeSelectionBox, addOneRandomPlane, addFiveRandomShips);

            addRandom.getChildren().addAll(new Label("Add random ships:"), addRandomHBox);
        }

        HBox selectShip = new HBox();
        String notSelectedPrompt = "<unknown>";
        Label idLabel = new Label(notSelectedPrompt);
        Label nameLabel = new Label(notSelectedPrompt);
        Label companyNameLabel = new Label(notSelectedPrompt);
        Label typeLabel = new Label(notSelectedPrompt);
        Label passengersLabel = new Label(notSelectedPrompt);
        Label weaponTypeLabel = new Label(notSelectedPrompt);
        Label planesCreatedLabel = new Label(notSelectedPrompt);
        {
            shipsSelectionBox = new ComboBox();
            shipsSelectionBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
                        Ship selectedShip = null;
                        Ship oldShip = null;
                        ShipType shipType = null;
                        for (Ship s : main.ferries) {
                            if (oldShip == null && s.name == oldValue) {
                                oldShip = s;
                            }
                            if (selectedShip == null && s.name == newValue) {
                                selectedShip = s;
                                shipType = ShipType.FERRY;
                            }
                        }
                        for (Ship s : main.carriers) {
                            if (oldShip == null && s.name == oldValue) {
                                oldShip = s;
                            }
                            if (selectedShip == null && s.name == newValue) {
                                selectedShip = s;
                                shipType = ShipType.CARRIER;
                            }
                        }
                        if (oldShip != null) {
                            oldShip.setDarkColor();
                        }
                        if (selectedShip != null) {
                            selectedShip.setBrightColor();
                            idLabel.setText(Integer.toString(selectedShip.UID));
                            nameLabel.setText(selectedShip.name);
                            typeLabel.setText(shipType.name());
                            switch(shipType) {
                                case FERRY -> {
                                    String companyName = ((Ferry)selectedShip).companyName;
                                    int passengers = ((Ferry)selectedShip).currentPassengers;
                                    companyNameLabel.setText(companyName);
                                    passengersLabel.setText(Integer.toString(passengers));
                                    weaponTypeLabel.setText("<only in carriers>");
                                    planesCreatedLabel.setText("<only in carriers>");
                                }
                                case CARRIER -> {
                                    WeaponType weaponType = ((Carrier)selectedShip).weaponType;
                                    int planesCreated = ((Carrier)selectedShip).planesCreated;
                                    companyNameLabel.setText("<only in ferries>");
                                    passengersLabel.setText("<only in ferries>");
                                    weaponTypeLabel.setText(weaponType.name());
                                    planesCreatedLabel.setText(Integer.toString(planesCreated));
                                }
                            }
                        } else {
                            idLabel.setText(notSelectedPrompt);
                            nameLabel.setText(notSelectedPrompt);
                            companyNameLabel.setText(notSelectedPrompt);
                            typeLabel.setText(notSelectedPrompt);
                            passengersLabel.setText(notSelectedPrompt);
                            weaponTypeLabel.setText(notSelectedPrompt);
                            planesCreatedLabel.setText(notSelectedPrompt);
                        }
                    }
            );


            Button deleteButton = new Button("Delete");
            deleteButton.setOnMouseClicked(e -> {
                String shipName = (String) shipsSelectionBox.getSelectionModel().getSelectedItem();
                DeleteShip(shipName);
            });
            Button deleteAllButton = new Button("Delete All");
            deleteAllButton.setOnMouseClicked(e -> {
                while (main.ferries.size() > 0) {
                    DeleteShip(main.ferries.get(0).name);
                }
                while (main.carriers.size() > 0) {
                    DeleteShip(main.carriers.get(0).name);
                }
            });

            selectShip.setPadding(new Insets(10, 10, 10, 10));
            selectShip.setSpacing(5);
            selectShip.setAlignment(Pos.TOP_LEFT);
            selectShip.getChildren().addAll(new Label("Ship:"), shipsSelectionBox);
            selectShip.getChildren().addAll(deleteButton, deleteAllButton);
        }

        HBox createFighterPlane = new HBox();
        {
            createFighterPlane.setPadding(new Insets(0,10,10,10));
            Button createButton = new Button("Create Fighter Plane");
            createButton.setOnMouseClicked(e -> {
                String name = (String)shipsSelectionBox.getSelectionModel().getSelectedItem();
                if(name == null) return;
                for(Carrier c : main.carriers) {
                    if(c.name.equals(name)) {
                        main.mapStage.createRandomPlane(PlaneType.ARMY, c);
                        return;
                    }
                }
            });
            createFighterPlane.getChildren().add(createButton);
        }
        resetShipSelectionBox();

        GridPane shipDetails = new GridPane();
        {
            shipDetails.setPadding(new Insets(10, 10, 10, 10));
            shipDetails.setVgap(5);
            shipDetails.setHgap(5);
            shipDetails.setAlignment(Pos.TOP_LEFT);
            shipDetails.add(new Label("ID:"), 0, 1);
            shipDetails.add(idLabel, 1, 1);
            shipDetails.add(new Label("Name:"), 0, 2);
            shipDetails.add(nameLabel, 1, 2);
            shipDetails.add(new Label("Type:"), 0, 3);
            shipDetails.add(typeLabel, 1, 3);
            shipDetails.add(new Label("Company:"), 0, 4);
            shipDetails.add(companyNameLabel, 1, 4);
            shipDetails.add(new Label("Passengers:"), 0, 5);
            shipDetails.add(passengersLabel, 1, 5);
            shipDetails.add(new Label("Weapon Type:"), 0, 6);
            shipDetails.add(weaponTypeLabel, 1, 6);
            shipDetails.add(new Label("Planes created:"), 0, 7);
            shipDetails.add(planesCreatedLabel, 1, 7);

        }

        vbox.getChildren().addAll(addRandom, selectShip, createFighterPlane, shipDetails);
    }

    /**
     * Finds airport with specified name and deletes it.
     * @param name Name of the airport to be deleted.
     */
    private void DeleteAirport(String name) {
        if (name != null) {
            for (int i = 0; i < main.airports.size(); i++) {
                Airport a = main.airports.get(i);
                if (name.equals(a.name)) {
                    main.mapStage.airportModels.getChildren().remove(a.getModel());
                    for (int ii = 0; ii < main.passengerPlanes.size(); ii++) {
                        DeleteAirportFromPlane(a, main.passengerPlanes.get(ii));
                    }
                    for (int ii = 0; ii < main.fighterPlanes.size(); ii++) {
                        DeleteAirportFromPlane(a, main.fighterPlanes.get(ii));
                    }
                    main.airports.remove(a);
                    while (main.panelWrap.airportsSelectionBox.getItems().remove(name));
                    i--;
                }
            }

        }
    }

    /**
     * After deleting an airport you can delete it from specified planes path. It also makes sure there is no plane with less than 2 airports on their path.
     * @param airport Airport that was deleted.
     * @param plane Plane to check for the airport.
     */
    private void DeleteAirportFromPlane(Airport airport, Plane plane) {
        int initialPathSize = plane.path.size();
        while (plane.path.remove(airport)) ;
        Platform.runLater(() -> {
            if (initialPathSize != plane.path.size()) {
                if (plane.path.size() > 1) {
                    plane.nextAirport = plane.path.get(0);
                    plane.lastAirport = plane.path.get(plane.path.size() - 1);
                    plane.currentAirportIndex = 0;
                } else {
                    plane.lastAirport = null;
                    plane.nextAirport = null;
                    plane.stop();
                    main.mapStage.planeModels.getChildren().remove(plane.getModel());
                    main.passengerPlanes.remove(plane);
                    main.fighterPlanes.remove(plane);
                    main.panelWrap.planesSelectionBox.getItems().remove(main.panelWrap.planesSelectionBox.getValue());
                }
            }
        });
    }

    /**
     * Finds plane with specified name and deletes it. It checks both passenger and fighter planes lists.
     * @param name Name of the plane to be deleted.
     */
    private void DeletePlane(String name) {
        if (name != null) {
            for (int i = 0; i < main.passengerPlanes.size(); i++) {
                Plane p = main.passengerPlanes.get(i);
                if (name.equals(p.name)) {
                    p.stop();
                    main.mapStage.planeModels.getChildren().remove(p.getModel());
                    main.passengerPlanes.remove(p);
                    main.panelWrap.planesSelectionBox.getItems().remove(main.panelWrap.planesSelectionBox.getValue());
                }
            }
            for (int i = 0; i < main.fighterPlanes.size(); i++) {
                Plane p = main.fighterPlanes.get(i);
                if (name.equals(p.name)) {
                    p.stop();
                    main.mapStage.planeModels.getChildren().remove(p.getModel());
                    main.fighterPlanes.remove(p);
                    main.panelWrap.planesSelectionBox.getItems().remove(main.panelWrap.planesSelectionBox.getValue());
                }
            }
        }
    }

    /**
     * Finds ship with specified name and deletes it. It checks both ferries and carriers lists.
     * @param name Name of the ship to be deleted.
     */
    private void DeleteShip(String name) {
        if (name != null) {
            for (int i = 0; i < main.ferries.size(); i++) {
                Ship s = main.ferries.get(i);
                if (name.equals(s.name)) {
                    s.stop();
                    main.mapStage.shipModels.getChildren().remove(s.getModel());
                    main.ferries.remove(s);
                    main.panelWrap.shipsSelectionBox.getItems().remove(main.panelWrap.shipsSelectionBox.getValue());
                }
            }
            for (int i = 0; i < main.carriers.size(); i++) {
                Ship s = main.carriers.get(i);
                if (name.equals(s.name)) {
                    s.stop();
                    main.mapStage.shipModels.getChildren().remove(s.getModel());
                    main.carriers.remove(s);
                    main.panelWrap.shipsSelectionBox.getItems().remove(main.panelWrap.shipsSelectionBox.getValue());
                }
            }
        }
    }

    /**
     * Finds a plane with specified name and forces it to do emergency landing.
     * @param planeName Name of the plane to be forced to emergency land.
     */
    private void CallEmergency(String planeName) {
        if (planeName != null) {
            Plane p = null;
            PlaneType planeType = null;
            for (int i = 0; i < main.passengerPlanes.size(); i++) {
                p = main.passengerPlanes.get(i);
                if (planeName.equals(p.name)) {
                    planeType = PlaneType.PASSENGER;
                    break;
                }
            }
            for (int i = 0; i < main.fighterPlanes.size(); i++) {
                if(p != null) {
                    break;
                }
                p = main.fighterPlanes.get(i);
                if (planeName.equals(p.name)) {
                    planeType = PlaneType.ARMY;
                    break;
                }
            }
            if(p != null) {
                Vector3D position2D = Vector3D.Flat(p.position);
                Vector3D nextAirportPosition2D = Vector3D.Flat(p.nextAirport.position);
                double mag = Vector3D.Mag(Vector3D.Sub(position2D, nextAirportPosition2D));
                for(Airport a : main.airports) {
                    if(a.planeType == planeType){
                        Vector3D airportPosition2D = Vector3D.Flat(a.position);
                        Vector3D difference = Vector3D.Sub(position2D, airportPosition2D);
                        double newMag = Vector3D.Mag(difference);
                        if(newMag < mag) {
                            mag = newMag;
                            p.nextAirport = a;
                        }
                    }
                }
            }
        }
    }

    /**
     * Deletes all of the elements in airport selection box and adds existing airports to the list.
     */
    public void resetAirportSelectionBox() {
        airportsSelectionBox.getItems().clear();
        for (Airport airport : main.airports) {
            airportsSelectionBox.getItems().add(airport.name);
        }
        if (airportsSelectionBox.getItems().size() > 0) {
            airportsSelectionBox.setValue(airportsSelectionBox.getItems().get(0));
        }
    }

    /**
     * Deletes all of the elements in planes selection box and adds existing planes to the list.
     */
    public void resetPlaneSelectionBox() {
        planesSelectionBox.getItems().clear();
        for (Plane plane : main.passengerPlanes) {
            planesSelectionBox.getItems().add(plane.name);
        }
        for (Plane plane : main.fighterPlanes) {
            planesSelectionBox.getItems().add(plane.name);
        }
        if (airportsSelectionBox.getItems().size() > 0) {
            planesSelectionBox.setValue(airportsSelectionBox.getItems().get(0));
        }
    }

    /**
     * Deletes all of the elements in ships selection box and adds existing ships to the list.
     */
    public void resetShipSelectionBox() {
        shipsSelectionBox.getItems().clear();
        for (Ship ship : main.ferries) {
            shipsSelectionBox.getItems().add(ship.name);
        }
        for (Ship ship : main.carriers) {
            shipsSelectionBox.getItems().add(ship.name);
        }
        if (shipsSelectionBox.getItems().size() > 0) {
            shipsSelectionBox.setValue(shipsSelectionBox.getItems().get(0));
        }
    }

    public void updateAirportTab() {
        Platform.runLater(() -> {
            String value = (String)airportsSelectionBox.getSelectionModel().getSelectedItem();
            airportsSelectionBox.setValue("");
            airportsSelectionBox.setValue(value);
        });
    }

    public void updatePlanesTab() {
        Platform.runLater(() -> {
            String value = (String)planesSelectionBox.getSelectionModel().getSelectedItem();
            planesSelectionBox.setValue("");
            planesSelectionBox.setValue(value);
        });
    }

    public void updateShipsTab() {
        Platform.runLater(() -> {
            String value = (String)shipsSelectionBox.getSelectionModel().getSelectedItem();
            shipsSelectionBox.setValue("");
            shipsSelectionBox.setValue(value);
        });
    }

    public ControlWindow(Main main) {
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        stage = new Stage();
        this.main = main;
        this.size = new int[]{400, 720};
        this.stage.setTitle("whoosh: Control Panel");
        basicSetup();
        airportsTabSetup();
        planesTabSetup();
        shipsTabSetup();
        tabPane.getTabs().addAll(airportsTab, planesTab, shipsTab);
        stage.show();
    }
}
