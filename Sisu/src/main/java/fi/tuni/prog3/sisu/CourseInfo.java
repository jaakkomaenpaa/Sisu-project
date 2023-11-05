/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fi.tuni.prog3.sisu;

import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * A class which contains methods to modify the right side window,
 * not to be initialized
 * @author jaakk
 */
public class CourseInfo {
    
    /**
     * Shows info of a single course in the right window
     * @param course the course of which info is shown
     */
    public static void showCourseInfo(Course course) {
        Sisu.rightVBox.getChildren().clear();
        
        
        //Creating text of course contents
        TextFlow contentInfo = new TextFlow();
        //Stripping html tags out of fetched string with a regex
        Text content = new Text(course.getContent().replaceAll("\\<.*?\\>", ""));
        contentInfo.getChildren().addAll(content, new Text("\n"));
        
        //Creating text of course outcomes
        TextFlow outcomeInfo = new TextFlow();    
        Text outcomes = new Text(course.getOutcomes().replaceAll("\\<.*?\\>", ""));
        outcomeInfo.getChildren().addAll(outcomes, new Text("\n"));
        
        Sisu.rightVBox.getChildren().addAll(contentInfo, outcomeInfo);
        VBox.setMargin(contentInfo, new Insets(40, 30, 0, 30));
        VBox.setMargin(outcomeInfo, new Insets(20, 30, 0, 30));
        
    }
    
    /**
     * Displays all courses being straight under a specific module
     * @param module the module for which courses are displayed
     */
    public static void showCourseChoices(StudyModule module, DegreeProgram degree) {
        
        Sisu.rightVBox.getChildren().clear();
        VBox courseChoices = new VBox();
        //Fetching courses and making them visible
        for (Course course : module.getCourses()) {
            
            Label courseLabel = new Label(course.getName());
            HBox courseChoice = new HBox(getCourseCheck(course, degree), courseLabel);
            courseChoices.getChildren().add(courseChoice);
        }
        
        //Aligning
        courseChoices.prefWidthProperty().bind(Sisu.rightVBox.widthProperty());
        courseChoices.setPadding(new Insets(100, 0, 0, 100));
        Sisu.rightVBox.getChildren().add(courseChoices);
        
    }
    
    /**
     * Creates a checkbox which is used to alter the state of a single course
     * @param course the course to change the state of
     * @return CheckBox
     */
    private static CheckBox getCourseCheck(Course course, DegreeProgram degree) {
        
        CheckBox checkBox = new CheckBox();
        //Keeping the state of the courses visible in the ui
        if (course.getState().equals("COMPLETED")) {
            checkBox.setSelected(true);
        }
        //Changing the state and keeping track of which degree was edited
        checkBox.setOnAction(event -> {
            if (checkBox.isSelected()) {
                course.setState("COMPLETED");
                Sisu.editedDegree = degree;
            } else {
                course.setState("DEFAULT");
                Sisu.editedDegree = degree;
            }
        });
        
        return checkBox;
    }
    
}
