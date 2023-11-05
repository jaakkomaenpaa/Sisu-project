/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fi.tuni.prog3.sisu;

import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;

/**
 * A class that stores the methods to create most of the ui objects for the
 * degrees tab
 * @author jaakk
 */
public class DegreeTab {
    
    /**
     * Ux helper, keeps track whether hover effect for course info should be 
     * on or not
     */
    private static boolean moduleLock = false;
    
    /**
     * Creates a BorderPane that holds all contents for the degree tab
     * @return BorderPane
     */
    public static BorderPane getDegreeWindow() {
        // Creating the main layout
        BorderPane degreeWindow = new BorderPane();
        degreeWindow.setPadding(new Insets(10, 10, 10, 10));

        // Adding a quit button to the BorderPane
        Button quitButton = Sisu.getQuitButton();
        BorderPane.setMargin(quitButton, new Insets(10, 10, 0, 10));
        degreeWindow.setBottom(quitButton);
        BorderPane.setAlignment(quitButton, Pos.TOP_RIGHT);
        
        degreeWindow.setTop(getTopPane());
        degreeWindow.setCenter(getCenterHBox());
        
        return degreeWindow;
    }
    
     /**
     * Creates an arrow button that can be used to hide and show the whole
     * module tree under a single degree
     * @param degreeContents a VBox which will contain all modules and courses
     * @param subModules modules under the degree
     * @param degree the degree that is shown
     * @return a ToggleButton with an arrow icon
     */
    public static ToggleButton getDegreeToggle(VBox degreeContents, List<StudyModule> subModules, DegreeProgram degree) {
        
        //Drawing the arrow
        Polygon arrow = new Polygon();
        arrow.getPoints().addAll(0.0, 0.0, 6.0, 0.0, 3.0, 8.0);
        ToggleButton toggleArrow = new ToggleButton();
        toggleArrow.setGraphic(arrow);
        toggleArrow.setStyle("-fx-background-color: #ffffff;");
        
        //Setting display and turning the arrow
        toggleArrow.setOnAction(event -> {
           boolean isVisible = !toggleArrow.isSelected();
           if (isVisible) {
               UiServices.displayDegreeContents(degreeContents, subModules, degree);
               arrow.setRotate(0);
           } else {
               degreeContents.getChildren().clear();
               arrow.setRotate(270);
           } 
        });
        
        return toggleArrow;
    }
    
    /**
     * Creates a toggle button to show and hide modules, becomes somewhat 
     * recursive along with UiServices.displayModuleContents()
     * @param moduleView the VBox that contains the module contents
     * @param courses courses under the module
     * @param modules modules under the module
     * @param indent desired indent amount
     * @param degree the degree the module is under
     * @return a ToggleButton for the module that the moduleView is part of
     */
    public static ToggleButton getModuleToggle(VBox moduleView, List<Course> courses, List<StudyModule> modules, 
            int indent, DegreeProgram degree) {
        
        //Creating a toggle button with an arrow icon inside it
        Polygon arrow = new Polygon();
        arrow.getPoints().addAll(0.0, 0.0, 6.0, 0.0, 3.0, 8.0);
        arrow.setRotate(270);
        
        ToggleButton toggleArrow = new ToggleButton();
        toggleArrow.setGraphic(arrow);
        toggleArrow.setStyle("-fx-background-color: #ffffff;");
        
        //Setting the ability to hide and show courses using the toggle arrow
        toggleArrow.setOnAction(event -> {
             boolean isVisible = toggleArrow.isSelected();
             if (isVisible) {
                UiServices.displayModuleContents(moduleView, courses, modules, indent + 20, degree);
                arrow.setRotate(0);
             } else {
                moduleView.getChildren().clear();
                arrow.setRotate(270);
             }
        });

        return toggleArrow;
    }
    
    /**
     * Creates a course name and its style
     * @param course the Course that the label is created for
     * @param indent desired indent level
     * @return HBox containing label and icon
     */
    public static HBox getClickableCourse(Course course, int indent) {

        Label courseLabel = new Label(String.format(
              "%s | %d credits",
              course.getName(),
              course.getMinCredits()
        ));

        //Creating action effects
        courseLabel.setOnMouseEntered(event -> {
            courseLabel.setCursor(Cursor.HAND); 
            courseLabel.setStyle("-fx-font-weight: bold;");
            //When module is clicked, courses are "locked" from viewing by hover
            if (!moduleLock) {
                CourseInfo.showCourseInfo(course);
            } 
        });

        courseLabel.setOnMouseExited(event -> {
           courseLabel.setCursor(Cursor.DEFAULT); 
           courseLabel.setStyle(" -fx-font-weight: normal;");
           if (!moduleLock) {
               Sisu.rightVBox.getChildren().clear();
           }
        });
        
        courseLabel.setOnMouseClicked(event -> {
            //Releasing the "lock" after a course is clicked
            moduleLock = false;
        });
        
        ImageView icon = getCourseIcon(course);
        HBox courseHeader = new HBox(icon, courseLabel);
        HBox.setMargin(icon, new Insets(0, 0, 0, indent));
        courseLabel.setPadding(new Insets(0, 0, 0, 5));

        return courseHeader;
    }
    
    /**
     * Creates a module header that contains the toggle button and module name
     * @param moduleView the VBox containing contents of the module
     * @param module module that the header is created for
     * @param indent desired indent level
     * @param degree the degree that the module is part of
     * @return a HBox containing the header
     */
    public static HBox getClickableModule(VBox moduleView, StudyModule module, int indent, DegreeProgram degree) {
        
        Label moduleLabel = new Label(String.format(
                "%s | %d / %d credits",
                module.getName(),
                module.getEarnedCredits(0),
                module.getMinCredits()
        ));
        
        ToggleButton moduleToggle = DegreeTab.getModuleToggle(
                    moduleView, module.getCourses(), module.getSubModules(), indent, degree
        );
        HBox moduleHeader = new HBox(moduleToggle, moduleLabel);
        
        HBox.setMargin(moduleLabel, new Insets(5, 0, 0, 2));
        moduleHeader.setPadding(new Insets(0, 0, 0, indent));
        
        //Making the module clickable only if there are courses directly under it
        if (!module.getCourses().isEmpty()) {
            
            moduleLabel.setOnMouseEntered(event -> {
                moduleLabel.setCursor(Cursor.HAND); 
                moduleLabel.setStyle("-fx-font-weight: bold;");
            });

            moduleLabel.setOnMouseExited(event -> {
               moduleLabel.setCursor(Cursor.DEFAULT); 
               moduleLabel.setStyle(" -fx-font-weight: normal;");
            });
            //"Locking" the module when clicked so that the course window
            //doesn't accidentally change to showing course info
            moduleLabel.setOnMouseClicked(event -> {
                CourseInfo.showCourseChoices(module, degree);
                moduleLock = true;
            });
        }
        
        return moduleHeader;
    }
    
    
    /**
    * Creates a single degree label and makes it clickable
    * @param degree the DegreeProgram that the label describes
    */
    public static void createClickableDegree(DegreeProgram degree) {
        //Hardcoded to 0 credits gained for now
        Label degreeLabel = new Label(String.format(
                "%s | %d / %d credits",
                degree.getName(),
                degree.getEarnedCredits(),
                degree.getMinCredits()
        ));
        
        degreeLabel.setOnMouseEntered(event -> {
            degreeLabel.setCursor(Cursor.HAND); 
            degreeLabel.setStyle("-fx-font-weight: bold;");
        });

        degreeLabel.setOnMouseExited(event -> {
           degreeLabel.setCursor(Cursor.DEFAULT); 
           degreeLabel.setStyle(" -fx-font-weight: normal;");
        });
        
        degreeLabel.setOnMouseClicked(event -> {
           UiServices.displayTree(degree);
        });
        
        VBox.setMargin(degreeLabel, new Insets(2, 0, 0, 10));
        Sisu.degreeView.getChildren().add(degreeLabel);
    }
    
    /**
     * Creates a BorderPane to store the back button and header
     * @return BorderPane
     */
    private static BorderPane getTopPane() {
        
        Button backButton = getBackButton();
        
        BorderPane topPane = new BorderPane();
        
        topPane.setLeft(backButton);
        topPane.setCenter(Sisu.headerLabel);
        Sisu.headerLabel.setFont(new Font("Arial", 20));
        
        return topPane;
    }
    
    /**
     * Creates the back button which is used to go from a single degree view
     * to degree select view
     * @return back Button
     */
    private static Button getBackButton() {
        
        Button backButton = new Button("Back");
        //Backing to the course select view
        backButton.setOnMousePressed(event -> {
            Sisu.leftVBox.getChildren().clear();
            createLeftVBoxContents();
        });
        //Binding the button to a boolean, which defines if the button should
        //be disabled or enabled
        backButton.disableProperty().bind(Sisu.onFirstWindow);
        
        return backButton;
    }
    
    /**
     * Creates the main structure of the ui
     * @return HBox
     */
    private static HBox getCenterHBox() {
        
        HBox centerHBox = new HBox(10);
        centerHBox.getChildren().addAll(getLeftVBox(), getRightVBox());
        
        return centerHBox;
    }
    
    /**
     * Creates the left side box, which will contain all degree info
     * @return VBox
     */
    private static VBox getLeftVBox() {
        
        //Binding the box to the stage so that the width changes along with
        //the window
        Sisu.leftVBox.prefWidthProperty().bind(Sisu.mainStage.widthProperty());
        Sisu.leftVBox.setStyle("-fx-background-color: #8fc6fd;");
        createLeftVBoxContents();
        
        return Sisu.leftVBox;
    }
    
    /**
     * Creates the right side box, which will contain all course info
     * @return VBox
     */
    private static VBox getRightVBox() {
        
        //Binding the box to the stage so that the width changes along with
        //the window
        Sisu.rightVBox.prefWidthProperty().bind(Sisu.mainStage.widthProperty());
        Sisu.rightVBox.setStyle("-fx-background-color: #8fc6fd;");
        
        return Sisu.rightVBox;
    }
    
    /**
     * Creates the contents for the left side window
     */
    private static void createLeftVBoxContents() {
        
        Sisu.onFirstWindow.set(true);
        TextField searchField = getSearchField();
        VBox.setMargin(searchField, new Insets(10, 100, 10, 10));
        
        //Creating a scrollable pane inside the left window
        ScrollPane scrollWindow = new ScrollPane(Sisu.degreeView);
        //Increasing the scrolling speed, since the default is way too slow
        scrollWindow.getContent().setOnScroll(event -> {
            double deltaY = event.getDeltaY() * 0.002;    
            scrollWindow.setVvalue(scrollWindow.getVvalue() - deltaY);
        });
        //Aligning width and height
        scrollWindow.prefHeightProperty().bind(Sisu.leftVBox.heightProperty());
        scrollWindow.prefWidthProperty().bind(Sisu.leftVBox.widthProperty());
        searchField.prefWidthProperty().bind(Sisu.leftVBox.widthProperty().multiply(0.5).subtract(20));
        
        Sisu.leftVBox.getChildren().addAll(searchField, scrollWindow);
        
        UiServices.displayDegrees(Sisu.allDegrees);
    }
    
    /**
     * Creates a search field, which can be used to filter degrees
     * @return TextField
     */
    private static TextField getSearchField() {
        
        TextField searchField = new TextField();
        
        searchField.setPromptText("Search degrees");
        //Creating a filtering method which filters degrees when the value of
        //the search field is changed
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String filter = searchField.getText().toLowerCase();
            UiServices.filterDegrees(filter);
        });
        
        return searchField;
    }
    
    /**
     * Creates the icon which is displayed next to the course name
     * @param course the Course object that the icon is created for
     * @return an ImageView with the icon
     */
    private static ImageView getCourseIcon(Course course) {
        
        //Fetching the possible icons
        Image completeIcon = new Image(DegreeTab.class.getResourceAsStream("/laurelleaf.png"));
        Image icon = new Image(DegreeTab.class.getResourceAsStream("/hyphen.png"));
        
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(15);
        iconView.setFitHeight(15);
        
        //Binding the icon to a course state so that the icon changes with the 
        //state, depending if the course is marked as completed or not
        SimpleStringProperty courseState = new SimpleStringProperty(course.getState());
        iconView.imageProperty().bind(Bindings.createObjectBinding(() -> {
            String state = courseState.get();
            if (state.equals("COMPLETED")) {
                return completeIcon;
            } else {
                return icon;
            }
        }, courseState));
        
        return iconView;
    }
    
}