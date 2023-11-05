/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fi.tuni.prog3.sisu;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;

/**
 * Class that stores methods to display lists etc in the degrees tab, not to
 * be initialized
 * 
 * @author jaakk
 */
public class UiServices {

    /**
     * Displays all contents that are under the selected parent module
     * 
     * @param moduleView the VBox that the contents are stored in
     * @param courses    list of courses under the module
     * @param modules    list of modules under the module
     * @param indent     desired indent level
     * @param degree     the DegreeProgram that the module is part of
     */
    public static void displayModuleContents(VBox moduleView, List<Course> courses, List<StudyModule> modules,
            int indent, DegreeProgram degree) {

        // Sorting in alphabetical order
        courses.stream()
                .sorted(Comparator.comparing(Course::getName))
                .collect(Collectors.toList());
        modules.stream()
                .sorted(Comparator.comparing(StudyModule::getName))
                .collect(Collectors.toList());

        // Creating headers for all the modules under the parent module
        for (StudyModule module : modules) {
            // VBox to store contents of a child module
            VBox subModuleView = new VBox();
            HBox subModuleHeader = DegreeTab.getClickableModule(subModuleView, module, indent, degree);
            VBox subModuleBox = new VBox(subModuleHeader, subModuleView);
            moduleView.getChildren().add(subModuleBox);
        }
        // Creating headers for all the courses under the parent module
        for (Course course : courses) {
            moduleView.getChildren().add(DegreeTab.getClickableCourse(course, indent));
        }
    }

    /**
     * Displays all modules that are under the selected degree
     * 
     * @param contents   VBox to store the modules in
     * @param subModules list of the StudyModules
     * @param degree     selected degree
     */
    public static void displayDegreeContents(VBox contents, List<StudyModule> subModules, DegreeProgram degree) {

        // Going through all modules and styling and displaying their contents
        for (StudyModule module : subModules) {
            // VBox to store the module contents
            VBox moduleView = new VBox();
            HBox moduleHeader = DegreeTab.getClickableModule(moduleView, module, 20, degree);
            VBox moduleBox = new VBox(moduleHeader, moduleView);
            contents.getChildren().add(moduleBox);
        }

    }

    /**
     * Filters degrees and calls displayDegrees to display them
     * 
     * @param filter string that is used to filter degree names
     */
    public static void filterDegrees(String filter) {

        List<DegreeProgram> degrees = Sisu.allDegrees;
        // Goes through the degree list and prints degrees that match the filter
        if (filter.isEmpty()) {
            displayDegrees(degrees);
        } else {
            List<DegreeProgram> filteredDegrees = degrees.stream()
                    .filter(degree -> degree.getName().toLowerCase().contains(filter))
                    .collect(Collectors.toList());
            displayDegrees(filteredDegrees);
        }
    }

    /**
     * Displays all degrees in the given list
     * 
     * @param degrees the list of DegreePrograms
     */
    public static void displayDegrees(List<DegreeProgram> degrees) {

        Sisu.headerLabel.setText("Degrees");
        // Sorting the degrees
        degrees.stream()
                .sorted(Comparator.comparing(DegreeProgram::getName))
                .collect(Collectors.toList());

        Sisu.degreeView.getChildren().clear();
        for (DegreeProgram degree : degrees) {
            DegreeTab.createClickableDegree(degree);
        }

    }

    /**
     * Displays the full "module tree" which contains the study modules of a
     * degree and the courses inside each module
     * 
     * @param degree degree of which contents are wanted to display
     */
    public static void displayTree(DegreeProgram degree) {

        Sisu.leftVBox.getChildren().clear();
        Sisu.headerLabel.setText("Study structure");
        Sisu.onFirstWindow.set(false);

        Label degreeLabel = new Label(String.format(
                "%s | %d / %d credits",
                degree.getName(),
                degree.getEarnedCredits(),
                degree.getMinCredits()));

        // Creating a StoredInfo object which will be passed to the file reader
        StoredInfo info = new StoredInfo(Sisu.studentName, Sisu.studentNumber,
                Sisu.startYear, Sisu.gradYear);

        try {
            // Initializing a new DegreeProgram, of which constructor will fetch
            // the full module tree
            DegreeProgram degreeToShow = new DegreeProgram(
                    degree.getName(), degree.getGroupId(), info, Sisu.isExistingAccount);
            List<StudyModule> subModules = degreeToShow.getSubModules();

            // Creating the main structure for degree tree
            VBox degreeContents = new VBox();
            ToggleButton degreeToggle = DegreeTab.getDegreeToggle(degreeContents, subModules, degree);
            HBox degreeHeader = new HBox(degreeToggle, degreeLabel);
            HBox.setMargin(degreeLabel, new Insets(5, 0, 0, 2));
            VBox degreeStructure = new VBox(degreeHeader, degreeContents);

            // Creating a scrollable window in case the structure becomes too long for the
            // window
            ScrollPane scrollWindow = new ScrollPane(degreeStructure);
            scrollWindow.prefHeightProperty().bind(Sisu.leftVBox.heightProperty());
            scrollWindow.prefWidthProperty().bind(Sisu.leftVBox.widthProperty());

            degreeStructure.setStyle("-fx-background-color: #ffffff;");
            degreeStructure.prefHeightProperty().bind(scrollWindow.heightProperty().subtract(10));
            degreeStructure.prefWidthProperty().bind(scrollWindow.widthProperty().subtract(10));

            // Increasing scrolling speed
            scrollWindow.getContent().setOnScroll(event -> {
                double deltaY = event.getDeltaY() * 0.002;
                scrollWindow.setVvalue(scrollWindow.getVvalue() - deltaY);
            });

            Sisu.leftVBox.getChildren().add(scrollWindow);
            displayDegreeContents(degreeContents, subModules, degreeToShow);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
