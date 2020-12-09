package main.java;

import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main extends Application {
    private Group root = new Group();

    int imageWidth = 0;
    int imageHeight = 0;

    private void loadMap() {
        String localPath = System.getProperty("user.dir");
        File colorFile = null;
        File grayscaleFile = null;
        BufferedImage colorImage = null;
        BufferedImage grayscaleImage = null;

        try{
            colorFile = new File(localPath+"\\src\\main\\resources\\map2\\color.png");
            colorImage = ImageIO.read(colorFile);
        }catch(IOException e){
            System.out.println(e);
        }
        imageHeight = colorImage.getHeight();
        imageWidth = colorImage.getWidth();

        try{
            grayscaleFile = new File(localPath+"\\src\\main\\resources\\map2\\heightmap.png");
            grayscaleImage = ImageIO.read(grayscaleFile);
        }catch(IOException e){
            System.out.println(e);
        }
        for(int x=0; x<imageWidth; x++) {
            for(int z=0; z<imageHeight; z++) {
                Box box = new Box();
                int sampleGrayscale = grayscaleImage.getRGB(x,z);
                int rGrayscale = (sampleGrayscale>>16) & 0xff;
                int gGrayscale = (sampleGrayscale>>8) & 0xff;
                int bGrayscale = sampleGrayscale & 0xff;
                double height = (double)(rGrayscale+gGrayscale+bGrayscale)/(3*256)*7+1;
                Translate translate = new Translate(x,-height,z);
                Scale scale = new Scale(0.5,height,0.5);

                int sampleColor = colorImage.getRGB(x,z);
                int rColor = (sampleColor>>16) & 0xff;
                int gColor = (sampleColor>>8) & 0xff;
                int bColor = sampleColor & 0xff;
                box.getTransforms().setAll(translate, scale);
                box.setMaterial(new PhongMaterial(Color.rgb(rColor,gColor,bColor)));
                root.getChildren().add(box);
            }
        }
    }

    public void start(Stage primaryStage) throws Exception {
        loadMap();
        Scene scene = new Scene(root,1280,720, true);
        CameraController cameraController = new CameraController(
                new Point3D(imageWidth/2,-5, imageHeight/2),
                30,
                15,
                180);
        scene.setFill(Color.grayRgb(32));
        scene.setCamera(cameraController.getCamera());

        scene.setOnKeyPressed(e -> {
            switch(e.getCode()){
                case W:
                    cameraController.moveCameraRelative(5,0,0);
                    break;
                case A:
                    cameraController.moveCameraRelative(0,-5,0);
                    break;
                case S:
                    cameraController.moveCameraRelative(-5,0,0);
                    break;
                case D:
                    cameraController.moveCameraRelative(0,5,0);
                    break;
                case CONTROL:
                    cameraController.moveCameraRelative(0,0,10);
                    break;
                case SHIFT:
                    cameraController.moveCameraRelative(0,0,-10);
                    break;
            }
        });


        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
