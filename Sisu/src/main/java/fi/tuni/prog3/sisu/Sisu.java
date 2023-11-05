package fi.tuni.prog3.sisu;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * JavaFX Sisu
 * 
 * The services will be called in the data structure classes
 * so after the user settings have been given, all the necessary modules courses
 * etc will exist, this will be triggered with a "DegreeProgram" class
 * initialization
 * 
 * This class contains many public variables, since they will be needed to
 * access from multiple classes, and it seemed logical to store them all in the 
 * same place
 * 
 */
public class Sisu extends Application {

    /**
     * Variables needed from the form, will be updated and passed to file reader
     */
    public static String studentName = "null";
    public static String studentNumber = "null";
    public static int startYear = 0;
    public static int gradYear = 0;
    public static boolean isExistingAccount = false;
    
    /**
     * Stores the latest DegreeProgram of which courses are selected or 
     * deselected
     */
    public static DegreeProgram editedDegree = null;

    /**
     * List storing all degrees
     */
    public static List<DegreeProgram> allDegrees = new ArrayList<>();

    /** 
     * Keeps track whether the user is on the degree window, used to check
     * if back button should be enabled or disabled
     */ 
    public static BooleanProperty onFirstWindow = new SimpleBooleanProperty(true);
    
    /**
     * Label that contains the current header
     */
    public static Label headerLabel = new Label();
    
    /**
     * UI components that will be needed to access from other classes
     */
    public static Stage mainStage;
    public static Tab degreeTab = new Tab("Degrees");
    public static Tab studentTab = new Tab("Student info");
    public static TabPane tabPane = new TabPane();
    public static VBox leftVBox = new VBox();
    public static VBox rightVBox = new VBox();
    public static VBox degreeView = new VBox();
    
    /**
     * Prepares the ui components
     * @param stage the ui stage
     */
    @Override
    public void start(Stage stage) {

        Programs programs = new Programs();
        allDegrees = programs.getAllPrograms();
        // Enabling the Stage object to be accessible in all classes
        mainStage = stage;
        
        tabPane.getTabs().add(studentTab);
        
        Scene scene = new Scene(tabPane, 1300, 800);
        scene.getRoot().requestFocus();

        studentTab.setContent(StudentTab.getStudentWindow());
        degreeTab.setContent(DegreeTab.getDegreeWindow());

        // Setting the tabs non-closable, because there is no way to reopen them
        studentTab.setClosable(false);
        degreeTab.setClosable(false);
        
        mainStage.setScene(scene);
        mainStage.setTitle("SisuGUI");
        mainStage.setFullScreen(true);
        mainStage.show();

        
    }

    /**
     * Launches the program
     * @param args most probably not used
     */
    public static void main(String[] args) {

        launch();
    }

    // Creates and returns the quit button
    public static Button getQuitButton() {

        Button button = new Button("Quit");
        
        //Saves the edited degree in the file after quitting the program
        button.setOnAction((ActionEvent event) -> {
            if (editedDegree != null) {
                try {
                    editedDegree.save();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            Platform.exit();
        });

        return button;
    }
    
}