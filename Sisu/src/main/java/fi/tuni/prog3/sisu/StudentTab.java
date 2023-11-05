/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fi.tuni.prog3.sisu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * A class that contains methods to modify the student info tab, not to
 * be initialized
 * @author jaakk
 */
public class StudentTab {
    
    //Min and max possible start and graduation years
    private static final int EARLIEST_START_YEAR = 2016;
    private static final int LATEST_START_YEAR = 2023;
    private static final int EARLIEST_END_YEAR = 2023;
    private static final int LATEST_END_YEAR = 2030;
    
    //Objects that the student info is taken from
    private static TextField nameField = new TextField();
    private static TextField numberField = new TextField();
    private static ComboBox<Integer> startYear = new ComboBox<>();
    private static ComboBox<Integer> gradYear = new ComboBox<>();
    
    /**
     * Creates the main structure for the tab
     * @return BorderPane that contains the structure
     */
    public static BorderPane getStudentWindow() {
        
        BorderPane studentWindow = new BorderPane();
        studentWindow.setPadding(new Insets(10, 10, 10, 10));
        
        Label header = new Label("Student information");
        header.setFont(new Font("Arial", 20));
        studentWindow.setTop(header);
        BorderPane.setAlignment(header, Pos.CENTER);
        
        Button quitButton = Sisu.getQuitButton();
        BorderPane.setMargin(quitButton, new Insets(10, 10, 0, 10));
        studentWindow.setBottom(quitButton);
        BorderPane.setAlignment(quitButton, Pos.TOP_RIGHT);
                
        HBox centerHBox = new HBox(10);
        centerHBox.getChildren().addAll(getInfoForm(), getInfoWindow());
        
        studentWindow.setCenter(centerHBox);
        return studentWindow;
    }
    
    /**
     * Creates the form in which the info will be given
     * @return GridPane that contains the form
     */
    private static GridPane getInfoForm() {
        
        GridPane form = new GridPane();
        form.prefWidthProperty().bind(Sisu.mainStage.widthProperty());
        form.setStyle("-fx-background-color: #8ed1de");
        form.setAlignment(Pos.TOP_CENTER);
        form.setPadding(new Insets(50, 50, 0, 0));
        
        nameField.setPromptText("Name");
        nameField.setStyle("-fx-prompt-text-fill: gray;");
        
        numberField.setPromptText("Student number (mandatory)");
        numberField.setStyle("-fx-prompt-text-fill: gray;");
        //Disabling the numberfield until the user has selected the login type
        numberField.setDisable(true);
        
        createComboBoxes(form);
        createAccountButtons(form, numberField);
        
        form.add(nameField, 0, 1, 3, 1);
        form.add(numberField, 0, 2, 3, 1);
        form.add(getSubmitButton(), 1, 6);
        
        return form;
    }
    
    /**
     * Creates the buttons that are used to select whether the user wants 
     * to create a new account or use an existing one
     * @param form the form the buttons will be added to
     * @param numberField TextField that the student number is input in
     */
    private static void createAccountButtons(GridPane form, TextField numberField) {
        
        Button newAccount = new Button("Create new");
        Button existingAccount = new Button("Use existing");
        newAccount.setStyle("-fx-background-color: #dcdcdc;");
        existingAccount.setStyle("-fx-background-color: #dcdcdc;");
        
        //Making the buttons "toggle" so that only one can be selected at a time
        newAccount.setOnAction(event -> {
            newAccount.setStyle("-fx-background-color: #93ef98;");
            existingAccount.setStyle("-fx-background-color: dcdcdc;");
            numberField.setDisable(false);
            Sisu.isExistingAccount = false;
        });
        
        existingAccount.setOnAction(event -> {
            existingAccount.setStyle("-fx-background-color: #93ef98;");
            newAccount.setStyle("-fx-background-color: dcdcdc;");
            newAccount.getStyleClass().add("button");
            numberField.setDisable(false);
            Sisu.isExistingAccount = true;
        });
        
        form.add(newAccount, 0, 0);
        form.add(existingAccount, 1, 0);
    }
    /**
     * Creates the ComboBoxes which are used to select start and graduation year
     * @param form the form the boxes will be added to
     */
    private static void createComboBoxes(GridPane form) {
        
        ObservableList<Integer> startYears = FXCollections.observableArrayList();
        for (int year = EARLIEST_START_YEAR; year <=LATEST_START_YEAR; year++) {
            startYears.add(year);
        }
        ObservableList<Integer> gradYears = FXCollections.observableArrayList();
        for (int year = EARLIEST_END_YEAR; year <= LATEST_END_YEAR; year++) {
            gradYears.add(year);
        }
        
        startYear.getItems().addAll(startYears);
        gradYear.getItems().addAll(gradYears);
        
        Label startLabel = new Label("Start year:");
        Label endLabel = new Label("Graduation:");
        
        form.add(startLabel, 0, 3);
        form.add(endLabel, 2, 3);
        form.add(startYear, 0, 4);
        form.add(gradYear,2, 4);
        
    }
    
    /**
     * Creates the instructions window
     * @return VBox containing instructions
     */
    private static VBox getInfoWindow() {
        
        VBox infoWindow = new VBox();
        infoWindow.prefWidthProperty().bind(Sisu.mainStage.widthProperty());
        infoWindow.setStyle("-fx-background-color: #50ec7c");
        
        Label header = new Label("Instructions");
        header.setFont(new Font("Arial", 16));
        infoWindow.setAlignment(Pos.TOP_CENTER);
        infoWindow.setPadding(new Insets(20, 0, 0, 0));
        
        TextFlow formInfo = new TextFlow();
        Text formText = new Text("First, the user should input their name and student "
                + "number, which will be used to fetch their study information. "
                + "The starting and graduation years are also asked but they are not "
                + "too important. After the information has been given, the user should "
                + "press the \"submit\" -button, which will redirect them to view "
                + "degree structures. ");
        
        TextFlow degreeInfo = new TextFlow();
        Text degreeText = new Text("The degree window shows all available degrees, "
                + "which can be found by either scrolling down or finding them by their "
                + "name in the search field. The degree structure can be viewed by "
                + "clicking on the degree. Different modules inside the degree can be "
                + "hidden or shown from the arrows next to them. To view info of a "
                + "single course, just hover your mouse over the course name. To select "
                + "or deselect courses, press on the module name and the courses will "
                + "appear in the right side window with checkboxes next to them.");
        
        formInfo.getChildren().addAll(formText, new Text("\n"));
        degreeInfo.getChildren().addAll(degreeText, new Text("\n"));
        infoWindow.getChildren().addAll(header, formInfo, degreeInfo);
        
        VBox.setMargin(formInfo, new Insets(20, 30, 0, 30));
        VBox.setMargin(degreeInfo, new Insets(20, 30, 0, 30));
        
        return infoWindow;
    }
    
    /**
     * Creates the button used to submit the form
     * @return Button 
     */
    private static Button getSubmitButton() {
        
        Button submit = new Button("Submit");
        GridPane.setMargin(submit, new Insets(20, 0, 0, 0));
        
        //Disabled until a student number is set
        submit.setDisable(true);
        numberField.textProperty().addListener(event -> {
            if (!numberField.getText().isEmpty()) {
                submit.setDisable(false);
            } else {
                submit.setDisable(true);
            }
        });
        
        //Moving the user to the degrees tab after submitting
        submit.setOnAction(event -> {
           Sisu.tabPane.getTabs().add(Sisu.degreeTab);
           Sisu.tabPane.getSelectionModel().select(Sisu.degreeTab);
           //Initializing student info variables
           Sisu.studentName = nameField.getText();
           Sisu.studentNumber = numberField.getText();
           
           if (startYear.getValue() != null) {
               Sisu.startYear = startYear.getValue();
           }
           if (gradYear.getValue() != null) {
               Sisu.gradYear = gradYear.getValue();
           }
               
           Sisu.tabPane.getTabs().remove(Sisu.studentTab);
        });
        
        return submit;
    }
    
    
    
}
