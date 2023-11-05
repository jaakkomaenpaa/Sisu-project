package fi.tuni.prog3.sisu;

/**
 * Data class for representing individual course instance.
 * Offers only getters to interact with the attributes.
 */
public class Course extends DegreeModule {

    private String content;
    private String outcomes;
    private CourseState state;

    private enum CourseState {
        DEFAULT,
        SELECTED,
        COMPLETED
    }

    /**
     * Initializes the super class and the descriptive attributes.
     * 
     * @param name       name of the Course.
     * @param id         id of the Course.
     * @param groupId    group id of the Course.
     * @param minCredits minimum credits of the Course.
     * @param content    short string description of the course contents
     * @param outcomes   short description of the learning outcomes
     */
    public Course(String name, String id, String groupId,
            int minCredits, String content, String outcomes) {
        super(name, id, groupId, minCredits);
        this.content = content;
        this.outcomes = outcomes;
        this.state = CourseState.DEFAULT;

    }

    public void print() {
        System.out.println("Course: " + name);
    }

    public String getContent() {
        return content;
    }

    public String getOutcomes() {
        return outcomes;
    }

    public String getState() {
        return state.toString();
    }

    public void setState(String stateString) {

        switch (stateString.toString()) {
            case "DEFAULT":
                this.state = CourseState.DEFAULT;
                break;
            case "SELECTED":
                this.state = CourseState.SELECTED;
                break;
            case "COMPLETED":
                this.state = CourseState.COMPLETED;
                break;

        }
    }

}