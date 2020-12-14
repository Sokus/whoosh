package main.java;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class PanelWrap {
    Stage stage;
    Scene scene;
    GridPane gridPane;
    int[] size;

    Main main;

    private void basicSetup()
    {
        scene = new Scene(gridPane, size[0],size[1]);
        stage.setScene(scene);
        stage.onCloseRequestProperty().set(e->{ Platform.exit(); });

        ComboBox comboBox = new ComboBox();
        for(Airport airport : main.airports)
        {
            comboBox.getItems().add(airport.name);
        }

        comboBox.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
                    System.out.println(oldValue + " -> " + newValue);
                }
        );

        Button button = new Button();
        button.setOnAction(e -> System.out.println(comboBox.getSelectionModel().getSelectedIndex()));

        gridPane.add(comboBox,0,0);
        gridPane.add(button,1,0);
        gridPane.setMinSize(400, 200);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.CENTER);
        stage.setX(main.mapWrap.stage.getX()+main.mapWrap.size[0]);
        stage.setY(main.mapWrap.stage.getY());
    }

    public PanelWrap(Main main) {
        gridPane = new GridPane();
        stage = new Stage();
        this.main = main;
        this.size = new int[] {300,720};
        basicSetup();
        stage.show();
    }
}
