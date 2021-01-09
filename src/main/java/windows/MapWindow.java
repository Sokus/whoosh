package main.java.windows;

import javafx.application.Platform;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.stage.Stage;
import main.java.Airport;
import main.java.CameraController;
import main.java.Main;
import main.java.vehicles.*;
import main.java.terrain.Chunk;
import main.java.terrain.Terrain;
import main.java.terrain.TerrainType;
import main.java.utility.Vector3D;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;

public class MapWindow {
    Main main;
    Stage stage;
    Scene scene;
    int[] size;
    Group root;
    Group terrainGroup;
    Group airportModels;
    public Group planeModels;
    public Group shipModels;
    String[] names;

    CameraController cameraController = new CameraController();

    File colorFile;
    File grayscaleFile;
    BufferedImage colorImage;
    BufferedImage grayscaleImage;
    private int[] imageSize = {0, 0};

    double waterLevel;
    double peakLevel;

    Random random = new Random();

    /**
     * Basic setup of the Map window. It creates all the groups and interactions needed to show the scene.
     */
    private void basicSetup() {
        terrainGroup = new Group();
        airportModels = new Group();
        planeModels = new Group();
        shipModels = new Group();
        root.getChildren().addAll(terrainGroup, airportModels, planeModels, shipModels);
        scene = new Scene(root, size[0], size[1], true);
        stage.setScene(scene);
        stage.setX(5);
        stage.setY(5);
        scene.setFill(Color.grayRgb(32));
        scene.setCamera(cameraController.getCamera());

        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case W -> cameraController.setRotationRelative(5, 0, 0);
                case A -> cameraController.setRotationRelative(0, -5, 0);
                case S -> cameraController.setRotationRelative(-5, 0, 0);
                case D -> cameraController.setRotationRelative(0, 5, 0);
                case CONTROL -> cameraController.setRotationRelative(0, 0, 10);
                case SHIFT -> cameraController.setRotationRelative(0, 0, -10);
            }
        });

        stage.onCloseRequestProperty().set(e -> {
            main.stop();
            Platform.exit();
        });

        names = new String[]{"One",
                "Two",
                "Three",
                "Four",
                "Five",
                "Six",
                "Seven",
                "Eight",
                "Nine",
                "Ten",
                "Eleven",
                "Twelve",
                "Thirteen",
                "Fourteen",
                "Fifteen",
                "Sixteen",
                "Seventeen",
                "Eighteen",
                "Nineteen",
                "Twenty"};
    }

    /**
     * Load the map files needed to create the terrain.
     */
    private void loadMapFiles() {
        try {
            colorFile = new File(main.workingDirectory + "\\src\\main\\resources\\map\\color.png");
            colorImage = ImageIO.read(colorFile);
        } catch (IOException e) {
            System.out.println(e);
        }

        try {
            grayscaleFile = new File(main.workingDirectory + "\\src\\main\\resources\\map\\heightmap.png");
            grayscaleImage = ImageIO.read(grayscaleFile);
        } catch (IOException e) {
            System.out.println(e);
        }

        if (colorImage != null) {
            imageSize[0] = colorImage.getWidth();
            imageSize[1] = colorImage.getHeight();
        }
    }

    /**
     * Build the map using data read from the images.
     */
    private void createMap() {
        main.terrain = new Terrain(imageSize[0], imageSize[1]);
        waterLevel = Double.MAX_VALUE;
        peakLevel = Double.MIN_VALUE;
        for (int x = 0; x < imageSize[0]; x++) {
            for (int z = 0; z < imageSize[1]; z++) {
                Box box = new Box();
                int sampleGrayscale = grayscaleImage.getRGB(x, z);
                int rGrayscale = (sampleGrayscale >> 16) & 0xff;
                int gGrayscale = (sampleGrayscale >> 8) & 0xff;
                int bGrayscale = sampleGrayscale & 0xff;
                double normalizedValue = (double) (rGrayscale + gGrayscale + bGrayscale) / (3 * 256);
                double height = normalizedValue * 7;
                if (height > peakLevel) peakLevel = height;
                if (height < waterLevel) waterLevel = height;
                main.terrain.setChunk(x, z, new Chunk(null, box));
                box.setTranslateX(x);
                box.setTranslateY(-height / 2);
                box.setTranslateZ(z);

                box.setWidth(1);
                box.setDepth(1);
                box.setHeight(height);

                int sampleColor = colorImage.getRGB(x, z);
                int rColor = (sampleColor >> 16) & 0xff;
                int gColor = (sampleColor >> 8) & 0xff;
                int bColor = sampleColor & 0xff;
                box.setMaterial(new PhongMaterial(Color.rgb(rColor, gColor, bColor)));
                terrainGroup.getChildren().add(box);
            }
        }
        for (int x = 0; x < main.terrain.size[0]; x++) {
            for (int z = 0; z < main.terrain.size[1]; z++) {
                Chunk temporary = main.terrain.getChunk(x, z);
                double height = temporary.model.getHeight();
                temporary.type = height > waterLevel + 0.1 ? TerrainType.LAND : TerrainType.WATER;
            }
        }
    }

    /**
     * Create an airport with unique ID and Name. It is randomly placed on piece of land.
     * @param planeType You can pre-define what type of airport is it going to be, leave null for random type.
     */
    public void createRandomAirport(PlaneType planeType) {
        int attemptsPerAirport = 1000;
        int distanceBetweenAirports = 4;
        int randX;
        int randZ;
        for (int j = 0; j < attemptsPerAirport; j++) {
            boolean canCreate = true;
            randX = random.nextInt(imageSize[0]);
            randZ = random.nextInt(imageSize[1]);
            if (main.terrain.getChunk(randX, randZ).type == TerrainType.LAND) {
                for (Airport a : main.airports) {
                    if (Math.abs(a.position.x - randX) < distanceBetweenAirports && Math.abs(a.position.z - randZ) < distanceBetweenAirports) {
                        canCreate = false;
                        break;
                    }
                }
                if (canCreate) {
                    int newID = 0;
                    for (Airport a : main.airports) {
                        if (a.ID == newID) {
                            newID++;
                        } else {
                            break;
                        }
                    }

                    String newName = "";
                    for (int i = 0; i < 8; i++) {
                        newName += Character.toString((char) (97 + random.nextInt(26)));
                    }

                    boolean[] nameCheck = new boolean[names.length];
                    for (Airport a : main.airports) {
                        for (int i = 0; i < names.length; i++) {
                            if (a.name.equals(names[i]))
                                nameCheck[i] = true;
                        }
                    }
                    for (int i = 0; i < names.length; i++) {
                        if (!nameCheck[i]) {
                            newName = names[i];
                            break;
                        }
                    }
                    if(planeType == null) {
                        planeType = random.nextBoolean() ? PlaneType.PASSENGER : PlaneType.ARMY;
                    }
                    Airport airport = new Airport(main.terrain,
                            newID,
                            newName,
                            planeType,
                            randX,
                            randZ);
                    main.airports.add(airport);
                    main.panelWrap.airportsSelectionBox.getItems().add(newName);
                    main.panelWrap.airportsSelectionBox.setValue(newName);
                    airportModels.getChildren().add(airport.getModel());
                    return;
                }
            }
        }
    }

    /**
     * Create a plane with unique ID and Name. Its path is randomly chosen from existing airports. Any plane needs at least 2 airports with maching type, fighter planes also need one ship on the map.
     * @param type You can pre-define what type of plane is it going to be, leave null for random type.
     * @param carrier You can specify a ship new Fighter Plane will be created on.
     */
    public void createRandomPlane(PlaneType type, Carrier carrier) {
        if (type == null) {
            type = random.nextBoolean() ? PlaneType.PASSENGER : PlaneType.ARMY;
        }
        Vector<Airport> airports = new Vector<>();
        for (Airport a : main.airports) {
            if (a.planeType == type) {
                airports.add(a);
            }
        }
        if(airports.size()<2) {
            return;
        }
        if(type == PlaneType.ARMY) {
            if(main.carriers.size()==0) {
                return;
            }
            if(carrier == null) {
                carrier = main.carriers.get(random.nextInt(main.carriers.size()));
            }
        }

        int newID = 0;
        for (int i = 0; i < main.passengerPlanes.size() || i < main.fighterPlanes.size(); i++) {
            boolean check = true;
            if (i < main.passengerPlanes.size()) {
                if (newID == main.passengerPlanes.get(i).UID) {
                    check = false;
                    newID++;
                }
            }
            if (i < main.fighterPlanes.size()) {
                if (newID == main.fighterPlanes.get(i).UID) {
                    check = false;
                    newID++;
                }
            }
            if (check) break;
        }

        String newName = "";
        for (int i = 0; i < 8; i++) {
            newName += Character.toString((char) (97 + random.nextInt(26)));
        }

        boolean[] nameCheck = new boolean[names.length];
        for (Vehicle v : main.passengerPlanes) {
            for (int i = 0; i < names.length; i++) {
                if (v.name.equals(names[i])) {
                    nameCheck[i] = true;
                    break;
                }
            }
        }
        for (Vehicle v : main.fighterPlanes) {
            for (int i = 0; i < names.length; i++) {
                if (v.name.equals(names[i])) {
                    nameCheck[i] = true;
                    break;
                }
            }
        }
        for (int i = 0; i < names.length; i++) {
            if (!nameCheck[i]) {
                newName = names[i];
                break;
            }
        }
        int pathLength = airports.size();
        Vector<Airport> path = new Vector<>(pathLength);
        for (int i = 0; i < pathLength; i++) {
            int index = random.nextInt(airports.size() - i) + i;
            Airport a = airports.get(index);
            path.add(a);
            airports.set(index, airports.get(i));
            airports.set(i, a);
        }
        Plane plane = null;
        switch (type) {
            case PASSENGER -> {
                Vector3D position = new Vector3D(random.nextInt(main.terrain.size[0]),
                        main.mapStage.peakLevel + 0.5,
                        random.nextInt(main.terrain.size[1]));
                plane = new PassengerPlane(newID, position, 2, peakLevel + 0.5);
                main.passengerPlanes.add((PassengerPlane) plane);
            }
            case ARMY -> {
                FighterPlane fighterPlane = new FighterPlane(newID, new Vector3D(carrier.position), 5, peakLevel + 0.5);
                plane = fighterPlane;
                fighterPlane.weaponType = carrier.weaponType;
                carrier.planesCreated++;
                main.fighterPlanes.add((FighterPlane) plane);
            }
        }
        plane.path = path;
        plane.nextAirport = path.get(0);
        plane.name = newName;
        planeModels.getChildren().add(plane.getModel());
        plane.start();
        main.panelWrap.planesSelectionBox.getItems().add(newName);
        main.panelWrap.planesSelectionBox.setValue(newName);
    }

    /**
     *  Creates a ship with unique ID and Name, spawned in a random place on water.
     * @param type You can pre-define what type of ship is it going to be, leave null for random type.
     */
    public void createRandomShip(ShipType type) {
        if(type == null) {
            type = random.nextBoolean() ? ShipType.FERRY : ShipType.CARRIER;
        }
        int newID = 0;
        for (int i = 0; i < main.ferries.size() || i < main.carriers.size(); i++) {
            boolean check = true;
            if (i < main.ferries.size()) {
                if (newID == main.ferries.get(i).UID) {
                    check = false;
                    newID++;
                }
            }
            if (i < main.carriers.size()) {
                if (newID == main.carriers.get(i).UID) {
                    check = false;
                    newID++;
                }
            }
            if (check) break;
        }

        String newName = "";
        for (int i = 0; i < 8; i++) {
            newName += Character.toString((char) (97 + random.nextInt(26)));
        }

        boolean[] nameCheck = new boolean[names.length];
        for (Vehicle v : main.ferries) {
            for (int i = 0; i < names.length; i++) {
                if (v.name.equals(names[i])) {
                    nameCheck[i] = true;
                    break;
                }
            }
        }
        for (Vehicle v : main.carriers) {
            for (int i = 0; i < names.length; i++) {
                if (v.name.equals(names[i])) {
                    nameCheck[i] = true;
                    break;
                }
            }
        }
        for (int i = 0; i < names.length; i++) {
            if (!nameCheck[i]) {
                newName = names[i];
                break;
            }
        }

        Vector3D position = null;
        for (int i = 0; i < 1000; i++) {
            position = new Vector3D(random.nextInt(main.terrain.size[0]),
                    main.mapStage.waterLevel,
                    random.nextInt(main.terrain.size[1]));
            if (main.terrain.getChunk((int) position.x, (int) position.z).type == TerrainType.WATER) {
                break;
            }
        }
        Ship ship = null;
        switch (type) {
            case FERRY -> {
                ship = new Ferry(newID, position, 3, waterLevel);
                ((Ferry)ship).companyName = names[random.nextInt(names.length)];
                main.ferries.add((Ferry) ship);
            }
            case CARRIER -> {
                ship = new Carrier(newID, position, 3, waterLevel);
                ((Carrier) ship).weaponType = random.nextBoolean() ? WeaponType.ROCKETS : WeaponType.CARABINE;
                main.carriers.add((Carrier) ship);
            }
        }
        ship.name = newName;
        ship.terrain = main.terrain;
        shipModels.getChildren().add(ship.getModel());
        ship.start();
        main.panelWrap.shipsSelectionBox.getItems().add(newName);
        main.panelWrap.shipsSelectionBox.setValue(newName);
    }

    public MapWindow(Main main, Stage stage) {
        this.main = main;
        root = new Group();
        this.stage = stage;
        this.stage.setTitle("whoosh: Map");
        size = new int[]{1280, 720};
        basicSetup();
        loadMapFiles();
        createMap();
        cameraController.focusPoint = new Point3D((double) imageSize[0] / 2, -5, (double) imageSize[1] / 2);
        cameraController.setRotationAbsolute(30, 15, 60);
        this.stage.show();
    }
}
