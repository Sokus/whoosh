package main.java;

import javafx.application.Platform;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class MapWrap {
    Stage stage;
    Scene scene;
    Group root;
    Group terrain;
    Group airportModels;
    int[] size;

    private Vector<Airport> airports;

    CameraController cameraController = new CameraController();

    File colorFile;
    File grayscaleFile;
    BufferedImage colorImage;
    BufferedImage grayscaleImage;
    private int[] imageSize = {0,0};

    String workingDirectory = System.getProperty("user.dir");

    private void basicSetup(){
        root.getChildren().addAll(terrain,airportModels);
        scene = new Scene(root, size[0], size[1], true);
        stage.setScene(scene);
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

        stage.onCloseRequestProperty().set(e->{ Platform.exit(); });
    }

    private void loadMapFiles() {
        try{
            colorFile = new File(workingDirectory+"\\src\\main\\resources\\map\\color.png");
            colorImage = ImageIO.read(colorFile);
        }catch(IOException e){
            System.out.println(e);
        }

        try{
            grayscaleFile = new File(workingDirectory+"\\src\\main\\resources\\map\\heightmap.png");
            grayscaleImage = ImageIO.read(grayscaleFile);
        }catch(IOException e){
            System.out.println(e);
        }

        imageSize[0] = colorImage.getWidth();
        imageSize[1] = colorImage.getHeight();
    }

    private void createMap() {
        for(int x=0; x<imageSize[0]; x++) {
            for(int z=imageSize[1]-1; z>=0; z--) {
                Box box = new Box();
                int sampleGrayscale = grayscaleImage.getRGB(x,z);
                int rGrayscale = (sampleGrayscale>>16) & 0xff;
                int gGrayscale = (sampleGrayscale>>8) & 0xff;
                int bGrayscale = sampleGrayscale & 0xff;
                double normalizedValue = (double)(rGrayscale+gGrayscale+bGrayscale)/(3*256);
                double height = normalizedValue*7;
                box.setTranslateX(x);
                box.setTranslateY(-height/2);
                box.setTranslateZ(z);

                box.setWidth(1);
                box.setDepth(1);
                box.setHeight(height);

                int sampleColor = colorImage.getRGB(x,z);
                int rColor = (sampleColor>>16) & 0xff;
                int gColor = (sampleColor>>8) & 0xff;
                int bColor = sampleColor & 0xff;
                box.setMaterial(new PhongMaterial(Color.rgb(rColor,gColor,bColor)));
                terrain.getChildren().add(box);
            }
        }
    }

    private void createAirports() {
        int[][] coordinates = {{5,1},{1,10},{0,19},{15,2},{15,15},{30,20},{29,10},{24,17},{25,25},{16,27}};
        String[] names = {"One","Two","Three","Four","Five","Six","Seven","Eight","Nine","Ten"};
        for (int i=0; i<coordinates.length; i++)
        {
            Airport airport = new Airport(terrain, names[i], PlaneType.PASSENGER, coordinates[i][0], coordinates[i][1]);
            airports.add(airport);
            airportModels.getChildren().add(airport.getModel());
        }
    }

    public MapWrap(Main main, Stage stage)
    {
        root = new Group();
        terrain = new Group();
        airportModels = new Group();
        this.airports = main.airports;
        this.stage = stage;
        size = new int[] {1280, 720};
        basicSetup();
        loadMapFiles();
        cameraController.focusPoint = new Point3D((double)imageSize[0]/2, -5, (double)imageSize[1]/2);
        cameraController.setRotationAbsolute(30, 15, 60);
        createMap();
        createAirports();

        this.stage.show();
    }
}
